package com.noisegain.metrologist_assistant.domain.passport.characteristics

import com.noisegain.metrologist_assistant.domain.passport.State
import java.time.LocalDate

@kotlinx.serialization.Serializable
data class Metrologic(
    val state: State = State.OPERABLE, //-
    val last: Long, // Date
    val nextDate: Long, // Date
    val next: Int, // Month
    val period: Int,
    val type: String,
    val lab: String
) {
    private fun daysFromEpochToString(days: Long) = LocalDate.ofEpochDay(days).toString()

    val lastAsString = daysFromEpochToString(last)
    val nextDateAsString = daysFromEpochToString(nextDate)
}
