package com.stdio.domain.usecase

import com.stdio.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class UploadImageUseCase(private val repository: ImageRepository) {
    suspend operator fun invoke(pngFlow: Flow<ByteArray>) = repository.uploadImage(pngFlow)
}