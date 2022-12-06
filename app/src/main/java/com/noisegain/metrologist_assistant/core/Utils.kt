package com.noisegain.metrologist_assistant.core

import timber.log.Timber

fun log(vararg args: Any?) = Timber.tag("METROLOGIST").d(args.contentToString())
fun log(message: Any?) = Timber.tag("METROLOGIST").d(message.toString())