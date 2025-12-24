package com.stdio.uploadimage.di

import com.stdio.uploadimage.viewmodel.ImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { ImageViewModel(get(), get(), get(), get()) }
}