package com.noisegain.metrologist_assistant.domain.passport.characteristics

import com.noisegain.metrologist_assistant.domain.passport.State

@kotlinx.serialization.Serializable
data class Metrologic(
    val state: State = State.OPERABLE, //-
    val last: Long? = null, // Date
    val next: Long?, // Date
    val period: Int?,
    val type: String,
    val lab: String
)
