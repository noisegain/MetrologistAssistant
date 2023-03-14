package com.noisegain.metrologist_assistant.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Filter
import androidx.compose.material.icons.rounded.Summarize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.noisegain.metrologist_assistant.ui.MainViewModel
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun SelectPassportsScreen(passports: State<List<MainViewModel.PassportWithCheckBox>>, onFilterPressed: () -> Unit,
                          onExportScreenPressed: () -> Unit) {
    val checkedAll = remember {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {
            Row {
                Checkbox(checked = checkedAll.value, onCheckedChange = {
                    checkedAll.value = it
                    passports.value.forEach { passport ->
                        passport.isChecked.value = it
                    }
                })
                CircleButton(onClick = onFilterPressed, icon = Icons.Rounded.Filter)
                CircleButton(onClick = onExportScreenPressed, icon = Icons.Rounded.Summarize)
            }
        }
    ) {
        LazyColumn(Modifier.padding(it)) {
            items(passports.value) { passport ->
                val x = passport.isChecked.collectAsState()
                PassportWithCheckBox(passport = passport, checked = x.value)
            }
        }
    }

    LaunchedEffect(key1 = null, block = {
        checkedAll.value = passports.value.all { it.isChecked.value }
    })
}

@Composable
fun PassportWithCheckBox(passport: MainViewModel.PassportWithCheckBox, checked: Boolean) {
    Row {
        Checkbox(checked = checked, onCheckedChange = { passport.isChecked.value = it })
        PassportCard(passport = passport.passport) {}
    }
}