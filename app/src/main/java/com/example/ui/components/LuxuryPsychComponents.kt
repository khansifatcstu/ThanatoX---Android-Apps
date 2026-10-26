package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.MoodLog
import com.example.data.remote.ApiConnectionStatus
import com.example.data.remote.GeminiPsychiatristService
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorSpace
import androidx.annotation.DrawableRes
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.example.ui.theme.CrimsonMedicalStar
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LotusSanctuaryDark
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Thread-safe in-memory cache that decodes raster drawables off the UI thread (`Dispatchers.IO`)
 * with explicit `ColorSpace.Named.SRGB` so HWUI never logs `Unknown dataspace 0` and `performTraversals`
 * never blocks on synchronous main-thread bitmap decoding (`Skipped frames / Davey!`).
 */
object ThanatoXBitmapCache {
  private val cache = ConcurrentHashMap<Int, ImageBitmap>()

  fun getCached(@DrawableRes resId: Int): ImageBitmap? = cache[resId]

  fun decodeSrgb(
    context: Context,
    @DrawableRes resId: Int,
    maxDimensionPx: Int = 1080
  ): ImageBitmap? {
    cache[resId]?.let { return it }
    return try {
      val resources = context.resources
      val boundsOpts = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
      }
      BitmapFactory.decodeResource(resources, resId, boundsOpts)

      var sampleSize = 1
      val rawMax = max(boundsOpts.outWidth, boundsOpts.outHeight)
      if (rawMax > maxDimensionPx && maxDimensionPx > 0) {
        while ((rawMax / (sampleSize * 2)) >= maxDimensionPx) {
          sampleSize *= 2
        }
      }

      val decodeOpts = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
        inPreferredConfig = Bitmap.Config.ARGB_8888
        inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB)
      }
      val decoded = BitmapFactory.decodeResource(resources, resId, decodeOpts) ?: return null
      val imageBitmap = decoded.asImageBitmap()
      cache[resId] = imageBitmap
      imageBitmap
    } catch (_: Exception) {
      null
    }
  }

  fun preloadAll(context: Context) {
    val appCtx = context.applicationContext
    val bgIds = listOf(
      R.drawable.thanatox_neural_bg,
      R.drawable.thanatox_home_bg,
      R.drawable.thanatox_chat_dark_bg,
      R.drawable.thanatox_chat_light_bg,
      R.drawable.thanatox_test_bg,
      R.drawable.thanatox_mindcare_bg
    )
    bgIds.forEach { decodeSrgb(appCtx, it, maxDimensionPx = 1080) }

    val iconIds = listOf(
      R.drawable.thanatox_app_icon,
      R.drawable.dr_sifat_khan_avatar,
      R.drawable.welcome_meditation_hero
    )
    iconIds.forEach { decodeSrgb(appCtx, it, maxDimensionPx = 512) }
  }
}

@Composable
fun rememberSrgbBitmapPainter(
  @DrawableRes id: Int,
  maxDimensionPx: Int = 1080
): Painter? {
  val context = LocalContext.current
  val bitmapState by produceState<ImageBitmap?>(
    initialValue = ThanatoXBitmapCache.getCached(id),
    key1 = id
  ) {
    if (value == null) {
      value = withContext(Dispatchers.IO) {
        ThanatoXBitmapCache.decodeSrgb(context.applicationContext, id, maxDimensionPx)
      }
    }
  }
  return remember(bitmapState) {
    bitmapState?.let { BitmapPainter(it) }
  }
}

val ThanatoXEnglishFontFamily = FontFamily(
  Font(R.font.outfit, FontWeight.Bold),
  Font(R.font.outfit, FontWeight.ExtraBold)
)

val ThanatoXDisplayAccentFontFamily = FontFamily(
  Font(R.font.cinzel, FontWeight.Bold),
  Font(R.font.cinzel, FontWeight.ExtraBold)
)

val ThanatoXBengaliFontFamily = FontFamily(
  Font(R.font.hind_siliguri, FontWeight.Bold),
  Font(R.font.hind_siliguri, FontWeight.SemiBold)
)

/**
 * Builds an [AnnotatedString] that styles any occurrence of "ThanatoX" or "থেনাটক্স"
 * with the signature Bioluminescent Teal-Gold Brand Wordmark (Aqua-Teal stem + Golden-Teal 'X' / 'ক্স'
 * with soft neon glow shadow). Works both when rendering the standalone app name and when the app name
 * is embedded inside a phrase like "থেনাটক্স-এ স্বাগতম" or "Welcome to ThanatoX".
 */
