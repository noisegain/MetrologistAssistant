package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.ui.theme.SexyButton


@Composable
fun SettingsScreen(
    onClearDatabaseClick: () -> Unit,
    onUploadPassportsClick: () -> Unit,
    onExportedClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mod = Modifier.fillMaxWidth(0.7f)

        SexyButton(
            mod, name = "Отчистить базу данных",
            onClick = onClearDatabaseClick,
            icon = Icons.Rounded.Remove
        )

        Spacer(modifier = Modifier.height(16.dp))

        SexyButton(
            mod, name = "Загрузить паспорта",
            onClick = onUploadPassportsClick,
            icon = Icons.Rounded.UploadFile
        )

        Spacer(modifier = Modifier.height(16.dp))

        SexyButton(
            mod, name = "Отчёты",
            onClick = onExportedClick,
            icon = Icons.Rounded.List
        )
    }
}