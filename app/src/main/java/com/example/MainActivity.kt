package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import kotlinx.coroutines.Dispatchers
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.auth.authStateFlow
import com.example.auth.onGoogleSignInClicked
import com.example.auth.signOut
import com.example.data.model.ConsultationMessage
import com.example.data.model.MoodLog
import com.example.data.model.PsychAssessment
import com.example.data.model.TherapyMode
import com.example.data.model.UiState
import com.example.data.model.buildSessionMoodTag
import com.example.data.model.extractSessionId
import com.example.data.remote.GeminiPsychiatristService
import com.example.data.repository.ThanatoXRepository
import com.example.ui.components.ChatbotSanctuaryAmbientBackground
import com.example.ui.components.HomeSanctuaryAmbientBackground
import com.example.ui.components.LotusSanctuaryIllustration
import com.example.ui.components.MindCareSanctuaryAmbientBackground
import com.example.ui.components.NeuralSynapseAmbientBackground
import com.example.ui.components.TestSanctuaryAmbientBackground
import com.example.ui.components.ThanatoXBitmapCache
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import com.example.ui.screens.AssessmentScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ConsultationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MindCareScreen
import com.example.ui.screens.MindCareSubSection
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ThanatoXViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import kotlinx.coroutines.launch

/**
 * 5 Bottom Navigation Tabs:
 * 1. হোম / Home (HOME)
 * 2. ড. সিফাত / Dr. Sifat (CONSULT - Live Streaming AI Psychiatrist Chatbot)
 * 3. পরীক্ষা / Test (ASSESSMENT - PHQ-9 / GAD-7 Clinical Screening)
 * 4. মনোযত্ন / Mind Care (MIND_CARE - Unified Hub for Toolkit & Journal)
 * 5. সেটিংস / Settings (SETTINGS - Full-screen Glass Settings & Controls)
 */
enum class ThanatoXTab(
  val icon: ImageVector,
  val testTag: String
) {
  HOME(Icons.Filled.Home, "nav_tab_home"),
  CONSULT(Icons.Outlined.ChatBubbleOutline, "nav_tab_consult"),
  ASSESSMENT(Icons.AutoMirrored.Outlined.Assignment, "nav_tab_assessment"),
  MIND_CARE(Icons.Default.SelfImprovement, "nav_tab_mind_care"),
  SETTINGS(Icons.Default.Settings, "nav_tab_settings");

  fun label(language: AppLanguage): String {
    val s = stringsFor(language)
    return when (this) {
      HOME -> s.tabHome
      CONSULT -> s.tabConsult
      ASSESSMENT -> s.tabWellnessTest
      MIND_CARE -> s.tabMindCare
      SETTINGS -> s.tabSettings
    }
  }
}

fun configureAppCheck(context: Context, intent: Intent?) {
  try {
    val firebaseApp = FirebaseApp.initializeApp(context) ?: FirebaseApp.getInstance()
    val debugToken = intent?.getStringExtra("FIREBASE_APPCHECK_DEBUG_TOKEN")
      ?.trim()
      ?.takeIf { it.isNotEmpty() }
    if (debugToken != null) {
      val prefsName = "com.google.firebase.appcheck.debug.store.${firebaseApp.persistenceKey}"
      context.applicationContext
        .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        .edit()
        .putString("com.google.firebase.appcheck.debug.DEBUG_SECRET", debugToken)
        .apply()
      Firebase.appCheck.installAppCheckProviderFactory(
        DebugAppCheckProviderFactory.getInstance()
      )
    }
  } catch (_: Exception) {
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val launchIntent = intent
    lifecycleScope.launch(Dispatchers.IO) {
      ThanatoXBitmapCache.preloadAll(applicationContext)
      configureAppCheck(applicationContext, launchIntent)
      GeminiPsychiatristService.loadSavedCustomKey(applicationContext)
    }
    enableEdgeToEdge()
    setContent {
      ThanatoXAppRoot()
    }
  }
}

