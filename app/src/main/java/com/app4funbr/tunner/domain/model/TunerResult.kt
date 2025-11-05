package com.app4funbr.tunner.domain.model

import kotlin.math.abs
import kotlin.math.log2

/**
 * Resultado da detecção de afinação.
 *
 * @property detectedFrequency Frequência detectada em Hz
 * @property closestNote Nota musical mais próxima
 * @property cents Desvio em cents (-100 a +100)
 * @property isInTune Indica se está afinado (dentro de ±5 cents)
 */
data class TunerResult(
    val detectedFrequency: Double,
    val closestNote: MusicalNote,
    val cents: Float,
    val isInTune: Boolean
) {
    companion object {
        private const val IN_TUNE_THRESHOLD: Float = 5f
        
        /**
         * Calcula o desvio em cents entre a frequência detectada e a nota mais próxima.
         */
        fun calculateCents(detectedFrequency: Double, noteFrequency: Double): Float {
            if (detectedFrequency <= 0 || noteFrequency <= 0) {
                return 0f
            }
            return (1200 * log2(detectedFrequency / noteFrequency)).toFloat()
        }
        
        /**
         * Cria um TunerResult a partir de uma frequência detectada.
         */
        fun fromFrequency(frequency: Double): TunerResult {
            val closestNote: MusicalNote = MusicalNote.fromFrequency(frequency)
            val cents: Float = calculateCents(frequency, closestNote.frequency)
            val isInTune: Boolean = abs(cents) < IN_TUNE_THRESHOLD
            return TunerResult(frequency, closestNote, cents, isInTune)
        }
    }
}

