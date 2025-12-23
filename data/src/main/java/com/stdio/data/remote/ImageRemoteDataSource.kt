package com.stdio.data.remote

import com.stdio.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ImageRemoteDataSource(private val client: HttpClient) {

    suspend fun uploadImage(data: ByteArray): Int {
        val request = "upload"
        return client.post(BuildConfig.BASE_URL + request) {
            contentType(ContentType.Image.PNG)
            setBody(data)
        }.status.value
    }

    fun close() {
        client.close()
    }
}