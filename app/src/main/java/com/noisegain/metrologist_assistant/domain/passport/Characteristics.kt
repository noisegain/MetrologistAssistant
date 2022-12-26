package com.noisegain.metrologist_assistant.domain.passport

import com.noisegain.metrologist_assistant.domain.passport.characteristics.Metrologic
import com.noisegain.metrologist_assistant.domain.passport.characteristics.Technical

@kotlinx.serialization.Serializable
data class Characteristics(val technical: Technical? = null, val metrologic: Metrologic? = null)
