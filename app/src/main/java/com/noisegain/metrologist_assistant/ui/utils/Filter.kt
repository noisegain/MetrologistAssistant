package com.noisegain.metrologist_assistant.ui.utils

import com.noisegain.metrologist_assistant.domain.Passport

interface Filter {
    val filter: (Passport) -> Boolean
    operator fun invoke() = filter
}

sealed class Filters(override val filter: (Passport) -> Boolean) : Filter {
    class ByName(name: String) : Filters({
        it.name.contains(name, true)
    })
    class ByDivision(division: String) : Filters({
        it.division.contains(division, true)
    })
    class ByMVZ(mvz: String) : Filters({
        it.mvz.contains(mvz, true)
    })
    object Verified : Filters({
        TODO()
    })
    object UnVerified : Filters({
        TODO()
    })
    object InRepair : Filters({
        TODO()
    })
    object InConservation : Filters({
        TODO()
    })
    object WrittenOff : Filters({
        TODO()
    })
    object All : Filters({
        true
    })
}
