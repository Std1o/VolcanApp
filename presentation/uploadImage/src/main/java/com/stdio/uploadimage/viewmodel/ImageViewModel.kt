package com.stdio.uploadimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.domain.usecase.CloseStreamUseCase
import com.stdio.domain.usecase.UploadImageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val closeStreamUseCase: CloseStreamUseCase
) : ViewModel() {

    fun test(pngFlow: Flow<ByteArray>) {
        viewModelScope.launch {
            uploadImageUseCase(pngFlow)
            closeStreamUseCase()
        }
    }
}