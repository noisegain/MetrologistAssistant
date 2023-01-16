package com.noisegain.metrologist_assistant.domain

interface ReportWriter {
    fun write(report: Report)
}