fun buildThanatoXBrandAnnotatedString(
  fullText: String,
  isDark: Boolean,
  isOnTranslucentDarkBackdrop: Boolean = false
): AnnotatedString {
  val useLuminousPalette = isDark || isOnTranslucentDarkBackdrop

  val stemBrush = if (useLuminousPalette) {
    Brush.linearGradient(
      colors = listOf(
        Color(0xFFF0FDFA),
        Color(0xFF5EEAD4),
        Color(0xFF2DD4BF)
      )
    )
  } else {
    Brush.linearGradient(
      colors = listOf(
        Color(0xFF042F2E),
        Color(0xFF0F766E),
        Color(0xFF0D9488)
      )
    )
  }

  val accentXBrush = if (useLuminousPalette) {
    Brush.linearGradient(
      colors = listOf(
        Color(0xFFFDE68A),
        Color(0xFFF59E0B),
        Color(0xFF2DD4BF)
      )
    )
  } else {
    Brush.linearGradient(
      colors = listOf(
        Color(0xFFD97706),
        Color(0xFFB45309),
        Color(0xFF0D9488)
      )
    )
  }

  val glowShadow = if (useLuminousPalette) {
    Shadow(
      color = Color(0xFF2DD4BF).copy(alpha = 0.55f),
      offset = Offset(0f, 2f),
      blurRadius = 14f
    )
  } else {
    Shadow(
      color = Color(0xFF0D9488).copy(alpha = 0.28f),
      offset = Offset(0f, 1.5f),
      blurRadius = 8f
    )
  }

  val goldGlowShadow = if (useLuminousPalette) {
    Shadow(
      color = Color(0xFFFBBF24).copy(alpha = 0.65f),
      offset = Offset(0f, 2f),
      blurRadius = 16f
    )
  } else {
    Shadow(
      color = Color(0xFFD97706).copy(alpha = 0.35f),
      offset = Offset(0f, 1.5f),
      blurRadius = 9f
    )
  }

  return buildAnnotatedString {
    when {
      fullText.contains("ThanatoX") -> {
        val startIdx = fullText.indexOf("ThanatoX")
        val before = fullText.substring(0, startIdx)
        val after = fullText.substring(startIdx + "ThanatoX".length)

        if (before.isNotEmpty()) {
          append(before)
        }
        withStyle(
          SpanStyle(
            brush = stemBrush,
            fontFamily = ThanatoXEnglishFontFamily,
            fontWeight = FontWeight.ExtraBold,
            shadow = glowShadow
          )
        ) {
          append("Thanato")
        }
        withStyle(
          SpanStyle(
            brush = accentXBrush,
            fontFamily = ThanatoXDisplayAccentFontFamily,
            fontWeight = FontWeight.ExtraBold,
            shadow = goldGlowShadow
          )
        ) {
          append("X")
        }
        if (after.isNotEmpty()) {
          append(after)
        }
      }

      fullText.contains("থেনাটক্স") -> {
        val startIdx = fullText.indexOf("থেনাটক্স")
        val before = fullText.substring(0, startIdx)
        val after = fullText.substring(startIdx + "থেনাটক্স".length)

        if (before.isNotEmpty()) {
          append(before)
        }
        withStyle(
          SpanStyle(
            brush = stemBrush,
            fontFamily = ThanatoXBengaliFontFamily,
            fontWeight = FontWeight.ExtraBold,
            shadow = glowShadow
          )
        ) {
          append("থেনাট")
        }
        withStyle(
          SpanStyle(
            brush = accentXBrush,
            fontFamily = ThanatoXBengaliFontFamily,
            fontWeight = FontWeight.ExtraBold,
            shadow = goldGlowShadow
          )
        ) {
          append("ক্স")
        }
        if (after.isNotEmpty()) {
          append(after)
        }
      }

      else -> {
        withStyle(
          SpanStyle(
            brush = stemBrush,
            fontWeight = FontWeight.ExtraBold,
            shadow = glowShadow
          )
        ) {
          append(fullText)
        }
      }
    }
  }
}

