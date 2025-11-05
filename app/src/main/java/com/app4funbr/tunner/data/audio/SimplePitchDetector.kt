package com.app4funbr.tunner.data.audio

import android.util.Log
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detector de pitch simples usando algoritmo de Autocorrelação.
 */
class SimplePitchDetector(private val sampleRate: Int) {
    companion object {
        private const val TAG = "SimplePitchDetector"
        private const val MIN_RMS_THRESHOLD = 0.005f
        private const val MIN_FREQUENCY = 50.0
        private const val MAX_FREQUENCY = 1500.0
    }
    
    /**
     * Detecta a frequência fundamental usando autocorrelação.
     */
    fun detectPitch(audioBuffer: FloatArray): Double {
        val rms: Float = calculateRMS(audioBuffer)
        if (rms < MIN_RMS_THRESHOLD) {
            return -1.0
        }
        val normalized: FloatArray = normalizeAudio(audioBuffer, rms)
        val pitch: Double = detectPitchAutocorrelation(normalized)
        return if (pitch > MIN_FREQUENCY && pitch < MAX_FREQUENCY) {
            pitch
        } else {
            -1.0
        }
    }
    
    private fun normalizeAudio(buffer: FloatArray, rms: Float): FloatArray {
        val maxValue: Float = buffer.maxOfOrNull { abs(it) } ?: 0f
        if (maxValue < 0.001f) {
            return FloatArray(buffer.size)
        }
        return FloatArray(buffer.size) { index -> 
            buffer[index] / maxValue 
        }
    }
    
    private fun calculateRMS(buffer: FloatArray): Float {
        val sumSquares: Float = buffer.fold(0f) { acc, sample -> 
            acc + sample * sample 
        }
        return sqrt(sumSquares / buffer.size)
    }
    
    private fun detectPitchAutocorrelation(buffer: FloatArray): Double {
        val size: Int = buffer.size
        val autocorrelation: FloatArray = calculateAutocorrelation(buffer)
        val minLag: Int = (sampleRate / MAX_FREQUENCY).toInt()
        val maxLag: Int = (sampleRate / MIN_FREQUENCY).toInt()
        val peakIndex: Int = findFirstPeak(autocorrelation, minLag, maxLag.coerceAtMost(size / 2 - 1))
        if (peakIndex <= 0) {
            return -1.0
        }
        val refinedPeak: Double = parabolicInterpolation(autocorrelation, peakIndex)
        return sampleRate / refinedPeak
    }
    
    private fun calculateAutocorrelation(buffer: FloatArray): FloatArray {
        val size: Int = buffer.size
        val result: FloatArray = FloatArray(size / 2)
        for (lag in 0 until size / 2) {
            var sum: Float = 0f
            val limit: Int = size / 2
            for (i in 0 until limit) {
                sum += buffer[i] * buffer[i + lag]
            }
            result[lag] = sum / limit
        }
        return result
    }
    
    private fun findFirstPeak(data: FloatArray, minLag: Int, maxLag: Int): Int {
        var maxValue: Float = 0f
        var maxIndex: Int = -1
        val threshold: Float = data[0] * 0.3f
        for (i in minLag until maxLag.coerceAtMost(data.size - 1)) {
            if (data[i] > threshold && data[i] > data[i - 1] && data[i] > data[i + 1]) {
                if (data[i] > maxValue) {
                    maxValue = data[i]
                    maxIndex = i
                }
            }
        }
        return maxIndex
    }
    
    private fun parabolicInterpolation(data: FloatArray, index: Int): Double {
        if (index <= 0 || index >= data.size - 1) {
            return index.toDouble()
        }
        val alpha: Float = data[index - 1]
        val beta: Float = data[index]
        val gamma: Float = data[index + 1]
        val offset: Double = 0.5 * (alpha - gamma) / (alpha - 2 * beta + gamma)
        return index + offset
    }
}

