package com.noisegain.metrologist_assistant.data

import androidx.room.*
import com.noisegain.metrologist_assistant.domain.entity.Passport

@Dao
interface PassportsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passport: Passport)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passports: List<Passport>)

    @Delete
    suspend fun delete(passport: Passport)

    @Query("SELECT * FROM passports")
    suspend fun getAll(): List<Passport>

    @Query("DELETE FROM passports")
    suspend fun clear()
}