/**
 * Unified Signature Brand Wordmark for "ThanatoX" / "থেনাটক্স".
 * Renders with a Bioluminescent Aqua-Teal to Golden-Teal gradient, custom typography,
 * and subtle neon synapse glow across all headers and screens.
 */
@Composable
fun ThanatoXBrandWordmark(
  text: String,
  language: AppLanguage,
  modifier: Modifier = Modifier,
  fontSize: TextUnit = 34.sp,
  isOnTranslucentDarkBackdrop: Boolean = false,
  showUnderlineAccent: Boolean = false
) {
  val isDark = LocalIsDarkTheme.current
  val styledText = remember(text, isDark, isOnTranslucentDarkBackdrop) {
    buildThanatoXBrandAnnotatedString(
      fullText = text,
      isDark = isDark,
      isOnTranslucentDarkBackdrop = isOnTranslucentDarkBackdrop
    )
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    Text(
      text = styledText,
      style = MaterialTheme.typography.headlineLarge.copy(
        fontFamily = if (language == AppLanguage.EN) ThanatoXEnglishFontFamily else ThanatoXBengaliFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize,
        letterSpacing = if (language == AppLanguage.EN) 1.2.sp else 0.4.sp,
        lineHeight = (fontSize.value * 1.18f).sp
      ),
      maxLines = 1,
      softWrap = false,
      overflow = TextOverflow.Ellipsis
    )

    if (showUnderlineAccent) {
      Spacer(modifier = Modifier.height(3.dp))
      Box(
        modifier = Modifier
          .width(78.dp)
          .height(2.2.dp)
          .clip(RoundedCornerShape(50))
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color.Transparent,
                Color(0xFF2DD4BF).copy(alpha = 0.85f),
                Color(0xFFFBBF24).copy(alpha = 0.90f),
                Color(0xFF2DD4BF).copy(alpha = 0.85f),
                Color.Transparent
              )
            )
          )
      )
    }
  }
}

/**
 * Stylish Italic Glow App Subtitle displayed directly underneath the App Name.
 * Framed by subtle bioluminescent ornamental lines and diamond accents so it is
 * unmistakably recognized as the app's official subtitle/tagline.
 */
@Composable
fun ThanatoXAppSubtitleBadge(
  subtitle: String,
  language: AppLanguage,
  modifier: Modifier = Modifier,
  isOnTranslucentDarkBackdrop: Boolean = true
) {
  val isDark = LocalIsDarkTheme.current
  val useLuminous = isDark || isOnTranslucentDarkBackdrop

  val textGradient = if (useLuminous) {
    Brush.horizontalGradient(
      colors = listOf(
        Color(0xFFCCFBF1),
        Color(0xFFFDE68A),
        Color(0xFF99F6E4)
      )
    )
  } else {
    Brush.horizontalGradient(
      colors = listOf(
        Color(0xFF0F766E),
        Color(0xFFB45309),
        Color(0xFF0D9488)
      )
    )
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
    ) {
      // Left ornamental glowing taper line
      Box(
        modifier = Modifier
          .width(24.dp)
          .height(1.2.dp)
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color.Transparent,
                Color(0xFF5EEAD4).copy(alpha = 0.8f),
                Color(0xFFFBBF24).copy(alpha = 0.85f)
              )
            )
          )
      )

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium.copy(
          brush = textGradient,
          fontFamily = if (language == AppLanguage.EN) ThanatoXEnglishFontFamily else ThanatoXBengaliFontFamily,
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.SemiBold,
          fontSize = if (language == AppLanguage.EN) 13.5.sp else 14.sp,
          letterSpacing = if (language == AppLanguage.EN) 0.45.sp else 0.2.sp,
          lineHeight = 19.sp,
          shadow = Shadow(
            color = if (useLuminous) Color(0xFF042F2E).copy(alpha = 0.9f) else Color.Transparent,
            offset = Offset(0f, 1.5f),
            blurRadius = 8f
          )
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f, fill = false)
      )

      Spacer(modifier = Modifier.width(8.dp))

      // Right ornamental glowing taper line
      Box(
        modifier = Modifier
          .width(24.dp)
          .height(1.2.dp)
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color(0xFFFBBF24).copy(alpha = 0.85f),
                Color(0xFF5EEAD4).copy(alpha = 0.8f),
                Color.Transparent
              )
            )
          )
      )
    }
  }
}

/**
 * Official 4-Color Google 'G' Emblem inside a clean white circular badge,
 * matching professional Google Sign-In buttons across Android apps.
 */
