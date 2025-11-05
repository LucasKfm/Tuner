package com.app4funbr.tunner.di

import com.app4funbr.tunner.presentation.tuner.TunerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo Koin da camada Presentation.
 */
val presentationModule = module {
    viewModel {
        TunerViewModel(
            startTuningUseCase = get(),
            stopTuningUseCase = get()
        )
    }
}

