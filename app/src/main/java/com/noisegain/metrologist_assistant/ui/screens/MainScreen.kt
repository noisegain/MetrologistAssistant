package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.utils.Filters


@Composable
fun MainScreen(onClick: ((Passport) -> Boolean) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mod = Modifier.fillMaxWidth(0.7f)
        SexyButton(mod, name = "Непроверенные", onClick = {
            onClick(Filters.UnVerified())
        })
        SexyButton(mod, name = "Проверенные", onClick = {
            onClick(Filters.Verified())
        })
        SexyButton(mod, name = "В ремонте", onClick = {
            onClick(Filters.InRepair())
        })
        SexyButton(mod, name = "На консервации", onClick = {
            onClick(Filters.InConservation())
        })
        SexyButton(mod, name = "Списанные", onClick = {
            onClick(Filters.WrittenOff())
        })
        SexyButton(mod, name = "Все", onClick = {
            onClick(Filters.All())
        })
    }
}