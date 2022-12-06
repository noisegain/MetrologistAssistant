package com.noisegain.metrologist_assistant.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noisegain.metrologist_assistant.domain.Passport

@Dao
interface PassportsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passport: Passport)

    @Delete
    suspend fun delete(passport: Passport)

    @Query("SELECT * FROM passports")
    suspend fun getAll(): List<Passport>
}