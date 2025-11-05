package com.app4funbr.tunner.presentation.tuner.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app4funbr.tunner.presentation.tuner.TunerColor
import kotlin.math.cos
import kotlin.math.sin

/**
 * Componente de arco/velocímetro do afinador.
 */
@Composable
fun TunerGauge(
    cents: Float,
    color: TunerColor,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX: Float = size.width / 2
                val centerY: Float = size.height * 0.9f
                val radius: Float = size.width * 0.35f
                drawArc(
                    color = Color.Gray.copy(alpha = 0.3f),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
                val needleAngle: Double = Math.toRadians((180 + cents * 1.8).toDouble())
                val needleLength: Float = radius * 0.85f
                val needleEndX: Float = centerX + cos(needleAngle).toFloat() * needleLength
                val needleEndY: Float = centerY - sin(needleAngle).toFloat() * needleLength
                drawLine(
                    color = getColorForTuner(color),
                    start = Offset(centerX, centerY),
                    end = Offset(needleEndX, needleEndY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            GaugeLabels(modifier = Modifier.matchParentSize())
        }
    }
}

@Composable
private fun GaugeLabels(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "-100c",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, bottom = 16.dp)
        )
        Text(
            text = "0c",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        Text(
            text = "+100c",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 32.dp, bottom = 16.dp)
        )
    }
}

private fun getColorForTuner(tunerColor: TunerColor): Color {
    return when (tunerColor) {
        TunerColor.GREEN -> Color(0xFF10B981)
        TunerColor.CYAN -> Color(0xFF06B6D4)
        TunerColor.RED -> Color(0xFFEF4444)
        TunerColor.GRAY -> Color.Gray
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0f172a)
@Composable
fun PreviewTunerGaugeInTune() {
    TunerGauge(cents = 0f, color = TunerColor.GREEN)
}

@Preview(showBackground = true, backgroundColor = 0xFF0f172a)
@Composable
fun PreviewTunerGaugeHigh() {
    TunerGauge(cents = 45f, color = TunerColor.CYAN)
}

@Preview(showBackground = true, backgroundColor = 0xFF0f172a)
@Composable
fun PreviewTunerGaugeLow() {
    TunerGauge(cents = -60f, color = TunerColor.RED)
}

