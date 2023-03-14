package com.noisegain.metrologist_assistant.data

import androidx.room.*
import com.noisegain.metrologist_assistant.domain.entity.Exported
import com.noisegain.metrologist_assistant.domain.entity.Passport

@Dao
interface ExportedDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passport: Exported)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passports: List<Exported>)

    @Delete
    suspend fun delete(passport: Exported)

    @Query("SELECT * FROM exported")
    suspend fun getAll(): List<Exported>

    @Query("DELETE FROM exported")
    suspend fun clear()
}