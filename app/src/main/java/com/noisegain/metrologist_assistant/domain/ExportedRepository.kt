package com.noisegain.metrologist_assistant.domain

import com.noisegain.metrologist_assistant.domain.entity.Exported

interface ExportedRepository {
    suspend fun addExported(exported: Exported)
    suspend fun addExports(exported: List<Exported>)
    suspend fun deleteExport(exported: Exported)
    suspend fun getAll(): List<Exported>
    suspend fun clear()
}