@Composable
fun ThanatoXAppRoot(auth: FirebaseAuth = Firebase.auth) {
  var selectedLanguage by rememberSaveable { mutableStateOf(AppLanguage.BN) }
  var isDarkMode by rememberSaveable { mutableStateOf(true) }
  var isGuestSessionActive by rememberSaveable { mutableStateOf(false) }
  var guestSessionEpoch by rememberSaveable { mutableIntStateOf(0) }
  val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
  val user = currentUser

  MyApplicationTheme(darkTheme = isDarkMode) {
    if (user != null) {
      ThanatoXAuthenticatedShell(
        currentUserId = user.uid,
        displayName = user.displayName ?: user.email ?: "",
        userEmail = user.email ?: user.displayName ?: "",
        language = selectedLanguage,
        isDarkMode = isDarkMode,
        onToggleLanguage = {
          selectedLanguage = if (selectedLanguage == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
        },
        onToggleDarkMode = {
          isDarkMode = !isDarkMode
        },
        onSignedOut = {
          isGuestSessionActive = false
        }
      )
    } else if (isGuestSessionActive) {
      androidx.compose.runtime.key(guestSessionEpoch) {
        ThanatoXGuestSanctuaryShell(
          language = selectedLanguage,
          isDarkMode = isDarkMode,
          onToggleLanguage = {
            selectedLanguage = if (selectedLanguage == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
          },
          onToggleDarkMode = {
            isDarkMode = !isDarkMode
          },
          onExitGuestSession = {
            isGuestSessionActive = false
            guestSessionEpoch++
          }
        )
      }
    } else {
      AuthScreen(
        language = selectedLanguage,
        isDarkMode = isDarkMode,
        onToggleLanguage = {
          selectedLanguage = if (selectedLanguage == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
        },
        onToggleDarkMode = {
          isDarkMode = !isDarkMode
        },
        onAuthSuccess = {
          isGuestSessionActive = false
        },
        onContinueAsGuest = {
          guestSessionEpoch++
          isGuestSessionActive = true
        }
      )
    }
  }
}

@Composable
fun ThanatoXTopHeaderBar(
  language: AppLanguage
) {
  val strings = stringsFor(language)
  val isDark = LocalIsDarkTheme.current
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.72f else 0.62f))
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      LotusSanctuaryIllustration(
        modifier = Modifier.size(42.dp),
        isCircular = false,
        cornerRadius = 12.dp
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        com.example.ui.components.ThanatoXBrandWordmark(
          text = strings.appTitle,
          language = language,
          fontSize = 22.sp,
          isOnTranslucentDarkBackdrop = false,
          showUnderlineAccent = false
        )
        Text(
          text = strings.appTagline,
          style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = if (language == AppLanguage.EN) {
              com.example.ui.components.ThanatoXEnglishFontFamily
            } else {
              com.example.ui.components.ThanatoXBengaliFontFamily
            },
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            fontSize = 12.5.sp
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          softWrap = false,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
      }
    }
  }
}

