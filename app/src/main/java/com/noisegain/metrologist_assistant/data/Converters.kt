package com.noisegain.metrologist_assistant.data

import androidx.room.TypeConverter
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.Chronics
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromCharacteristics(characteristics: Characteristics?): String? {
        return characteristics?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun characteristicsFromString(string: String?): Characteristics? {
        return string?.let { Json.decodeFromString(string) }
    }

    @TypeConverter
    fun fromChronics(chronics: Chronics?): String? {
        return chronics?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun chronicsFromString(string: String?): Chronics? {
        return string?.let { Json.decodeFromString(string) }
    }
}
