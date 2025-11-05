package com.app4funbr.tunner.data.repository

import com.app4funbr.tunner.data.audio.AudioCaptureManager
import com.app4funbr.tunner.data.audio.PitchDetector
import com.app4funbr.tunner.domain.model.TunerResult
import com.app4funbr.tunner.domain.repository.ITunerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementação do repositório de afinação.
 */
class TunerRepositoryImpl(
    private val audioCaptureManager: AudioCaptureManager,
    private val pitchDetector: PitchDetector
) : ITunerRepository {
    
    override fun startTuning(): Flow<TunerResult> {
        return audioCaptureManager.startCapture()
            .map { audioBuffer -> processAudioBuffer(audioBuffer) }
    }
    
    override fun stopTuning(): Unit {
        audioCaptureManager.stopCapture()
    }
    
    private fun processAudioBuffer(audioBuffer: FloatArray): TunerResult {
        val frequency: Double = pitchDetector.detectPitch(audioBuffer)
        return if (frequency > 0) {
            TunerResult.fromFrequency(frequency)
        } else {
            createEmptyResult()
        }
    }
    
    private fun createEmptyResult(): TunerResult {
        return TunerResult.fromFrequency(0.0)
    }
}

