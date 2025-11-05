package com.app4funbr.tunner.domain.model

import kotlin.math.log2
import kotlin.math.pow

/**
 * Representa uma nota musical.
 *
 * @property name Nome da nota (ex: "A4", "C#5")
 * @property frequency Frequência fundamental em Hz
 * @property midiNumber Número MIDI da nota (0-127)
 */
data class MusicalNote(
    val name: String,
    val frequency: Double,
    val midiNumber: Int
) {
    companion object {
        private val NOTE_NAMES: List<String> = listOf(
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
        )
        
        /**
         * Converte uma frequência em Hz para a nota musical mais próxima.
         */
        fun fromFrequency(frequency: Double): MusicalNote {
            if (frequency <= 0) {
                return MusicalNote("--", 0.0, 0)
            }
            val midiNumber: Int = (12 * log2(frequency / 440.0) + 69).toInt()
            val clampedMidi: Int = midiNumber.coerceIn(0, 127)
            val octave: Int = (clampedMidi / 12) - 1
            val noteIndex: Int = clampedMidi % 12
            val noteName: String = NOTE_NAMES[noteIndex] + octave
            val noteFrequency: Double = 440.0 * 2.0.pow((clampedMidi - 69) / 12.0)
            return MusicalNote(noteName, noteFrequency, clampedMidi)
        }
        
        /**
         * Gera todas as notas de A0 a G7#.
         */
        fun getAllNotes(): List<MusicalNote> {
            return (21..108).map { midiNumber ->
                val octave: Int = (midiNumber / 12) - 1
                val noteIndex: Int = midiNumber % 12
                val noteName: String = NOTE_NAMES[noteIndex] + octave
                val frequency: Double = 440.0 * 2.0.pow((midiNumber - 69) / 12.0)
                MusicalNote(noteName, frequency, midiNumber)
            }
        }
    }
}

