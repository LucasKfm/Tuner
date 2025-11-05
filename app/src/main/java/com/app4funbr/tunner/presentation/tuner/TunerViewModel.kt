package com.app4funbr.tunner.presentation.tuner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app4funbr.tunner.domain.model.TunerResult
import com.app4funbr.tunner.domain.usecase.StartTuningUseCase
import com.app4funbr.tunner.domain.usecase.StopTuningUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel do afinador.
 */
class TunerViewModel(
    private val startTuningUseCase: StartTuningUseCase,
    private val stopTuningUseCase: StopTuningUseCase
) : ViewModel() {
    
    private val _uiState: MutableStateFlow<TunerUiState> = MutableStateFlow(TunerUiState())
    val uiState: StateFlow<TunerUiState> = _uiState.asStateFlow()
    
    private var tuningJob: Job? = null
    
    /**
     * Inicia a afinação.
     */
    fun startTuning(): Unit {
        if (tuningJob?.isActive == true) {
            return
        }
        _uiState.value = _uiState.value.copy(isTuning = true)
        tuningJob = viewModelScope.launch {
            startTuningUseCase()
                .catch { }
                .collect { result -> updateUiState(result) }
        }
    }
    
    /**
     * Para a afinação.
     */
    fun stopTuning(): Unit {
        tuningJob?.cancel()
        tuningJob = null
        stopTuningUseCase()
        _uiState.value = _uiState.value.copy(
            isTuning = false,
            currentNote = "--",
            frequency = 0.0,
            cents = 0f,
            isInTune = false
        )
    }
    
    private fun updateUiState(result: TunerResult): Unit {
        _uiState.value = _uiState.value.copy(
            currentNote = result.closestNote.name,
            frequency = result.detectedFrequency,
            cents = result.cents.coerceIn(-100f, 100f),
            isInTune = result.isInTune
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        stopTuning()
    }
}

