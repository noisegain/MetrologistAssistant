package com.noisegain.metrologist_assistant.domain.passport.characteristics

@kotlinx.serialization.Serializable
data class Technical(
    val count: Int = 1,
    val limit: String,
    val accuracy: String,
    val info: List<String> = emptyList(),
)