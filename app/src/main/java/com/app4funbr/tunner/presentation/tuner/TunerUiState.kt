package com.app4funbr.tunner.presentation.tuner

import com.app4funbr.tunner.domain.model.MusicalNote

/**
 * Estado da UI do afinador.
 */
data class TunerUiState(
    val currentNote: String = "--",
    val frequency: Double = 0.0,
    val cents: Float = 0f,
    val isInTune: Boolean = false,
    val isTuning: Boolean = false,
    val allNotes: List<MusicalNote> = MusicalNote.getAllNotes()
) {
    /**
     * Retorna a cor do feedback baseado no desvio.
     */
    fun getFeedbackColor(): TunerColor {
        return when {
            isInTune -> TunerColor.GREEN
            cents > 0 -> TunerColor.CYAN
            else -> TunerColor.RED
        }
    }
    
    /**
     * Retorna a mensagem de status baseada no desvio.
     */
    fun getStatusMessage(): String {
        return when {
            frequency <= 0 -> "Aguardando som..."
            isInTune -> "Afinado!"
            cents > 0 -> "Um pouco alto"
            else -> "Um pouco baixo"
        }
    }
}

/**
 * Cores de feedback do afinador.
 */
enum class TunerColor {
    GREEN,
    CYAN,
    RED,
    GRAY
}

