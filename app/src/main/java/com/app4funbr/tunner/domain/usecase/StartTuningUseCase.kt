package com.app4funbr.tunner.domain.usecase

import com.app4funbr.tunner.domain.model.TunerResult
import com.app4funbr.tunner.domain.repository.ITunerRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para iniciar a afinação.
 */
class StartTuningUseCase(
    private val repository: ITunerRepository
) {
    operator fun invoke(): Flow<TunerResult> {
        return repository.startTuning()
    }
}

