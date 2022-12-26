package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.utils.Filters


@Composable
fun MainScreen(onClick: ((Passport) -> Boolean) -> Unit) {
    Column {
        SexyButton(name = "Непроверенные", onClick = {
            onClick(Filters.UnVerified())
        })
        SexyButton(name = "Проверенные", onClick = {
            onClick(Filters.Verified())
        })
        SexyButton(name = "В ремонте", onClick = {
            onClick(Filters.InRepair())
        })
        SexyButton(name = "На консервации", onClick = {
            onClick(Filters.InConservation())
        })
        SexyButton(name = "Списанные", onClick = {
            onClick(Filters.WrittenOff())
        })
        SexyButton(name = "Все", onClick = {
            onClick(Filters.All())
        })
    }
}