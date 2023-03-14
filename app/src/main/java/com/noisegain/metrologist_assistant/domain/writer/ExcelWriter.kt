package com.noisegain.metrologist_assistant.domain.writer

import io.github.evanrupert.excelkt.Workbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle

interface ExcelWriter<T> {
    fun write(data: T)
    fun Workbook.init(): Styles {
        val bold = createFont {
            bold = true
        }
        val headerStyle = createCellStyle {
            setFont(bold)
            borderTop = BorderStyle.MEDIUM
            borderBottom = BorderStyle.MEDIUM
            borderLeft = BorderStyle.MEDIUM
            borderRight = BorderStyle.MEDIUM
            wrapText = true
        }
        val borderStyle = createCellStyle {
            setFont(bold)
            borderTop = BorderStyle.DASHED
            borderBottom = BorderStyle.MEDIUM
            wrapText = true
        }
        val style = createCellStyle {
            wrapText = true
        }
        return Styles(headerStyle, borderStyle, style)
    }

    data class Styles(val headerStyle: XSSFCellStyle, val borderStyle: XSSFCellStyle, val style: XSSFCellStyle)
}