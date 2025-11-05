package com.app4funbr.tunner.presentation.tuner

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app4funbr.tunner.presentation.tuner.components.NoteScrollRow
import com.app4funbr.tunner.presentation.tuner.components.TunerGauge
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

/**
 * Tela principal do afinador.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(viewModel: TunerViewModel = koinViewModel()) {
    val uiState: TunerUiState by viewModel.uiState.collectAsState()
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    LaunchedEffect(audioPermissionState.status.isGranted) {
        if (audioPermissionState.status.isGranted) {
            viewModel.startTuning()
        } else {
            audioPermissionState.launchPermissionRequest()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tunner",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0f172a),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        TunerContent(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun TunerContent(
    uiState: TunerUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0f172a), Color(0xFF1e293b))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NoteScrollRow(
                notes = uiState.allNotes,
                currentNote = uiState.currentNote,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = uiState.currentNote,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = getColorForUi(uiState.getFeedbackColor())
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format("%.1f", uiState.frequency)} Hz",
                    fontSize = 20.sp,
                    color = Color(0xFFFFD166)
                )
                Text(
                    text = "•",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${String.format("%+.1f", uiState.cents)}c",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
        }
        TunerGauge(
            cents = uiState.cents,
            color = uiState.getFeedbackColor(),
            modifier = Modifier.size(300.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = uiState.getStatusMessage(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = getColorForUi(uiState.getFeedbackColor())
            )
        }
    }
}

private fun getColorForUi(tunerColor: TunerColor): Color {
    return when (tunerColor) {
        TunerColor.GREEN -> Color(0xFF10B981)
        TunerColor.CYAN -> Color(0xFF06B6D4)
        TunerColor.RED -> Color(0xFFEF4444)
        TunerColor.GRAY -> Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTunerScreen() {
    MaterialTheme {
        TunerContent(
            uiState = TunerUiState(
                currentNote = "A4",
                frequency = 440.0,
                cents = 0f,
                isInTune = true,
                isTuning = true
            )
        )
    }
}

