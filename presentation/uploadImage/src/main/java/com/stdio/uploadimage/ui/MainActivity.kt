package com.stdio.uploadimage.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.graphics.createBitmap
import coil.compose.AsyncImage
import com.stdio.uploadimage.AsyncStreamUploader
import com.stdio.uploadimage.ui.theme.VolcanAppTheme
import com.stdio.uploadimage.viewmodel.ImageViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VolcanAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val imageBytes = createWhiteRectanglePng(150, 200)
    AsyncImage(
        contentDescription = "",
        model = imageBytes
    )
    val viewmodel = koinViewModel<ImageViewModel>()
    viewmodel.test()
    LaunchedEffect(imageBytes) {
        val uploader = AsyncStreamUploader()

        try {

            println("Starting upload of ${imageBytes.size} bytes...")

            val result = uploader.streamUpload(
                imageBytes,
                "http://192.168.1.12:80/upload"
            )

            println("Upload result: $result")
        } catch (e: Exception) {
            println("Upload failed: ${e.message}")
        } finally {
            uploader.close()
        }
    }
}

private fun createWhiteRectanglePng(width: Int, height: Int): ByteArray {
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)

    canvas.drawColor(Color.WHITE)

    val borderPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), borderPaint)

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}