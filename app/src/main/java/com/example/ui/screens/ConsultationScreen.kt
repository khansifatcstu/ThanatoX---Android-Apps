package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.SentimentSatisfiedAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ConsultationMessage
import com.example.data.model.TherapyMode
import com.example.data.model.UiState
import com.example.data.model.buildSessionMoodTag
import com.example.data.model.extractAttachmentLabel
import com.example.data.model.extractSessionId
import com.example.data.remote.ApiConnectionStatus
import com.example.data.remote.ChatAttachmentPayload
import com.example.data.remote.GeminiPsychiatristService
import com.example.ui.components.ChatbotSanctuaryAmbientBackground
import com.example.ui.components.DoctorSifatAvatar
import com.example.ui.components.GradientGlassCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import com.google.firebase.Timestamp
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Light Mode Chat Colors — Translucent Glass over "Who am I?" Light Background with Black/Dark Typography
private val LightChatScreenBg = Color(0xFFD6E4E8)
private val LightSageHeaderBg = Color(0xFFDCEFF2).copy(alpha = 0.86f)
private val LightDoctorBubbleBg = Color(0xFFE6F4F6).copy(alpha = 0.84f)
private val LightUserBubbleBg = Color(0xFFB5E8E2).copy(alpha = 0.88f)
private val LightDoctorTitleGreen = Color(0xFF0B1114)
private val UserBubbleTeal = Color(0xFF0B6B5D)
private val LightComposerPillBg = Color(0xFFE2F2F5).copy(alpha = 0.88f)
private val LightComposerPillBorder = Color(0xFF0D9488).copy(alpha = 0.55f)
private val LightTimestampMutedColor = Color(0xFF223036)
private val LightBlackTextColor = Color(0xFF0B1114)

// Dark Mode Chat Colors — Translucent Midnight Teal Glass over "Who am I?" Dark Background
private val DarkChatScreenBg = Color(0xFF080E12)
private val DarkSageHeaderBg = Color(0xFF0F2628).copy(alpha = 0.88f)
private val DarkDoctorBubbleBg = Color(0xFF122C2F).copy(alpha = 0.82f)
private val DarkDoctorTitleTeal = Color(0xFF42D6B4)
private val DarkComposerPillBg = Color(0xFF102629).copy(alpha = 0.88f)
private val DarkComposerPillBorder = Color(0xFF234E52)
private val DarkTimestampMutedColor = Color(0xFF9AC2BC)

data class ChatSessionSummary(
  val sessionId: String,
  val title: String,
  val lastUpdatedDate: Date,
  val messageCount: Int
)

