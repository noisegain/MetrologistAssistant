package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.ui.theme.SexyButton


@Composable
fun ShowPassportScreen(
    passportState: State<Passport?>,
    onLoadPhotoClick: (Passport) -> Unit,
    onLoadCertificateClick: (Passport) -> Unit,
    onEditClick: (Passport) -> Unit,
) {
    val passport = passportState.value ?: return
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = passport.name, style = MaterialTheme.typography.h5)
                Text(text = passport.division, style = MaterialTheme.typography.h5)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "МВЗ: ${passport.mvz}", style = MaterialTheme.typography.h6)
                Text(text = passport.type, style = MaterialTheme.typography.h6)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Без НДС: ${passport.costRaw}", style = MaterialTheme.typography.h6)
                Text(text = "С НДС: ${passport.costFull}", style = MaterialTheme.typography.h6)
            }
            Text("Примечание: ${passport.notes}", style = MaterialTheme.typography.h6)
        }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                SexyButton(
                    name = "Фото",
                    modifier = Modifier.fillMaxWidth(0.75f),
                    enabled = passport.photoUri != null,
                    onClick = { onLoadPhotoClick(passport) }
                )
            }
            Row {
                SexyButton(
                    name = "Свидетельство",
                    modifier = Modifier.fillMaxWidth(0.75f),
                    enabled = passport.certificateUri != null,
                    onClick = { onLoadCertificateClick(passport) }
                )
            }
        }
    }
}