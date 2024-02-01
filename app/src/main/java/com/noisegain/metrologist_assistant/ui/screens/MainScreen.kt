package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.utils.Filter
import com.noisegain.metrologist_assistant.ui.utils.Filters


@Composable
fun MainScreen(onFilterClick: (Filter) -> Unit, onExportClick: () -> Unit, onSettingsClick: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mod = Modifier.fillMaxWidth(0.7f)
        SexyButton(mod, name = "Неповеренные", onClick = {
            onFilterClick(Filters.UnVerified)
        })
        SexyButton(mod, name = "Поверенные", onClick = {
            onFilterClick(Filters.Verified)
        })
        SexyButton(mod, name = "В ремонте", onClick = {
            onFilterClick(Filters.InRepair)
        })
        SexyButton(mod, name = "На Поверке", onClick = {
            onFilterClick(Filters.InValidation)
        })
        SexyButton(mod, name = "Списанные", onClick = {
            onFilterClick(Filters.WrittenOff)
        })
        SexyButton(mod, name = "Все", onClick = {
            onFilterClick(Filters.All)
        })
        Spacer(modifier = Modifier.height(16.dp))
        SexyButton(mod, name = "Акт на вывоз", onClick = {
            onExportClick()
        }, icon = Icons.Rounded.ImportExport, enabled = false)
        Spacer(modifier = Modifier.height(16.dp))
        SexyButton(mod, name = "Настройки", onClick = {
            onSettingsClick()
        }, icon = Icons.Rounded.Settings)
    }
}