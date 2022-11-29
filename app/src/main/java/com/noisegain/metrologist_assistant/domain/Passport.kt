package com.noisegain.metrologist_assistant.domain

import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.Chronics
import com.noisegain.metrologist_assistant.domain.passport.State

data class Passport(
    val name: String,
    val division: String,
    val mvzNumber: String,
    val status: State,
    val characteristics: Characteristics,
    val chronics: Chronics,
    val cost: Int,
    val certificate: String,
    val photo: String
)
