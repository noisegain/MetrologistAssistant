package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.entity.Report
import com.noisegain.metrologist_assistant.ui.MainViewModel
import com.noisegain.metrologist_assistant.ui.theme.Background2
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.theme.SexyTextField
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun SelectedPassportsOnExport(
    viewModel: MainViewModel,
    onExportPressed: (String) -> Unit,
    onEditPressed: () -> Unit,
) {
    val popupShow = remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        LazyColumn {
            items(viewModel.passportsOnExport) { passport ->
                PassportItem(
                    passport = passport,
                    onRemovePressed = { viewModel.passportsOnExport.remove(passport) }
                )
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            CircleButton(onClick = onEditPressed, icon = Icons.Rounded.Edit)
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            if (popupShow.value) {
                ExportWritePopup(suggestions = viewModel.exportActNamesSuggestions.collectAsState(), onExportClick
                = onExportPressed) {
                    popupShow.value = false
                    viewModel.passportsOnExport.clear()
                }
            } else {
                SexyButton(onClick = { onExportPressed("test") }) {
                    Text(text = "Создать отчёт")
                }
            }
        }
    }
}

@Composable
fun ExportWritePopup(
    suggestions: State<Set<String>?>,
    onExportClick: (String) -> Unit,
    onCollapsePopup: () -> Unit
) {
    Column(Modifier.padding(16.dp), Arrangement.spacedBy(8.dp), Alignment.CenterHorizontally) {
        Text(
            text = "Создание отчёта",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        )
        val filename = remember { mutableStateOf("test") }
        SexyTextField(
            init = filename.value,
            onChange = { filename.value = it },
            placeholder = "Имя файла"
        )
        if (filename.value.isNotEmpty()) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Report.Type.values()) {
                    SexyButton(
                        onClick = {
                            //onSelectReportTypeClick(it)
                            onExportClick(filename.value)
                            onCollapsePopup()
                        },
                        name = it.name,
                    )
                }
            }
        } else {
            Text(
                text = "Введите имя файла",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                color = MaterialTheme.colors.error
            )
        }
    }
}
@Composable
fun PassportItem(passport: Passport, onRemovePressed: () -> Unit) {
    Row(Modifier.fillMaxWidth(0.95f)) {
        PassportCard(passport = passport, onClick = {})
        CircleButton(onClick = onRemovePressed, icon = Icons.Rounded.Delete)
    }
}
