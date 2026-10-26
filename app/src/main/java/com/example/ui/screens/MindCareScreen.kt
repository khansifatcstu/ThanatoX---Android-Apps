package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MoodLog
import com.example.data.model.PsychAssessment
import com.example.data.model.UiState
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme

enum class MindCareSubSection {
  HUB,
  TOOLKIT,
  JOURNAL,
  SESSION
}

@Composable
fun MindCareScreen(
  language: AppLanguage,
  activeSection: MindCareSubSection,
  onSelectSection: (MindCareSubSection) -> Unit,
  moodLogsState: UiState<List<MoodLog>>,
  isSubmittingMood: Boolean,
  onSaveMoodLog: (Int, String, Int, Int, String) -> Unit,
  onDeleteMoodLog: (String) -> Unit,
  assessmentsState: UiState<List<PsychAssessment>> = UiState.Success(emptyList()),
  onSaveSessionReport: (String, String) -> Unit = { _, _ -> },
  onDeleteSessionReport: (String) -> Unit = {},
  onOpenLiveChatbot: () -> Unit = {}
) {
  BackHandler(enabled = activeSection != MindCareSubSection.HUB) {
    onSelectSection(MindCareSubSection.HUB)
  }

  val savedSessionReports = ((assessmentsState as? UiState.Success)?.data.orEmpty())
    .filter { it.assessmentType == "PSYCH_SESSION" }

  Column(modifier = Modifier.fillMaxSize()) {
    // Top Liquid Glass Segmented Switcher Bar (Hub | Toolkit | Journal | Session)
    MindCareTopSwitcherBar(
      language = language,
      activeSection = activeSection,
      onSelectSection = onSelectSection
    )

    Box(modifier = Modifier.weight(1f)) {
      when (activeSection) {
        MindCareSubSection.HUB -> {
          val savedCount = (moodLogsState as? UiState.Success)?.data?.size ?: 0
          MindCareHubOverview(
            language = language,
            savedJournalCount = savedCount,
            onOpenToolkit = { onSelectSection(MindCareSubSection.TOOLKIT) },
            onOpenJournal = { onSelectSection(MindCareSubSection.JOURNAL) },
            onOpenSession = { onSelectSection(MindCareSubSection.SESSION) }
          )
        }

        MindCareSubSection.TOOLKIT -> {
          ToolkitScreen(language = language)
        }

        MindCareSubSection.JOURNAL -> {
          MoodAndCalmScreen(
            language = language,
            moodLogsState = moodLogsState,
            isSubmitting = isSubmittingMood,
            onSaveMoodLog = onSaveMoodLog,
            onDeleteMoodLog = onDeleteMoodLog
          )
        }

        MindCareSubSection.SESSION -> {
          PsychologicalSessionSection(
            language = language,
            savedSessions = savedSessionReports,
            onSaveSessionReport = onSaveSessionReport,
            onDeleteSessionReport = onDeleteSessionReport,
            onOpenLiveChatbot = onOpenLiveChatbot
          )
        }
      }
    }
  }
}

