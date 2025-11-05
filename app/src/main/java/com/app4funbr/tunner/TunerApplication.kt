package com.app4funbr.tunner

import android.app.Application
import com.app4funbr.tunner.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Classe Application customizada para inicializar o Koin.
 */
class TunerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TunerApplication)
            modules(appModules)
        }
    }
}

