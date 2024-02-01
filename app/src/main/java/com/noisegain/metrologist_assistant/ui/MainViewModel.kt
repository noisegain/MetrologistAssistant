package com.noisegain.metrologist_assistant.ui

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.*
import com.noisegain.metrologist_assistant.domain.entity.ExportAct
import com.noisegain.metrologist_assistant.domain.entity.Exported
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.entity.Report
import com.noisegain.metrologist_assistant.domain.writer.ExcelType
import com.noisegain.metrologist_assistant.domain.writer.ExportActWriter
import com.noisegain.metrologist_assistant.domain.writer.ReportWriter
import com.noisegain.metrologist_assistant.ui.utils.Filters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val passportsRepository: PassportsRepository,
    private val exportedRepository: ExportedRepository,
    private val passportsParser: PassportsParser,
    private val reportWriter: ReportWriter,
    private val exportActWriter: ExportActWriter,
    private val context: Application
) : ViewModel() {

    private val _passports = MutableStateFlow(emptyList<Passport>())
    val passports = _passports.asStateFlow()

    private val _curPassport = MutableStateFlow<Passport?>(null)
    val curPassport = _curPassport.asStateFlow()

    private val filter: MutableStateFlow<(Passport) -> Boolean> = MutableStateFlow { true }

    private fun resetFilter() {
        filter.update { Filters.All() }
    }

    private val loadType = MutableStateFlow(LoadType.PHOTO)

    private val reportType = MutableStateFlow(Report.Type.ByMonth)

    private val _exported = MutableStateFlow(emptyList<Exported>())
    val exported = _exported.asStateFlow()

    private val excelType = MutableStateFlow(ExcelType.REPORT)
    fun setReportType(type: Report.Type) {
        excelType.update { ExcelType.REPORT }
        reportType.update { type }
    }

    fun selectExportAct() {
        excelType.update { ExcelType.EXPORT_ACT }
    }

    private fun setLoadType(type: LoadType) {
        loadType.update { type }
    }

    fun setLoadTypePhoto() = setLoadType(LoadType.PHOTO)
    fun setLoadTypeCertificate() = setLoadType(LoadType.CERTIFICATE)

    val filteredPassports = passports.combine(filter) { passports, filter ->
        passports.filter(filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val filteredPassportsCheckBox = filteredPassports.map {
        it.map { passport ->
            PassportWithCheckBox(
                passport, MutableStateFlow(
                    passport in
                            passportsOnExport
                )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    data class PassportWithCheckBox(
        val passport: Passport,
        var isChecked: MutableStateFlow<Boolean> = MutableStateFlow(true)
    )

    fun selectedAction() {
        val selected =
            filteredPassportsCheckBox.value.filter { it.isChecked.value }.map { it.passport }
                .filter { it !in passportsOnExport }
        passportsOnExport.addAll(selected)
        Toast.makeText(context, "Выбрано ${selected.size} новых приборов", Toast.LENGTH_SHORT)
            .show()
        resetFilter()
    }

    val passportsOnExport = mutableStateListOf<Passport>()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    val dataStore = context.dataStore

    private val exportActNamesKey = stringSetPreferencesKey("exportActNames")

    val exportActNamesSuggestions = dataStore.data.map { it[exportActNamesKey] }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPassports()
            fetchExported()
        }
    }

    fun importPassports(contentResolver: ContentResolver, uri: Uri) {
        val passports = passportsParser.parse(contentResolver.openInputStream(uri) ?: return)
        viewModelScope.launch(Dispatchers.IO) {
            passportsRepository.addPassports(passports)
            fetchPassports()
        }
    }

    fun selectPassport(passport: Passport) = _curPassport.update { passport }

    fun filterPassports(filter: (Passport) -> Boolean) = this.filter.update { filter }

    fun removeContent(passport: Passport) {
        val passport1 = if (loadType.value == LoadType.PHOTO) {
            passport.copy(photoUri = null)
        } else {
            passport.copy(certificateUri = null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            passportsRepository.addPassport(passport1)
            fetchPassports()
        }
    }

    fun loadContent(
        contentResolver: ContentResolver,
        uri: Uri,
        out: OutputStream,
        filename: String
    ) {
        curPassport.value ?: return
        out.use {
            contentResolver.openInputStream(uri)?.use {
                it.copyTo(out)
            }
        }
        log(uri.path)
        _curPassport.update {
            if (loadType.value == LoadType.PHOTO) {
                it?.copy(photoUri = filename)
            } else {
                it?.copy(certificateUri = filename)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            passportsRepository.addPassport(curPassport.value ?: return@launch)
            fetchPassports()
        }
    }

    fun savePassport(passport: Passport) {
        viewModelScope.launch(Dispatchers.IO) {
            passportsRepository.addPassport(passport)
            fetchPassports()
        }
    }

    fun onExportClick(filename: String) {
        val dir = File(context.filesDir, MainActivity.REPORTS_DIR)
        if (!dir.exists() && !dir.mkdir()) throw Exception("Can't create dir")

        val uri = FileProvider.getUriForFile(
            context,
            MainActivity.Companion.Tags.PROVIDER,
            File(dir, "$filename.xlsx")
        )
        exportPassports(context.contentResolver.openOutputStream(uri)!!, filename)
    }

    fun exportPassports(out: OutputStream, filename: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (excelType.value == ExcelType.REPORT) {
                reportWriter.write(
                    Report(out, filteredPassports.value, reportType.value)
                )
            } else {
                exportActWriter.write(
                    ExportAct(out, context.resources, filteredPassports.value)
                )
            }
            exportedRepository.addExported(Exported(filename, excelType.value))
            fetchExported()
        }
    }

    fun deleteExport(export: Exported) {
        val dir = File(context.filesDir, MainActivity.REPORTS_DIR)
        val uri = FileProvider.getUriForFile(
            context,
            MainActivity.Companion.Tags.PROVIDER,
            File(dir, export.uri)
        )
        context.contentResolver.delete(uri, null)
        viewModelScope.launch(Dispatchers.IO) {
            exportedRepository.deleteExport(export)
            fetchExported()
        }
    }

    fun clearPassports() {
        viewModelScope.launch(Dispatchers.IO) {
            passportsRepository.clear()
            fetchPassports()
        }
    }

    private suspend fun fetchPassports() {
        _passports.update { passportsRepository.getAll() }
    }

    private suspend fun fetchExported() {
        _exported.update { exportedRepository.getAll() }
    }

    enum class LoadType {
        PHOTO, CERTIFICATE
    }
}