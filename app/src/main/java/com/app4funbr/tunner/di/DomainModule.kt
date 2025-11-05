package com.app4funbr.tunner.di

import com.app4funbr.tunner.domain.usecase.StartTuningUseCase
import com.app4funbr.tunner.domain.usecase.StopTuningUseCase
import org.koin.dsl.module

/**
 * Módulo Koin da camada Domain.
 */
val domainModule = module {
    factory {
        StartTuningUseCase(repository = get())
    }
    
    factory {
        StopTuningUseCase(repository = get())
    }
}

