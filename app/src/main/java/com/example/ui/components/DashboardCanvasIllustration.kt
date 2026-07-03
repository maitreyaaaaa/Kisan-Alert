package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DashboardCanvasIllustration() {
  Canvas(
    modifier = Modifier
      .fillMaxWidth()
      .height(110.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))
        )
      )
  ) {
    val width = size.width
    val height = size.height

    // Draw glowing sunset/sunrise in background
    drawCircle(
      color = Color(0xFFFEF08A).copy(alpha = 0.2f),
      radius = 90.dp.toPx(),
      center = Offset(width * 0.85f, height * 0.2f)
    )
    drawCircle(
      color = Color(0xFFFBBF24).copy(alpha = 0.4f),
      radius = 50.dp.toPx(),
      center = Offset(width * 0.85f, height * 0.2f)
    )

    // Draw rolling agricultural hills
    val hill1 = Path().apply {
      moveTo(0f, height)
      quadraticTo(width * 0.25f, height * 0.6f, width * 0.6f, height)
      close()
    }
    drawPath(
      path = hill1,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1B4332), Color(0xFF0F172A))
      )
    )

    val hill2 = Path().apply {
      moveTo(width * 0.4f, height)
      quadraticTo(width * 0.75f, height * 0.45f, width, height)
      lineTo(width, height)
      lineTo(width * 0.4f, height)
      close()
    }
    drawPath(
      path = hill2,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2D6A4F), Color(0xFF1B4332))
      )
    )

    // Draw grid patterns representing crop rows
    drawArc(
      color = Color(0xFF52B788).copy(alpha = 0.2f),
      startAngle = 180f,
      sweepAngle = 90f,
      useCenter = false,
      size = Size(120.dp.toPx(), 120.dp.toPx()),
      topLeft = Offset(-30.dp.toPx(), 60.dp.toPx()),
      style = Stroke(width = 2.dp.toPx())
    )

    // Glowing notification pulse overlay representing warnings
    drawCircle(
      color = Color(0xFFF59E0B).copy(alpha = 0.15f),
      radius = 25.dp.toPx(),
      center = Offset(width * 0.15f, height * 0.4f)
    )
    drawCircle(
      color = Color(0xFFF59E0B),
      radius = 4.dp.toPx(),
      center = Offset(width * 0.15f, height * 0.4f)
    )
  }
}
