package com.noisegain.metrologist_assistant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noisegain.metrologist_assistant.domain.entity.Exported
import com.noisegain.metrologist_assistant.domain.entity.Passport

@Database(entities = [Passport::class, Exported::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passportsDAO(): PassportsDAO
    abstract fun exportedDAO(): ExportedDAO
}