package com.noisegain.metrologist_assistant.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.domain.PassportsParser
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PassportsRepository,
    private val passportsParser: PassportsParser
) : ViewModel() {

    private val _passports = MutableStateFlow(emptyList<Passport>())
    val passports = _passports.asStateFlow()

    private val _curPassport = MutableStateFlow<Passport?>(null)
    val curPassport = _curPassport.asStateFlow()

    private val filter: MutableStateFlow<(Passport) -> Boolean> = MutableStateFlow { true }

    val filteredPassports = passports.combine(filter) { passports, filter ->
        passports.filter(filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _passports.update { repository.getAll() }
        }
    }

    fun loadContent(contentResolver: ContentResolver, uri: Uri) {
        val passports = passportsParser.parse(contentResolver.openInputStream(uri) ?: return)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPassports(passports)
            _passports.update { repository.getAll() }
        }
    }

    fun selectPassport(passport: Passport) = _curPassport.update { passport }

    fun filterPassports(filter: (Passport) -> Boolean) = this.filter.update { filter }
}