@Composable
fun GoogleGLogoIcon(
  modifier: Modifier = Modifier,
  badgeSize: Dp = 28.dp
) {
  Surface(
    shape = CircleShape,
    color = Color.White,
    shadowElevation = 1.dp,
    modifier = modifier.size(badgeSize)
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
    ) {
      val strokeW = size.minDimension * 0.22f
      val diameter = size.minDimension - strokeW
      val topLeft = Offset(strokeW / 2f, strokeW / 2f)
      val arcSize = Size(diameter, diameter)

      // 1. Google Red (Top arc)
      drawArc(
        color = Color(0xFFEA4335),
        startAngle = 215f,
        sweepAngle = 102f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeW, cap = StrokeCap.Butt)
      )

      // 2. Google Yellow (Left arc)
      drawArc(
        color = Color(0xFFFBBC05),
        startAngle = 145f,
        sweepAngle = 72f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeW, cap = StrokeCap.Butt)
      )

      // 3. Google Green (Bottom arc)
      drawArc(
        color = Color(0xFF34A853),
        startAngle = 45f,
        sweepAngle = 102f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeW, cap = StrokeCap.Butt)
      )

      // 4. Google Blue (Lower-right arc + horizontal G crossbar)
      drawArc(
        color = Color(0xFF4285F4),
        startAngle = -12f,
        sweepAngle = 60f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeW, cap = StrokeCap.Butt)
      )

      val cy = size.height * 0.5f
      val cx = size.width * 0.5f
      drawLine(
        color = Color(0xFF4285F4),
        start = Offset(cx + strokeW * 0.05f, cy),
        end = Offset(size.width - strokeW * 0.12f, cy),
        strokeWidth = strokeW * 0.95f,
        cap = StrokeCap.Butt
      )
    }
  }
}

/**
 * Professional Guest Shield & User Profile Silhouette Logo for Guest Mode Sign-In.
 */
@Composable
fun GuestModeBadgeIcon(
  modifier: Modifier = Modifier,
  badgeSize: Dp = 28.dp
) {
  val primary = MaterialTheme.colorScheme.primary
  val container = MaterialTheme.colorScheme.primaryContainer
  Surface(
    shape = CircleShape,
    color = container,
    border = BorderStroke(1.dp, primary.copy(alpha = 0.55f)),
    modifier = modifier.size(badgeSize)
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .padding(4.5.dp)
    ) {
      val w = size.width
      val h = size.height
      val cx = w * 0.5f

      // Protective Shield Outline
      val shieldPath = Path().apply {
        moveTo(cx, h * 0.06f)
        lineTo(w * 0.88f, h * 0.22f)
        cubicTo(
          w * 0.88f, h * 0.56f,
          w * 0.72f, h * 0.82f,
          cx, h * 0.95f
        )
        cubicTo(
          w * 0.28f, h * 0.82f,
          w * 0.12f, h * 0.56f,
          w * 0.12f, h * 0.22f
        )
        close()
      }
      drawPath(
        path = shieldPath,
        color = primary.copy(alpha = 0.16f)
      )
      drawPath(
        path = shieldPath,
        color = primary,
        style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round)
      )

      // Guest User Head inside Shield
      drawCircle(
        color = primary,
        radius = w * 0.14f,
        center = Offset(cx, h * 0.39f)
      )

      // Guest User Shoulders Arc inside Shield
      val shouldersPath = Path().apply {
        moveTo(cx - w * 0.22f, h * 0.74f)
        quadraticTo(cx, h * 0.54f, cx + w * 0.22f, h * 0.74f)
      }
      drawPath(
        path = shouldersPath,
        color = primary,
        style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
      )
    }
  }
}

/**
 * Zero-Lag GPU-Accelerated Neural Background using the user's custom 3D Bioluminescent Neuron
 * & Golden Action-Potential artwork (`R.drawable.thanatox_neural_bg`).
 *
 * Performance Guarantee:
 * Animation state values (`breathScaleState.value` and `glowPulseState.value`) are read EXCLUSIVELY
 * inside `Modifier.graphicsLayer { ... }` blocks, bypassing Compose Recomposition and Layout phases
 * completely and running 100% on the Android GPU RenderThread at 60 FPS with zero lag.
 *
 * Dual-Theme Customization:
 * - Dark Theme: Rich cyan/emerald neurons and golden lightning bolts over a Deep Teal-Obsidian vignette.
 * - Light Theme: Crystal-clear Aqua-Emerald & Teal-Lagoon bioluminescent ColorMatrix with zero chalky white fog,
 *   keeping every neuron branch and golden electrical signal sharp, vivid, and aesthetic.
 */
