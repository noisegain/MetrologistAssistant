package com.noisegain.metrologist_assistant.core

import com.noisegain.metrologist_assistant.domain.entity.Passport
import timber.log.Timber
import java.util.*

fun log(vararg args: Any?) = Timber.tag("METROLOGIST").d(args.contentToString())
fun log(message: Any?) = Timber.tag("METROLOGIST").d(message.toString())

val Passport.valid
    get() = characteristics?.metrologic?.let {
        it.next
    }

val Passport.uuid
    get() = mvz + UUID.randomUUID().toString()
