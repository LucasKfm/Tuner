package com.app4funbr.tunner.data.audio

/**
 * Detecta a frequência (pitch) de um sinal de áudio.
 */
class PitchDetector(private val sampleRate: Int) {
    private val simplePitchDetector: SimplePitchDetector = SimplePitchDetector(sampleRate)
    
    /**
     * Processa um buffer de áudio e retorna a frequência detectada.
     *
     * @return Frequência em Hz, ou -1 se não detectada
     */
    fun detectPitch(audioBuffer: FloatArray): Double {
        return simplePitchDetector.detectPitch(audioBuffer)
    }
}

