package com.noisegain.metrologist_assistant.domain

import java.io.InputStream

fun interface Parser<T> {
    fun parse(inputStream: InputStream): T
}