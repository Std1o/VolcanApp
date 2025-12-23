package com.stdio.domain.usecase

import com.stdio.domain.repository.ImageRepository

class UploadImageUseCase(private val repository: ImageRepository) {
    suspend operator fun invoke(data: ByteArray) = repository.uploadImage(data)
}