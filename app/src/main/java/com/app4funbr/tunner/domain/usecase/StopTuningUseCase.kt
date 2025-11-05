package com.app4funbr.tunner.domain.usecase

import com.app4funbr.tunner.domain.repository.ITunerRepository

/**
 * Caso de uso para parar a afinação.
 */
class StopTuningUseCase(
    private val repository: ITunerRepository
) {
    operator fun invoke(): Unit {
        repository.stopTuning()
    }
}

