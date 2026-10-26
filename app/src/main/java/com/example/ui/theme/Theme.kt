package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalIsDarkTheme = staticCompositionLocalOf { true }

private val AquaEmeraldLightColorScheme = lightColorScheme(
  primary = DeepForestEmerald,
  onPrimary = Color(0xFFF2FFFC),
  primaryContainer = HeroMintGreen,
  onPrimaryContainer = TextPrimaryCharcoal,
  secondary = DeepEmeraldDark,
  onSecondary = Color(0xFFF2FFFC),
  secondaryContainer = SageGrayPill,
  onSecondaryContainer = TextPrimaryCharcoal,
  tertiary = CrimsonMedicalStar,
  onTertiary = Color(0xFFF2FFFC),
  tertiaryContainer = SoftRoseCircle,
  onTertiaryContainer = CrimsonMedicalStar,
  background = ClinicalOffWhiteBg,
  onBackground = LightOnBackgroundPearl,
  surface = PureWhiteSurface,
  onSurface = TextPrimaryCharcoal,
  surfaceVariant = SageGrayPill,
  onSurfaceVariant = TextSecondarySlate,
  outline = BorderSubtleMint,
  error = CrimsonMedicalStar
)

private val NeuralDarkColorScheme = darkColorScheme(
  primary = DarkPrimaryTeal,
  onPrimary = DarkBackgroundNavy,
  primaryContainer = DarkHeroContainer,
  onPrimaryContainer = DarkTextPrimary,
  secondary = HeroMintGreen,
  onSecondary = DarkBackgroundNavy,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = DarkTextPrimary,
  tertiary = CrimsonMedicalStar,
  onTertiary = Color.White,
  tertiaryContainer = DarkSurfaceVariant,
  onTertiaryContainer = SoftRoseCircle,
  background = DarkBackgroundNavy,
  onBackground = DarkTextPrimary,
  surface = DarkSurfaceCard,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorderSubtle,
  error = Color(0xFFFF6B6B)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) NeuralDarkColorScheme else AquaEmeraldLightColorScheme
  CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}
