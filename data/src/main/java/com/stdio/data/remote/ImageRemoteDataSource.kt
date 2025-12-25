package com.stdio.data.remote

import com.stdio.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.flow.Flow

class ImageRemoteDataSource(private val client: HttpClient) {

    suspend fun uploadImage(pngFlow: Flow<ByteArray>): Int {
        val request = "upload"
        return client.post(BuildConfig.BASE_URL + request) {
            contentType(ContentType.Image.PNG)
            setBody(object : OutgoingContent.WriteChannelContent() {
                override suspend fun writeTo(channel: ByteWriteChannel) {
                    pngFlow.collect { chunk ->
                        channel.writeFully(chunk)
                    }
                    channel.close(null)
                }

                override val contentLength: Long? = null // Неизвестная длина
            })
        }.status.value
    }

    fun close() {
        client.close()
    }
}