@Composable
fun ConsultationScreen(
  language: AppLanguage,
  consultationsState: UiState<List<ConsultationMessage>>,
  selectedMode: TherapyMode,
  isAiThinking: Boolean,
  streamingReplyText: String? = null,
  onSelectMode: (TherapyMode) -> Unit,
  onSendMessage: (String, String, ChatAttachmentPayload?) -> Unit,
  onDeleteMessage: (String) -> Unit,
  onDeleteSession: ((String) -> Unit)? = null,
  onBackClick: (() -> Unit)? = null,
  onToggleDarkMode: (() -> Unit)? = null,
  onToggleLanguage: (() -> Unit)? = null,
  currentUserDisplayName: String? = null,
  onGoogleSignInClick: (() -> Unit)? = null,
  onSignOutClick: (() -> Unit)? = null,
  initialSessionId: String? = null,
  onSessionIdChanged: ((String) -> Unit)? = null
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  var inputText by rememberSaveable { mutableStateOf("") }
  var showOverflowMenu by remember { mutableStateOf(false) }
  var showHistoryDialog by rememberSaveable { mutableStateOf(false) }
  var showDedicatedApiDialog by rememberSaveable { mutableStateOf(false) }
  var showAttachmentSheet by rememberSaveable { mutableStateOf(false) }
  var pendingAttachment by remember { mutableStateOf<ChatAttachmentPayload?>(null) }

  val listState = rememberLazyListState()
  val strings = stringsFor(language)
  val apiStatus by GeminiPsychiatristService.connectionStatus.collectAsStateWithLifecycle()
  val isDark = LocalIsDarkTheme.current
  val density = LocalDensity.current
  val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

  val chatBg = if (isDark) DarkChatScreenBg else LightChatScreenBg
  val headerBg = if (isDark) DarkSageHeaderBg else LightSageHeaderBg
  val headerDividerColor = if (isDark) Color(0xFF234E52).copy(alpha = 0.7f) else Color(0xFF0D9488).copy(alpha = 0.4f)
  val headerTitleColor = if (isDark) Color(0xFFF2F6FA) else LightBlackTextColor
  val headerSubtitleColor = if (isDark) Color(0xFFA7CEC9) else Color(0xFF1C282E)
  val composerBg = if (isDark) DarkComposerPillBg else LightComposerPillBg
  val composerBorder = if (isDark) DarkComposerPillBorder else LightComposerPillBorder
  val composerTextColor = if (isDark) Color(0xFFF2F6FA) else LightBlackTextColor
  val composerHintColor = if (isDark) Color(0xFF8DB5AF) else Color(0xFF28383E)

  val allMessages = (consultationsState as? UiState.Success)?.data.orEmpty()

  // Default to a fresh new chat session on initial app launch, and keep the active session across tab switches
  var activeSessionId by rememberSaveable(initialSessionId) {
    mutableStateOf(
      initialSessionId?.takeIf { it.isNotBlank() }
        ?: "s_${System.currentTimeMillis().toString(36)}"
    )
  }

  val updateActiveSession: (String) -> Unit = { newSessionId ->
    activeSessionId = newSessionId
    onSessionIdChanged?.invoke(newSessionId)
  }

  val currentSessionId = activeSessionId.ifBlank { "default" }
  val messages = remember(allMessages, currentSessionId) {
    allMessages.filter { it.extractSessionId() == currentSessionId }
  }

  // Group all messages into chat sessions for the Chat History popup
  val sessionSummaries = remember(allMessages, language) {
    allMessages
      .groupBy { it.extractSessionId() }
      .map { (sid, sessionMsgs) ->
        val firstUserMsg = sessionMsgs.firstOrNull { it.role == "user" } ?: sessionMsgs.first()
        val rawTitle = firstUserMsg.extractAttachmentLabel()?.let { att ->
          "📎 $att — ${firstUserMsg.text}"
        } ?: firstUserMsg.text
        val cleanTitle = rawTitle.replace("\n", " ").trim().take(52).ifBlank {
          strings.doctorHeaderName
        }
        val latestTs = sessionMsgs.maxOfOrNull { it.createdAt?.toDate()?.time ?: 0L } ?: System.currentTimeMillis()
        ChatSessionSummary(
          sessionId = sid,
          title = cleanTitle,
          lastUpdatedDate = Date(latestTs),
          messageCount = sessionMsgs.size
        )
      }
      .sortedByDescending { it.lastUpdatedDate.time }
  }

  // Launchers for Gallery Photo, Camera, and PDF/Document upload
  val galleryPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      scope.launch {
        val loaded = loadAttachmentFromUri(context, uri, forceImage = true)
        if (loaded != null) {
          pendingAttachment = loaded
        }
      }
    }
  }

  val cameraPreviewLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    if (bitmap != null) {
      scope.launch {
        val loaded = encodeBitmapToAttachment(
          bitmap = bitmap,
          fileName = "Camera_${System.currentTimeMillis() % 100000}.jpg"
        )
        if (loaded != null) {
          pendingAttachment = loaded
        }
      }
    }
  }

  val documentPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
  ) { uri: Uri? ->
    if (uri != null) {
      scope.launch {
        val loaded = loadAttachmentFromUri(context, uri, forceImage = false)
        if (loaded != null) {
          pendingAttachment = loaded
        }
      }
    }
  }

  LaunchedEffect(messages.size, isAiThinking, streamingReplyText, isKeyboardOpen, currentSessionId) {
    val totalItems = 1 + messages.size + (if (isAiThinking) 1 else 0)
    if (totalItems > 0) {
      listState.animateScrollToItem(totalItems - 1)
    }
  }

  // 1. Chat History Popup Dialog
  if (showHistoryDialog) {
    ChatHistoryPopupDialog(
      language = language,
      sessions = sessionSummaries,
      activeSessionId = currentSessionId,
      onSelectSession = { chosenSessionId ->
        updateActiveSession(chosenSessionId)
        showHistoryDialog = false
      },
      onDeleteSession = { targetSid ->
        if (onDeleteSession != null) {
          onDeleteSession(targetSid)
        } else {
          allMessages
            .filter { it.extractSessionId() == targetSid }
            .forEach { msg ->
              if (msg.id.isNotBlank()) onDeleteMessage(msg.id)
            }
        }
        if (targetSid == currentSessionId) {
          updateActiveSession("s_${System.currentTimeMillis().toString(36)}")
        }
      },
      onCreateNewChat = {
        updateActiveSession("s_${System.currentTimeMillis().toString(36)}")
        inputText = ""
        pendingAttachment = null
        showHistoryDialog = false
      },
      onDismiss = { showHistoryDialog = false }
    )
  }

  // 2. Dedicated API Key Modal (triggered from 3-dot -> API)
  if (showDedicatedApiDialog) {
    ChatDedicatedApiDialog(
      language = language,
      apiStatus = apiStatus,
      onDismiss = { showDedicatedApiDialog = false }
    )
  }

  // 3. Attachment Upload Popup Sheet (triggered from AttachFile icon next to Sticker icon)
  if (showAttachmentSheet) {
    AttachmentUploadSheetDialog(
      language = language,
      onPickGalleryPhoto = {
        showAttachmentSheet = false
        galleryPickerLauncher.launch(
          PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
      },
      onTakeCameraPhoto = {
        showAttachmentSheet = false
        cameraPreviewLauncher.launch(null)
      },
      onPickDocument = {
        showAttachmentSheet = false
        documentPickerLauncher.launch(
          arrayOf("application/pdf", "text/*", "image/*")
        )
      },
      onDismiss = { showAttachmentSheet = false }
    )
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(chatBg)
  ) {
    // Full-screen Chatbot background (Dark Mode = first image, Light Mode = second image)
    ChatbotSanctuaryAmbientBackground()

    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Pinned Top Header Bar (Always visible even when keyboard opens, like Gemini & ChatGPT)
      ConsultationPinnedTopBar(
        language = language,
        isDark = isDark,
        isAiThinking = isAiThinking,
        apiStatus = apiStatus,
        headerBg = headerBg,
        headerTitleColor = headerTitleColor,
        headerSubtitleColor = headerSubtitleColor,
        headerDividerColor = headerDividerColor,
        showOverflowMenu = showOverflowMenu,
        onShowOverflowMenuChange = { showOverflowMenu = it },
        onBackClick = onBackClick,
        onCreateNewChat = {
          showOverflowMenu = false
          updateActiveSession("s_${System.currentTimeMillis().toString(36)}")
          inputText = ""
          pendingAttachment = null
        },
        onOpenHistory = {
          showOverflowMenu = false
          showHistoryDialog = true
        },
        onOpenApiDialog = {
          showOverflowMenu = false
          showDedicatedApiDialog = true
        }
      )

      // Chat Bubbles Stream (Resizes smoothly between Pinned Top Bar and Bottom Input Bar)
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 14.dp)
      ) {
        when (consultationsState) {
          is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              CircularProgressIndicator(color = UserBubbleTeal)
            }
          }

          is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text(
                text = strings.genericErrorToast,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }

          is UiState.Success -> {
            LazyColumn(
              state = listState,
              modifier = Modifier
                .fillMaxSize()
                .testTag("chatbot_message_list"),
              contentPadding = PaddingValues(vertical = 16.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              item(key = "dr_sifat_welcome_bubble_$currentSessionId") {
                ConsultationBubble(
                  language = language,
                  message = ConsultationMessage(
                    id = "",
                    role = "psychiatrist",
                    therapyMode = selectedMode.firestoreKey,
                    text = strings.welcomeDoctorMessage,
                    moodTag = "",
                    createdAt = Timestamp.now()
                  ),
                  onDelete = null
                )
              }

              items(messages, key = { it.id.ifEmpty { it.hashCode().toString() } }) { message ->
                ConsultationBubble(
                  language = language,
                  message = message,
                  onDelete = if (message.id.isNotBlank()) {
                    { onDeleteMessage(message.id) }
                  } else {
                    null
                  }
                )
              }

              if (isAiThinking) {
                item(key = "live_streaming_bubble") {
                  LiveStreamingBubble(
                    language = language,
                    partialText = streamingReplyText
                  )
                }
              }
            }
          }
        }
      }

      // Pending Attachment Preview Chip (shown above composer bar when a photo/PDF/doc is attached)
      val currentAttachment = pendingAttachment
      if (currentAttachment != null) {
        Surface(
          shape = RoundedCornerShape(18.dp),
          color = if (isDark) Color(0xFF123134).copy(alpha = 0.94f) else Color(0xFFC6F6EC).copy(alpha = 0.94f),
          border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2DD4BF).copy(alpha = 0.6f) else Color(0xFF0D9488).copy(alpha = 0.65f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("pending_attachment_chip")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Surface(
                shape = CircleShape,
                color = UserBubbleTeal.copy(alpha = 0.2f),
                modifier = Modifier.size(34.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = if (currentAttachment.isImage) Icons.Default.Image else Icons.Default.Description,
                    contentDescription = null,
                    tint = if (isDark) Color(0xFF42D6B4) else UserBubbleTeal,
                    modifier = Modifier.size(19.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = currentAttachment.fileName,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = composerTextColor,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = if (currentAttachment.isImage) {
                    if (language == AppLanguage.BN) "ছবি সংযুক্ত • এআই বিশ্লেষণের জন্য প্রস্তুত" else "Image attached • Ready for AI analysis"
                  } else {
                    if (language == AppLanguage.BN) "ডকুমেন্ট সংযুক্ত • এআই বিশ্লেষণের জন্য প্রস্তুত" else "Document attached • Ready for AI analysis"
                  },
                  style = MaterialTheme.typography.labelSmall,
                  color = composerHintColor,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            IconButton(
              onClick = { pendingAttachment = null },
              modifier = Modifier
                .size(30.dp)
                .testTag("remove_pending_attachment_button")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = composerTextColor,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Clean Pill Bottom Composer Bar
      val canSend = (inputText.isNotBlank() || pendingAttachment != null) && !isAiThinking
      Surface(
        shape = RoundedCornerShape(32.dp),
        color = composerBg,
        border = BorderStroke(1.dp, composerBorder),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 24.dp, max = 104.dp)
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
          ) {
            if (inputText.isEmpty()) {
              Text(
                text = strings.chatInputPlaceholder,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = composerHintColor
              )
            }
            BasicTextField(
              value = inputText,
              onValueChange = { inputText = it },
              textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = composerTextColor,
                fontSize = 16.sp
              ),
              cursorBrush = SolidColor(UserBubbleTeal),
              maxLines = 4,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("consultation_input_field")
            )
          }

          IconButton(
            onClick = {
              inputText = if (inputText.isBlank()) "😊 " else "$inputText 😊"
            },
            modifier = Modifier.size(38.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.SentimentSatisfiedAlt,
              contentDescription = null,
              tint = composerHintColor,
              modifier = Modifier.size(23.dp)
            )
          }

          // Attachment Icon next to Sticker Icon -> Opens Multimodal Upload Sheet (Gallery / Camera / PDF & Doc)
          IconButton(
            onClick = { showAttachmentSheet = true },
            modifier = Modifier
              .size(38.dp)
              .testTag("chat_attach_file_button")
          ) {
            Icon(
              imageVector = Icons.Default.AttachFile,
              contentDescription = strings.attachmentSheetTitle,
              tint = if (pendingAttachment != null) {
                if (isDark) Color(0xFF42D6B4) else UserBubbleTeal
              } else {
                composerHintColor
              },
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          // Circular Teal Send Button
          Surface(
            shape = CircleShape,
            color = if (canSend) UserBubbleTeal else UserBubbleTeal.copy(alpha = 0.55f),
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .clickable(enabled = canSend) {
                val attachmentToSend = pendingAttachment
                val textToSend = inputText.trim().ifEmpty {
                  strings.attachmentDefaultPrompt
                }
                val taggedMood = buildSessionMoodTag(
                  sessionId = currentSessionId,
                  attachmentName = attachmentToSend?.fileName,
                  baseTag = selectedMode.title(language)
                )
                inputText = ""
                pendingAttachment = null
                onSendMessage(textToSend, taggedMood, attachmentToSend)
              }
              .testTag("send_consultation_button")
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = strings.doctorHeaderName,
                tint = Color.White,
                modifier = Modifier.size(21.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ConsultationPinnedTopBar(
  language: AppLanguage,
  isDark: Boolean,
  isAiThinking: Boolean,
  apiStatus: ApiConnectionStatus,
  headerBg: Color,
  headerTitleColor: Color,
  headerSubtitleColor: Color,
  headerDividerColor: Color,
  showOverflowMenu: Boolean,
  onShowOverflowMenuChange: (Boolean) -> Unit,
  onBackClick: (() -> Unit)?,
  onCreateNewChat: () -> Unit,
  onOpenHistory: () -> Unit,
  onOpenApiDialog: () -> Unit
) {
  val strings = stringsFor(language)
  Surface(
    color = headerBg,
    shadowElevation = 4.dp,
    modifier = Modifier
      .fillMaxWidth()
      .zIndex(2f)
      .testTag("doctor_pinned_top_bar")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          IconButton(
            onClick = { onBackClick?.invoke() },
            modifier = Modifier.testTag("chat_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              tint = headerTitleColor,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          DoctorSifatAvatar(
            modifier = Modifier.size(44.dp)
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = strings.doctorHeaderName,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
              ),
              color = headerTitleColor,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            val activeProviderLabel = apiStatus.activeCustomProvider?.displayName(language)
            Text(
              text = if (isAiThinking) {
                strings.doctorStatusStreaming
              } else if (!activeProviderLabel.isNullOrBlank()) {
                "${strings.doctorHeaderRole} • $activeProviderLabel"
              } else {
                strings.doctorHeaderRole
              },
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp
              ),
              color = headerSubtitleColor,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Box {
          IconButton(
            onClick = { onShowOverflowMenuChange(true) },
            modifier = Modifier.testTag("chat_more_options_button")
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = strings.chatMenuHistory,
              tint = headerTitleColor,
              modifier = Modifier.size(24.dp)
            )
          }

          DropdownMenu(
            expanded = showOverflowMenu,
            onDismissRequest = { onShowOverflowMenuChange(false) },
            shape = RoundedCornerShape(18.dp),
            containerColor = if (isDark) Color(0xFF0E2628) else Color(0xFFD6F7EE),
            border = BorderStroke(
              1.dp,
              if (isDark) Color(0xFF2DD4BF).copy(alpha = 0.45f) else Color(0xFF0D9488).copy(alpha = 0.55f)
            )
          ) {
            DropdownMenuItem(
              text = {
                Text(
                  text = strings.chatMenuNewChat,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                  ),
                  color = headerTitleColor
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.AddCircleOutline,
                  contentDescription = null,
                  tint = if (isDark) Color(0xFF42D6B4) else Color(0xFF0B6B5D)
                )
              },
              onClick = onCreateNewChat,
              modifier = Modifier.testTag("menu_create_new_chat")
            )

            HorizontalDivider(
              color = headerDividerColor.copy(alpha = 0.35f),
              modifier = Modifier.padding(horizontal = 12.dp)
            )

            DropdownMenuItem(
              text = {
                Text(
                  text = strings.chatMenuHistory,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                  ),
                  color = headerTitleColor
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.History,
                  contentDescription = null,
                  tint = if (isDark) Color(0xFF42D6B4) else Color(0xFF0B6B5D)
                )
              },
              onClick = onOpenHistory,
              modifier = Modifier.testTag("menu_chat_history")
            )

            HorizontalDivider(
              color = headerDividerColor.copy(alpha = 0.35f),
              modifier = Modifier.padding(horizontal = 12.dp)
            )

            DropdownMenuItem(
              text = {
                Text(
                  text = strings.chatMenuApi,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                  ),
                  color = headerTitleColor
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Key,
                  contentDescription = null,
                  tint = if (isDark) Color(0xFFFBBF24) else Color(0xFFD97706)
                )
              },
              onClick = onOpenApiDialog,
              modifier = Modifier.testTag("menu_api_key")
            )
          }
        }
      }
      HorizontalDivider(
        thickness = 1.dp,
        color = headerDividerColor
      )
    }
  }
}

/**
 * Popup Dialog displaying previous chat sessions with title, date, tap-to-open, and delete button.
 */
@Composable
private fun ChatHistoryPopupDialog(
  language: AppLanguage,
  sessions: List<ChatSessionSummary>,
  activeSessionId: String,
  onSelectSession: (String) -> Unit,
  onDeleteSession: (String) -> Unit,
  onCreateNewChat: () -> Unit,
  onDismiss: () -> Unit
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current
  val dateFormatter = remember {
    SimpleDateFormat("dd MMM yyyy • h:mm a", Locale.getDefault())
  }

  Dialog(onDismissRequest = onDismiss) {
    GradientGlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("chat_history_dialog"),
      cornerRadius = 26.dp,
      isHighlighted = true,
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.95f else 0.92f)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.chatHistoryTitle,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = strings.apiCloseButton,
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = strings.chatHistorySubtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (sessions.isEmpty()) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = strings.chatHistoryEmpty,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 320.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(sessions, key = { it.sessionId }) { session ->
              val isCurrent = session.sessionId == activeSessionId
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isCurrent) {
                  MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f)
                } else {
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                },
                border = BorderStroke(
                  width = if (isCurrent) 1.4.dp else 1.dp,
                  color = if (isCurrent) {
                    MaterialTheme.colorScheme.primary
                  } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                  }
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .clickable { onSelectSession(session.sessionId) }
                  .testTag("history_session_item_${session.sessionId}")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = session.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                          fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                      )
                      if (isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                          shape = RoundedCornerShape(50),
                          color = MaterialTheme.colorScheme.primary
                        ) {
                          Text(
                            text = strings.chatHistoryActiveBadge,
                            style = MaterialTheme.typography.labelSmall.copy(
                              fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                          )
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                      text = dateFormatter.format(session.lastUpdatedDate),
                      style = MaterialTheme.typography.labelMedium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      maxLines = 1
                    )
                  }

                  IconButton(
                    onClick = { onDeleteSession(session.sessionId) },
                    modifier = Modifier
                      .size(38.dp)
                      .testTag("delete_session_${session.sessionId}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = strings.chatHistoryDeleteDesc,
                      tint = MaterialTheme.colorScheme.error,
                      modifier = Modifier.size(21.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = onCreateNewChat,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = DeepForestEmerald,
              contentColor = Color.White
            ),
            modifier = Modifier.testTag("history_new_chat_button")
          ) {
            Icon(
              imageVector = Icons.Default.AddCircleOutline,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = strings.chatMenuNewChat,
              fontWeight = FontWeight.Bold
            )
          }

          TextButton(onClick = onDismiss) {
            Text(
              text = strings.apiCloseButton,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

/**
 * Dedicated API Key Modal opened from the 3-dot "API" option in the chat screen.
 * Auto-detects any pasted API key (Meta, Grok, Claude, ChatGPT, Gemini, DeepSeek, OpenRouter)
 * while keeping the default Gemini & Firebase AI engine unchanged when no custom key is set.
 */
@Composable
private fun ChatDedicatedApiDialog(
  language: AppLanguage,
  apiStatus: ApiConnectionStatus,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current

  var keyInput by remember { mutableStateOf("") }
  var isTesting by remember { mutableStateOf(false) }
  var feedbackMessage by remember { mutableStateOf<String?>(null) }
  var isErrorFeedback by remember { mutableStateOf(false) }

  val liveDetectedProvider = remember(keyInput) {
    GeminiPsychiatristService.detectProviderFromKey(keyInput)
  }
  val connectedProvider = apiStatus.activeCustomProvider

  Dialog(onDismissRequest = onDismiss) {
    GradientGlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("chat_dedicated_api_dialog"),
      cornerRadius = 26.dp,
      isHighlighted = true,
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.95f else 0.92f)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Key,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = strings.chatApiModalTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = strings.chatApiModalSubtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Supported Auto-Detect Providers Pill Row
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

        Spacer(modifier = Modifier.height(12.dp))

        // Active Engine Status Banner
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
                    "কাস্টম এপিআই সংযুক্ত: ${connectedProvider.displayNameBn}" +
                      (apiStatus.lastLatencyMs?.let { " • ${it} মি.সে." } ?: "")
                  } else {
                    "Connected Custom API: ${connectedProvider.displayNameEn}" +
                      (apiStatus.lastLatencyMs?.let { " • ${it}ms" } ?: "")
                  }
                } else if (apiStatus.lastLatencyMs != null && apiStatus.isLiveConnected) {
                  strings.apiConnectedSuccess(apiStatus.lastLatencyMs)
                } else {
                  strings.apiConnectedUsingBuiltIn
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = if (connectedProvider != null) {
                  if (language == AppLanguage.BN) {
                    "সক্রিয় এআই মডেল প্রোভাইডার: ${connectedProvider.displayNameBn} (ডিফল্ট ফলব্যাক সহ)"
                  } else {
                    "Active AI Provider: ${connectedProvider.displayNameEn} (with Default Fallback)"
                  }
                } else {
                  "${strings.doctorHeaderName} • ${strings.doctorHeaderRole}"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

        // Instant Auto-Detected API Badge when user pastes or types a key
        if (liveDetectedProvider != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = DeepForestEmerald.copy(alpha = 0.16f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("detected_api_provider_badge")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
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
              }

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
                if (trimmedKey.isNotBlank() && finalProvider != null) {
                  // Keep saved and inform user which provider was detected
                  isErrorFeedback = true
                  feedbackMessage = if (language == AppLanguage.BN) {
                    "${finalProvider.displayNameBn} এপিআই কি যাচাই ব্যর্থ হয়েছে (ডিফল্ট ব্যাকআপ সক্রিয় থাকবে)"
                  } else {
                    "${finalProvider.displayNameEn} key verification failed (Default fallback remains active)"
                  }
                } else {
                  isErrorFeedback = true
                  feedbackMessage = strings.apiErrorInvalidKey
                }
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
              GeminiPsychiatristService.saveCustomKey(context, "", null)
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

/**
 * Popup Sheet Dialog for uploading Gallery Photo, Camera Photo, or PDF/Document for Gemini AI analysis.
 */
@Composable
private fun AttachmentUploadSheetDialog(
  language: AppLanguage,
  onPickGalleryPhoto: () -> Unit,
  onTakeCameraPhoto: () -> Unit,
  onPickDocument: () -> Unit,
  onDismiss: () -> Unit
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current

  Dialog(onDismissRequest = onDismiss) {
    GradientGlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("attachment_picker_sheet_dialog"),
      cornerRadius = 26.dp,
      isHighlighted = true,
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.95f else 0.92f)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = strings.attachmentSheetTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = strings.apiCloseButton,
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = strings.attachmentSheetSubtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        AttachmentOptionRow(
          icon = Icons.Default.Image,
          title = strings.attachmentOptionGallery,
          subtitle = strings.attachmentOptionGallerySub,
          accentColor = Color(0xFF14B8A6),
          testTag = "attach_option_gallery",
          onClick = onPickGalleryPhoto
        )

        Spacer(modifier = Modifier.height(10.dp))

        AttachmentOptionRow(
          icon = Icons.Default.CameraAlt,
          title = strings.attachmentOptionCamera,
          subtitle = strings.attachmentOptionCameraSub,
          accentColor = Color(0xFFF59E0B),
          testTag = "attach_option_camera",
          onClick = onTakeCameraPhoto
        )

        Spacer(modifier = Modifier.height(10.dp))

        AttachmentOptionRow(
          icon = Icons.Default.Description,
          title = strings.attachmentOptionDocument,
          subtitle = strings.attachmentOptionDocumentSub,
          accentColor = Color(0xFF38BDF8),
          testTag = "attach_option_document",
          onClick = onPickDocument
        )
      }
    }
  }
}

@Composable
private fun AttachmentOptionRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  accentColor: Color,
  testTag: String,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.45f)),
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        shape = CircleShape,
        color = accentColor.copy(alpha = 0.18f),
        modifier = Modifier.size(44.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(22.dp)
          )
        }
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun LiveStreamingBubble(
  language: AppLanguage,
  partialText: String?
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current
  val bubbleBg = if (isDark) DarkDoctorBubbleBg else LightDoctorBubbleBg
  val titleColor = if (isDark) DarkDoctorTitleTeal else LightDoctorTitleGreen
  val textColor = if (isDark) Color(0xFFF2F6FA) else LightBlackTextColor
  val borderColor = if (isDark) Color(0xFF234E52) else Color(0xFF0D9488).copy(alpha = 0.48f)

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Start
  ) {
    Card(
      modifier = Modifier
        .widthIn(min = 160.dp, max = 310.dp)
        .testTag("live_streaming_bubble_card"),
      shape = RoundedCornerShape(20.dp),
      border = BorderStroke(1.dp, borderColor),
      colors = CardDefaults.cardColors(containerColor = bubbleBg),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          DoctorSifatAvatar(modifier = Modifier.size(26.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = strings.doctorBubbleTitle,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = titleColor
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = if (!partialText.isNullOrBlank()) {
            "$partialText ▍"
          } else {
            strings.doctorThinking
          },
          style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 23.sp
          ),
          color = textColor
        )
      }
    }
  }
}

