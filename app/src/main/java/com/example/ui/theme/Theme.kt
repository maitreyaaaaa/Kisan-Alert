package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = SoftMint,
  secondary = LightForestGreen,
  tertiary = AccentGold,
  background = SlateDark,
  surface = SlateCard,
  onPrimary = DeepForestGreen,
  onSecondary = Color.White,
  onTertiary = Color.Black,
  onBackground = TextPrimary,
  onSurface = TextPrimary
)

private val LightColorScheme = lightColorScheme(
  primary = DeepForestGreen,
  secondary = LightForestGreen,
  tertiary = HarvestGold,
  background = Color(0xFFF1F5F9), // Slate 100 for light mode
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onTertiary = Color.White,
  onBackground = SlateDark,
  onSurface = SlateDark
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Preserves brand identity (greens, golds) rather than override with system palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
