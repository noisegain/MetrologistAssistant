package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Print
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.entity.Report
import com.noisegain.metrologist_assistant.ui.theme.Background2
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.theme.SexyTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PassportsScreen(
    passports: State<List<Passport>>,
    onPassportClick: (Passport) -> Unit,
    onFilterClick: () -> Unit,
    onExportClick: (String) -> Unit,
    onShowExportedClick: () -> Unit,
    onSelectReportTypeClick: (Report.Type) -> Unit,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetPeekHeight = 0.dp,
        sheetContent = {
            ReportWritePopup(onSelectReportTypeClick, onExportClick) {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }
    ) {
        Column(Modifier.fillMaxSize(), Arrangement.SpaceBetween) {
            LazyColumn(
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(passports.value) {
                    PassportCard(it, onPassportClick)
                }
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.Background2),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Всего: ${passports.value.size}",
                        style = MaterialTheme.typography.body1
                    )
                    CircleButton(onClick = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }, icon = Icons.Rounded.Print)
                    CircleButton(onClick = onShowExportedClick, icon = Icons.Rounded.OpenInBrowser)
                    CircleButton(
                        onClick = onFilterClick,
                        icon = Icons.Rounded.Menu
                    )
                }
            }
        }
    }
}

@Composable
fun ReportWritePopup(
    onSelectReportTypeClick: (Report.Type) -> Unit,
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
                            onSelectReportTypeClick(it)
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
fun PassportCard(passport: Passport, onClick: (Passport) -> Unit) {
    Card(
        Modifier
            .fillMaxWidth(0.95f)
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick(passport)
            },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = passport.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(0.75f)
                )
                Text(text = passport.division, style = MaterialTheme.typography.body2)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = passport.mvz, style = MaterialTheme.typography.caption)
                Text(text = passport.type, style = MaterialTheme.typography.body2)
            }
        }
    }
}
