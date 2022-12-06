package com.noisegain.metrologist_assistant.domain.passport.chronics

@kotlinx.serialization.Serializable
data class Chronic(val date: Long, val event: Event, val lab: String, val cost: Double)
