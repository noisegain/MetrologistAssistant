package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.entity.Report
import com.noisegain.metrologist_assistant.domain.writer.ReportWriter
import io.github.evanrupert.excelkt.workbook
import org.apache.poi.ss.usermodel.BorderStyle
import javax.inject.Inject


class ReportWriterImpl @Inject constructor() : ReportWriter {
    override fun write(data: Report) {
        workbook {
            val (headerStyle, borderStyle, style) = init()
            sheet {
                xssfSheet.createFreezePane(0, 1)
                row(headerStyle) {
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
                    cell("Цена без НДС")
                    cell("Цена с НДС")
                    cell("Примечание")
                }
                xssfSheet.setColumnWidth(1, 15 * 256)
                xssfSheet.setColumnWidth(4, 15 * 256)
                xssfSheet.setColumnWidth(5, 10 * 256)
                var index = 0
                fun borderRow(list: List<Passport>) {
                    row(borderStyle) {
                        cell("Кол-во позиций:")
                        cell(list.size)
                        cell("")
                        cell("Кол-во СИ:")
                        cell(list.sumOf { passport -> passport.count ?: 0 })
                        repeat(9) {
                            cell("")
                        }
                        cell("Сумма без НДС:")
                        cell(list.sumOf { passport ->
                            (passport.costRaw ?: 0.0) * (passport.count ?: 0)
                        })
                        cell("")
                        cell("Сумма с НДС:")
                        cell(list.sumOf { passport ->
                            (passport.costFull ?: 0.0) * (passport.count ?: 0)
                        })
                    }
                }

                val passports = when (data.type) {
                    Report.Type.ByMVZ -> data.passports.groupBy { it.mvz }.values.sortedBy { it[0].mvz }
                    Report.Type.ByMonth -> data.passports.groupBy { it.characteristics.metrologic.next }
                        .values.sortedBy { it[0].characteristics.metrologic.next }
                }
                passports.forEach {
                    if (data.type == Report.Type.ByMonth) {
                        row {}
                        row(borderStyle) {
                            cell("Месяц:")
                            // get month name from int
                            fun monthName(month: Int): String {
                                return when (month) {
                                    1 -> "Январь"
                                    2 -> "Февраль"
                                    3 -> "Март"
                                    4 -> "Апрель"
                                    5 -> "Май"
                                    6 -> "Июнь"
                                    7 -> "Июль"
                                    8 -> "Август"
                                    9 -> "Сентябрь"
                                    10 -> "Октябрь"
                                    11 -> "Ноябрь"
                                    12 -> "Декабрь"
                                    else -> "Неизвестный месяц"
                                }
                            }
                            val month = it[0].characteristics.metrologic.next
                            cell("$month - ${monthName(month)}")
                            repeat(17) { cell("") }
                        }
                    }
                    it.sortedWith(
                        compareBy(
                            Passport::mvz,
                            Passport::division,
                            Passport::name
                        )
                    ).forEach { passport ->
                        row(style) {
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
                            cell(passport.costRaw ?: "")
                            cell(passport.costFull ?: "")
                            cell(passport.notes)
                        }
                    }
                    borderRow(it)
                }
                borderRow(data.passports)
            }
        }.xssfWorkbook.write(data.out)
    }
}