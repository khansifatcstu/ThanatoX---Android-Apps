package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PsychAssessment
import com.example.data.model.PsychologicalTestDefinition
import com.example.data.model.PsychologicalTestsCatalog
import com.example.data.model.PsychologyArticle
import com.example.data.model.PsychologyArticlesCatalog
import com.example.ui.components.StylishApronDoctorAvatar
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.QuickMoodOption
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
  language: AppLanguage,
  savedAssessments: List<PsychAssessment>,
  onOpenLiveChatbot: () -> Unit,
  onSelectQuickMood: (QuickMoodOption) -> Unit,
  onOpenJournalTab: () -> Unit,
  onOpenScreeningTab: (String?) -> Unit
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current
  var selectedMoodIndex by rememberSaveable { mutableIntStateOf(-1) }
  var selectedArticleId by rememberSaveable { mutableStateOf<String?>(null) }
  var showAllArticlesScreen by rememberSaveable { mutableStateOf(false) }
  var showAllTestsScreen by rememberSaveable { mutableStateOf(false) }

  val allTests = PsychologicalTestsCatalog.allTests
  val articles = PsychologyArticlesCatalog.articles

  val activeArticle = selectedArticleId?.let { id ->
    articles.find { it.id == id }
  }

  // 1. Dedicated Full Article Detail Screen
  if (activeArticle != null) {
    ArticleDetailScreen(
      article = activeArticle,
      language = language,
      onBackClick = { selectedArticleId = null }
    )
    return
  }

  // 2. "View All Articles" ("সবগুলো দেখুন") Full List Screen
  if (showAllArticlesScreen) {
    AllArticlesListScreen(
      articles = articles,
      language = language,
      onSelectArticle = { article -> selectedArticleId = article.id },
      onBackClick = { showAllArticlesScreen = false }
    )
    return
  }

  // 3. "View All Tests" ("সবগুলো দেখুন") Full List Screen
  if (showAllTestsScreen) {
    AllTestsListScreen(
      tests = allTests,
      language = language,
      onSelectTest = { testKey ->
        showAllTestsScreen = false
        onOpenScreeningTab(testKey)
      },
      onBackClick = { showAllTestsScreen = false }
    )
    return
  }

  val previewArticles = articles.take(2)
  val previewTests = allTests.take(2)
  val viewAllLabel = if (language == AppLanguage.BN) "সবগুলো দেখুন" else "View All"

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val isCompactHeight = maxHeight < 630.dp
    val cardSpacing = if (isCompactHeight) 6.dp else 9.dp
    val horizontalPad = 12.dp
    val verticalPad = if (isCompactHeight) 4.dp else 7.dp
    val introTitleSize = if (isCompactHeight) 15.5.sp else 17.sp
    val introTitleLineHeight = if (isCompactHeight) 20.sp else 22.sp
    val introFontSize = if (isCompactHeight) 12.5.sp else 13.8.sp
    val introLineHeight = if (isCompactHeight) 17.2.sp else 19.5.sp

    // 4-Box Full-Screen Fitted Home Layout (fills 100% of screen height without scrolling or empty bottom space)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = horizontalPad, vertical = verticalPad),
      verticalArrangement = Arrangement.spacedBy(cardSpacing)
    ) {
      // BOX 1: Enlarged ThanatoX Introduction Box (~36% height, large legible typography, full text without truncation)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1.44f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
          width = 1.3.dp,
          color = if (isDark) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
          } else {
            Color(0xFF0D9488).copy(alpha = 0.75f)
          }
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
            alpha = if (isDark) 0.82f else 0.78f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 8.dp else 11.dp),
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = strings.heroTitle,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = introTitleSize,
              lineHeight = introTitleLineHeight
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(4.dp))

          // Full untruncated description text in larger, comfortable reading size filling the box
          Text(
            text = strings.heroDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = introFontSize,
              lineHeight = introLineHeight
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.96f)
          )
        }
      }

      // BOX 2: Fitted Mood Selector Card ("আজ আপনার মন কেমন?")
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.84f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
          width = 1.1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.75f else 0.65f)
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDark) 0.80f else 0.74f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 6.dp else 9.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = strings.homeMoodQuestion,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (isCompactHeight) 14.8.sp else 15.8.sp
              ),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
            )

            Text(
              text = strings.viewJournalAction,
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (isCompactHeight) 12.5.sp else 13.2.sp
              ),
              color = MaterialTheme.colorScheme.primary,
              maxLines = 1,
              softWrap = false,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onOpenJournalTab() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .testTag("home_view_journal_button")
            )
          }

          BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val count = strings.quickMoodOptions.size.coerceAtLeast(1)
            val totalSpacing = 6.dp * (count - 1)
            val calculatedTileWidth = ((maxWidth - totalSpacing) / count).coerceAtLeast(58.dp)

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              strings.quickMoodOptions.forEachIndexed { idx, moodOption ->
                val isSelected = selectedMoodIndex == idx
                Surface(
                  shape = RoundedCornerShape(13.dp),
                  color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.90f)
                  } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.78f else 0.68f)
                  },
                  border = BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) {
                      MaterialTheme.colorScheme.primary
                    } else {
                      MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                    }
                  ),
                  modifier = Modifier
                    .widthIn(min = calculatedTileWidth)
                    .clip(RoundedCornerShape(13.dp))
                    .clickable {
                      selectedMoodIndex = idx
                      onSelectQuickMood(moodOption)
                    }
                    .testTag("home_mood_tile_$idx")
                ) {
                  Column(
                    modifier = Modifier.padding(
                      vertical = if (isCompactHeight) 5.dp else 7.dp,
                      horizontal = 4.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Text(
                      text = moodOption.emoji,
                      fontSize = if (isCompactHeight) 17.sp else 19.sp,
                      textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = moodOption.label,
                      style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isCompactHeight) 11.2.sp else 12.sp
                      ),
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = TextAlign.Center,
                      maxLines = 1,
                      softWrap = false
                    )
                  }
                }
              }
            }
          }
        }
      }

      // BOX 3: Fitted Articles Box (2 Sample Articles + "সবগুলো দেখুন" beside Heading)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.88f)
          .testTag("home_all_articles_single_box"),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
          width = 1.1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.75f else 0.65f)
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDark) 0.80f else 0.74f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 6.dp else 9.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Box Header: "আর্টিকেল" on left + "সবগুলো দেখুন" on right
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
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(7.dp))
              Text(
                text = if (language == AppLanguage.BN) "আর্টিকেল" else "Articles",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = if (isCompactHeight) 14.8.sp else 15.8.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Surface(
              shape = RoundedCornerShape(50),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.88f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
              modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { showAllArticlesScreen = true }
                .testTag("home_view_all_articles_button")
            ) {
              Text(
                text = viewAllLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = if (isCompactHeight) 11.5.sp else 12.2.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          // 2 Preview Articles inside the fitted box
          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            previewArticles.forEachIndexed { index, article ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .clickable { selectedArticleId = article.id }
                  .padding(vertical = if (isCompactHeight) 3.dp else 5.dp, horizontal = 4.dp)
                  .testTag("home_article_card_${article.id}"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(
                  modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                ) {
                  Text(
                    text = article.heading(language),
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = if (isCompactHeight) 13.sp else 14.sp,
                      lineHeight = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = "${article.writerName(language)} • ${article.topicTitle(language)}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      fontSize = if (isCompactHeight) 11.sp else 11.8.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }

                Icon(
                  imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(19.dp)
                )
              }

              if (index < previewArticles.lastIndex) {
                HorizontalDivider(
                  color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
              }
            }
          }
        }
      }

      // BOX 4: Fitted Clinical Scales & Psychological Tests Box (2 Sample Tests + "সবগুলো দেখুন" beside Heading)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.84f)
          .testTag("home_all_tests_single_box"),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
          width = 1.1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.75f else 0.65f)
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDark) 0.80f else 0.74f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 6.dp else 9.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Box Header: "ক্লিনিক্যাল স্কেল ও মনস্তাত্ত্বিক পরীক্ষা" on left + "সবগুলো দেখুন" on right
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.Assignment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(7.dp))
              Text(
                text = strings.homeScreeningSectionTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = if (isCompactHeight) 14.2.sp else 15.2.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Surface(
              shape = RoundedCornerShape(50),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.88f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
              modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { showAllTestsScreen = true }
                .testTag("home_view_all_screenings_button")
            ) {
              Text(
                text = viewAllLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = if (isCompactHeight) 11.5.sp else 12.2.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          // 2 Preview Tests inside the fitted box
          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            previewTests.forEachIndexed { index, testDef ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .clickable { onOpenScreeningTab(testDef.key) }
                  .padding(vertical = if (isCompactHeight) 4.dp else 6.dp, horizontal = 4.dp)
                  .testTag("home_screening_card_${testDef.key}"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = testDef.title(language),
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isCompactHeight) 13.sp else 14.sp,
                    lineHeight = 18.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                )

                Icon(
                  imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(19.dp)
                )
              }

              if (index < previewTests.lastIndex) {
                HorizontalDivider(
                  color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
              }
            }
          }
        }
      }
    }

    // Messenger-Style Draggable Circular Floating Chat Bubble (Chat Head) for Dr. Sifat Khan
    DraggableDoctorChatBubble(
      maxWidthDp = maxWidth,
      maxHeightDp = maxHeight,
      contentDescription = strings.heroTalkToDoctorButton,
      onClick = onOpenLiveChatbot
    )
  }
}