@Composable
fun NeuralSynapseAmbientBackground(
  modifier: Modifier = Modifier
) {
  val isDark = LocalIsDarkTheme.current
  val painter = rememberSrgbBitmapPainter(id = R.drawable.thanatox_neural_bg, maxDimensionPx = 1080)

  val baseColorFilter = remember(isDark) {
    if (isDark) {
      null
    } else {
      ColorFilter.colorMatrix(
        ColorMatrix(
          floatArrayOf(
            0.94f, 0.05f, 0.00f, 0f, 10f,
            0.05f, 1.12f, 0.10f, 0f, 44f,
            0.04f, 0.14f, 1.08f, 0f, 42f,
            0.00f, 0.00f, 0.00f, 1f, 0f
          )
        )
      )
    }
  }

  val themeOverlayBrush = remember(isDark) {
    if (isDark) {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF051311).copy(alpha = 0.44f),
          Color(0xFF071B18).copy(alpha = 0.26f),
          Color(0xFF051311).copy(alpha = 0.50f)
        )
      )
    } else {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF0A3F3A).copy(alpha = 0.22f),
          Color(0xFF0F766E).copy(alpha = 0.08f),
          Color(0xFF083531).copy(alpha = 0.26f)
        )
      )
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(if (isDark) Color(0xFF051210) else Color(0xFF0D4A44))
      .testTag("neural_synapse_ambient_background")
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = baseColorFilter,
        alpha = if (isDark) 0.78f else 0.94f,
        modifier = Modifier.fillMaxSize()
      )
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(themeOverlayBrush)
    )
  }
}

/**
 * Exclusive full-screen background for the Home Page (`ThanatoXTab.HOME`),
 * preserving the exact original look of `R.drawable.thanatox_home_bg` across both Dark and Light modes.
 */
@Composable
fun HomeSanctuaryAmbientBackground(
  modifier: Modifier = Modifier
) {
  val painter = rememberSrgbBitmapPainter(id = R.drawable.thanatox_home_bg, maxDimensionPx = 1080)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF041418))
      .testTag("home_sanctuary_ambient_background")
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

/**
 * Exclusive full-screen background for the Chatbot Screen (`ThanatoXTab.CONSULT`),
 * displaying `R.drawable.thanatox_chat_dark_bg` in Dark Mode and
 * `R.drawable.thanatox_chat_light_bg` in Light Mode with their original colors intact.
 */
@Composable
fun ChatbotSanctuaryAmbientBackground(
  modifier: Modifier = Modifier
) {
  val isDark = LocalIsDarkTheme.current
  val painter = rememberSrgbBitmapPainter(
    id = if (isDark) R.drawable.thanatox_chat_dark_bg else R.drawable.thanatox_chat_light_bg,
    maxDimensionPx = 1080
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(if (isDark) Color(0xFF080E12) else Color(0xFFD6E4E8))
      .testTag("chatbot_sanctuary_ambient_background")
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

/**
 * Exclusive full-screen background for the Test / Assessment Screen (`ThanatoXTab.ASSESSMENT`),
 * preserving the exact original look of `R.drawable.thanatox_test_bg` across both Dark and Light modes.
 */
@Composable
fun TestSanctuaryAmbientBackground(
  modifier: Modifier = Modifier
) {
  val painter = rememberSrgbBitmapPainter(id = R.drawable.thanatox_test_bg, maxDimensionPx = 1080)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF050709))
      .testTag("test_sanctuary_ambient_background")
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

/**
 * Exclusive full-screen background for the Mind Care Screen (`ThanatoXTab.MIND_CARE`),
 * preserving the exact original look of `R.drawable.thanatox_mindcare_bg` across both Dark and Light modes.
 */
@Composable
fun MindCareSanctuaryAmbientBackground(
  modifier: Modifier = Modifier
) {
  val painter = rememberSrgbBitmapPainter(id = R.drawable.thanatox_mindcare_bg, maxDimensionPx = 1080)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF040507))
      .testTag("mindcare_sanctuary_ambient_background")
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

