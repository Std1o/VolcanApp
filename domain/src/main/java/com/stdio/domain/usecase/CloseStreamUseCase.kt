package com.stdio.domain.usecase

import com.stdio.domain.repository.ImageRepository

class CloseStreamUseCase(private val repository: ImageRepository) {
    operator fun invoke() = repository.close()
}