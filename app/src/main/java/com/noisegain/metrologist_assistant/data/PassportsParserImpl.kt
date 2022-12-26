package com.noisegain.metrologist_assistant.data

import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.domain.PassportsParser
import com.noisegain.metrologist_assistant.domain.passport.Characteristics
import com.noisegain.metrologist_assistant.domain.passport.characteristics.Metrologic
import com.noisegain.metrologist_assistant.domain.passport.characteristics.Technical
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.InputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
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
            // 13 Дата поверки, 14 Сумма без НДС, руб., 15 Всего с НДС, руб.]
            log(lines.joinToString("\n") { it.joinToString("|||") })
            return lines.map {
                Passport(
                    division = it[0],
                    mvz = it[1],
                    type = it[4],
                    name = it[3],
                    id = it[5],
                    characteristics = Characteristics(
                        Technical(info = listOf(it[6])),
                        Metrologic(
                            last = SimpleDateFormat(
                                "dd.MM.yyyy",
                                Locale.getDefault()
                            ).parse(it[13])?.time ?: 0L,
                            next = it[10].toLongOrNull(),
                            period = it[9].toIntOrNull(),
                            type = it[2],
                            lab = it[8]
                        )
                    ),
                    count = it[11].toIntOrNull(),
                    costRaw = it[14].replace(" ", "").replace(',', '.').toDoubleOrNull(),
                    costFull = it[15].replace(" ", "").replace(',', '.').toDoubleOrNull()
                )
            }
        }
        return emptyList()
    }
}