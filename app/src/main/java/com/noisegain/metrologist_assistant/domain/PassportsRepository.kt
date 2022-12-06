package com.noisegain.metrologist_assistant.domain

interface PassportsRepository {
    suspend fun first() : Passport
    suspend fun addPassport(passport: Passport)
    suspend fun deletePassport(passport: Passport)
    suspend fun getAll(): List<Passport>
}