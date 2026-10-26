package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PsychologyArticle
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.theme.DeepForestEmerald

@Composable
fun ArticleDetailScreen(
  article: PsychologyArticle,
  language: AppLanguage,
  onBackClick: () -> Unit
) {
  BackHandler {
    onBackClick()
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("article_detail_screen"),
    contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Top Back Navigation Bar Card
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
          modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onBackClick() }
            .testTag("article_detail_back_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = if (language == AppLanguage.BN) "ফিরে যান" else "Back",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.BN) "হোম পেজে ফিরে যান" else "Back to Home",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // 2. Article Header Card (Topic, Heading & Writer Name)
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 26.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          // Topic Pill
          Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = article.topicTitle(language),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Article Heading
          Text(
            text = article.heading(language),
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp,
              lineHeight = 30.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Writer Info Row
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (language == AppLanguage.BN) {
                "লেখক: ${article.writerName(language)}"
              } else {
                "Written by: ${article.writerName(language)}"
              },
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
              ),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
          Spacer(modifier = Modifier.height(14.dp))

          // Easy-to-understand Introduction
          Text(
            text = article.intro(language),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontSize = 16.sp,
              lineHeight = 25.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // 3. Detailed Article Sections
    items(article.sections) { section ->
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = section.subheading(language),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              lineHeight = 25.sp
            ),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = section.body(language),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontSize = 15.5.sp,
              lineHeight = 24.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // 4. Key Takeaways / What You Can Apply Today
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = if (language == AppLanguage.BN) {
              "এই আর্টিকেল থেকে শিক্ষণীয় ও করণীয় বিষয়সমূহ:"
            } else {
              "Key Takeaways & Practical Lessons:"
            },
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(10.dp))

          article.keyTakeaways(language).forEach { takeaway ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                  .padding(top = 2.dp)
                  .size(18.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = takeaway,
                style = MaterialTheme.typography.bodyLarge.copy(
                  fontSize = 15.sp,
                  lineHeight = 22.sp,
                  fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = onBackClick,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("article_detail_bottom_back_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = DeepForestEmerald,
              contentColor = Color.White
            )
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.BN) {
                "পড়া শেষ • হোম পেজে ফিরে যান"
              } else {
                "Done Reading • Back to Home"
              },
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
