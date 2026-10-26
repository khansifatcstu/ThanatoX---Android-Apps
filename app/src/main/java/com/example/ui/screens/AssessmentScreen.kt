package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PsychAssessment
import com.example.data.model.PsychConditionAnalysis
import com.example.data.model.PsychologicalTestsCatalog
import com.example.data.model.TestCategory
import com.example.data.model.UiState
import com.example.data.remote.GeminiPsychiatristService
import com.example.ui.components.DoctorSifatAvatar
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun AssessmentScreen(
  language: AppLanguage,
  assessmentsState: UiState<List<PsychAssessment>>,
  initialAssessmentKey: String? = null,
  onSubmitAssessment: (String, Int, String, String) -> Unit,
  onDeleteAssessment: (String) -> Unit = {}
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current
  val glassCardColor = MaterialTheme.colorScheme.surface.copy(
    alpha = if (isDark) 0.54f else 0.58f
  )
  val scope = rememberCoroutineScope()
  val allTests = PsychologicalTestsCatalog.allTests

  var selectedTypeKey by rememberSaveable {
    mutableStateOf(
      if (!initialAssessmentKey.isNullOrBlank() && allTests.any { it.key == initialAssessmentKey }) {
        initialAssessmentKey
      } else {
        PsychologicalTestsCatalog.clinicalTests.first().key
      }
    )
  }

  val activeTest = remember(selectedTypeKey) {
    PsychologicalTestsCatalog.findTestByKey(selectedTypeKey)
  }

  var selectedCategory by rememberSaveable {
    mutableStateOf(activeTest.category)
  }

  val categoryTests = remember(selectedCategory) {
    PsychologicalTestsCatalog.testsForCategory(selectedCategory)
  }

  var currentQuestionIndex by rememberSaveable(selectedTypeKey) { mutableIntStateOf(0) }
  val selectedAnswers = remember(selectedTypeKey) {
    mutableStateListOf<Int>().apply {
      repeat(activeTest.questions.size) { add(-1) }
    }
  }
  var currentAnalysis by remember(selectedTypeKey) { mutableStateOf<PsychConditionAnalysis?>(null) }
  var isGeneratingAiInsight by remember(selectedTypeKey) { mutableStateOf(false) }

  LaunchedEffect(initialAssessmentKey) {
    if (!initialAssessmentKey.isNullOrBlank()) {
      val matched = allTests.find { it.key == initialAssessmentKey }
      if (matched != null) {
        selectedCategory = matched.category
        selectedTypeKey = matched.key
      }
    }
  }

  val rawAssessments = (assessmentsState as? UiState.Success)?.data.orEmpty()
  val assessments = remember(rawAssessments) {
    rawAssessments.filter { it.assessmentType != "PSYCH_SESSION" }
  }
  val totalQuestions = activeTest.questions.size.coerceAtLeast(1)
  val safeQuestionIndex = currentQuestionIndex.coerceIn(0, totalQuestions - 1)
  val activeQuestion = activeTest.questions[safeQuestionIndex]
  val currentSelectedScore = selectedAnswers.getOrNull(safeQuestionIndex) ?: -1

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Header, 2-Category Tab Switcher & Test Selector Card
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true,
        containerColor = glassCardColor
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.PsychologyAlt,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.assessmentTitle,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = strings.assessmentSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 2 Category Tabs: Original Clinical Scales (4) vs Psychological Tests (5)
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              val categories = listOf(
                TestCategory.CLINICAL_SCALES to (
                  if (language == AppLanguage.BN) "অরিজিনাল ক্লিনিক্যাল স্কেল (৪)"
                  else "Original Clinical Scales (4)"
                ),
                TestCategory.PSYCHOLOGICAL_TESTS to (
                  if (language == AppLanguage.BN) "মনস্তাত্ত্বিক পরীক্ষা (৫)"
                  else "Psychological Tests (5)"
                )
              )

              categories.forEach { (category, tabTitle) ->
                val isTabSelected = selectedCategory == category
                Surface(
                  color = if (isTabSelected) DeepForestEmerald else Color.Transparent,
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                      if (selectedCategory != category) {
                        selectedCategory = category
                        val firstInCat = PsychologicalTestsCatalog.testsForCategory(category).firstOrNull()
                        if (firstInCat != null) {
                          selectedTypeKey = firstInCat.key
                        }
                      }
                    }
                    .testTag("assessment_category_${category.name.lowercase()}")
                ) {
                  Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                  ) {
                    Text(
                      text = tabTitle,
                      style = MaterialTheme.typography.labelLarge,
                      fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.SemiBold,
                      color = if (isTabSelected) {
                        Color.White
                      } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                      },
                      maxLines = 1,
                      softWrap = false,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Category Test Selector Chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            categoryTests.forEach { testDef ->
              val isSelected = selectedTypeKey == testDef.key
              FilterChip(
                selected = isSelected,
                onClick = {
                  selectedTypeKey = testDef.key
                },
                label = {
                  Text(
                    text = testDef.shortTitle(language),
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                },
                modifier = Modifier.testTag("assessment_type_${testDef.key}"),
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = DeepForestEmerald,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Active Test Title & Description Banner
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = activeTest.title(language),
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.weight(1f)
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = activeTest.subtitle(language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
              )
            }
          }
        }
      }
    }

    // 2. Either Step-by-Step Question Wizard OR Detailed Condition Explanation Report
    val completedReport = currentAnalysis
    if (completedReport == null) {
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 24.dp,
          containerColor = glassCardColor
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            // Progress Header: Question X of 7 + Dimension Pill
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (language == AppLanguage.BN) {
                  "প্রশ্ন ${safeQuestionIndex + 1} / $totalQuestions"
                } else {
                  "Question ${safeQuestionIndex + 1} of $totalQuestions"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                softWrap = false
              )

              Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = activeQuestion.dimension(language),
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  softWrap = false,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
              progress = { (safeQuestionIndex + 1).toFloat() / totalQuestions.toFloat() },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
              color = DeepForestEmerald,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Real-life Psychologist Question Text
            Text(
              text = activeQuestion.question(language),
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 26.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Realistic Situational Answer Options for this specific question
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              activeQuestion.options.forEachIndexed { optionIdx, choice ->
                val isChosen = currentSelectedScore == choice.score
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (isChosen) {
                    MaterialTheme.colorScheme.primaryContainer
                  } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                  },
                  border = BorderStroke(
                    width = if (isChosen) 1.8.dp else 1.dp,
                    color = if (isChosen) {
                      MaterialTheme.colorScheme.primary
                    } else {
                      MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                    }
                  ),
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                      selectedAnswers[safeQuestionIndex] = choice.score
                    }
                    .testTag("question_${safeQuestionIndex}_option_$optionIdx")
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = if (isChosen) {
                        Icons.Default.CheckCircle
                      } else {
                        Icons.Outlined.RadioButtonUnchecked
                      },
                      contentDescription = null,
                      tint = if (isChosen) {
                        MaterialTheme.colorScheme.primary
                      } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                      },
                      modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                      text = choice.label(language),
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                        lineHeight = 22.sp
                      ),
                      color = if (isChosen) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                      } else {
                        MaterialTheme.colorScheme.onSurface
                      }
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Step Navigation Controls: Previous & Next / Finish
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (safeQuestionIndex > 0) {
                OutlinedButton(
                  onClick = {
                    currentQuestionIndex = (safeQuestionIndex - 1).coerceAtLeast(0)
                  },
                  modifier = Modifier
                    .weight(0.42f)
                    .height(50.dp)
                    .testTag("prev_question_button"),
                  shape = RoundedCornerShape(14.dp)
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (language == AppLanguage.BN) "পূর্ববর্তী" else "Previous",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    softWrap = false
                  )
                }
              }

              val isLastQuestion = safeQuestionIndex == totalQuestions - 1
              val canProceed = currentSelectedScore >= 0

              Button(
                onClick = {
                  val defaultScore = activeQuestion.options.firstOrNull()?.score ?: 0
                  val effectiveScore = if (currentSelectedScore >= 0) currentSelectedScore else defaultScore
                  selectedAnswers[safeQuestionIndex] = effectiveScore

                  if (!isLastQuestion) {
                    currentQuestionIndex = (safeQuestionIndex + 1).coerceAtMost(totalQuestions - 1)
                  } else {
                    val finalizedAnswers = activeTest.questions.mapIndexed { idx, q ->
                      val ans = selectedAnswers.getOrElse(idx) { -1 }
                      if (ans < 0) (q.options.firstOrNull()?.score ?: 0) else ans
                    }
                    val evaluation = PsychologicalTestsCatalog.evaluateCondition(
                      test = activeTest,
                      answers = finalizedAnswers,
                      language = language
                    )
                    currentAnalysis = evaluation

                    val qaPairs = activeTest.questions.mapIndexed { idx, q ->
                      val chosenScore = finalizedAnswers.getOrElse(idx) { q.options.firstOrNull()?.score ?: 0 }
                      val chosenLabel = q.options.find { it.score == chosenScore }?.label(language)
                        ?: q.options.first().label(language)
                      q.question(language) to chosenLabel
                    }

                    scope.launch {
                      isGeneratingAiInsight = true
                      val aiText = GeminiPsychiatristService.generatePsychologicalTestAnalysis(
                        testTitle = activeTest.title(language),
                        conditionName = evaluation.conditionName,
                        normalizedScore = evaluation.normalizedScore,
                        questionAnswerPairs = qaPairs,
                        language = language
                      )
                      isGeneratingAiInsight = false
                      val updatedEvaluation = if (aiText.isNotBlank()) {
                        evaluation.copy(aiPersonalAnalysis = aiText)
                      } else {
                        evaluation
                      }
                      currentAnalysis = updatedEvaluation

                      val doctorAnalysisText = updatedEvaluation.aiPersonalAnalysis?.takeIf { it.isNotBlank() }
                        ?: updatedEvaluation.deepExplanation

                      val fullReportText = buildString {
                        append(updatedEvaluation.deepExplanation)
                        append("\n\n[DR_ANALYSIS]\n")
                        append(doctorAnalysisText)
                      }

                      onSubmitAssessment(
                        activeTest.key,
                        updatedEvaluation.normalizedScore,
                        updatedEvaluation.conditionName,
                        fullReportText
                      )
                    }
                  }
                },
                enabled = canProceed || isLastQuestion,
                modifier = Modifier
                  .weight(if (safeQuestionIndex > 0) 0.58f else 1f)
                  .height(50.dp)
                  .testTag(if (isLastQuestion) "submit_assessment_button" else "next_question_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = DeepForestEmerald,
                  contentColor = Color.White
                )
              ) {
                if (isLastQuestion) {
                  Icon(
                    imageVector = Icons.Default.AssignmentTurnedIn,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = strings.submitAssessmentButton,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                  )
                } else {
                  Text(
                    text = if (language == AppLanguage.BN) "পরবর্তী প্রশ্ন" else "Next Question",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }
    } else {
      // 3. Detailed Psychological Condition Result & AI Psychologist Explanation Card
      item {
        GradientGlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("assessment_condition_result_card"),
          cornerRadius = 26.dp,
          isHighlighted = true,
          containerColor = glassCardColor
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer
              ) {
                val badgeLabel = if (activeTest.category == TestCategory.CLINICAL_SCALES) {
                  if (language == AppLanguage.BN) {
                    "ক্লিনিক্যাল স্কেল ফলাফল • স্কোর: ${completedReport.rawScore}/${completedReport.maxRawScore} (${completedReport.normalizedScore}%)"
                  } else {
                    "Clinical Scale Result • Score: ${completedReport.rawScore}/${completedReport.maxRawScore} (${completedReport.normalizedScore}%)"
                  }
                } else {
                  if (language == AppLanguage.BN) {
                    "মনস্তাত্ত্বিক ফলাফল • স্কোর: ${completedReport.normalizedScore}/১০০"
                  } else {
                    "Psychological Result • Score: ${completedReport.normalizedScore}/100"
                  }
                }
                Text(
                  text = badgeLabel,
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  maxLines = 1,
                  softWrap = false,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = completedReport.conditionName,
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 27.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = completedReport.conditionSummary,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            Spacer(modifier = Modifier.height(14.dp))

            // Condition Explanation Section
            Text(
              text = if (language == AppLanguage.BN) {
                "আপনার কন্ডিশনের বিস্তারিত ব্যাখ্যা:"
              } else {
                "Detailed Explanation of Your Condition:"
              },
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = completedReport.deepExplanation,
              style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 24.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Key Psychological Patterns Identified
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = if (language == AppLanguage.BN) {
                    "আপনার উত্তরে ফুটে ওঠা মনস্তাত্ত্বিক দিকসমূহ:"
                  } else {
                    "Key Psychological Patterns From Your Answers:"
                  },
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                completedReport.keyPatterns.forEach { pattern ->
                  Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .padding(0.dp),
                    ) {
                      Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxSize()
                      ) {}
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = pattern,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live AI Psychologist (Dr. Sifat Khan) Personalized Analysis Card
            Surface(
              shape = RoundedCornerShape(18.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  DoctorSifatAvatar(modifier = Modifier.size(36.dp))
                  Spacer(modifier = Modifier.width(10.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = if (language == AppLanguage.BN) {
                        "ড. সিফাত খানের মনস্তাত্ত্বিক বিশ্লেষণ"
                      } else {
                        "Dr. Sifat Khan's Psychological Analysis"
                      },
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onPrimaryContainer,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                    Text(
                      text = if (language == AppLanguage.BN) {
                        "আপনার দেওয়া প্রতিটি উত্তরের ব্যক্তিগত বিশ্লেষণ"
                      } else {
                        "Personalized analysis based on your exact answers"
                      },
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.primary,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isGeneratingAiInsight) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                  ) {
                    CircularProgressIndicator(
                      modifier = Modifier.size(18.dp),
                      strokeWidth = 2.dp,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = if (language == AppLanguage.BN) {
                        "ড. সিফাত খান আপনার উত্তরগুলো বিশ্লেষণ করছেন..."
                      } else {
                        "Dr. Sifat Khan is analyzing your specific answers..."
                      },
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                  }
                } else {
                  Text(
                    text = completedReport.aiPersonalAnalysis?.takeIf { it.isNotBlank() }
                      ?: completedReport.deepExplanation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Practical Actionable Steps
            Text(
              text = if (language == AppLanguage.BN) {
                "বাস্তবমুখী করণীয় পদক্ষেপ:"
              } else {
                "Recommended Practical Steps:"
              },
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            completedReport.actionableSteps.forEachIndexed { idx, stepText ->
              Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Text(
                  text = "${idx + 1}.",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.width(22.dp)
                )
                Text(
                  text = stepText,
                  style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
              onClick = {
                currentAnalysis = null
                currentQuestionIndex = 0
                for (i in selectedAnswers.indices) {
                  selectedAnswers[i] = -1
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("retake_assessment_button"),
              shape = RoundedCornerShape(14.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (language == AppLanguage.BN) {
                  "পুনরায় পরীক্ষা দিন বা অন্য পরীক্ষা বেছে নিন"
                } else {
                  "Retake Test or Choose Another Test"
                },
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }

    // 4. Past Saved Psychological Reports History
    item {
      Text(
        text = strings.savedAssessmentsTitle(assessments.size),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFF0FDFA)
      )
    }

    if (assessments.isEmpty()) {
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 18.dp,
          containerColor = glassCardColor
        ) {
          Text(
            text = strings.emptyAssessmentsText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(18.dp)
          )
        }
      }
    } else {
      items(assessments, key = { it.id.ifEmpty { it.hashCode().toString() } }) { item ->
        val dateStr = remember(item.createdAt) {
          val ts = item.createdAt ?: Timestamp.now()
          SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(ts.toDate())
        }
        val matchedTest = PsychologicalTestsCatalog.allTests.find { it.key == item.assessmentType }
        val localizedTypeLabel = matchedTest?.title(language)
          ?: strings.assessmentTypes.find { it.first == item.assessmentType }?.second
          ?: item.assessmentType

        val rawRec = item.recommendation
        val (conditionExplanationPart, doctorAnalysisPart) = remember(rawRec) {
          if (rawRec.contains("[DR_ANALYSIS]")) {
            val parts = rawRec.split("[DR_ANALYSIS]", limit = 2)
            parts[0].trim() to parts[1].trim()
          } else if (rawRec.contains("ড. সিফাত খানের বিশ্লেষণ:")) {
            val parts = rawRec.split("ড. সিফাত খানের বিশ্লেষণ:", limit = 2)
            parts[0].trim() to parts[1].trim()
          } else if (rawRec.contains("Dr. Sifat Khan's Analysis:")) {
            val parts = rawRec.split("Dr. Sifat Khan's Analysis:", limit = 2)
            parts[0].trim() to parts[1].trim()
          } else {
            "" to rawRec.trim()
          }
        }

        GradientGlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_assessment_card_${item.id}"),
          cornerRadius = 20.dp,
          containerColor = glassCardColor
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = localizedTypeLabel,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              // Direct Delete Icon Button on each Saved Test Report Card (matching Mood Journal)
              if (item.id.isNotBlank()) {
                IconButton(
                  onClick = { onDeleteAssessment(item.id) },
                  modifier = Modifier
                    .size(36.dp)
                    .testTag("delete_assessment_${item.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = if (language == AppLanguage.BN) "রিপোর্ট মুছে ফেলুন" else "Delete Report",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text(
                  text = "${item.severityLabel} (${item.totalScore}/100)",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = dateStr,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                softWrap = false
              )
            }

            if (conditionExplanationPart.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = conditionExplanationPart,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Highlighted Dr. Sifat Khan's Psychological Analysis Card inside every Saved Test Report
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  DoctorSifatAvatar(modifier = Modifier.size(30.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "ড. সিফাত খানের মনস্তাত্ত্বিক বিশ্লেষণ"
                    } else {
                      "Dr. Sifat Khan's Psychological Analysis"
                    },
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                  )
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = doctorAnalysisPart.ifBlank { item.recommendation },
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.2.sp,
                    lineHeight = 20.sp
                  ),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
          }
        }
      }
    }
  }
}
