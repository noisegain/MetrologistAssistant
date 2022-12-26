package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.Converter
import java.time.Instant

class InstantConverter: Converter<Instant> {
    override fun fromLong(value: Long): Instant {
        return Instant.ofEpochSecond(value)
    }

    override fun fromString(value: String): Instant {
        return Instant.parse(value)
    }

    override fun toLong(value: Instant): Long {
        return value.epochSecond
    }

    override fun toString(value: Instant): String {
        return value.toString()
    }
}
