package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.MainRepository

class MainRepositoryImpl : MainRepository {
    override fun callDb(): String {
        return "noisegain"
    }
}
