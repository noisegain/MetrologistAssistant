package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.ExportedRepository
import com.noisegain.metrologist_assistant.domain.entity.Exported
import javax.inject.Inject

class ExportedRepositoryImpl @Inject constructor(
    private val exportedDAO: ExportedDAO
) : ExportedRepository {

    override suspend fun addExported(exported: Exported) {
        exportedDAO.insert(exported)
    }

    override suspend fun addExports(exports: List<Exported>) {
        exportedDAO.insert(exports)
    }

    override suspend fun deleteExport(exported: Exported) {
        exportedDAO.delete(exported)
    }

    override suspend fun getAll(): List<Exported> = exportedDAO.getAll()

    override suspend fun clear() {
        exportedDAO.clear()
    }
}
