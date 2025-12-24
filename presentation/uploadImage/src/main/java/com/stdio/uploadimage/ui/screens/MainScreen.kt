package com.stdio.uploadimage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stdio.uploadimage.R
import com.stdio.uploadimage.viewmodel.ImageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ImageViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    val canGenerate by remember(uiState) {
        derivedStateOf {
            uiState.value.widthInput.isNotBlank() && uiState.value.heightInput.isNotBlank() && !uiState.value.isGenerating
        }
    }
    val canUpload by remember(uiState, viewModel.previewPng) {
        derivedStateOf {
            !uiState.value.isGenerating && viewModel.previewPng != null && !uiState.value.isUploading
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Поля ввода
        Row {
            OutlinedTextField(
                label = { Text(stringResource(R.string.width)) },
                value = uiState.value.widthInput,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { viewModel.onWidthChanged(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.height)) },
                value = uiState.value.heightInput,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { viewModel.onHeightChanged(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        // Кнопки
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onClick = { viewModel.generateImage() },
                enabled = canGenerate
            ) {
                if (uiState.value.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(stringResource(R.string.generate))
                }
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onClick = { viewModel.uploadImage() },
                enabled = canUpload
            ) {
                if (uiState.value.isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(stringResource(R.string.upload))
                }
            }
        }

        // Превью изображения
        viewModel.previewPng?.let { previewPng ->
            AsyncImage(
                model = previewPng,
                contentDescription = "Preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        // Сообщения об успехе
        uiState.value.uploadSuccess?.let { success ->
            Text(
                text = stringResource(if (success) R.string.upload_is_successful else R.string.upload_error),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Сообщения об ошибках
        uiState.value.errorMessage?.let { error ->
            Text(
                color = Color.Red,
                text = error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}