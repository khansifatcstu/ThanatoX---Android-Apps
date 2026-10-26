package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import kotlinx.coroutines.delay

@Composable
fun ToolkitScreen(
  language: AppLanguage
) {
  val strings = stringsFor(language)
  var isBreathingActive by rememberSaveable { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Banner
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.toolkitHeaderTitle,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = strings.toolkitHeaderSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // 1. 4-7-8 Guided Breathing Visualizer
    item {
      GuidedBreathingToolkitCard(
        language = language,
        isActive = isBreathingActive,
        onToggle = { isBreathingActive = !isBreathingActive }
      )
    }

    // 2. 5-4-3-2-1 Grounding Exercise Card
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.groundingTitle,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = strings.groundingSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          strings.groundingSteps.forEachIndexed { idx, step ->
            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${5 - idx}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = step,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    // 3. Progressive Muscle Relaxation Card
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.muscleRelaxTitle,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = strings.muscleRelaxSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
          strings.muscleRelaxSteps.forEach { step ->
            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Text(
                text = "• $step",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun GuidedBreathingToolkitCard(
  language: AppLanguage,
  isActive: Boolean,
  onToggle: () -> Unit
) {
  val strings = stringsFor(language)
  var phaseText by remember(language) { mutableStateOf(strings.breathingSubtitleIdle) }
  var secondsLeft by remember { mutableIntStateOf(4) }
  val primaryColor = MaterialTheme.colorScheme.primary
  val containerGlowColor = MaterialTheme.colorScheme.primaryContainer

  LaunchedEffect(isActive, language) {
    if (!isActive) {
      phaseText = strings.breathingSubtitleIdle
      secondsLeft = 4
      return@LaunchedEffect
    }
    while (isActive) {
      for (s in 4 downTo 1) {
        phaseText = strings.breathingInhale
        secondsLeft = s
        delay(1000L)
      }
      for (s in 7 downTo 1) {
        phaseText = strings.breathingHold
        secondsLeft = s
        delay(1000L)
      }
      for (s in 8 downTo 1) {
        phaseText = strings.breathingExhale
        secondsLeft = s
        delay(1000L)
      }
    }
  }

  GradientGlassCard(
    modifier = Modifier.fillMaxWidth(),
    cornerRadius = 24.dp,
    isHighlighted = isActive
  ) {
    Column(
      modifier = Modifier.padding(18.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Air,
          contentDescription = null,
          tint = primaryColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = strings.breathingTitle,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      if (isActive) {
        val transition = rememberInfiniteTransition(label = "toolkit_breath_anim")
        val radiusScale by transition.animateFloat(
          initialValue = 0.74f,
          targetValue = 1.22f,
          animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
          ),
          label = "toolkit_breath_radius"
        )
        Spacer(modifier = Modifier.height(14.dp))
        Box(
          modifier = Modifier.size(136.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2f, size.height / 2f)
            val r = (size.minDimension / 2.5f) * radiusScale
            drawCircle(
              brush = Brush.radialGradient(
                colors = listOf(containerGlowColor, Color.Transparent),
                center = c,
                radius = r * 1.25f
              ),
              radius = r * 1.25f,
              center = c
            )
            drawCircle(
              color = primaryColor,
              radius = r,
              center = c,
              style = Stroke(width = 3.dp.toPx())
            )
          }
          Text(
            text = "${secondsLeft}s",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = primaryColor
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = phaseText,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = primaryColor,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(12.dp))
      Button(
        onClick = onToggle,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("toggle_breathing_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = DeepForestEmerald,
          contentColor = Color.White
        )
      ) {
        Text(
          text = if (isActive) strings.breathingStopButton else strings.breathingStartButton,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          softWrap = false
        )
      }
    }
  }
}
