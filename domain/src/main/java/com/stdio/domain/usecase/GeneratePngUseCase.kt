package com.stdio.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.ByteArrayOutputStream
import java.util.zip.CRC32
import java.util.zip.Deflater

class GeneratePngUseCase {
    /**
     * Генерирует PNG потоково без создания полного Bitmap в памяти
     * @param maxBufferSize максимальный размер буфера в памяти (например, 1MB)
     */
    operator fun invoke(
        width: Int,
        height: Int,
        maxBufferSize: Int = 1024 * 1024 // 1MB
    ): Flow<ByteArray> = flow {
        // 1. PNG сигнатура (8 байт)
        val pngSignature = byteArrayOf(
            0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        )
        emit(pngSignature)

        // 2. IHDR чанк (25 байт)
        val ihdrChunk = createIHDRChunk(width, height)
        emit(ihdrChunk)

        // 3. IDAT чанки - генерируем данные построчно
        val deflater = Deflater(Deflater.DEFAULT_COMPRESSION)
        val idatBuffer = ByteArrayOutputStream()

        // Генерируем данные частями, не более maxBufferSize за раз
        val rowsPerChunk = maxOf(1, maxBufferSize / (width * 4)) // 4 байта на пиксель

        var currentRow = 0

        while (currentRow < height) {
            val rowsToGenerate = minOf(rowsPerChunk, height - currentRow)

            // Генерируем сканлайны для нескольких строк
            val scanlinesData = generateScanlines(width, rowsToGenerate)

            // Добавляем в буфер для сжатия
            idatBuffer.write(scanlinesData)

            // Если буфер достаточно заполнен или это последняя часть - отправляем
            if (idatBuffer.size() >= maxBufferSize / 2 || currentRow + rowsToGenerate >= height) {
                val compressed = compressData(idatBuffer.toByteArray(), deflater)
                if (compressed.isNotEmpty()) {
                    val idatChunk = createIDATChunk(compressed)
                    emit(idatChunk)
                }
                idatBuffer.reset()
            }

            currentRow += rowsToGenerate

            // Небольшая задержка для демонстрации потоковости
            kotlinx.coroutines.delay(1)
        }

        // 4. Закрываем deflater и отправляем остатки
        deflater.finish()
        val remaining = ByteArray(1024)
        val remainingSize = deflater.deflate(remaining)
        if (remainingSize > 0) {
            val finalData = remaining.copyOf(remainingSize)
            val finalIdatChunk = createIDATChunk(finalData)
            emit(finalIdatChunk)
        }

        deflater.end()

        // 5. IEND чанк (12 байт)
        val iendChunk = createIENDChunk()
        emit(iendChunk)
    }.flowOn(Dispatchers.Default)

    /**
     * Генерирует сканлайны для указанного количества строк
     * Каждая строка: [filter byte] + [width * 4 bytes] (RGBA)
     */
    private fun generateScanlines(
        width: Int,
        rows: Int
    ): ByteArray {
        val bytesPerPixel = 4 // RGBA
        val bytesPerRow = 1 + width * bytesPerPixel // filter byte + пиксели

        val result = ByteArray(rows * bytesPerRow)

        for (row in 0 until rows) {
            val rowStart = row * bytesPerRow

            // Filter byte (0 = none)
            result[rowStart] = 0

            // Генерируем пиксели для этой строки - все красные
            for (x in 0 until width) {
                val pixelStart = rowStart + 1 + x * bytesPerPixel

                // Красный цвет: R=255, G=0, B=0, A=255
                result[pixelStart] = 255.toByte()     // R
                result[pixelStart + 1] = 0.toByte()   // G
                result[pixelStart + 2] = 0.toByte()   // B
                result[pixelStart + 3] = 255.toByte() // A
            }
        }

        return result
    }

    private fun compressData(data: ByteArray, deflater: Deflater): ByteArray {
        deflater.setInput(data)
        val output = ByteArray(data.size * 2) // Буфер для сжатых данных
        val compressedSize = deflater.deflate(output)
        return output.copyOf(compressedSize)
    }

    private fun createIHDRChunk(width: Int, height: Int): ByteArray {
        val data = ByteArray(13)

        // Width (4 bytes, big-endian)
        data[0] = (width shr 24).toByte()
        data[1] = (width shr 16).toByte()
        data[2] = (width shr 8).toByte()
        data[3] = width.toByte()

        // Height (4 bytes, big-endian)
        data[4] = (height shr 24).toByte()
        data[5] = (height shr 16).toByte()
        data[6] = (height shr 8).toByte()
        data[7] = height.toByte()

        // Bit depth (8 bits per channel)
        data[8] = 8

        // Color type (6 = RGBA)
        data[9] = 6

        // Compression method (0 = deflate/inflate)
        data[10] = 0

        // Filter method (0 = adaptive filtering)
        data[11] = 0

        // Interlace method (0 = no interlace)
        data[12] = 0

        return createChunk("IHDR", data)
    }

    private fun createIDATChunk(data: ByteArray): ByteArray {
        return createChunk("IDAT", data)
    }

    private fun createIENDChunk(): ByteArray {
        return createChunk("IEND", ByteArray(0))
    }

    private fun createChunk(type: String, data: ByteArray): ByteArray {
        val result = ByteArrayOutputStream()

        // Length (4 bytes, big-endian)
        result.write(
            byteArrayOf(
                (data.size shr 24).toByte(),
                (data.size shr 16).toByte(),
                (data.size shr 8).toByte(),
                data.size.toByte()
            )
        )

        // Chunk type (4 bytes)
        result.write(type.toByteArray())

        // Data
        result.write(data)

        // CRC-32 (4 bytes)
        val crc = CRC32()
        crc.update(type.toByteArray())
        crc.update(data)
        val crcValue = crc.value

        result.write(
            byteArrayOf(
                (crcValue shr 24).toByte(),
                (crcValue shr 16).toByte(),
                (crcValue shr 8).toByte(),
                crcValue.toByte()
            )
        )

        return result.toByteArray()
    }
}