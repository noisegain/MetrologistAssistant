package com.noisegain.metrologist_assistant.domain.passport.characteristics

import com.noisegain.metrologist_assistant.domain.passport.State

@kotlinx.serialization.Serializable
data class Metrologic(
    val state: State,
    val last: Long, // Date
    val next: Long, // Date
    val period: Int,
    val type: String,
    val lab: String
)
