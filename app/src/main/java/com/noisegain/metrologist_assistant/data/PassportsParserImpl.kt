package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.PassportsParser
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.State
import com.noisegain.metrologist_assistant.domain.passport.characteristics.Metrologic
import com.noisegain.metrologist_assistant.domain.passport.characteristics.Technical
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.InputStream
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import javax.inject.Inject

class PassportsParserImpl @Inject constructor() : PassportsParser {
    override fun parse(inputStream: InputStream): List<Passport> {
        inputStream.use {
            val csvParser = CSVParserBuilder().withSeparator(';').withQuoteChar('"').build()
            val csvReader =
                CSVReaderBuilder(it.bufferedReader(Charset.forName("windows-1251"))).withCSVParser(
                    csvParser
                ).build()
            val header = csvReader.readNext()
            val lines = csvReader.readAll()
            log(header.mapIndexed { index, s -> "$index $s" })
            //[0 Участок, 1 МВЗ, 2 Тип услуги, 3 Наименование средства измерения, 4 Тип (модификация) средств измерений, 5 Идентификационный номер СИ,
            // 6 Метрологические характеристики, 7 Метрологические Предел (диапазон) измерений, 8 Место проведения поверки,
            // 9 Периодичность проведения  поверки (в месяцах), 10 Срок проведения поверки(указывать в календарном порядке с 1 по 12 м-ц),
            // 11 Количество СИ (шт.), 12 Крастность (количество каналов, количество шт. в наборе),
            // 13 Дата поверки, 14 Сумма без НДС, руб., 15 статус, 16 примечания]
//            log(lines.joinToString("\n") { it.joinToString("|||") })
            fun excelDouble(value: String) = value.replace(" ", "").replace(',', '.').toDoubleOrNull()
            return lines.filter { it.any(String::isNotBlank) }.map {
                val last2 = try {
                    LocalDate.parse(it[13], DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
                } catch (_: DateTimeParseException) {
                    null
                }
                val period = it[9].toIntOrNull() ?: 12
                val nextDate = last2?.plusDays(-1)?.plusMonths(period.toLong())
                val next = it[10].toIntOrNull() ?: nextDate?.monthValue
//                val nextDate = nextRaw.withMonth(next)
                fun calcTaxes(c: Double?) = c?.times(1.2)
                Passport(
                    division = it[0],
                    mvz = it[1],
                    type = it[4],
                    name = it[3],
                    id = it[5],
                    characteristics = Characteristics(
                        Technical(limit = it[7], accuracy = it[6], count = it[12].toInt()),
                        Metrologic(
                            last = last2?.toEpochDay() ?: Metrologic.DISCARDED,
                            next = next ?: Metrologic.NEXT_UNKNOWN,
                            nextDate = nextDate?.toEpochDay() ?: Metrologic.DISCARDED,
                            period = period,
                            type = it[2],
                            lab = it[8],
                        )
                    ),
                    count = it[11].toIntOrNull(),
                    costRaw = excelDouble(it[14]),
                    costFull = calcTaxes(excelDouble(it[14])),
                    notes = it[16],
                    status = if (it[15].isNotBlank()) {
                        if ("В Поверке" in it[15]) State.VALIDATION else State.OPERABLE
                    } else State.OPERABLE
                )
            }
        }
    }
}