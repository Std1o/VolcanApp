package com.stdio.volcanapp

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AsyncStreamUploader {
    private val client = HttpClient()

    suspend fun streamUpload(data: ByteArray, url: String): String {
        return client.post(url) {
            contentType(ContentType.Image.PNG)
            setBody(data)
        }.bodyAsText()
    }

    fun close() {
        client.close()
    }
}