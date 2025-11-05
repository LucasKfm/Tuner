package com.app4funbr.tunner.di

import com.app4funbr.tunner.data.audio.AudioCaptureManager
import com.app4funbr.tunner.data.audio.PitchDetector
import com.app4funbr.tunner.data.repository.TunerRepositoryImpl
import com.app4funbr.tunner.domain.repository.ITunerRepository
import org.koin.dsl.module

/**
 * Módulo Koin da camada Data.
 */
val dataModule = module {
    single {
        AudioCaptureManager()
    }
    
    single {
        PitchDetector(sampleRate = get<AudioCaptureManager>().getSampleRate())
    }
    
    single<ITunerRepository> {
        TunerRepositoryImpl(
            audioCaptureManager = get(),
            pitchDetector = get()
        )
    }
}

