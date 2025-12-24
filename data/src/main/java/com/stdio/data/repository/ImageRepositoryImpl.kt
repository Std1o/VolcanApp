package com.stdio.data.repository

import com.stdio.data.remote.ImageRemoteDataSource
import com.stdio.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(private val remoteDataSource: ImageRemoteDataSource) : ImageRepository {
    override suspend fun uploadImage(pngFlow: Flow<ByteArray>): Int =
        remoteDataSource.uploadImage(pngFlow)

    override fun close() = remoteDataSource.close()
}