/**
 * Messenger-style circular floating chat bubble (Chat Head) that can be dragged freely anywhere
 * across the Home screen and tapped to open the live consultation chat with Dr. Sifat Khan.
 */
@Composable
private fun DraggableDoctorChatBubble(
  maxWidthDp: androidx.compose.ui.unit.Dp,
  maxHeightDp: androidx.compose.ui.unit.Dp,
  contentDescription: String,
  onClick: () -> Unit
) {
  val density = LocalDensity.current
  val bubbleSizeDp = 60.dp
  val edgeMarginDp = 10.dp

  val maxWidthPx = with(density) { maxWidthDp.toPx() }
  val maxHeightPx = with(density) { maxHeightDp.toPx() }
  val bubbleSizePx = with(density) { bubbleSizeDp.toPx() }
  val marginPx = with(density) { edgeMarginDp.toPx() }

  val initialX = (maxWidthPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
  val initialY = (maxHeightPx * 0.62f).coerceIn(
    marginPx,
    (maxHeightPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
  )

  var offsetX by remember { mutableFloatStateOf(initialX) }
  var offsetY by remember { mutableFloatStateOf(initialY) }

  // Keep bubble inside screen bounds if container dimensions change
  val clampedX = offsetX.coerceIn(
    marginPx,
    (maxWidthPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
  )
  val clampedY = offsetY.coerceIn(
    marginPx,
    (maxHeightPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
  )

  Box(
    modifier = Modifier
      .offset { IntOffset(clampedX.roundToInt(), clampedY.roundToInt()) }
      .size(bubbleSizeDp)
      .pointerInput(maxWidthPx, maxHeightPx) {
        detectDragGestures { change, dragAmount ->
          change.consume()
          offsetX = (offsetX + dragAmount.x).coerceIn(
            marginPx,
            (maxWidthPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
          )
          offsetY = (offsetY + dragAmount.y).coerceIn(
            marginPx,
            (maxHeightPx - bubbleSizePx - marginPx).coerceAtLeast(marginPx)
          )
        }
      }
      .testTag("hero_open_chatbot_button"),
    contentAlignment = Alignment.Center
  ) {
    // Main Circular Avatar Bubble with Glowing Emerald Border
    Surface(
      shape = CircleShape,
      color = DeepForestEmerald,
      border = BorderStroke(
        width = 2.2.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            Color(0xFF5EEAD4),
            Color(0xFF14B8A6),
            Color(0xFF0D9488)
          )
        )
      ),
      modifier = Modifier
        .size(56.dp)
        .shadow(elevation = 10.dp, shape = CircleShape)
        .clip(CircleShape)
        .clickable { onClick() }
        .testTag("floating_doctor_chat_bubble")
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        StylishApronDoctorAvatar(
          modifier = Modifier.size(50.dp),
          isCircular = true
        )
      }
    }

    // Top-Right Online Green Dot Badge
    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(top = 3.dp, end = 3.dp)
        .size(14.dp)
        .clip(CircleShape)
        .background(Color.White)
        .padding(2.dp)
        .clip(CircleShape)
        .background(Color(0xFF22C55E))
    )

    // Bottom-Right Small Chat Icon Badge
    Surface(
      shape = CircleShape,
      color = DeepForestEmerald,
      border = BorderStroke(1.2.dp, Color(0xFF5EEAD4)),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .size(22.dp)
        .shadow(elevation = 4.dp, shape = CircleShape)
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Outlined.ChatBubbleOutline,
          contentDescription = contentDescription,
          tint = Color(0xFF99F6E4),
          modifier = Modifier.size(12.dp)
        )
      }
    }
  }
}

