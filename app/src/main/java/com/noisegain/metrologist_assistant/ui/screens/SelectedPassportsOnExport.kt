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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.ui.MainViewModel
import com.noisegain.metrologist_assistant.ui.theme.Background2
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.SexyButton

@Composable
fun SelectedPassportsOnExport(
    viewModel: MainViewModel,
    onExportPressed: (String) -> Unit,
    onEditPressed: () -> Unit,
) {
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
        SexyButton(onClick = { onExportPressed("test") }) {
            Text(text = "Создать отчёт")
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