@Composable
fun ThanatoXBottomNavigationBar(
  currentTab: ThanatoXTab,
  language: AppLanguage,
  onSelectTab: (ThanatoXTab) -> Unit
) {
  val isDark = LocalIsDarkTheme.current
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.82f else 0.72f),
    tonalElevation = 0.dp
  ) {
    ThanatoXTab.entries.forEach { tab ->
      val label = tab.label(language)
      val selected = currentTab == tab
      NavigationBarItem(
        selected = selected,
        onClick = { onSelectTab(tab) },
        icon = {
          Icon(
            imageVector = tab.icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 11.sp
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag(tab.testTag)
      )
    }
  }
}

@Composable
fun ThanatoXGuestSanctuaryShell(
  language: AppLanguage,
  isDarkMode: Boolean,
  onToggleLanguage: () -> Unit,
  onToggleDarkMode: () -> Unit,
  onExitGuestSession: () -> Unit
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  val strings = stringsFor(language)
  val apiStatus by GeminiPsychiatristService.connectionStatus.collectAsStateWithLifecycle()

  var currentTab by rememberSaveable { mutableStateOf(ThanatoXTab.HOME) }
  var mindCareSubSection by rememberSaveable { mutableStateOf(MindCareSubSection.HUB) }
  var selectedScreeningKey by rememberSaveable { mutableStateOf<String?>(null) }
  var activeChatSessionId by rememberSaveable {
    mutableStateOf("s_${System.currentTimeMillis().toString(36)}")
  }
  val snackbarHostState = remember { SnackbarHostState() }

  // Ephemeral Guest Database (removed completely on sign-out / exit)
  val localMessages = remember { mutableStateListOf<ConsultationMessage>() }
  val localMoodLogs = remember { mutableStateListOf<MoodLog>() }
  val localAssessments = remember { mutableStateListOf<PsychAssessment>() }
  var selectedMode by rememberSaveable { mutableStateOf(TherapyMode.CBT) }
  var isAiThinking by remember { mutableStateOf(false) }
  var streamingReplyText by remember { mutableStateOf<String?>(null) }

  val clearGuestDataAndExit: () -> Unit = {
    localMessages.clear()
    localMoodLogs.clear()
    localAssessments.clear()
    onExitGuestSession()
  }

  val performGuestGoogleSignIn: () -> Unit = {
    onGoogleSignInClicked(
      context = context,
      credentialManager = credentialManager,
      onAuthSuccess = {
        localMessages.clear()
        localMoodLogs.clear()
        localAssessments.clear()
      },
      onAuthError = { msg ->
        scope.launch { snackbarHostState.showSnackbar(msg) }
      },
      scope = scope,
      onAuthCancelled = {}
    )
  }

  BackHandler(enabled = currentTab != ThanatoXTab.HOME) {
    currentTab = ThanatoXTab.HOME
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // Full-interface GPU-accelerated background extending behind header, content, and bottom bar
    when (currentTab) {
      ThanatoXTab.HOME, ThanatoXTab.SETTINGS -> HomeSanctuaryAmbientBackground()
      ThanatoXTab.CONSULT -> Unit // ConsultationScreen renders its own full-screen ChatbotSanctuaryAmbientBackground
      ThanatoXTab.ASSESSMENT -> TestSanctuaryAmbientBackground()
      ThanatoXTab.MIND_CARE -> MindCareSanctuaryAmbientBackground()
    }

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Transparent,
      contentWindowInsets = WindowInsets.safeDrawing,
      snackbarHost = { SnackbarHost(snackbarHostState) },
      topBar = {
        if (currentTab != ThanatoXTab.CONSULT) {
          ThanatoXTopHeaderBar(
            language = language
          )
        }
      },
      bottomBar = {
        if (currentTab != ThanatoXTab.CONSULT) {
          ThanatoXBottomNavigationBar(
            currentTab = currentTab,
            language = language,
            onSelectTab = { selectedTab ->
              if (selectedTab == ThanatoXTab.MIND_CARE) {
                mindCareSubSection = MindCareSubSection.HUB
              }
              currentTab = selectedTab
            }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {

      when (currentTab) {
        ThanatoXTab.HOME -> {
          HomeScreen(
            language = language,
            savedAssessments = localAssessments.toList(),
            onOpenLiveChatbot = { currentTab = ThanatoXTab.CONSULT },
            onSelectQuickMood = { moodOption ->
              scope.launch {
                val insight = GeminiPsychiatristService.generateMoodInsight(
                  moodScore = moodOption.score,
                  emotionLabel = moodOption.label,
                  sleepHours = 7,
                  stressLevel = (11 - moodOption.score).coerceIn(1, 10),
                  journalNote = moodOption.label,
                  language = language
                )
                localMoodLogs.add(
                  0,
                  MoodLog(
                    id = UUID.randomUUID().toString(),
                    moodScore = moodOption.score.toLong(),
                    emotionLabel = moodOption.label,
                    sleepHours = 7L,
                    stressLevel = (11 - moodOption.score).coerceIn(1, 10).toLong(),
                    journalNote = moodOption.label,
                    aiInsight = insight,
                    createdAt = Timestamp.now()
                  )
                )
                snackbarHostState.showSnackbar(strings.moodSavedToast)
              }
            },
            onOpenJournalTab = {
              mindCareSubSection = MindCareSubSection.JOURNAL
              currentTab = ThanatoXTab.MIND_CARE
            },
            onOpenScreeningTab = { key ->
              selectedScreeningKey = key
              currentTab = ThanatoXTab.ASSESSMENT
            }
          )
        }

        ThanatoXTab.CONSULT -> {
          ConsultationScreen(
            language = language,
            consultationsState = UiState.Success(localMessages.toList()),
            selectedMode = selectedMode,
            isAiThinking = isAiThinking,
            streamingReplyText = streamingReplyText,
            onSelectMode = { selectedMode = it },
            onSendMessage = { userPrompt, moodTag, attachment ->
              val trimmed = userPrompt.trim()
              if ((trimmed.isNotEmpty() || attachment != null) && !isAiThinking) {
                val effectiveText = trimmed.ifEmpty { strings.attachmentDefaultPrompt }
                val targetSid = ConsultationMessage(moodTag = moodTag).extractSessionId()
                val historySnapshot = localMessages.filter { it.extractSessionId() == targetSid }
                localMessages.add(
                  ConsultationMessage(
                    id = UUID.randomUUID().toString(),
                    role = "user",
                    therapyMode = selectedMode.firestoreKey,
                    text = effectiveText,
                    moodTag = moodTag,
                    createdAt = Timestamp.now()
                  )
                )
                scope.launch {
                  isAiThinking = true
                  streamingReplyText = ""
                  val reply = GeminiPsychiatristService.generatePsychiatristReplyStream(
                    userPrompt = effectiveText,
                    therapyMode = selectedMode,
                    language = language,
                    recentHistory = historySnapshot,
                    attachment = attachment,
                    onPartialUpdate = { partial ->
                      streamingReplyText = partial
                    }
                  )
                  streamingReplyText = null
                  val aiMoodTag = buildSessionMoodTag(
                    sessionId = targetSid,
                    attachmentName = null,
                    baseTag = selectedMode.title(language)
                  )
                  localMessages.add(
                    ConsultationMessage(
                      id = UUID.randomUUID().toString(),
                      role = "psychiatrist",
                      therapyMode = selectedMode.firestoreKey,
                      text = reply,
                      moodTag = aiMoodTag,
                      createdAt = Timestamp.now()
                    )
                  )
                  isAiThinking = false
                }
              }
            },
            onDeleteMessage = { id ->
              localMessages.removeAll { it.id == id }
            },
            onDeleteSession = { targetSessionId ->
              localMessages.removeAll { it.extractSessionId() == targetSessionId }
            },
            onBackClick = {
              currentTab = ThanatoXTab.HOME
            },
            onToggleDarkMode = onToggleDarkMode,
            onToggleLanguage = onToggleLanguage,
            currentUserDisplayName = null,
            onGoogleSignInClick = performGuestGoogleSignIn,
            onSignOutClick = clearGuestDataAndExit,
            initialSessionId = activeChatSessionId,
            onSessionIdChanged = { activeChatSessionId = it }
          )
        }

        ThanatoXTab.ASSESSMENT -> {
          AssessmentScreen(
            language = language,
            assessmentsState = UiState.Success(localAssessments.toList()),
            initialAssessmentKey = selectedScreeningKey,
            onSubmitAssessment = { type, score, severity, recommendation ->
              localAssessments.add(
                0,
                PsychAssessment(
                  id = UUID.randomUUID().toString(),
                  assessmentType = type,
                  totalScore = score.toLong(),
                  severityLabel = severity,
                  recommendation = recommendation,
                  createdAt = Timestamp.now()
                )
              )
              scope.launch {
                snackbarHostState.showSnackbar(strings.assessmentSavedToast)
              }
            },
            onDeleteAssessment = { id ->
              localAssessments.removeAll { it.id == id }
            }
          )
        }

        ThanatoXTab.MIND_CARE -> {
          MindCareScreen(
            language = language,
            activeSection = mindCareSubSection,
            onSelectSection = { mindCareSubSection = it },
            moodLogsState = UiState.Success(localMoodLogs.toList()),
            isSubmittingMood = isAiThinking,
            onSaveMoodLog = { moodScore, emotionLabel, sleepHours, stressLevel, journalNote ->
              scope.launch {
                isAiThinking = true
                val insight = GeminiPsychiatristService.generateMoodInsight(
                  moodScore = moodScore,
                  emotionLabel = emotionLabel,
                  sleepHours = sleepHours,
                  stressLevel = stressLevel,
                  journalNote = journalNote,
                  language = language
                )
                localMoodLogs.add(
                  0,
                  MoodLog(
                    id = UUID.randomUUID().toString(),
                    moodScore = moodScore.toLong(),
                    emotionLabel = emotionLabel,
                    sleepHours = sleepHours.toLong(),
                    stressLevel = stressLevel.toLong(),
                    journalNote = journalNote,
                    aiInsight = insight,
                    createdAt = Timestamp.now()
                  )
                )
                isAiThinking = false
                snackbarHostState.showSnackbar(strings.moodSavedToast)
              }
            },
            onDeleteMoodLog = { id ->
              localMoodLogs.removeAll { it.id == id }
            },
            assessmentsState = UiState.Success(localAssessments.toList()),
            onSaveSessionReport = { problemTitle, prescriptionText ->
              localAssessments.add(
                0,
                PsychAssessment(
                  id = UUID.randomUUID().toString(),
                  assessmentType = "PSYCH_SESSION",
                  totalScore = 100L,
                  severityLabel = problemTitle,
                  recommendation = prescriptionText,
                  createdAt = Timestamp.now()
                )
              )
              scope.launch {
                snackbarHostState.showSnackbar(
                  if (language == AppLanguage.BN) {
                    "ড. সিফাত খানের সাইকোলজিকাল সেশন রিপোর্ট সংরক্ষিত হয়েছে"
                  } else {
                    "Dr. Sifat Khan's psychological session report saved"
                  }
                )
              }
            },
            onDeleteSessionReport = { id ->
              localAssessments.removeAll { it.id == id }
            },
            onOpenLiveChatbot = { currentTab = ThanatoXTab.CONSULT }
          )
        }

        ThanatoXTab.SETTINGS -> {
          SettingsScreen(
            language = language,
            apiStatus = apiStatus,
            isDarkMode = isDarkMode,
            onToggleDarkMode = onToggleDarkMode,
            onToggleLanguage = onToggleLanguage,
            currentUserDisplayName = null,
            currentUserEmail = null,
            onGoogleSignInClick = performGuestGoogleSignIn,
            onSignOutClick = clearGuestDataAndExit,
            onResetAllData = {
              localMessages.clear()
              localMoodLogs.clear()
              localAssessments.clear()
              activeChatSessionId = "s_${System.currentTimeMillis().toString(36)}"
              true
            }
          )
        }
      }
    }
  }
}
}

@Composable
fun ThanatoXAuthenticatedShell(
  currentUserId: String,
  displayName: String,
  userEmail: String = "",
  language: AppLanguage,
  isDarkMode: Boolean,
  onToggleLanguage: () -> Unit,
  onToggleDarkMode: () -> Unit,
  onSignedOut: () -> Unit = {}
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  val apiStatus by GeminiPsychiatristService.connectionStatus.collectAsStateWithLifecycle()

  val viewModel: ThanatoXViewModel = viewModel(
    key = currentUserId,
    factory = viewModelFactory {
      initializer {
        val app = checkNotNull(this[APPLICATION_KEY]) {
          "APPLICATION_KEY missing from CreationExtras"
        }
        val dbId = app.getString(R.string.firestore_database_id)
        val db = FirebaseFirestore.getInstance(dbId)
        ThanatoXViewModel(
          repository = ThanatoXRepository(db),
          currentUserId = currentUserId,
          initialDisplayName = displayName,
          initialLanguage = language
        )
      }
    }
  )

  LaunchedEffect(language) {
    viewModel.setLanguage(language)
  }

  var currentTab by rememberSaveable { mutableStateOf(ThanatoXTab.HOME) }
  var mindCareSubSection by rememberSaveable { mutableStateOf(MindCareSubSection.HUB) }
  var selectedScreeningKey by rememberSaveable { mutableStateOf<String?>(null) }
  var activeChatSessionId by rememberSaveable {
    mutableStateOf("s_${System.currentTimeMillis().toString(36)}")
  }
  val snackbarHostState = remember { SnackbarHostState() }

  val consultationsState by viewModel.consultationsState.collectAsStateWithLifecycle()
  val moodLogsState by viewModel.moodLogsState.collectAsStateWithLifecycle()
  val assessmentsState by viewModel.assessmentsState.collectAsStateWithLifecycle()
  val selectedMode by viewModel.selectedTherapyMode.collectAsStateWithLifecycle()
  val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
  val streamingReplyText by viewModel.streamingReplyText.collectAsStateWithLifecycle()
  val statusBanner by viewModel.statusBannerMessage.collectAsStateWithLifecycle()

  val performSignOut: () -> Unit = {
    signOut(
      credentialManager = credentialManager,
      onSignOutComplete = onSignedOut,
      scope = scope
    )
  }

  LaunchedEffect(statusBanner) {
    statusBanner?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearBannerMessage()
    }
  }

  BackHandler(enabled = currentTab != ThanatoXTab.HOME) {
    currentTab = ThanatoXTab.HOME
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // Full-interface GPU-accelerated background extending behind header, content, and bottom bar
    when (currentTab) {
      ThanatoXTab.HOME, ThanatoXTab.SETTINGS -> HomeSanctuaryAmbientBackground()
      ThanatoXTab.CONSULT -> Unit // ConsultationScreen renders its own full-screen ChatbotSanctuaryAmbientBackground
      ThanatoXTab.ASSESSMENT -> TestSanctuaryAmbientBackground()
      ThanatoXTab.MIND_CARE -> MindCareSanctuaryAmbientBackground()
    }

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Transparent,
      contentWindowInsets = WindowInsets.safeDrawing,
      snackbarHost = { SnackbarHost(snackbarHostState) },
      topBar = {
        if (currentTab != ThanatoXTab.CONSULT) {
          ThanatoXTopHeaderBar(
            language = language
          )
        }
      },
      bottomBar = {
        if (currentTab != ThanatoXTab.CONSULT) {
          ThanatoXBottomNavigationBar(
            currentTab = currentTab,
            language = language,
            onSelectTab = { selectedTab ->
              if (selectedTab == ThanatoXTab.MIND_CARE) {
                mindCareSubSection = MindCareSubSection.HUB
              }
              currentTab = selectedTab
            }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {

      when (currentTab) {
        ThanatoXTab.HOME -> {
          val savedAssessments = (assessmentsState as? UiState.Success)?.data.orEmpty()
          HomeScreen(
            language = language,
            savedAssessments = savedAssessments,
            onOpenLiveChatbot = { currentTab = ThanatoXTab.CONSULT },
            onSelectQuickMood = { moodOption ->
              viewModel.logMoodCheckIn(
                moodScore = moodOption.score,
                emotionLabel = moodOption.label,
                sleepHours = 7,
                stressLevel = (11 - moodOption.score).coerceIn(1, 10),
                journalNote = moodOption.label
              )
            },
            onOpenJournalTab = {
              mindCareSubSection = MindCareSubSection.JOURNAL
              currentTab = ThanatoXTab.MIND_CARE
            },
            onOpenScreeningTab = { key ->
              selectedScreeningKey = key
              currentTab = ThanatoXTab.ASSESSMENT
            }
          )
        }

        ThanatoXTab.CONSULT -> {
          ConsultationScreen(
            language = language,
            consultationsState = consultationsState,
            selectedMode = selectedMode,
            isAiThinking = isAiThinking,
            streamingReplyText = streamingReplyText,
            onSelectMode = viewModel::selectTherapyMode,
            onSendMessage = viewModel::sendConsultationMessage,
            onDeleteMessage = viewModel::deleteConsultationMessage,
            onDeleteSession = viewModel::deleteChatSession,
            onBackClick = {
              currentTab = ThanatoXTab.HOME
            },
            onToggleDarkMode = onToggleDarkMode,
            onToggleLanguage = onToggleLanguage,
            currentUserDisplayName = displayName.ifBlank { "User" },
            onGoogleSignInClick = null,
            onSignOutClick = performSignOut,
            initialSessionId = activeChatSessionId,
            onSessionIdChanged = { activeChatSessionId = it }
          )
        }

        ThanatoXTab.ASSESSMENT -> {
          AssessmentScreen(
            language = language,
            assessmentsState = assessmentsState,
            initialAssessmentKey = selectedScreeningKey,
            onSubmitAssessment = viewModel::submitClinicalAssessment,
            onDeleteAssessment = viewModel::deleteAssessment
          )
        }

        ThanatoXTab.MIND_CARE -> {
          MindCareScreen(
            language = language,
            activeSection = mindCareSubSection,
            onSelectSection = { mindCareSubSection = it },
            moodLogsState = moodLogsState,
            isSubmittingMood = isAiThinking,
            onSaveMoodLog = viewModel::logMoodCheckIn,
            onDeleteMoodLog = viewModel::deleteMoodLog,
            assessmentsState = assessmentsState,
            onSaveSessionReport = viewModel::savePsychSessionReport,
            onDeleteSessionReport = viewModel::deleteAssessment,
            onOpenLiveChatbot = { currentTab = ThanatoXTab.CONSULT }
          )
        }

        ThanatoXTab.SETTINGS -> {
          SettingsScreen(
            language = language,
            apiStatus = apiStatus,
            isDarkMode = isDarkMode,
            onToggleDarkMode = onToggleDarkMode,
            onToggleLanguage = onToggleLanguage,
            currentUserDisplayName = displayName.ifBlank { "User" },
            currentUserEmail = userEmail.ifBlank { displayName },
            onGoogleSignInClick = null,
            onSignOutClick = performSignOut,
            onResetAllData = {
              val ok = viewModel.resetAllUserData()
              if (ok) {
                activeChatSessionId = "s_${System.currentTimeMillis().toString(36)}"
              }
              ok
            }
          )
        }
      }
    }
  }
}
}