/**
 * Full-Screen List of All Psychology Articles opened when clicking "সবগুলো দেখুন" on the Articles box.
 * Clicking any article opens its [ArticleDetailScreen].
 */
@Composable
private fun AllArticlesListScreen(
  articles: List<PsychologyArticle>,
  language: AppLanguage,
  onSelectArticle: (PsychologyArticle) -> Unit,
  onBackClick: () -> Unit
) {
  val isDark = LocalIsDarkTheme.current

  BackHandler {
    onBackClick()
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("all_articles_list_screen"),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
          modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onBackClick() }
            .testTag("all_articles_back_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (language == AppLanguage.BN) "ফিরে যান" else "Back",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Text(
          text = if (language == AppLanguage.BN) "সকল মনস্তাত্ত্বিক আর্টিকেল" else "All Psychology Articles",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = Color(0xFFF0FDFA)
        )
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
          width = 1.1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.75f else 0.65f)
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDark) 0.80f else 0.74f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          articles.forEachIndexed { index, article ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectArticle(article) }
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .testTag("all_articles_item_${article.id}"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(
                modifier = Modifier
                  .weight(1f)
                  .padding(end = 10.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(50),
                  color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
                ) {
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "বিষয়: ${article.topicTitle(language)}"
                    } else {
                      "Topic: ${article.topicTitle(language)}"
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = article.heading(language),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(15.dp)
                  )
                  Spacer(modifier = Modifier.width(5.dp))
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "লেখক: ${article.writerName(language)}"
                    } else {
                      "Writer: ${article.writerName(language)}"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }

              Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
              )
            }

            if (index < articles.lastIndex) {
              HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.30f)
              )
            }
          }
        }
      }
    }
  }
}

