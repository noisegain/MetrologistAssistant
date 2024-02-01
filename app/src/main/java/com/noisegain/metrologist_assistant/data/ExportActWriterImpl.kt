package com.noisegain.metrologist_assistant.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.noisegain.metrologist_assistant.R
import com.noisegain.metrologist_assistant.domain.entity.ExportAct
import com.noisegain.metrologist_assistant.domain.writer.ExcelWriter
import com.noisegain.metrologist_assistant.domain.writer.ExportActWriter
import io.github.evanrupert.excelkt.workbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class ExportActWriterImpl @Inject constructor(
    override var styles: EnumMap<ExcelWriter.Styles, XSSFCellStyle>
) : ExportActWriter {
    override fun write(data: ExportAct) {
        workbook {
            init()
            sheet {
                row {
                    cell("№ п/п")
                    cell("Наименование материала")
                    cell("Заводской №")
                    cell("Кол-во, штук")
                }
                // Get the drawable resource ID
                val resourceId = R.drawable.img_suek

                // Load the drawable as a Bitmap
                val bitmap = BitmapFactory.decodeResource(data.resources, resourceId)

                // Convert the Bitmap to a ByteArray
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageBytes = outputStream.toByteArray()

                val drawing = xssfSheet.createDrawingPatriarch()

                val anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 3, 3)

                val pictureIndex = xssfWorkbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG)
                drawing.createPicture(anchor, pictureIndex)
            }
        }.xssfWorkbook.write(data.out)
    }
}