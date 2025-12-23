package com.stdio.data.repository

import com.stdio.data.remote.ImageRemoteDataSource
import com.stdio.domain.repository.ImageRepository

class ImageRepositoryImpl(private val remoteDataSource: ImageRemoteDataSource) : ImageRepository {
    override suspend fun uploadImage(data: ByteArray): Int =
        remoteDataSource.uploadImage(data)

    override fun close() = remoteDataSource.close()
}