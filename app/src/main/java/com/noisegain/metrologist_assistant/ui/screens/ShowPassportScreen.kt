package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.theme.SexyButton


/*
@params: passports - список паспортов
@params: onPassportClick - функция, которая вызывается при нажатии на паспорт
 */
@Composable
fun ShowPassportScreen(passportState: State<Passport?>) {
    val passport = passportState.value ?: return
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = passport.name, style = MaterialTheme.typography.h2)
                Text(text = passport.division, style = MaterialTheme.typography.h2)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "МВЗ: ${passport.mvz}", style = MaterialTheme.typography.h3)
                Text(text = passport.type, style = MaterialTheme.typography.h3)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Без НДС: ${passport.costRaw}", style = MaterialTheme.typography.h3)
                Text(text = "С НДС: ${passport.costFull}", style = MaterialTheme.typography.h3)
            }
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SexyButton(name = "Фото", modifier = Modifier.fillMaxWidth(0.8f)) //passport.photo != null)
            SexyButton(name = "Свидетельство", modifier = Modifier.fillMaxWidth(0.8f), enabled = passport.certificate != null)
        }
    }
}