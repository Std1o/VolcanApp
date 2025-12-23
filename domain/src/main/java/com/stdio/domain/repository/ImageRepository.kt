package com.stdio.domain.repository

interface ImageRepository {
    suspend fun uploadImage(data: ByteArray): Int
    fun close()
}