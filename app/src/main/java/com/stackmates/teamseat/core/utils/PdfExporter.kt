package com.stackmates.teamseat.core.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import java.io.File
import java.io.FileOutputStream

/**
 * 💻 أداة توليد كشف التيم الرسمي كملف PDF (Native Android PdfDocument)
 * جاهزة لتربطها بزر التصدير في شاشة آلاء (TASheetPreviewScreen).
 */
object PdfExporter {

    fun exportTeamSheetPdf(
        context: Context,
        team: Team,
        seats: List<Seat>
    ): Result<File> = runCatching {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // مقاس A4 قياسي
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
        }
        val bodyPaint = Paint().apply {
            textSize = 14f
        }

        var y = 60f
        canvas.drawText("كشف اعتماد تيم المشروع: ${team.projectName}", 50f, y, titlePaint)
        y += 35f
        canvas.drawText("الفئة / المادة: ${team.category}", 50f, y, bodyPaint)
        y += 25f
        canvas.drawText("المشرف الأكاديمي: ${team.supervisorDoctor}", 50f, y, bodyPaint)
        y += 25f
        canvas.drawText("المعيدة المسؤولة: ${team.teachingAssistant}", 50f, y, bodyPaint)
        y += 35f
        canvas.drawText("قائد التيم: ${team.leaderName}", 50f, y, titlePaint)
        y += 35f

        seats.forEach { seat ->
            val memberLabel = if (seat.status == SeatStatus.BOOKED) {
                seat.applicantName ?: "محجوز"
            } else {
                "فارغ (${seat.status.name})"
            }
            canvas.drawText(
                "مقعد ${seat.seatNumber} [${seat.roleRequired}]: $memberLabel",
                50f,
                y,
                bodyPaint
            )
            y += 26f
        }

        document.finishPage(page)

        val outputFile = File(context.cacheDir, "TeamSheet_${team.teamId}.pdf")
        FileOutputStream(outputFile).use { out ->
            document.writeTo(out)
        }
        document.close()
        outputFile
    }
}
