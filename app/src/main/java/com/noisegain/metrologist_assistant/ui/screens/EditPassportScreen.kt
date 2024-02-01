package com.noisegain.metrologist_assistant.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.ui.theme.SexyTextField

@Composable
fun EditPassportScreen(
    passportState: State<Passport?>,
    onLoadPhotoClick: (Passport) -> Unit,
    onLoadCertificateClick: (Passport) -> Unit,
    onRemovePhotoClick: (Passport) -> Unit,
    onRemoveCertificateClick: (Passport) -> Unit,
    onSavePassportClick: (Passport) -> Unit,
) {
    Column {
        passportState.value?.let { passport ->
            PassportForm(
                passport = passport,
                onLoadPhotoClick = { onLoadPhotoClick(passport) },
                onLoadCertificateClick = { onLoadCertificateClick(passport) },
                onRemovePhotoClick = { onRemovePhotoClick(passport) },
                onRemoveCertificateClick = { onRemoveCertificateClick(passport) },
                onSavePassportClick = { onSavePassportClick(passport) },
            )
        }
    }
}

@Composable
fun PassportForm(
    passport: Passport,
    onLoadPhotoClick: () -> Unit,
    onLoadCertificateClick: () -> Unit,
    onRemovePhotoClick: () -> Unit,
    onRemoveCertificateClick: () -> Unit,
    onSavePassportClick: () -> Unit
) {

}