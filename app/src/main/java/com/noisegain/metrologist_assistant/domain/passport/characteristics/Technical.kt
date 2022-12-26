package com.noisegain.metrologist_assistant.domain.passport.characteristics

@kotlinx.serialization.Serializable
data class Technical(
    val count: Int? = null,
    val info: List<String> = emptyList(),
)