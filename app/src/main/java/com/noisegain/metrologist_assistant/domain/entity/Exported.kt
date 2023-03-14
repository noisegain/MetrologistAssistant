package com.noisegain.metrologist_assistant.domain.entity

import androidx.room.Entity
import com.noisegain.metrologist_assistant.domain.writer.ExcelType


@Entity(tableName = "exported", primaryKeys = ["name"])
data class Exported(val name: String, val type: ExcelType) {
    val uri: String
        get() = "$name.xlsx"
}
