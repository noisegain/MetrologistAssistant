package com.noisegain.metrologist_assistant.domain.entity

import java.io.OutputStream

data class Report(val out: OutputStream, val passports: List<Passport>, val type: Type) {
    enum class Type(name: String) {
        ByMonth("По месяцам"), ByMVZ("По МВЗ")
    }
}
