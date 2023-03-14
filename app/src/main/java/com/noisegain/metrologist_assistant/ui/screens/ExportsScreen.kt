package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.domain.entity.Exported
import com.noisegain.metrologist_assistant.domain.writer.ExcelType
import com.noisegain.metrologist_assistant.ui.theme.SexyButton


@Composable
fun ExportsScreen(
    reports: State<List<Exported>>,
    showReport: (Exported) -> Unit,
    deleteReport: (Exported) -> Unit
) {
    val mode = remember {
        mutableStateOf(true)
    }
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { mode.value = !mode.value }) {
                Text("Режим удаления")
            }
        }
        Text("Reports:", style = MaterialTheme.typography.h4)
        ShowExported(
            reports.value.filter { it.type == ExcelType.REPORT },
            if (mode.value) showReport else deleteReport,
            mode.value
        )
        Spacer(Modifier.height(16.dp))
        Text("ExportActs:", style = MaterialTheme.typography.h4)
        ShowExported(
            reports.value.filter { it.type == ExcelType.EXPORT_ACT },
            if (mode.value) showReport else deleteReport,
            mode.value
        )
    }
}

@Composable
private fun ShowExported(
    reports: List<Exported>,
    action: (Exported) -> Unit,
    mode: Boolean
) {
    LazyColumn {
        items(reports) { report ->
            SexyButton(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                name = report.name,
                onClick = { action(report) },
                enabled = mode
            )
        }
    }
}