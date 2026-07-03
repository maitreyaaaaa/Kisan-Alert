package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun LeafDiagnosticCanvas(imageType: String) {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Draw main leaf outline path
    val leafPath = Path().apply {
      moveTo(w * 0.5f, h * 0.15f)
      cubicTo(w * 0.85f, h * 0.35f, w * 0.75f, h * 0.8f, w * 0.5f, h * 0.85f)
      cubicTo(w * 0.25f, h * 0.8f, w * 0.15f, h * 0.35f, w * 0.5f, h * 0.15f)
    }

    // Determine color based on plant status
    val leafColor = when (imageType) {
      "HEALTHY" -> Color(0xFF2D6A4F)
      "BLAST" -> Color(0xFF40916C)
      "BLIGHT" -> Color(0xFF52B788)
      else -> Color(0xFF1B4332)
    }

    // Draw solid leaf body
    drawPath(path = leafPath, color = leafColor)

    // Draw leaf central stem/vein
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.5f),
      start = Offset(w * 0.5f, h * 0.15f),
      end = Offset(w * 0.5f, h * 0.85f),
      strokeWidth = 3.dp.toPx()
    )

    // Draw secondary veins
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.35f),
      end = Offset(w * 0.68f, h * 0.45f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.35f),
      end = Offset(w * 0.32f, h * 0.45f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.55f),
      end = Offset(w * 0.72f, h * 0.65f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.55f),
      end = Offset(w * 0.28f, h * 0.65f),
      strokeWidth = 2.dp.toPx()
    )

    // Draw simulated spots / disease anomalies on leaf Canvas overlay
    if (imageType == "BLAST") {
      // Rice Blast distinct brown/grey elliptical lesion spots
      drawCircle(
        color = Color(0xFF78350F),
        radius = 8.dp.toPx(),
        center = Offset(w * 0.42f, h * 0.48f)
      )
      drawCircle(
        color = Color(0xFF94A3B8),
        radius = 4.dp.toPx(),
        center = Offset(w * 0.42f, h * 0.48f)
      )

      drawCircle(
        color = Color(0xFF78350F),
        radius = 12.dp.toPx(),
        center = Offset(w * 0.62f, h * 0.58f)
      )
      drawCircle(
        color = Color(0xFF94A3B8),
        radius = 6.dp.toPx(),
        center = Offset(w * 0.62f, h * 0.58f)
      )

      drawCircle(
        color = Color(0xFF78350F),
        radius = 7.dp.toPx(),
        center = Offset(w * 0.38f, h * 0.32f)
      )
    }

    if (imageType == "BLIGHT") {
      // Bacterial Blight long golden yellow stripes along the margins
      val blightMarginPathRight = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        cubicTo(w * 0.72f, h * 0.35f, w * 0.68f, h * 0.7f, w * 0.5f, h * 0.85f)
        cubicTo(w * 0.64f, h * 0.72f, w * 0.68f, h * 0.42f, w * 0.5f, h * 0.15f)
      }
      drawPath(path = blightMarginPathRight, color = Color(0xFFF59E0B).copy(alpha = 0.8f))

      val blightMarginPathLeft = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        cubicTo(w * 0.28f, h * 0.35f, w * 0.32f, h * 0.7f, w * 0.5f, h * 0.85f)
        cubicTo(w * 0.36f, h * 0.72f, w * 0.32f, h * 0.42f, w * 0.5f, h * 0.15f)
      }
      drawPath(path = blightMarginPathLeft, color = Color(0xFFFBBF24).copy(alpha = 0.6f))
    }
  }
}
