package com.noisegain.metrologist_assistant.domain

interface PassportsRepository {
    suspend fun addPassport(passport: Passport)
    suspend fun addPassports(passports: List<Passport>)
    suspend fun deletePassport(passport: Passport)
    suspend fun getAll(): List<Passport>
}