@Composable
private fun MindCareTopSwitcherBar(
  language: AppLanguage,
  activeSection: MindCareSubSection,
  onSelectSection: (MindCareSubSection) -> Unit
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current

  val items = listOf(
    Triple(
      MindCareSubSection.HUB,
      if (language == AppLanguage.BN) "হাব" else "Hub",
      Icons.Default.GridView
    ),
    Triple(
      MindCareSubSection.TOOLKIT,
      strings.tabToolkit,
      Icons.Default.SelfImprovement
    ),
    Triple(
      MindCareSubSection.JOURNAL,
      strings.tabMoodDiary,
      Icons.AutoMirrored.Outlined.MenuBook
    ),
    Triple(
      MindCareSubSection.SESSION,
      if (language == AppLanguage.BN) "সেশন" else "Session",
      Icons.Default.Psychology
    )
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(18.dp),
    color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.78f else 0.68f),
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.55f else 0.50f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      items.forEach { (section, label, icon) ->
        val selected = activeSection == section
        val pillBg = if (selected) {
          DeepForestEmerald
        } else {
          Color.Transparent
        }
        val contentColor = if (selected) {
          Color.White
        } else {
          MaterialTheme.colorScheme.onSurface
        }

        Surface(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onSelectSection(section) }
            .testTag("mind_care_switch_${section.name.lowercase()}"),
          shape = RoundedCornerShape(14.dp),
          color = pillBg
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 6.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = icon,
              contentDescription = label,
              tint = contentColor,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = label,
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp
              ),
              color = contentColor,
              maxLines = 1,
              softWrap = false,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MindCareHubOverview(
  language: AppLanguage,
  savedJournalCount: Int,
  onOpenToolkit: () -> Unit,
  onOpenJournal: () -> Unit,
  onOpenSession: () -> Unit
) {
  val strings = stringsFor(language)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 6.dp, bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Intro Banner
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Spa,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (language == AppLanguage.BN) {
                "মনোযত্ন ও আত্মপরিচর্যা হাব"
              } else {
                "Mind Care & Self-Care Hub"
              },
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (language == AppLanguage.BN) {
              "আপনার মানসিক প্রশান্তির জন্য থেরাপি টুলকিট, প্রতিদিনের মুড জার্নাল অথবা এআই সাইকিয়াট্রিস্টের সাথে সাইকোলজিকাল সেশন—যেকোনো একটি বেছে নিন।"
            } else {
              "Choose between guided therapy exercises in the Toolkit, daily emotional reflection in your Journal, or a structured AI Psychological Session."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Option Card 1: Psychological Session (সাইকোলজিকাল সেশন)
    item {
      MindCareFeatureOptionCard(
        title = if (language == AppLanguage.BN) {
          "সাইকোলজিকাল সেশন (Psychological Session)"
        } else {
          "Psychological Session (AI Psychiatrist)"
        },
        badgeText = if (language == AppLanguage.BN) {
          "সমস্যা নির্বাচন • ৫-ধাপ প্রশ্নোত্তর • প্রেসক্রিপশন"
        } else {
          "Problem Selection • 5-Step Q&A • Prescription"
        },
        subtitle = if (language == AppLanguage.BN) {
          "একজন সাইকিয়াট্রিস্ট যেভাবে ধাপে ধাপে প্রশ্ন করে সমস্যার মূল কারণ বের করেন এবং শেষে পূর্ণাঙ্গ প্রেসক্রিপশন বা গাইডলাইন দেন।"
        } else {
          "Experience a structured psychiatrist session with step-by-step clinical questions and a final personalized therapeutic guideline."
        },
        icon = Icons.Default.Psychology,
        highlights = if (language == AppLanguage.BN) {
          listOf(
            "প্রধান মানসিক সমস্যা নির্বাচন",
            "ধাপে ধাপে ক্লিনিক্যাল প্রশ্নোত্তর",
            "শেষে প্রেসক্রিপশন ও গাইডলাইন"
          )
        } else {
          listOf(
            "Select Primary Concern",
            "Step-by-Step Clinical Q&A",
            "Final Prescription & Guideline"
          )
        },
        ctaLabel = if (language == AppLanguage.BN) {
          "সাইকোলজিকাল সেশন শুরু করুন"
        } else {
          "Start Psychological Session"
        },
        testTag = "mind_care_card_session",
        onClick = onOpenSession
      )
    }

    // Option Card 2: Toolkit (টুলকিট)
    item {
      MindCareFeatureOptionCard(
        title = if (language == AppLanguage.BN) "থেরাপি টুলকিট (Toolkit)" else "Therapy Toolkit",
        badgeText = if (language == AppLanguage.BN) "৩টি বৈজ্ঞানিক অনুশীলন" else "3 Guided Exercises",
        subtitle = strings.toolkitHeaderSubtitle,
        icon = Icons.Default.SelfImprovement,
        highlights = if (language == AppLanguage.BN) {
          listOf(
            "৪-৭-৮ শ্বাস-প্রশ্বাস ব্যায়াম",
            "৫-৪-৩-২-১ গ্রাউন্ডিং মেডিটেশন",
            "পেশী শিথিলকরণ (PMR)"
          )
        } else {
          listOf(
            "4-7-8 Calm Breathing",
            "5-4-3-2-1 Sensory Grounding",
            "Muscle Relaxation (PMR)"
          )
        },
        ctaLabel = if (language == AppLanguage.BN) {
          "টুলকিটের সব অ্যাক্টিভিটি দেখুন"
        } else {
          "Explore All Toolkit Activities"
        },
        testTag = "mind_care_card_toolkit",
        onClick = onOpenToolkit
      )
    }

    // Option Card 3: Journal (জার্নাল)
    item {
      val journalBadge = if (language == AppLanguage.BN) {
        if (savedJournalCount > 0) "সংরক্ষিত নোট: $savedJournalCount" else "এআই বিশ্লেষণসহ ডায়েরি"
      } else {
        if (savedJournalCount > 0) "Saved Entries: $savedJournalCount" else "AI-Analyzed Diary"
      }

      MindCareFeatureOptionCard(
        title = if (language == AppLanguage.BN) "ডায়েরি ও মুড জার্নাল (Journal)" else "Mood & Diary Journal",
        badgeText = journalBadge,
        subtitle = strings.moodSectionSubtitle,
        icon = Icons.AutoMirrored.Outlined.MenuBook,
        highlights = if (language == AppLanguage.BN) {
          listOf(
            "প্রতিদিনের মেজাজ ও ঘুম ট্র্যাকার",
            "ড. সিফাতের এআই জার্নাল বিশ্লেষণ",
            "মানসিক অবস্থার গ্রাফ"
          )
        } else {
          listOf(
            "Daily Mood & Sleep Tracker",
            "Dr. Sifat's AI Journal Insight",
            "Mood & Stress Trend Graph"
          )
        },
        ctaLabel = if (language == AppLanguage.BN) {
          "জার্নালে প্রবেশ করুন"
        } else {
          "Open Mood & Diary Journal"
        },
        testTag = "mind_care_card_journal",
        onClick = onOpenJournal
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MindCareFeatureOptionCard(
  title: String,
  badgeText: String,
  subtitle: String,
  icon: ImageVector,
  highlights: List<String>,
  ctaLabel: String,
  testTag: String,
  onClick: () -> Unit
) {
  GradientGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(26.dp))
      .clickable { onClick() }
      .testTag(testTag),
    cornerRadius = 26.dp,
    isHighlighted = true
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = title,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(28.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = title,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
              shape = RoundedCornerShape(50),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
              border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
              )
            ) {
              Text(
                text = badgeText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        highlights.forEach { pill ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
            border = BorderStroke(
              width = 1.dp,
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
            )
          ) {
            Text(
              text = "• $pill",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Surface(
        shape = RoundedCornerShape(16.dp),
        color = DeepForestEmerald,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = ctaLabel,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = Color.White,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