@Composable
fun ConsultationBubble(
  language: AppLanguage,
  message: ConsultationMessage,
  onDelete: (() -> Unit)?
) {
  val strings = stringsFor(language)
  val isDoctor = message.role == "psychiatrist"
  val isDark = LocalIsDarkTheme.current
  val doctorBubbleBg = if (isDark) DarkDoctorBubbleBg else LightDoctorBubbleBg
  val userBubbleBg = if (isDark) UserBubbleTeal else LightUserBubbleBg
  val doctorTitleColor = if (isDark) DarkDoctorTitleTeal else LightDoctorTitleGreen
  val doctorTextColor = if (isDark) Color(0xFFF2F6FA) else LightBlackTextColor
  val userTextColor = if (isDark) Color.White else LightBlackTextColor
  val doctorTimestampColor = if (isDark) DarkTimestampMutedColor else LightTimestampMutedColor
  val userTimestampColor = if (isDark) Color.White.copy(alpha = 0.85f) else LightTimestampMutedColor
  val userCheckmarkColor = if (isDark) Color.White.copy(alpha = 0.9f) else LightBlackTextColor
  val bubbleBorder = if (isDoctor) {
    if (isDark) Color(0xFF234E52) else Color(0xFF0D9488).copy(alpha = 0.48f)
  } else {
    if (isDark) Color(0xFF2DD4BF).copy(alpha = 0.45f) else Color(0xFF0D9488).copy(alpha = 0.55f)
  }

  val attachmentLabel = remember(message.moodTag) {
    message.extractAttachmentLabel()
  }

  val displayTime = remember(message.createdAt) {
    val ts = message.createdAt ?: Timestamp.now()
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(ts.toDate())
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isDoctor) Arrangement.Start else Arrangement.End
  ) {
    Card(
      modifier = Modifier
        .widthIn(min = 140.dp, max = 310.dp)
        .let { mod ->
          if (onDelete != null) {
            mod.clickable { onDelete() }
          } else {
            mod
          }
        },
      shape = RoundedCornerShape(20.dp),
      border = BorderStroke(1.dp, bubbleBorder),
      colors = CardDefaults.cardColors(
        containerColor = if (isDoctor) doctorBubbleBg else userBubbleBg
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp)) {
        if (isDoctor) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            DoctorSifatAvatar(modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.doctorBubbleTitle,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              ),
              color = doctorTitleColor
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        if (!attachmentLabel.isNullOrBlank()) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isDark) Color.Black.copy(alpha = 0.22f) else Color.Black.copy(alpha = 0.08f),
            border = BorderStroke(
              1.dp,
              if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.22f)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = null,
                tint = if (isDark) Color.White else LightBlackTextColor,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = attachmentLabel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isDark) Color.White else LightBlackTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }

        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 23.sp
          ),
          color = if (isDoctor) doctorTextColor else userTextColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.align(Alignment.End),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = displayTime,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
            color = if (isDoctor) doctorTimestampColor else userTimestampColor
          )
          if (!isDoctor) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = null,
              tint = userCheckmarkColor,
              modifier = Modifier.size(15.dp)
            )
          }
        }
      }
    }
  }
}

