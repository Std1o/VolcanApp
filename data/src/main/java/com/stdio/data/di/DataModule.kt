package com.stdio.data.di

import android.util.Log
import com.stdio.data.BuildConfig
import com.stdio.data.remote.ImageRemoteDataSource
import com.stdio.data.repository.ImageRepositoryImpl
import com.stdio.domain.repository.ImageRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.dsl.module

private const val TAG = "HTTP"

val dataModule = module {
    fun providesKtorClient(): HttpClient = HttpClient {
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    fun provideImageRemoteDataSource(client: HttpClient): ImageRemoteDataSource =
        ImageRemoteDataSource(client)

    fun provideImageRepository(remoteDataSource: ImageRemoteDataSource): ImageRepository =
        ImageRepositoryImpl(remoteDataSource)

    factory { providesKtorClient() }
    factory { provideImageRemoteDataSource(get()) }
    factory { provideImageRepository(get()) }

}