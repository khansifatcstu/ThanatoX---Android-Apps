package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.remote.ApiConnectionStatus
import com.example.data.remote.GeminiPsychiatristService
import com.example.ui.components.GoogleGLogoIcon
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
  language: AppLanguage,
  apiStatus: ApiConnectionStatus,
  isDarkMode: Boolean = LocalIsDarkTheme.current,
  onToggleDarkMode: () -> Unit,
  onToggleLanguage: () -> Unit,
  currentUserDisplayName: String? = null,
  currentUserEmail: String? = null,
  onGoogleSignInClick: (() -> Unit)? = null,
  onSignOutClick: (() -> Unit)? = null,
  onResetAllData: (suspend () -> Boolean)? = null
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val strings = stringsFor(language)

  var showApiConfigDialog by remember { mutableStateOf(false) }
  var showPrivacyDialog by remember { mutableStateOf(false) }
  var showResetConfirmDialog by remember { mutableStateOf(false) }
  var isResettingData by remember { mutableStateOf(false) }
  var resetConfirmationNotice by remember { mutableStateOf<String?>(null) }

  var keyInput by remember { mutableStateOf("") }
  var isTesting by remember { mutableStateOf(false) }
  var feedbackMessage by remember { mutableStateOf<String?>(null) }
  var isErrorFeedback by remember { mutableStateOf(false) }

  val connectedProvider = apiStatus.activeCustomProvider

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("settings_screen_root"),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Screen Title Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = null,
          tint = Color(0xFF5EEAD4),
          modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = if (language == AppLanguage.BN) "সেটিংস" else "Settings",
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
          ),
          color = Color(0xFFF0FDFA)
        )
      }
    }

    // 2. Unified Professional Row-Based Settings Card (Matching Screenshot Format in ThanatoX Theme)
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 26.dp,
        containerColor = MaterialTheme.colorScheme.surface.copy(
          alpha = if (isDarkMode) 0.82f else 0.78f
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ) {
          // ROW 1: Dark Mode / Light Mode Toggle Row
          SettingsOptionRow(
            icon = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
            title = if (language == AppLanguage.BN) "থিম মোড (ডার্ক / লাইট)" else "Theme Mode (Dark / Light)",
            subtitle = if (isDarkMode) {
              if (language == AppLanguage.BN) "ডার্ক মোড সক্রিয়" else "Dark Mode Active"
            } else {
              if (language == AppLanguage.BN) "লাইট মোড সক্রিয়" else "Light Mode Active"
            },
            badgeText = if (isDarkMode) {
              if (language == AppLanguage.BN) "ডার্ক" else "Dark"
            } else {
              if (language == AppLanguage.BN) "লাইট" else "Light"
            },
            testTag = "settings_dark_mode_toggle",
            onClick = onToggleDarkMode
          )

          HorizontalDivider(
            modifier = Modifier.padding(horizontal = 18.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
          )

          // ROW 2: App Language Toggle Row
          SettingsOptionRow(
            icon = Icons.Default.Language,
            title = if (language == AppLanguage.BN) "অ্যাপের ভাষা (Language)" else "App Language (ভাষা)",
            subtitle = if (language == AppLanguage.BN) "বাংলা ⇄ ইংরেজি পরিবর্তন করুন" else "Switch between English & Bengali",
            badgeText = if (language == AppLanguage.BN) "বাংলা" else "English",
            testTag = "settings_language_toggle",
            onClick = onToggleLanguage
          )

          HorizontalDivider(
            modifier = Modifier.padding(horizontal = 18.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
          )

          // ROW 3: AI Engine & Custom API Key Row (Meta, Grok, Claude, GPT, Gemini)
          SettingsOptionRow(
            icon = Icons.Default.Key,
            title = if (language == AppLanguage.BN) "এপিআই ও এআই ইঞ্জিন" else "API & AI Engine",
            subtitle = if (connectedProvider != null) {
              if (language == AppLanguage.BN) {
                "সংযুক্ত: ${connectedProvider.displayNameBn}"
              } else {
                "Connected: ${connectedProvider.displayNameEn}"
              }
            } else {
              if (language == AppLanguage.BN) {
                "ডিফল্ট জেমিনি ও ফায়ারবেস এআই (কাস্টম কি যুক্ত করুন)"
              } else {
                "Default Gemini & Firebase AI (Tap to customize)"
              }
            },
            badgeText = connectedProvider?.shortBadge ?: if (language == AppLanguage.BN) "ডিফল্ট" else "Default",
            testTag = "settings_api_engine_row",
            onClick = { showApiConfigDialog = true }
          )

          HorizontalDivider(
            modifier = Modifier.padding(horizontal = 18.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
          )

          // ROW 4: Privacy & Cloud Security Info Row
          SettingsOptionRow(
            icon = Icons.Default.Lock,
            title = if (language == AppLanguage.BN) "গোপনীয়তা ও ডাটা সুরক্ষা" else "Privacy & Data Security",
            subtitle = if (language == AppLanguage.BN) {
              "আপনার সকল চ্যাট ও রিপোর্ট এনক্রিপ্টেড ও নিরাপদ"
            } else {
              "Your chats, journals, and reports are encrypted"
            },
            badgeText = null,
            testTag = "settings_privacy_row",
            onClick = { showPrivacyDialog = true }
          )

          // ROW 5 (Bottom of Card): Email Display + Sign Out / Google Sign-In Option Together
          if (onSignOutClick != null || onGoogleSignInClick != null) {
            HorizontalDivider(
              modifier = Modifier.padding(horizontal = 18.dp),
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
            )

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
              // Email / Session Info Row right above Sign Out
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = if (!currentUserEmail.isNullOrBlank()) {
                      currentUserEmail
                    } else if (!currentUserDisplayName.isNullOrBlank()) {
                      currentUserDisplayName
                    } else {
                      strings.guestSessionBadge
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )

                  Text(
                    text = if (!currentUserEmail.isNullOrBlank() || !currentUserDisplayName.isNullOrBlank()) {
                      if (language == AppLanguage.BN) "সংযুক্ত গুগল ইমেইল অ্যাকাউন্ট" else "Connected Google Email Account"
                    } else {
                      if (language == AppLanguage.BN) "গেস্ট সেশন চালু রয়েছে" else "Guest Session Active"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Sign-In (for Guest) and Sign-Out Action Buttons attached with Email
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                if (onGoogleSignInClick != null) {
                  OutlinedButton(
                    onClick = onGoogleSignInClick,
                    modifier = Modifier
                      .weight(1f)
                      .height(44.dp)
                      .testTag("dialog_google_sign_in_button"),
                    shape = RoundedCornerShape(12.dp)
                  ) {
                    GoogleGLogoIcon(badgeSize = 18.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = strings.signInButton,
                      color = MaterialTheme.colorScheme.primary,
                      fontWeight = FontWeight.Bold,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }

                if (onSignOutClick != null) {
                  OutlinedButton(
                    onClick = onSignOutClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                      .weight(1f)
                      .height(44.dp)
                      .testTag("dialog_sign_out_button")
                  ) {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.Logout,
                      contentDescription = null,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = strings.signOutDescription,
                      fontWeight = FontWeight.Bold,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // 3. Prominent Red "Reset All Data" Pill Button (Separate from Sign Out, matching screenshot)
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = Color(0xFFC81E1E),
          border = BorderStroke(1.dp, Color(0xFFFCA5A5).copy(alpha = 0.55f)),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(50))
            .clickable(enabled = !isResettingData) {
              showResetConfirmDialog = true
            }
            .testTag("reset_all_data_button")
        ) {
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            if (isResettingData) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.2.dp,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(10.dp))
            } else {
              Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
              text = if (language == AppLanguage.BN) {
                "সকল ডাটা রিসেট করুন (Reset All Data)"
              } else {
                "Reset All Data"
              },
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              ),
              color = Color.White,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = resetConfirmationNotice ?: if (language == AppLanguage.BN) {
            "সাইন আউট না করেই এই অ্যাকাউন্টের সকল চ্যাট, জার্নাল ও রিপোর্ট রিসেট করুন।"
          } else {
            "Reset all chats, journals, and reports under this account without signing out."
          },
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = if (resetConfirmationNotice != null) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.5.sp
          ),
          color = if (resetConfirmationNotice != null) {
            Color(0xFF5EEAD4)
          } else {
            Color(0xFFE2E8F0)
          },
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
      }
    }
  }

  // Confirmation Dialog for "Reset All Data"
  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      icon = {
        Icon(
          imageVector = Icons.Default.DeleteForever,
          contentDescription = null,
          tint = Color(0xFFEF4444),
          modifier = Modifier.size(32.dp)
        )
      },
      title = {
        Text(
          text = if (language == AppLanguage.BN) {
            "সকল ডাটা রিসেট নিশ্চিতকরণ"
          } else {
            "Confirm Reset All Data"
          },
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = if (language == AppLanguage.BN) {
            "আপনি কি নিশ্চিতভাবে আপনার অ্যাকাউন্টের অধীনে থাকা সকল চ্যাট হিস্ট্রি, মুড জার্নাল এবং সাইকোলজিক্যাল পরীক্ষার রিপোর্ট মুছে ফেলে রিসেট করতে চান? (এতে আপনার অ্যাকাউন্ট সাইন আউট হবে না)"
          } else {
            "Are you sure you want to permanently reset all chat history, mood journals, and psychological test reports under this account? (You will remain signed in.)"
          }
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showResetConfirmDialog = false
            scope.launch {
              isResettingData = true
              val ok = onResetAllData?.invoke() ?: true
              isResettingData = false
              resetConfirmationNotice = if (ok) {
                if (language == AppLanguage.BN) {
                  "কনফার্মেশন সম্পন্ন: আপনার অ্যাকাউন্টের সকল ডাটা সফলভাবে রিসেট করা হয়েছে।"
                } else {
                  "Confirmation complete: All account data has been reset."
                }
              } else {
                strings.genericErrorToast
              }
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC81E1E),
            contentColor = Color.White
          ),
          modifier = Modifier.testTag("confirm_reset_all_data_button")
        ) {
          Text(
            text = if (language == AppLanguage.BN) "হ্যাঁ, রিসেট করুন" else "Yes, Reset All",
            fontWeight = FontWeight.Bold
          )
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showResetConfirmDialog = false },
          modifier = Modifier.testTag("cancel_reset_all_data_button")
        ) {
          Text(
            text = if (language == AppLanguage.BN) "বাতিল করুন" else "Cancel",
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    )
  }

  // Privacy & Security Info Dialog
  if (showPrivacyDialog) {
    AlertDialog(
      onDismissRequest = { showPrivacyDialog = false },
      icon = {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary
        )
      },
      title = {
        Text(
          text = if (language == AppLanguage.BN) {
            "গোপনীয়তা ও ডাটা সুরক্ষা"
          } else {
            "Privacy & Data Security"
          },
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = if (language == AppLanguage.BN) {
            "থেনাটক্স-এ আপনার সকল মানসিক স্বাস্থ্য পরামর্শ, ড. সিফাত খানের সাথে চ্যাট, প্রতিদিনের মুড জার্নাল এবং পরীক্ষার রিপোর্ট কঠোর গোপনীয়তা ও ক্লাউড সিকিউরিটি রুলসের অধীনে শুধুমাত্র আপনার ইমেইল অ্যাকাউন্টেই সংরক্ষিত থাকে।"
          } else {
            "All your psychiatric consultations with Dr. Sifat Khan, daily mood journals, and test reports are strictly private and isolated under your personal account."
          }
        )
      },
      confirmButton = {
        TextButton(onClick = { showPrivacyDialog = false }) {
          Text(
            text = if (language == AppLanguage.BN) "ঠিক আছে" else "Got It",
            fontWeight = FontWeight.Bold
          )
        }
      }
    )
  }

  // API & AI Engine Configuration Dialog (opened when clicking the API & AI Engine row)
  if (showApiConfigDialog) {
    val liveDetectedProvider = remember(keyInput) {
      GeminiPsychiatristService.detectProviderFromKey(keyInput)
    }

    Dialog(onDismissRequest = { showApiConfigDialog = false }) {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.apiDialogTitle,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = strings.apiDialogSubtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = if (language == AppLanguage.BN) {
                "সাপোর্টেড (অটো-ডিটেক্ট): Meta • Grok • Claude • GPT • Gemini • DeepSeek"
              } else {
                "Auto-Detects: Meta • Grok • Claude • GPT • Gemini • DeepSeek"
              },
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

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
                  text = if (connectedProvider != null) {
                    if (language == AppLanguage.BN) {
                      "কাস্টম এপিআই সংযুক্ত: ${connectedProvider.displayNameBn}"
                    } else {
                      "Connected Custom API: ${connectedProvider.displayNameEn}"
                    }
                  } else {
                    strings.apiConnectedUsingBuiltIn
                  },
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  maxLines = 1
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = keyInput,
            onValueChange = {
              keyInput = it
              feedbackMessage = null
            },
            label = { Text(strings.apiKeyInputLabel, maxLines = 1) },
            placeholder = { Text(strings.apiKeyInputPlaceholder, maxLines = 1) },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("custom_api_key_input"),
            shape = RoundedCornerShape(14.dp)
          )

          if (liveDetectedProvider != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = DeepForestEmerald.copy(alpha = 0.16f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("settings_detected_api_provider_badge")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (language == AppLanguage.BN) {
                    "শনাক্তকৃত এপিআই: ${liveDetectedProvider.displayNameBn}"
                  } else {
                    "Detected API: ${liveDetectedProvider.displayNameEn}"
                  },
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                  shape = RoundedCornerShape(50),
                  color = MaterialTheme.colorScheme.primary
                ) {
                  Text(
                    text = liveDetectedProvider.shortBadge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }
            }
          }

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

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = {
              scope.launch {
                isTesting = true
                feedbackMessage = null
                val trimmedKey = keyInput.trim()
                if (trimmedKey.isNotBlank()) {
                  GeminiPsychiatristService.saveCustomKey(context, trimmedKey, liveDetectedProvider)
                }
                val result = GeminiPsychiatristService.testApiKeyConnection(trimmedKey, context)
                isTesting = false
                val finalProvider = GeminiPsychiatristService.getActiveCustomProvider() ?: liveDetectedProvider
                result.onSuccess { ms ->
                  isErrorFeedback = false
                  feedbackMessage = if (finalProvider != null) {
                    if (language == AppLanguage.BN) {
                      "সফলভাবে কানেক্ট হয়েছে: ${finalProvider.displayNameBn} • ${ms} মি.সে."
                    } else {
                      "Connected to ${finalProvider.displayNameEn} • ${ms}ms"
                    }
                  } else {
                    strings.apiConnectedSuccess(ms)
                  }
                }.onFailure {
                  isErrorFeedback = true
                  feedbackMessage = strings.apiErrorInvalidKey
                }
              }
            },
            enabled = !isTesting,
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
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

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            TextButton(
              onClick = {
                GeminiPsychiatristService.saveCustomKey(context, "", null)
                keyInput = ""
                isErrorFeedback = false
                feedbackMessage = strings.apiConnectedUsingBuiltIn
              }
            ) {
              Text(strings.apiClearCustomKeyButton, maxLines = 1)
            }

            TextButton(onClick = { showApiConfigDialog = false }) {
              Text(strings.apiCloseButton, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SettingsOptionRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  badgeText: String?,
  testTag: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 18.dp, vertical = 15.dp)
      .testTag(testTag),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier
        .weight(1f)
        .padding(end = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(21.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          ),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.5.sp
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (!badgeText.isNullOrBlank()) {
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.primaryContainer,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.40f))
        ) {
          Text(
            text = badgeText,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(22.dp)
      )
    }
  }
}