private suspend fun encodeBitmapToAttachment(
  bitmap: Bitmap,
  fileName: String
): ChatAttachmentPayload? = withContext(Dispatchers.IO) {
  try {
    val maxDim = 1024
    val w = bitmap.width
    val h = bitmap.height
    val scaled = if (w > maxDim || h > maxDim) {
      val ratio = maxDim.toFloat() / max(w, h).toFloat()
      Bitmap.createScaledBitmap(
        bitmap,
        (w * ratio).roundToInt().coerceAtLeast(1),
        (h * ratio).roundToInt().coerceAtLeast(1),
        true
      )
    } else {
      bitmap
    }
    val out = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, 82, out)
    val base64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
    ChatAttachmentPayload(
      fileName = fileName,
      mimeType = "image/jpeg",
      base64Data = base64,
      isImage = true
    )
  } catch (_: Exception) {
    null
  }
}

private suspend fun loadAttachmentFromUri(
  context: Context,
  uri: Uri,
  forceImage: Boolean
): ChatAttachmentPayload? = withContext(Dispatchers.IO) {
  try {
    val resolver = context.contentResolver
    val detectedMime = resolver.getType(uri) ?: if (forceImage) "image/jpeg" else "application/pdf"
    val fileName = queryDisplayName(context, uri)
      ?: if (detectedMime.startsWith("image/")) "Image_${System.currentTimeMillis() % 10000}.jpg"
      else "Document_${System.currentTimeMillis() % 10000}.pdf"

    if (forceImage || detectedMime.startsWith("image/")) {
      val bitmap = resolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream)
      } ?: return@withContext null
      return@withContext encodeBitmapToAttachment(bitmap, fileName)
    }

    // For PDF or Text/Document files (read up to 3.5 MB)
    val maxBytes = 3_500_000
    val rawBytes = resolver.openInputStream(uri)?.use { stream ->
      val buffer = ByteArrayOutputStream()
      val chunk = ByteArray(16_384)
      var total = 0
      var read: Int
      while (stream.read(chunk).also { read = it } != -1) {
        val toWrite = read.coerceAtMost(maxBytes - total)
        if (toWrite > 0) {
          buffer.write(chunk, 0, toWrite)
          total += toWrite
        }
        if (total >= maxBytes) break
      }
      buffer.toByteArray()
    } ?: return@withContext null

    val base64 = Base64.encodeToString(rawBytes, Base64.NO_WRAP)
    val textPreview = if (detectedMime.startsWith("text/")) {
      rawBytes.toString(Charsets.UTF_8).take(6000)
    } else {
      null
    }
    val safeMime = when {
      detectedMime == "application/pdf" || fileName.lowercase().endsWith(".pdf") -> "application/pdf"
      detectedMime.startsWith("text/") -> "text/plain"
      else -> detectedMime
    }
    ChatAttachmentPayload(
      fileName = fileName,
      mimeType = safeMime,
      base64Data = base64,
      isImage = false,
      extractedTextPreview = textPreview
    )
  } catch (_: Exception) {
    null
  }
}

private fun queryDisplayName(context: Context, uri: Uri): String? {
  return try {
    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
      if (cursor.moveToFirst()) {
        val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (idx >= 0) cursor.getString(idx) else null
      } else {
        null
      }
    }
  } catch (_: Exception) {
    null
  }
}
