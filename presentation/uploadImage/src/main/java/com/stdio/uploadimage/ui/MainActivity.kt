package com.stdio.uploadimage.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.stdio.uploadimage.ui.theme.VolcanAppTheme
import com.stdio.uploadimage.utils.ImageUtils.createPreviewPng
import com.stdio.uploadimage.viewmodel.ImageViewModel
import org.koin.androidx.compose.koinViewModel

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
    val viewmodel = koinViewModel<ImageViewModel>()

    val originalWidth = 1000
    val originalHeight = 90000
    val maxPreviewSize = 800

    val previewPng by remember(originalWidth, originalHeight) {
        derivedStateOf {
            createPreviewPng(originalWidth, originalHeight, maxPreviewSize)
        }
    }

    AsyncImage(
        model = previewPng,
        contentDescription = "Preview",
        modifier = modifier.fillMaxSize()
    )

    LaunchedEffect(Unit) {
        try {
            val fullPngFlow = viewmodel.generatePng(
                originalWidth,
                originalHeight
            )
            viewmodel.test(fullPngFlow)
        } catch (e: Exception) {
            println("Upload failed: ${e.message}")
        }
    }
}