package com.noisegain.metrologist_assistant.domain

import androidx.room.Entity
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.Chronics
import com.noisegain.metrologist_assistant.domain.passport.State

@Entity(tableName = "passports", primaryKeys = ["name", "mvz"])
data class Passport(
    val name: String,
    val division: String,
    val mvz: String,
    val status: State,
    val characteristics: Characteristics? = null,
    val chronics: Chronics? = null,
    val cost: Int? = null,
    val certificate: String? = null,
    val photo: String? = null
)
