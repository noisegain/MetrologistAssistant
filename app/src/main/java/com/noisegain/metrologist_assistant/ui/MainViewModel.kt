package com.noisegain.metrologist_assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PassportsRepository
) : ViewModel() {

    private val _cur = MutableStateFlow("")
    val cur = _cur.asStateFlow()

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.addPassport(
//                Passport(
//                    "noisegain", "CT", "1313", State.OPERABLE,
//                    Characteristics(
//                        Technical(1.0, measureUnit = MeasureUnit.BAR, divCost = 2),
//                        Metrologic(State.OPERABLE, 0, 1, 2, "AMOGUS", "YURGA_LABS")
//                    )
//                )
//            )
//        }
    }

    fun loadSmth() {
        viewModelScope.launch(Dispatchers.IO) {
            _cur.value = repository.getAll().toString()
        }
    }
}