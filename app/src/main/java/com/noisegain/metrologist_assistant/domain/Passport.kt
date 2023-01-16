package com.noisegain.metrologist_assistant.domain

import androidx.room.Entity
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.Chronics
import com.noisegain.metrologist_assistant.domain.passport.State
import com.noisegain.metrologist_assistant.domain.passport.chronics.Event

@Entity(tableName = "passports", primaryKeys = ["name", "mvz", "type", "id"])
data class Passport(
    val name: String, //
    val division: String, //
    val mvz: String, //
    val type: String, //
    val id: String, //
    val count: Int?, //
    val event: Event = Event.VALIDATION, //
    val status: State = State.OPERABLE, // ????
    val characteristics: Characteristics,
    val chronics: Chronics = Chronics(),
    val costRaw: Double? = null, //
    val costFull: Double? = null, //
    val certificateUri: String? = null, // -
    val photoUri: String? = null, // -
//    val attachments: List<String> = listOf(), // -
    val notes: String = ""
)
