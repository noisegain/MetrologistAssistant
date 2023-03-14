package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import javax.inject.Inject

class PassportsRepositoryImpl @Inject constructor(
    private val passportsDAO: PassportsDAO
) : PassportsRepository {

    override suspend fun addPassport(passport: Passport) {
        passportsDAO.insert(passport)
    }

    override suspend fun addPassports(passports: List<Passport>) {
        passportsDAO.insert(passports)
    }

    override suspend fun deletePassport(passport: Passport) {
        passportsDAO.delete(passport)
    }

    override suspend fun getAll(): List<Passport> = passportsDAO.getAll()

    override suspend fun clear() {
        passportsDAO.clear()
    }
}
