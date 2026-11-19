package com.plexus.example.libs.androidutils.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun Context.openPdf(pdf: File) {
    val intent = Intent(Intent.ACTION_VIEW)
        .setDataAndType(pdfUri(pdf), PDF_MIME_TYPE)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        startActivity(Intent.createChooser(intent, null))
    }
}

fun Context.sharePdf(pdf: File) {
    val intent = Intent(Intent.ACTION_SEND)
        .setType(PDF_MIME_TYPE)
        .putExtra(Intent.EXTRA_STREAM, pdfUri(pdf))
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(Intent.createChooser(intent, null))
}

private fun Context.pdfUri(pdf: File) = FileProvider.getUriForFile(this, "$packageName.fileprovider", pdf)

private const val PDF_MIME_TYPE = "application/pdf"
