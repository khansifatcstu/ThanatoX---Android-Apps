package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.auth.onGoogleSignInClicked
import com.example.ui.components.DarkModeToggleButton
import com.example.ui.components.GoogleGLogoIcon
import com.example.ui.components.LotusSanctuaryIllustration
import com.example.ui.components.NeuralSynapseAmbientBackground
import com.example.ui.components.ThanatoXAppSubtitleBadge
import com.example.ui.components.ThanatoXBengaliFontFamily
import com.example.ui.components.ThanatoXBrandWordmark
import com.example.ui.components.ThanatoXEnglishFontFamily
import com.example.ui.components.buildThanatoXBrandAnnotatedString
import com.example.ui.components.rememberSrgbBitmapPainter
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald

@Composable
fun AuthScreen(
  language: AppLanguage,
  isDarkMode: Boolean,
  onToggleLanguage: () -> Unit,
  onToggleDarkMode: () -> Unit,
  onAuthSuccess: () -> Unit,
  onContinueAsGuest: () -> Unit = {}
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  val strings = stringsFor(language)

  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .testTag("welcome_screen_root")
  ) {
    // Full-screen GPU-accelerated bioluminescent neural background
    NeuralSynapseAmbientBackground()

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Transparent,
      contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .widthIn(max = 520.dp)
            .align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // 1. Top Brand Header Section:
          // - Large Stylish App Name (Bioluminescent Teal-Gold Wordmark) + App Icon beside it
          // - Directly below App Name: Stylish Italic Glow App Subtitle
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("welcome_app_brand_header")
            ) {
              ThanatoXBrandWordmark(
                text = strings.appTitle,
                language = language,
                fontSize = 36.sp,
                isOnTranslucentDarkBackdrop = true,
                showUnderlineAccent = true
              )
              Spacer(modifier = Modifier.width(12.dp))
              Surface(
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 6.dp,
                border = BorderStroke(
                  1.5.dp,
                  Brush.linearGradient(
                    colors = listOf(
                      Color(0xFF5EEAD4).copy(alpha = 0.90f),
                      Color(0xFFFBBF24).copy(alpha = 0.75f),
                      Color(0xFF2DD4BF).copy(alpha = 0.85f)
                    )
                  )
                ),
                modifier = Modifier.size(48.dp)
              ) {
                LotusSanctuaryIllustration(
                  modifier = Modifier.fillMaxSize(),
                  isCircular = false,
                  cornerRadius = 14.dp
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // App Subtitle directly underneath the App Name with unmistakable italic glow styling
            ThanatoXAppSubtitleBadge(
              subtitle = strings.welcomeSubtitle,
              language = language,
              isOnTranslucentDarkBackdrop = true,
              modifier = Modifier.testTag("welcome_subtitle_text")
            )
          }

          // 2. Center Sanctuary Section:
          // - Open (boxless) large stylish Welcome Greeting ("থেনাটক্স-এ স্বাগতম" / "Welcome to ThanatoX")
          // - Directly below: Dark Mode & Language Switcher Options (without any outer wrapper box)
          // - Directly below (closely attached): Meditation Hero Artwork in Glowing Glass Frame
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          ) {
            val welcomeStyledText = remember(strings.welcomeTitle, isDarkMode) {
              buildThanatoXBrandAnnotatedString(
                fullText = strings.welcomeTitle,
                isDark = isDarkMode,
                isOnTranslucentDarkBackdrop = true
              )
            }

            // Large, prominent Welcome Greeting directly on the open canvas (no outer box)
            Text(
              text = welcomeStyledText,
              style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = if (language == AppLanguage.EN) ThanatoXEnglishFontFamily else ThanatoXBengaliFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (language == AppLanguage.EN) 30.sp else 32.sp,
                letterSpacing = 0.5.sp,
                lineHeight = if (language == AppLanguage.EN) 38.sp else 40.sp,
                shadow = Shadow(
                  color = Color(0xFF021F1D).copy(alpha = 0.92f),
                  offset = Offset(0f, 3f),
                  blurRadius = 14f
                )
              ),
              color = Color(0xFFF0FDFA),
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("welcome_title_text")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dark Mode & Language Switcher Row directly below the welcome text without an outer container box
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              DarkModeToggleButton(
                isDarkMode = isDarkMode,
                onToggle = onToggleDarkMode
              )
              Spacer(modifier = Modifier.width(12.dp))
              LanguageSwitchPill(
                language = language,
                onToggle = onToggleLanguage
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            WelcomeMeditationHeroIcon(
              isDarkMode = isDarkMode,
              modifier = Modifier.testTag("welcome_meditation_hero_icon")
            )
          }

          // 5. Bottom Sign-In Options:
          // - Google Sign-In inside an Emerald Button Box
          // - Guest Sign-In outside of any box as normal clickable text
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Button(
              onClick = {
                errorMessage = null
                isLoading = true
                onGoogleSignInClicked(
                  context = context,
                  credentialManager = credentialManager,
                  onAuthSuccess = {
                    isLoading = false
                    onAuthSuccess()
                  },
                  onAuthError = { msg ->
                    isLoading = false
                    errorMessage = msg
                  },
                  scope = coroutineScope,
                  onAuthCancelled = {
                    isLoading = false
                  }
                )
              },
              enabled = !isLoading,
              modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("google_sign_in_button"),
              shape = RoundedCornerShape(16.dp),
              border = BorderStroke(
                width = 1.2.dp,
                color = if (isDarkMode) Color(0xFF2DD4BF).copy(alpha = 0.65f)
                else Color(0xFF5EEAD4).copy(alpha = 0.78f)
              ),
              colors = ButtonDefaults.buttonColors(
                containerColor = DeepForestEmerald,
                contentColor = Color.White
              )
            ) {
              if (isLoading) {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  color = Color.White,
                  strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = strings.connectingGoogle,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  softWrap = false,
                  overflow = TextOverflow.Ellipsis
                )
              } else {
                GoogleGLogoIcon(badgeSize = 28.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = strings.signInButton,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = if (language == AppLanguage.EN) ThanatoXEnglishFontFamily else ThanatoXBengaliFontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                  ),
                  maxLines = 1,
                  softWrap = false,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            if (errorMessage != null) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Boxless normal clickable text for Guest Sign-In with stylish luminous typography
            Text(
              text = strings.welcomeGuestButton,
              style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = if (language == AppLanguage.EN) ThanatoXEnglishFontFamily else ThanatoXBengaliFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp,
                letterSpacing = 0.3.sp,
                shadow = Shadow(
                  color = Color(0xFF042F2E).copy(alpha = 0.85f),
                  offset = Offset(0f, 1.5f),
                  blurRadius = 8f
                )
              ),
              color = Color(0xFFCCFBF1),
              textAlign = TextAlign.Center,
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(enabled = !isLoading) { onContinueAsGuest() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .testTag("continue_as_guest_button")
            )
          }
        }
      }
    }
  }
}

