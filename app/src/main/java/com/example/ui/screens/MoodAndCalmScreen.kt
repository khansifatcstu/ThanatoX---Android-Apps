package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.MoodLog
import com.example.data.model.UiState
import com.example.ui.components.GradientGlassCard
import com.example.ui.components.MoodSparklineChart
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun MoodAndCalmScreen(
  language: AppLanguage,
  moodLogsState: UiState<List<MoodLog>>,
  isSubmitting: Boolean,
  onSaveMoodLog: (Int, String, Int, Int, String) -> Unit,
  onDeleteMoodLog: (String) -> Unit
) {
  val strings = stringsFor(language)
  var moodScore by rememberSaveable { mutableIntStateOf(7) }
  var selectedEmotionIndex by rememberSaveable { mutableIntStateOf(0) }
  var sleepHours by rememberSaveable { mutableIntStateOf(7) }
  var stressLevel by rememberSaveable { mutableIntStateOf(4) }
  var journalNote by rememberSaveable { mutableStateOf("") }

  val currentEmotion = strings.emotions.getOrElse(selectedEmotionIndex) { strings.emotions.first() }
  val logs = (moodLogsState as? UiState.Success)?.data.orEmpty()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    if (logs.isNotEmpty()) {
      item {
        MoodSparklineChart(
          language = language,
          logs = logs
        )
      }
    }

    // Daily Mood, Sleep & Diary Logger Card
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.moodSectionTitle,
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = strings.moodSectionSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "${strings.selectEmotionLabel}: $currentEmotion",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            strings.emotions.forEachIndexed { idx, emotion ->
              FilterChip(
                selected = selectedEmotionIndex == idx,
                onClick = { selectedEmotionIndex = idx },
                label = { Text(emotion) }
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = strings.moodScoreLabel(moodScore),
            style = MaterialTheme.typography.labelLarge
          )
          Slider(
            value = moodScore.toFloat(),
            onValueChange = { moodScore = it.roundToInt().coerceIn(1, 10) },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.testTag("mood_score_slider")
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Bedtime,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = strings.sleepHoursLabel(sleepHours),
              style = MaterialTheme.typography.labelLarge,
              maxLines = 1,
              softWrap = false
            )
          }
          Slider(
            value = sleepHours.toFloat(),
            onValueChange = { sleepHours = it.roundToInt().coerceIn(0, 14) },
            valueRange = 0f..14f,
            steps = 13
          )

          Text(
            text = strings.stressLevelLabel(stressLevel),
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            softWrap = false
          )
          Slider(
            value = stressLevel.toFloat(),
            onValueChange = { stressLevel = it.roundToInt().coerceIn(1, 10) },
            valueRange = 1f..10f,
            steps = 8
          )

          OutlinedTextField(
            value = journalNote,
            onValueChange = { journalNote = it },
            label = { Text(strings.diaryInputLabel) },
            placeholder = { Text(strings.diaryInputPlaceholder) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("mood_journal_input"),
            shape = RoundedCornerShape(14.dp),
            minLines = 3,
            maxLines = 5
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = {
              val noteToSave = journalNote.trim().ifEmpty { currentEmotion }
              onSaveMoodLog(moodScore, currentEmotion, sleepHours, stressLevel, noteToSave)
              journalNote = ""
            },
            enabled = !isSubmitting,
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("save_mood_log_button"),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text(
              text = if (isSubmitting) strings.savingMoodButton else strings.saveMoodButton,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              softWrap = false
            )
          }
        }
      }
    }

    // Mood & Diary History
    item {
      Text(
        text = strings.moodHistoryTitle(logs.size),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    if (logs.isEmpty()) {
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 18.dp
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = strings.emptyMoodHistory,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(logs, key = { it.id.ifEmpty { it.hashCode().toString() } }) { log ->
        MoodLogCard(
          language = language,
          log = log,
          onDelete = { if (log.id.isNotBlank()) onDeleteMoodLog(log.id) }
        )
      }
    }
  }
}

@Composable
private fun MoodLogCard(
  language: AppLanguage,
  log: MoodLog,
  onDelete: () -> Unit
) {
  val strings = stringsFor(language)
  val dateStr = remember(log.createdAt) {
    val ts = log.createdAt ?: Timestamp.now()
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(ts.toDate())
  }

  GradientGlassCard(
    modifier = Modifier.fillMaxWidth(),
    cornerRadius = 20.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
        ) {
          Text(
            text = "${log.emotionLabel} • ${log.moodScore}/10",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = dateStr,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = strings.deleteMessageDesc,
              modifier = Modifier.size(18.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = strings.sleepAndStressSummary(log.sleepHours, log.stressLevel),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = log.journalNote,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (log.aiInsight.isNotBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = log.aiInsight,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(12.dp)
          )
        }
      }
    }
  }
}
