package com.stdio.uploadimage.model

data class ImageUiState(
    val widthInput: String = "1000",
    val heightInput: String = "90000",
    val isGenerating: Boolean = false,
    val isUploading: Boolean = false,
    val generatedWidth: Int = 0,
    val generatedHeight: Int = 0,
    val errorMessage: String? = null,
    val uploadSuccess: Boolean? = null
)