/**
 * Custom Meditation Hero Icon (`R.drawable.welcome_meditation_hero`) with transparent background,
 * housed inside a glowing translucent glass sanctuary frame and color-harmonized with the
 * Bioluminescent Neural Synapse graphics in both Dark Mode and Light Mode.
 */
@Composable
fun WelcomeMeditationHeroIcon(
  isDarkMode: Boolean,
  modifier: Modifier = Modifier
) {
  val meditationColorFilter = remember(isDarkMode) {
    if (isDarkMode) {
      ColorFilter.colorMatrix(
        ColorMatrix(
          floatArrayOf(
            0.88f, 0.02f, 0.00f, 0f, -6f,
            0.04f, 1.06f, 0.08f, 0f, 8f,
            0.04f, 0.12f, 1.08f, 0f, 12f,
            0.00f, 0.00f, 0.00f, 1f, 0f
          )
        )
      )
    } else {
      ColorFilter.colorMatrix(
        ColorMatrix(
          floatArrayOf(
            0.92f, 0.04f, 0.00f, 0f, 4f,
            0.04f, 1.08f, 0.08f, 0f, 14f,
            0.03f, 0.10f, 1.06f, 0f, 16f,
            0.00f, 0.00f, 0.00f, 1f, 0f
          )
        )
      )
    }
  }

  val frameBackgroundBrush = remember(isDarkMode) {
    if (isDarkMode) {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF072622).copy(alpha = 0.72f),
          Color(0xFF0B3B35).copy(alpha = 0.56f),
          Color(0xFF061E1B).copy(alpha = 0.76f)
        )
      )
    } else {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF0E524A).copy(alpha = 0.48f),
          Color(0xFF147469).copy(alpha = 0.36f),
          Color(0xFF0B433D).copy(alpha = 0.52f)
        )
      )
    }
  }

  val frameBorderBrush = remember(isDarkMode) {
    if (isDarkMode) {
      Brush.linearGradient(
        colors = listOf(
          Color(0xFF2DD4BF).copy(alpha = 0.85f),
          Color(0xFFFBBF24).copy(alpha = 0.55f),
          Color(0xFF10B981).copy(alpha = 0.75f)
        )
      )
    } else {
      Brush.linearGradient(
        colors = listOf(
          Color(0xFF5EEAD4).copy(alpha = 0.90f),
          Color(0xFFFDE68A).copy(alpha = 0.68f),
          Color(0xFF2DD4BF).copy(alpha = 0.85f)
        )
      )
    }
  }

  Surface(
    shape = RoundedCornerShape(32.dp),
    color = Color.Transparent,
    border = BorderStroke(1.8.dp, frameBorderBrush),
    shadowElevation = if (isDarkMode) 10.dp else 6.dp,
    modifier = modifier.size(216.dp)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxSize()
        .background(frameBackgroundBrush)
        .padding(12.dp)
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width * 0.5f, size.height * 0.48f)
        drawCircle(
          brush = Brush.radialGradient(
            colors = if (isDarkMode) {
              listOf(
                Color(0xFF2DD4BF).copy(alpha = 0.36f),
                Color(0xFFF59E0B).copy(alpha = 0.14f),
                Color.Transparent
              )
            } else {
              listOf(
                Color(0xFF5EEAD4).copy(alpha = 0.42f),
                Color(0xFFFDE68A).copy(alpha = 0.18f),
                Color.Transparent
              )
            },
            center = center,
            radius = size.minDimension * 0.52f
          ),
          radius = size.minDimension * 0.52f,
          center = center
        )
      }

      val heroPainter = rememberSrgbBitmapPainter(
        id = R.drawable.welcome_meditation_hero,
        maxDimensionPx = 512
      )
      if (heroPainter != null) {
        Image(
          painter = heroPainter,
          contentDescription = null,
          contentScale = ContentScale.Fit,
          colorFilter = meditationColorFilter,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

/**
 * Compact Language Pill (`🌐 বাংলা` / `🌐 English`) that never wraps across lines.
 */
@Composable
fun LanguageSwitchPill(
  language: AppLanguage,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val strings = stringsFor(language)
  Surface(
    shape = RoundedCornerShape(50),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.74f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
    modifier = modifier
      .clip(RoundedCornerShape(50))
      .clickable { onToggle() }
      .testTag("language_toggle_button")
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Language,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(5.dp))
      Text(
        text = strings.languageBadgeLabel,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        softWrap = false
      )
    }
  }
}
