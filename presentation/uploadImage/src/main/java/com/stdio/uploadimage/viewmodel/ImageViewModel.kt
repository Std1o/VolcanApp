package com.stdio.uploadimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.domain.usecase.CloseStreamUseCase
import com.stdio.domain.usecase.GeneratePngUseCase
import com.stdio.domain.usecase.UploadImageUseCase
import com.stdio.uploadimage.model.ImageUiState
import com.stdio.uploadimage.utils.ImageUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val closeStreamUseCase: CloseStreamUseCase,
    private val generatePngUseCase: GeneratePngUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    var previewPng: ByteArray? = null
        private set
    private var fullPngFlow: Flow<ByteArray>? = null

    fun onWidthChanged(width: String) {
        _uiState.update { it.copy(widthInput = width) }
    }

    fun onHeightChanged(height: String) {
        _uiState.update { it.copy(heightInput = height) }
    }

    fun generateImage() {
        val width = _uiState.value.widthInput.toIntOrNull() ?: 0
        val height = _uiState.value.heightInput.toIntOrNull() ?: 0

        _uiState.update {
            it.copy(
                isGenerating = true,
                errorMessage = null,
                uploadSuccess = null
            )
        }

        viewModelScope.launch {
            try {
                val previewPng = ImageUtils.createPreviewPng(width, height, MAX_PREVIEW_SIZE)

                val fullPngFlow = generatePngUseCase(width = width, height = height)

                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        generatedWidth = width,
                        generatedHeight = height
                    )
                }
                this@ImageViewModel.fullPngFlow = fullPngFlow
                this@ImageViewModel.previewPng = previewPng
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        errorMessage = e.message ?: "Ошибка генерации"
                    )
                }
            }
        }
    }

    fun uploadImage() {
        val flow = fullPngFlow ?: return

        _uiState.update {
            it.copy(
                isUploading = true,
                errorMessage = null,
                uploadSuccess = null
            )
        }

        viewModelScope.launch {
            try {
                val result = uploadImageUseCase(flow)
                closeStreamUseCase()
                _uiState.update {
                    it.copy(
                        isUploading = false,
                        uploadSuccess = result == RESPONSE_OK
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isUploading = false,
                        errorMessage = e.message ?: "Ошибка загрузки",
                        uploadSuccess = false
                    )
                }
            }
        }
    }

    companion object {
        private const val MAX_PREVIEW_SIZE = 800
        private const val RESPONSE_OK = 200
    }
}