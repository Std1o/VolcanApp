package com.stdio.data.di

import android.util.Log
import com.stdio.data.remote.ImageRemoteDataSource
import com.stdio.data.repository.ImageRepositoryImpl
import com.stdio.domain.repository.ImageRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.dsl.module

val dataModule = module {
    fun providesKtorClient(): HttpClient = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HTTP", message)
                }
            }
            level = LogLevel.ALL
        }
    }

    fun provideImageRemoteDataSource(client: HttpClient): ImageRemoteDataSource =
        ImageRemoteDataSource(client)

    fun provideImageRepository(remoteDataSource: ImageRemoteDataSource): ImageRepository =
        ImageRepositoryImpl(remoteDataSource)

    single { providesKtorClient() }
    single { provideImageRemoteDataSource(get()) }
    single { provideImageRepository(get()) }

}