/**
 * Official Dr. Sifat Khan (ড. সিফাত খান) circular avatar displaying the full uncropped portrait
 * (`R.drawable.dr_sifat_khan_avatar`).
 */
@Composable
fun DoctorSifatAvatar(
  modifier: Modifier = Modifier
) {
  val painter = rememberSrgbBitmapPainter(id = R.drawable.dr_sifat_khan_avatar, maxDimensionPx = 256)
  Surface(
    shape = CircleShape,
    color = Color(0xFF0B2220),
    border = BorderStroke(1.2.dp, Color(0xFF2DD4BF).copy(alpha = 0.65f)),
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .clip(CircleShape),
      contentAlignment = Alignment.Center
    ) {
      if (painter != null) {
        Image(
          painter = painter,
          contentDescription = "ড. সিফাত খান",
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

/**
 * Aesthetic Anime / Illustration-style Doctor Avatar wearing a crisp white medical apron (lab coat),
 * emerald-teal shirt & tie, designer gold glasses, and a golden-teal stethoscope
 * (`R.drawable.ic_aesthetic_apron_doctor`).
 */
@Composable
fun StylishApronDoctorAvatar(
  modifier: Modifier = Modifier,
  isCircular: Boolean = false,
  cornerRadius: Dp = 16.dp
) {
  val shape = if (isCircular) CircleShape else RoundedCornerShape(cornerRadius)
  Surface(
    shape = shape,
    color = Color(0xFF061E22),
    border = BorderStroke(
      width = 1.5.dp,
      brush = Brush.linearGradient(
        colors = listOf(
          Color(0xFF5EEAD4),
          Color(0xFFFBBF24),
          Color(0xFF2DD4BF)
        )
      )
    ),
    modifier = modifier.testTag("stylish_apron_doctor_avatar")
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_aesthetic_apron_doctor),
      contentDescription = "ড. সিফাত খান অ্যাসথেটিক অবতার",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .clip(shape)
    )
  }
}

/**
 * Official ThanatoX Neural Brain Icon (`R.drawable.thanatox_app_icon`) replacing all previous
 * lotus flower illustrations across the Welcome Screen, Top Header Bar, and Home Hero Card.
 */
@Composable
fun LotusSanctuaryIllustration(
  modifier: Modifier = Modifier,
  isCircular: Boolean = false,
  cornerRadius: Dp = 22.dp
) {
  val shape = if (isCircular) CircleShape else RoundedCornerShape(cornerRadius)
  val painter = rememberSrgbBitmapPainter(id = R.drawable.thanatox_app_icon, maxDimensionPx = 256)
  Box(
    modifier = modifier
      .clip(shape)
      .background(LotusSanctuaryDark),
    contentAlignment = Alignment.Center
  ) {
    if (painter != null) {
      Image(
        painter = painter,
        contentDescription = "ThanatoX App Icon",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

/**
 * Dark / Light Mode Toggle Pill Button placed alongside the Language Switcher on both the
 * Welcome Screen and Main Header Bar.
 */
@Composable
fun DarkModeToggleButton(
  isDarkMode: Boolean,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDarkMode) 0.78f else 0.72f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
    modifier = modifier
      .size(36.dp)
      .clip(CircleShape)
      .clickable { onToggle() }
      .testTag("dark_mode_toggle_button")
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
fun HeaderSettingsGearButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = LocalIsDarkTheme.current
  Surface(
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.78f else 0.72f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
    modifier = modifier
      .size(40.dp)
      .clip(CircleShape)
      .clickable { onClick() }
      .testTag("header_settings_button")
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = Icons.Default.Settings,
        contentDescription = "Settings",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
fun MedicalStarButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = LocalIsDarkTheme.current
  Surface(
    shape = CircleShape,
    color = if (isDark) Color(0xFF2B1C28) else Color(0xFFD5F5EE).copy(alpha = 0.75f),
    modifier = modifier
      .size(36.dp)
      .clip(CircleShape)
      .clickable { onClick() }
      .testTag("api_key_indicator_button")
  ) {
    Box(contentAlignment = Alignment.Center) {
      Canvas(modifier = Modifier.size(18.dp)) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2.1f
        val angles = listOf(0f, 60f, 120f)
        angles.forEach { deg ->
          val rad = deg * (PI.toFloat() / 180f)
          val dx = cos(rad) * r
          val dy = sin(rad) * r
          drawLine(
            color = if (isDark) Color(0xFFFF6B6B) else CrimsonMedicalStar,
            start = Offset(c.x - dx, c.y - dy),
            end = Offset(c.x + dx, c.y + dy),
            strokeWidth = 3.2.dp.toPx(),
            cap = StrokeCap.Round
          )
        }
      }
    }
  }
}

/**
 * Translucent Aqua-Teal Glassmorphism Card for both Dark Mode and Light Mode.
 * Uses 0.dp elevation to avoid Android's opaque shadow rectangle artifact behind semi-transparent surfaces.
 */
@Composable
fun GradientGlassCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 24.dp,
  isHighlighted: Boolean = false,
  containerColor: Color = MaterialTheme.colorScheme.surface.copy(
    alpha = if (LocalIsDarkTheme.current) 0.78f else 0.68f
  ),
  content: @Composable ColumnScope.() -> Unit
) {
  val isDark = LocalIsDarkTheme.current
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(cornerRadius),
    border = BorderStroke(
      width = if (isHighlighted) 1.5.dp else 1.1.dp,
      color = if (isHighlighted) {
        if (isDark) {
          MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
        } else {
          Color(0xFF0D9488).copy(alpha = 0.75f)
        }
      } else {
        if (isDark) {
          MaterialTheme.colorScheme.outline.copy(alpha = 0.75f)
        } else {
          MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)
        }
      }
    ),
    colors = CardDefaults.cardColors(
      containerColor = containerColor
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(content = content)
  }
}

@Composable
fun ApiStatusAndKeyDialog(
  language: AppLanguage,
  apiStatus: ApiConnectionStatus,
  onDismiss: () -> Unit,
  isDarkMode: Boolean = LocalIsDarkTheme.current,
  onToggleDarkMode: (() -> Unit)? = null,
  onToggleLanguage: (() -> Unit)? = null,
  currentUserDisplayName: String? = null,
  onGoogleSignInClick: (() -> Unit)? = null,
  onSignOutClick: (() -> Unit)? = null
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val strings = stringsFor(language)

  var keyInput by remember { mutableStateOf("") }
  var isTesting by remember { mutableStateOf(false) }
  var feedbackMessage by remember { mutableStateOf<String?>(null) }
  var isErrorFeedback by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    GradientGlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("api_status_dialog"),
      cornerRadius = 26.dp,
      isHighlighted = true,
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isDarkMode) 0.94f else 0.90f)
    ) {
      Column(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.BN) "সেটিংস ও নিয়ন্ত্রণ" else "Settings & Controls",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Theme (Dark / Light Mode) & Language Controls inside Settings
        if (onToggleDarkMode != null || onToggleLanguage != null) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              if (onToggleDarkMode != null) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleDarkMode() }
                    .padding(vertical = 6.dp, horizontal = 4.dp)
                    .testTag("settings_dark_mode_toggle"),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = if (language == AppLanguage.BN) "থিম মোড" else "App Theme",
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = if (isDarkMode) {
                          if (language == AppLanguage.BN) "ডার্ক মোড" else "Dark Mode"
                        } else {
                          if (language == AppLanguage.BN) "লাইট মোড" else "Light Mode"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1
                      )
                    }
                  }
                }
              }

              if (onToggleDarkMode != null && onToggleLanguage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(6.dp))
              }

              if (onToggleLanguage != null) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleLanguage() }
                    .padding(vertical = 6.dp, horizontal = 4.dp)
                    .testTag("settings_language_toggle"),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Language,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = if (language == AppLanguage.BN) "ভাষা (Language)" else "Language (ভাষা)",
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                  ) {
                    Text(
                      text = if (language == AppLanguage.BN) "বাংলা ⇄ EN" else "English ⇄ বাং",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onPrimaryContainer,
                      maxLines = 1,
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Account & Sign-Out Section inside Settings
        if (onGoogleSignInClick != null || onSignOutClick != null) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              if (!currentUserDisplayName.isNullOrBlank()) {
                Text(
                  text = strings.cloudSignedInAs(currentUserDisplayName),
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.primary,
                  maxLines = 1,
                  modifier = Modifier.fillMaxWidth()
                )
              } else {
                Text(
                  text = strings.guestSessionBadge,
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.primary,
                  maxLines = 1,
                  modifier = Modifier.fillMaxWidth()
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              if (onGoogleSignInClick != null) {
                OutlinedButton(
                  onClick = {
                    onGoogleSignInClick()
                    onDismiss()
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialog_google_sign_in_button"),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  GoogleGLogoIcon(badgeSize = 22.dp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = strings.signInButton,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
              }

              if (onSignOutClick != null) {
                OutlinedButton(
                  onClick = {
                    onSignOutClick()
                    onDismiss()
                  },
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialog_sign_out_button")
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = strings.signOutDescription,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. AI Psychiatrist & API Status / Custom Key Section
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (apiStatus.lastLatencyMs != null && apiStatus.isLiveConnected) {
                  strings.apiConnectedSuccess(apiStatus.lastLatencyMs)
                } else {
                  strings.apiConnectedUsingBuiltIn
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1
              )
              Text(
                text = "${strings.doctorHeaderName} • ${strings.doctorHeaderRole}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = keyInput,
          onValueChange = { keyInput = it },
          label = { Text(strings.apiKeyInputLabel, maxLines = 1) },
          placeholder = { Text(strings.apiKeyInputPlaceholder, maxLines = 1) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_api_key_input"),
          shape = RoundedCornerShape(14.dp)
        )

        if (feedbackMessage != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = feedbackMessage.orEmpty(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isErrorFeedback) {
              MaterialTheme.colorScheme.error
            } else {
              MaterialTheme.colorScheme.primary
            }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = {
            scope.launch {
              isTesting = true
              feedbackMessage = null
              if (keyInput.isNotBlank()) {
                GeminiPsychiatristService.saveCustomKey(context, keyInput.trim())
              }
              val result = GeminiPsychiatristService.testApiKeyConnection(keyInput.trim())
              isTesting = false
              result.onSuccess { ms ->
                isErrorFeedback = false
                feedbackMessage = strings.apiConnectedSuccess(ms)
              }.onFailure {
                isErrorFeedback = true
                feedbackMessage = strings.apiErrorInvalidKey
              }
            }
          },
          enabled = !isTesting,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("save_api_key_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = DeepForestEmerald,
            contentColor = Color.White
          )
        ) {
          if (isTesting) {
            CircularProgressIndicator(
              modifier = Modifier.size(18.dp),
              strokeWidth = 2.dp,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
          }
          Text(
            text = if (keyInput.isNotBlank()) strings.apiSaveAndConnectButton else strings.apiTestButton,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          TextButton(
            onClick = {
              GeminiPsychiatristService.saveCustomKey(context, "")
              keyInput = ""
              isErrorFeedback = false
              feedbackMessage = strings.apiConnectedUsingBuiltIn
            }
          ) {
            Text(strings.apiClearCustomKeyButton, maxLines = 1)
          }

          TextButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_api_dialog_button")
          ) {
            Text(strings.apiCloseButton, fontWeight = FontWeight.Bold, maxLines = 1)
          }
        }
      }
    }
  }
}

@Composable
fun MoodSparklineChart(
  language: AppLanguage,
  logs: List<MoodLog>,
  modifier: Modifier = Modifier
) {
  if (logs.isEmpty()) return
  val strings = stringsFor(language)
  val recent = remember(logs) { logs.take(7).reversed() }
  val primaryColor = MaterialTheme.colorScheme.primary

  GradientGlassCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 22.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = strings.moodTrendTitle,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "${recent.last().moodScore}/10",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = primaryColor
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(90.dp)
      ) {
        val w = size.width
        val h = size.height
        val count = recent.size
        val moodPath = Path()

        recent.forEachIndexed { index, item ->
          val x = if (count == 1) w / 2f else (index.toFloat() / (count - 1)) * (w - 24.dp.toPx()) + 12.dp.toPx()
          val moodRatio = (item.moodScore.coerceIn(1, 10) - 1) / 9f
          val yMood = h - (moodRatio * (h - 24.dp.toPx())) - 12.dp.toPx()

          if (index == 0) {
            moodPath.moveTo(x, yMood)
          } else {
            moodPath.lineTo(x, yMood)
          }

          drawCircle(
            color = primaryColor,
            radius = 4.5.dp.toPx(),
            center = Offset(x, yMood)
          )
        }

        if (count > 1) {
          drawPath(
            path = moodPath,
            color = primaryColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
          )
        }
      }
    }
  }
}
