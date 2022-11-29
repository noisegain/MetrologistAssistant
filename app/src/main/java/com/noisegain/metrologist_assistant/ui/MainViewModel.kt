package com.noisegain.metrologist_assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noisegain.metrologist_assistant.domain.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _cur = MutableStateFlow("")
    val cur = _cur.asStateFlow()

    fun loadSmth() {
        viewModelScope.launch {
            delay(2000)
            _cur.value = repository.callDb()
        }
    }
}