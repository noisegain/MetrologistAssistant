package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import javax.inject.Inject

class PassportsRepositoryImpl @Inject constructor(
    private val passportsDAO: PassportsDAO
) : PassportsRepository {
    override suspend fun first(): Passport {
        return passportsDAO.getAll().first()
    }

    override suspend fun addPassport(passport: Passport) {
        passportsDAO.insert(passport)
    }

    override suspend fun deletePassport(passport: Passport) {
        passportsDAO.delete(passport)
    }

    override suspend fun getAll(): List<Passport> = passportsDAO.getAll()
}
