package com.app4funbr.tunner.domain.repository

import com.app4funbr.tunner.domain.model.TunerResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório de afinação.
 */
interface ITunerRepository {
    /**
     * Inicia a captura de áudio e detecção de frequência.
     *
     * @return Flow contínuo com resultados de afinação
     */
    fun startTuning(): Flow<TunerResult>
    
    /**
     * Para a captura de áudio.
     */
    fun stopTuning()
}

