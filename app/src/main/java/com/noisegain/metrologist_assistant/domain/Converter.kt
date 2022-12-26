package com.noisegain.metrologist_assistant.domain

interface Converter<T> {
    fun toString(value: T): String
    fun toLong(value: T): Long
    fun fromString(value: String): T
    fun fromLong(value: Long): T
}
