package com.stdio.domain.repository

import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun uploadImage(pngFlow: Flow<ByteArray>): Int
    fun close()
}