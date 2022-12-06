package com.noisegain.metrologist_assistant.domain.passport

import com.noisegain.metrologist_assistant.domain.passport.chronics.Chronic

@kotlinx.serialization.Serializable
data class Chronics(val data: List<Chronic>)
