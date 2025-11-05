package com.app4funbr.tunner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app4funbr.tunner.presentation.tuner.TunerScreen
import com.app4funbr.tunner.ui.theme.TunnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TunnerTheme {
                TunerScreen()
            }
        }
    }
}