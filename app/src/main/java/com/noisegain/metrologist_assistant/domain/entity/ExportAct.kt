package com.noisegain.metrologist_assistant.domain.entity

import android.content.res.Resources
import java.io.OutputStream

data class ExportAct(val out: OutputStream, val resources: Resources, val passports: List<Passport>)
