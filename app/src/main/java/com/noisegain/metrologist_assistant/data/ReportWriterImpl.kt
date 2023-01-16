package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.Report
import com.noisegain.metrologist_assistant.domain.ReportWriter
import io.github.evanrupert.excelkt.workbook
import org.apache.poi.ss.usermodel.BorderStyle
import javax.inject.Inject


class ReportWriterImpl @Inject constructor() : ReportWriter {
    override fun write(report: Report) {
        workbook {
            sheet {
                xssfSheet.createFreezePane(0, 1)
                val style = createCellStyle {
                    setFont(createFont {
                        bold = true
                    })
                    borderTop = BorderStyle.MEDIUM
                    borderBottom = BorderStyle.MEDIUM
                    borderLeft = BorderStyle.MEDIUM
                    borderRight = BorderStyle.MEDIUM
                    wrapText = true
                }
                row(style) {
                    cell("№ п/п")
                    cell("Участок")
                    cell("МВЗ")
                    cell("Тип услуги")
                    cell("Наименование СИ")
                    cell("Тип (модификация) СИ")
                    cell("Номер СИ")
                    cell("Класс точности")
                    cell("Предел (диапазон)")
                    cell("Место проведения поверки")
                    cell("Периодичность проведения поверки")
                    cell("Срок проведения поверки")
                    cell("Количество СИ")
                    cell("Кратность")
                    cell("Дата поверки")
                    cell("Дата следующей поверки")
                    cell("Примечание")
                }
                var index = 0
                report.passports.groupBy { it.mvz }.values.forEach {
                    it.forEach { passport ->
                        row {
                            cell(++index)
                            cell(passport.division)
                            cell(passport.mvz)
                            cell(passport.characteristics.metrologic.type)
                            cell(passport.name)
                            cell(passport.type)
                            cell(passport.id)
                            cell(passport.characteristics.technical.accuracy)
                            cell(passport.characteristics.technical.limit)
                            cell(passport.characteristics.metrologic.lab)
                            cell(passport.characteristics.metrologic.period)
                            cell(passport.characteristics.metrologic.next)
                            cell(passport.count ?: "")
                            cell(passport.characteristics.technical.count)
                            cell(passport.characteristics.metrologic.lastAsString)
                            cell(passport.characteristics.metrologic.nextDateAsString)
                            cell(passport.notes)
                        }
                    }
                    row {
                        cell("Кол-во СИ")
                        cell(it.size)
                    }
                }
            }
        }.xssfWorkbook.write(report.out)
    }
}