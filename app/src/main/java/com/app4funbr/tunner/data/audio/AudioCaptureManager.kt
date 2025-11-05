package com.app4funbr.tunner.data.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

/**
 * Gerencia a captura de áudio do microfone.
 */
class AudioCaptureManager {
    companion object {
        private const val TAG = "AudioCaptureManager"
        private const val SAMPLE_RATE: Int = 44100
        private const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT
        private const val BUFFER_SIZE_MULTIPLIER: Int = 2
    }
    
    private var audioRecord: AudioRecord? = null
    private var isCapturing: Boolean = false
    
    /**
     * Inicia a captura de áudio e retorna um Flow de buffers de áudio.
     */
    fun startCapture(): Flow<FloatArray> = flow {
        try {
            val bufferSize: Int = calculateBufferSize()
            Log.d(TAG, "Iniciando captura com buffer size: $bufferSize")
            audioRecord = createAudioRecord(bufferSize)
            val state: Int = audioRecord?.state ?: AudioRecord.STATE_UNINITIALIZED
            if (state != AudioRecord.STATE_INITIALIZED) {
                Log.e(TAG, "AudioRecord não foi inicializado corretamente")
                return@flow
            }
            audioRecord?.startRecording()
            isCapturing = true
            Log.d(TAG, "Gravação iniciada")
            val shortBuffer = ShortArray(bufferSize)
            while (isCapturing && coroutineContext.isActive) {
                val readResult: Int = audioRecord?.read(shortBuffer, 0, bufferSize) ?: 0
                if (readResult > 0) {
                    val floatBuffer: FloatArray = convertToFloat(shortBuffer, readResult)
                    emit(floatBuffer)
                } else {
                    Log.w(TAG, "Falha ao ler áudio: $readResult")
                    delay(50)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro na captura de áudio", e)
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Para a captura de áudio.
     */
    fun stopCapture(): Unit {
        isCapturing = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
    
    private fun calculateBufferSize(): Int {
        val minBufferSize: Int = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT
        )
        Log.d(TAG, "Min buffer size: $minBufferSize")
        return minBufferSize * BUFFER_SIZE_MULTIPLIER
    }
    
    private fun createAudioRecord(bufferSize: Int): AudioRecord {
        return AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSize
        )
    }
    
    private fun convertToFloat(shortBuffer: ShortArray, size: Int): FloatArray {
        return FloatArray(size) { index ->
            shortBuffer[index] / 32768f
        }
    }
    
    fun getSampleRate(): Int = SAMPLE_RATE
}

