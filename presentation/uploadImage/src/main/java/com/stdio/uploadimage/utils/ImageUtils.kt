package com.stdio.uploadimage.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun createPreviewPng(
        originalWidth: Int,
        originalHeight: Int,
        maxSize: Int
    ): ByteArray {
        val scale = if (originalWidth > originalHeight) {
            maxSize.toFloat() / originalWidth
        } else {
            maxSize.toFloat() / originalHeight
        }

        val previewWidth = (originalWidth * scale).toInt().coerceAtLeast(1)
        val previewHeight = (originalHeight * scale).toInt().coerceAtLeast(1)

        val bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.RED)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        bitmap.recycle()

        return outputStream.toByteArray()
    }
}