/**
 * Full-Screen List of All 9 Clinical & Psychological Tests opened when clicking "সবগুলো দেখুন" on the Tests box.
 * Clicking any test name navigates directly to [AssessmentScreen] for that test.
 */
@Composable
private fun AllTestsListScreen(
  tests: List<PsychologicalTestDefinition>,
  language: AppLanguage,
  onSelectTest: (String) -> Unit,
  onBackClick: () -> Unit
) {
  val isDark = LocalIsDarkTheme.current

  BackHandler {
    onBackClick()
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("all_tests_list_screen"),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
          modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onBackClick() }
            .testTag("all_tests_back_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (language == AppLanguage.BN) "ফিরে যান" else "Back",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Text(
          text = if (language == AppLanguage.BN) {
            "সকল ক্লিনিক্যাল ও মনস্তাত্ত্বিক পরীক্ষা"
          } else {
            "All Clinical & Psychological Tests"
          },
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.5.sp
          ),
          color = Color(0xFFF0FDFA)
        )
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
          width = 1.1.dp,
          color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.75f else 0.65f)
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDark) 0.80f else 0.74f
          )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          tests.forEachIndexed { index, testDef ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectTest(testDef.key) }
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .testTag("all_tests_item_${testDef.key}"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(17.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = testDef.title(language),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.5.sp,
                    lineHeight = 21.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.width(8.dp))

              Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
              )
            }

            if (index < tests.lastIndex) {
              HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.30f)
              )
            }
          }
        }
      }
    }
  }
}
