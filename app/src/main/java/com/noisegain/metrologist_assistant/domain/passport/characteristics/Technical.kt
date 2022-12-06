package com.noisegain.metrologist_assistant.domain.passport.characteristics

@kotlinx.serialization.Serializable
data class Technical(
    val precision: Double,
    val info: List<String> = emptyList(),
    val measureUnit: MeasureUnit,
    val divCost: Int
)