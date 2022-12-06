package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import timber.log.Timber

class PassportsRepositoryLoggerImpl(
    private val mainRep: PassportsRepository
) : PassportsRepository by mainRep {
    override suspend fun first(): Passport {
        Timber.d("Logging first")
        return mainRep.first()
    }
}