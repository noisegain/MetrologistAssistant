package com.noisegain.metrologist_assistant.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PassportsRepository,
    private val passportsParser: PassportsParser,
    private val reportWriter: ReportWriter,
) : ViewModel() {

    private val _passports = MutableStateFlow(emptyList<Passport>())
    val passports = _passports.asStateFlow()

    private val _curPassport = MutableStateFlow<Passport?>(null)
    val curPassport = _curPassport.asStateFlow()

    private val filter: MutableStateFlow<(Passport) -> Boolean> = MutableStateFlow { true }

    private val loadType = MutableStateFlow(LoadType.PHOTO)

    private fun setLoadType(type: LoadType) {
        loadType.update { type }
    }

    fun setLoadTypePhoto() = setLoadType(LoadType.PHOTO)
    fun setLoadTypeCertificate() = setLoadType(LoadType.CERTIFICATE)

    val filteredPassports = passports.combine(filter) { passports, filter ->
        passports.filter(filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _passports.update { repository.getAll() }
        }
    }

    fun importPassports(contentResolver: ContentResolver, uri: Uri) {
        val passports = passportsParser.parse(contentResolver.openInputStream(uri) ?: return)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPassports(passports)
            _passports.update { repository.getAll() }
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
            repository.addPassport(passport1)
            _passports.update { repository.getAll() }
        }
    }

    fun loadContent(contentResolver: ContentResolver, uri: Uri, out: OutputStream, filename: String) {
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
            repository.addPassport(curPassport.value ?: return@launch)
            _passports.update { repository.getAll() }
        }
    }

    fun savePassport(passport: Passport) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPassport(passport)
            _passports.update { repository.getAll() }
        }
    }

    fun exportPassports(out: OutputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            reportWriter.write(Report(out, filteredPassports.value))
        }
    }

    enum class LoadType {
        PHOTO, CERTIFICATE
    }
}