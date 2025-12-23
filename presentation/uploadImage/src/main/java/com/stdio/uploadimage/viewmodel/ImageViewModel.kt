package com.stdio.uploadimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.domain.usecase.CloseStreamUseCase
import com.stdio.domain.usecase.UploadImageUseCase
import kotlinx.coroutines.launch

class ImageViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val closeStreamUseCase: CloseStreamUseCase
) : ViewModel() {

    fun test(data: ByteArray) {
        viewModelScope.launch {
            uploadImageUseCase(data)
            closeStreamUseCase()
        }
    }
}