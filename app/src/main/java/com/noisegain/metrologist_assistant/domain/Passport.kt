package com.noisegain.metrologist_assistant.domain

import androidx.room.Entity
import com.noisegain.metrologist_assistant.data.InstantConverter
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.Chronics
import com.noisegain.metrologist_assistant.domain.passport.State
import java.time.Instant
import java.time.temporal.ChronoUnit

@Entity(tableName = "passports", primaryKeys = ["name", "mvz"])
data class Passport(
    val name: String, //
    val division: String, //
    val mvz: String, //
    val type: String, //
    val id: String, //
    val count: Int?, //
    val status: State = State.OPERABLE, // ????
    val characteristics: Characteristics? = null,
    val chronics: Chronics? = null,
    val costRaw: Double? = null, //
    val costFull: Double? = null, //
    val certificate: String? = null, // -
    val photo: String? = null // -
)
