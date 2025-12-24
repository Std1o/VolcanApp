package com.stdio.uploadimage.model

data class ImageUiState(
    val widthInput: String = "",
    val heightInput: String = "",
    val isGenerating: Boolean = false,
    val isUploading: Boolean = false,
    val errorMessage: String? = null,
    val uploadSuccess: Boolean? = null
)