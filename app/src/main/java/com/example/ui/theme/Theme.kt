package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = GreenPrimaryDarkTheme,
  onPrimary = GreenPrimaryDark,
  primaryContainer = GreenPrimary,
  onPrimaryContainer = GreenContainer,
  secondary = SaffronSecondaryDarkTheme,
  onSecondary = Color(0xFF4E1600),
  secondaryContainer = Color(0xFF6F2600),
  onSecondaryContainer = SaffronContainer,
  tertiary = NavyTertiaryDarkTheme,
  onTertiary = Color(0xFF001F6E),
  tertiaryContainer = NavyTertiary,
  onTertiaryContainer = NavyContainer,
  background = BackgroundDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = Color(0xFF5E6962)
)

private val LightColorScheme = lightColorScheme(
  primary = GreenPrimary,
  onPrimary = Color.White,
  primaryContainer = GreenContainer,
  onPrimaryContainer = OnGreenContainer,
  secondary = SaffronSecondary,
  onSecondary = Color.White,
  secondaryContainer = SaffronContainer,
  onSecondaryContainer = OnSaffronContainer,
  tertiary = NavyTertiary,
  onTertiary = Color.White,
  tertiaryContainer = NavyContainer,
  onTertiaryContainer = OnNavyContainer,
  background = BackgroundLight,
  onBackground = TextPrimary,
  surface = SurfaceLight,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondary,
  outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use GramSeva's signature government/civic green & saffron palette
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
