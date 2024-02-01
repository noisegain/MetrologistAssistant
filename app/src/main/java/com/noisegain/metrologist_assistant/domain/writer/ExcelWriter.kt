package com.noisegain.metrologist_assistant.domain.writer

import io.github.evanrupert.excelkt.Workbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.util.*

interface ExcelWriter<T> {
    fun write(data: T)

    var styles: EnumMap<Styles, XSSFCellStyle>
    fun Workbook.init() {
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
        val money = createCellStyle {
            dataFormat = xssfWorkbook.createDataFormat().getFormat("#0.00")
        }
        styles = EnumMap<Styles, XSSFCellStyle>(Styles::class.java).apply {
            set(Styles.HEADER, headerStyle)
            set(Styles.BORDER, borderStyle)
            set(Styles.STYLE, style)
            set(Styles.MONEY, money)
        }
    }
    enum class Styles {
        HEADER, BORDER, STYLE, MONEY
    }
}