package com.noisegain.metrologist_assistant.domain.passport.characteristics

import com.noisegain.metrologist_assistant.domain.passport.State
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    private fun daysFromEpochToString(days: Long) =
        if (days == DISCARDED) "Списан" else LocalDate.ofEpochDay(days).format(DateTimeFormatter.ofPattern("dd.MM.yy"))

    val nextDateAsDate: LocalDate
        get() = LocalDate.ofEpochDay(nextDate)
    val lastAsString: String = daysFromEpochToString(last)
    val nextDateAsString: String = daysFromEpochToString(nextDate)
    companion object {
        const val DISCARDED = -1L
        const val NEXT_UNKNOWN = 13
    }
}
