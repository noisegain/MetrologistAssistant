package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column(Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        SexyTextField(placeholder = "Имя", init = name, onChange = { name = it })
        SexyTextField(placeholder = "Подразделение", init = division, onChange = { division = it })
        SexyTextField(placeholder = "МВЗ", init = mvz, onChange = { mvz = it })
        SexyButton(name = "Применить", onClick = { onFilterPressed(makeFilter()) })
    }
}