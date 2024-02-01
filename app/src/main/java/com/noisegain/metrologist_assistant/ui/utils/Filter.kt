package com.noisegain.metrologist_assistant.ui.utils

import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.passport.State
import java.time.LocalDate

interface Filter {
    val filter: (Passport) -> Boolean
    operator fun invoke() = filter

    companion object {
        fun combine(vararg filters: Filter): Filter {
            return object : Filter {
                override val filter: (Passport) -> Boolean =
                    { passport -> filters.all { it.filter(passport) } }
            }
        }

        fun new(f: (Passport) -> Boolean): Filter {
            return object : Filter {
                override val filter = f
            }
        }
    }
}

sealed class Filters(override val filter: (Passport) -> Boolean) : Filter {
    class ByName(name: String) : Filters(getFilterOrAll(name, compareBy(Passport::name, name)))

    class ByDivision(division: String) :
        Filters(getFilterOrAll(division, compareBy(Passport::division, division)))

    class ByMVZ(mvz: String) : Filters(getFilterOrAll(mvz, compareBy(Passport::mvz, mvz)))

    object Verified : Filters({
        it.characteristics.metrologic.nextDateAsDate.year > LocalDate.now().year
    })

    object UnVerified : Filters({
        it.characteristics.metrologic.nextDateAsDate.year == LocalDate.now().year
    })

    object InRepair : Filters({
        it.status == State.REPAIR
    })

    object InConservation : Filters({
        it.status == State.CONSERVATION
    })

    object WrittenOff : Filters({
        it.status == State.DISCARDED
    })

    object InValidation : Filters({
        it.status == State.VALIDATION
    })

    object All : Filters({
        true
    })

    companion object {
        fun getFilterOrAll(field: String, filter: (Passport) -> Boolean) =
            if (field.isBlank()) All.filter else filter

        private fun avoidTransliteration(s: String) = s.uppercase().map { c ->
            when (c) {
                'А' -> 'A'
                'В' -> 'B'
                'Е' -> 'E'
                'К' -> 'K'
                'М' -> 'M'
                'Н' -> 'H'
                'О' -> 'O'
                'Р' -> 'P'
                'С' -> 'C'
                'Т' -> 'T'
                'Х' -> 'X'
                else -> c
            }
        }.joinToString(separator = "")

        private fun compareBy(
            field: (Passport) -> String,
            compareTo: String
        ): (Passport) -> Boolean = {
            avoidTransliteration(field(it)).contains(avoidTransliteration(compareTo), true)
        }

    }
}
