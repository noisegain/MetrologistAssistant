package com.noisegain.metrologist_assistant.domain.passport

enum class State {
    OPERABLE,
    VALIDATION,
    PRR,
    REPAIR,
    CONSERVATION,
    DISCARDED;

    override fun toString(): String {
        return when (this) {
            OPERABLE -> "В эксплуатации"
            VALIDATION -> "На поверке"
            PRR -> "На ПРР"
            REPAIR -> "В ремонте"
            CONSERVATION -> "На консервации"
            DISCARDED -> "Списан"
        }
    }
}
