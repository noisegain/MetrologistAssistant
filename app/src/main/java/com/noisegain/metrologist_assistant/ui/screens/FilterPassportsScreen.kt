package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.ui.theme.SexyButton
import com.noisegain.metrologist_assistant.ui.theme.SexyTextField
import com.noisegain.metrologist_assistant.ui.utils.Filter
import com.noisegain.metrologist_assistant.ui.utils.Filters

@Composable
fun FilterPassportsScreen(
    onFilterPressed: (Filter) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var division by remember { mutableStateOf("") }
    var mvz by remember { mutableStateOf("") }
    fun makeFilter() = Filter.combine(Filters.ByName(name), Filters.ByDivision(division), Filters.ByMVZ(mvz))
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Фильтрация", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Column(Modifier.fillMaxHeight(0.75f).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =
        Arrangement
            .spacedBy(8.dp, Alignment.CenterVertically)) {
            SexyTextField(placeholder = "Имя", init = name, onChange = { name = it })
            SexyTextField(placeholder = "Подразделение", init = division, onChange = { division = it })
            SexyTextField(placeholder = "МВЗ", init = mvz, onChange = { mvz = it })
        }
        SexyButton(name = "Применить", onClick = { onFilterPressed(makeFilter()) })
    }
}