package com.stdio.uploadimage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stdio.uploadimage.R
import com.stdio.uploadimage.utils.ImageUtils.createPreviewPng
import com.stdio.uploadimage.viewmodel.ImageViewModel
import org.koin.androidx.compose.koinViewModel

private const val MAX_PREVIEW_SIZE = 800

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewmodel = koinViewModel<ImageViewModel>()
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val originalWidth = 1000
    val originalHeight = 90000
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Row {
            OutlinedTextField(
                label = { Text(stringResource(R.string.width)) },
                value = width,
                onValueChange = { width = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.height)) },
                value = height,
                onValueChange = { height = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }

        Row(modifier = Modifier.padding(top = 30.dp)) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                onClick = {

                }) {
                Text(stringResource(R.string.generate))
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                onClick = {

                }) {
                Text(stringResource(R.string.upload))
            }
        }

        val previewPng by remember(originalWidth, originalHeight) {
            derivedStateOf {
                createPreviewPng(originalWidth, originalHeight, MAX_PREVIEW_SIZE)
            }
        }
        AsyncImage(
            model = previewPng,
            contentDescription = "Preview",
            modifier = modifier.fillMaxSize()
        )
    }

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