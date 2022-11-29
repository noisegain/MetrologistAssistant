package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.MainRepository
import timber.log.Timber

class MainRepositoryLoggerImpl(
    private val mainRep: MainRepository
) : MainRepository by mainRep {
    override fun callDb(): String {
        Timber.d("Logging callDB")
        return mainRep.callDb()
    }
}