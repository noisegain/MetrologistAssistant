package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Print
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.theme.Background2
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.utils.Filters

@Composable
fun PassportsScreen(
    passports: State<List<Passport>>,
    onPassportClick: (Passport) -> Unit,
    onSortClick: ((Passport) -> Boolean) -> Unit,
    onExportClick: (String) -> Unit,
    onShowExportedClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize(), Arrangement.SpaceBetween) {
        LazyColumn(
            Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            items(passports.value) {
                PassportCard(it, onPassportClick)
            }
        }
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colors.Background2), contentAlignment = Alignment.Center
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
                CircleButton(onClick = { onExportClick("test") }, icon = Icons.Rounded.Print)
                CircleButton(onClick = onShowExportedClick, icon = Icons.Rounded.OpenInBrowser)
                CircleButton(
                    onClick = { onSortClick(Filters.ByName("метр").filter) },
                    icon = Icons.Rounded.Menu
                )
            }
        }
    }
}

@Composable
fun PassportCard(passport: Passport, onClick: (Passport) -> Unit) {
    log("Passport: $passport")
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
