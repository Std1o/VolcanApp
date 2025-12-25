package com.stdio.domain.di

import com.stdio.domain.repository.ImageRepository
import com.stdio.domain.usecase.CloseStreamUseCase
import com.stdio.domain.usecase.GeneratePngUseCase
import com.stdio.domain.usecase.UploadImageUseCase
import org.koin.dsl.module

val domainModule = module {
    fun providesUploadImageUseCase(repository: ImageRepository) = UploadImageUseCase(repository)

    fun providesCloseStreamUseCase(repository: ImageRepository) = CloseStreamUseCase(repository)

    fun providesGeneratePngUseCase() = GeneratePngUseCase()

    single { providesUploadImageUseCase(get()) }
    single { providesCloseStreamUseCase(get()) }
    single { providesGeneratePngUseCase() }
}