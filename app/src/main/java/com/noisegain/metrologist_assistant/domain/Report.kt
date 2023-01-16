package com.noisegain.metrologist_assistant.domain

import java.io.OutputStream

data class Report(val out: OutputStream, val passports: List<Passport>)
