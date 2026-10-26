package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ConsultationMessage
import com.example.data.model.MoodLog
import com.example.data.model.PsychAssessment
import com.example.data.model.TherapyMode
import com.example.data.model.UiState
import com.example.data.model.UserProfile
import com.example.data.model.buildSessionMoodTag
import com.example.data.model.extractSessionId
import com.example.data.remote.ChatAttachmentPayload
import com.example.data.remote.GeminiPsychiatristService
import com.example.data.repository.ThanatoXRepository
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThanatoXViewModel(
  private val repository: ThanatoXRepository,
  private val currentUserId: String,
  private val initialDisplayName: String,
  initialLanguage: AppLanguage = AppLanguage.BN
) : ViewModel() {

  private val _appLanguage = MutableStateFlow(initialLanguage)
  val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

  val userProfileState: StateFlow<UiState<UserProfile?>> =
    repository.observeUserProfile(currentUserId)
      .map<UserProfile?, UiState<UserProfile?>> { UiState.Success(it) }
      .catch { e ->
        Log.w(TAG, "Error observing user profile", e)
        emit(UiState.Error(e.message ?: "Error"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Loading
      )

  val consultationsState: StateFlow<UiState<List<ConsultationMessage>>> =
    repository.observeConsultations(currentUserId)
      .map<List<ConsultationMessage>, UiState<List<ConsultationMessage>>> { UiState.Success(it) }
      .catch { e ->
        Log.w(TAG, "Error observing consultations", e)
        emit(UiState.Error(e.message ?: "Error"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Loading
      )

  val moodLogsState: StateFlow<UiState<List<MoodLog>>> =
    repository.observeMoodLogs(currentUserId)
      .map<List<MoodLog>, UiState<List<MoodLog>>> { UiState.Success(it) }
      .catch { e ->
        Log.w(TAG, "Error observing mood logs", e)
        emit(UiState.Error(e.message ?: "Error"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Loading
      )

  val assessmentsState: StateFlow<UiState<List<PsychAssessment>>> =
    repository.observeAssessments(currentUserId)
      .map<List<PsychAssessment>, UiState<List<PsychAssessment>>> { UiState.Success(it) }
      .catch { e ->
        Log.w(TAG, "Error observing assessments", e)
        emit(UiState.Error(e.message ?: "Error"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Loading
      )

  private val _selectedTherapyMode = MutableStateFlow(TherapyMode.CBT)
  val selectedTherapyMode: StateFlow<TherapyMode> = _selectedTherapyMode.asStateFlow()

  private val _isAiThinking = MutableStateFlow(false)
  val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

  private val _streamingReplyText = MutableStateFlow<String?>(null)
  val streamingReplyText: StateFlow<String?> = _streamingReplyText.asStateFlow()

  private val _statusBannerMessage = MutableStateFlow<String?>(null)
  val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

  init {
    viewModelScope.launch {
      repository.ensureUserProfile(
        displayName = initialDisplayName.ifBlank { "Member" },
        semesterFocus = "Mental Wellness"
      )
    }
  }

  fun setLanguage(language: AppLanguage) {
    _appLanguage.value = language
  }

  fun toggleLanguage() {
    _appLanguage.value = if (_appLanguage.value == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
  }

  fun selectTherapyMode(mode: TherapyMode) {
    _selectedTherapyMode.value = mode
  }

  fun clearBannerMessage() {
    _statusBannerMessage.value = null
  }

  fun sendConsultationMessage(
    userText: String,
    moodTag: String,
    attachment: ChatAttachmentPayload? = null
  ) {
    val trimmed = userText.trim()
    if ((trimmed.isEmpty() && attachment == null) || _isAiThinking.value) return

    val mode = _selectedTherapyMode.value
    val lang = _appLanguage.value
    val effectiveText = trimmed.ifEmpty { stringsFor(lang).attachmentDefaultPrompt }
    val targetSessionId = ConsultationMessage(moodTag = moodTag).extractSessionId()
    val historySnapshot = (consultationsState.value as? UiState.Success)?.data
      .orEmpty()
      .filter { it.extractSessionId() == targetSessionId }

    viewModelScope.launch {
      _isAiThinking.value = true
      _streamingReplyText.value = ""
      val userResult = repository.addConsultationMessage(
        role = "user",
        therapyMode = mode.firestoreKey,
        text = effectiveText,
        moodTag = moodTag
      )
      if (userResult.isFailure) {
        _statusBannerMessage.value = stringsFor(lang).genericErrorToast
        _streamingReplyText.value = null
        _isAiThinking.value = false
        return@launch
      }

      val aiReply = GeminiPsychiatristService.generatePsychiatristReplyStream(
        userPrompt = effectiveText,
        therapyMode = mode,
        language = lang,
        recentHistory = historySnapshot,
        attachment = attachment,
        onPartialUpdate = { partial ->
          _streamingReplyText.value = partial
        }
      )

      _streamingReplyText.value = null
      val aiMoodTag = buildSessionMoodTag(
        sessionId = targetSessionId,
        attachmentName = null,
        baseTag = mode.title(lang)
      )
      repository.addConsultationMessage(
        role = "psychiatrist",
        therapyMode = mode.firestoreKey,
        text = aiReply,
        moodTag = aiMoodTag
      )
      _isAiThinking.value = false
    }
  }

  fun deleteConsultationMessage(messageId: String) {
    if (messageId.isBlank()) return
    viewModelScope.launch {
      repository.deleteConsultationMessage(messageId)
    }
  }

  fun deleteChatSession(sessionId: String) {
    if (sessionId.isBlank()) return
    val sessionMessages = (consultationsState.value as? UiState.Success)?.data
      .orEmpty()
      .filter { it.extractSessionId() == sessionId }
    viewModelScope.launch {
      sessionMessages.forEach { msg ->
        if (msg.id.isNotBlank()) {
          repository.deleteConsultationMessage(msg.id)
        }
      }
    }
  }

  fun logMoodCheckIn(
    moodScore: Int,
    emotionLabel: String,
    sleepHours: Int,
    stressLevel: Int,
    journalNote: String
  ) {
    val lang = _appLanguage.value
    viewModelScope.launch {
      _isAiThinking.value = true
      val insight = GeminiPsychiatristService.generateMoodInsight(
        moodScore = moodScore,
        emotionLabel = emotionLabel,
        sleepHours = sleepHours,
        stressLevel = stressLevel,
        journalNote = journalNote,
        language = lang
      )
      val result = repository.addMoodLog(
        moodScore = moodScore,
        emotionLabel = emotionLabel,
        sleepHours = sleepHours,
        stressLevel = stressLevel,
        journalNote = journalNote,
        aiInsight = insight
      )
      _isAiThinking.value = false
      if (result.isSuccess) {
        val newWellness = ((moodScore * 6) + ((10 - stressLevel) * 4)).coerceIn(20, 99)
        repository.ensureUserProfile(
          displayName = initialDisplayName.ifBlank { "Member" },
          semesterFocus = emotionLabel,
          wellnessScore = newWellness
        )
        _statusBannerMessage.value = stringsFor(lang).moodSavedToast
      } else {
        _statusBannerMessage.value = stringsFor(lang).genericErrorToast
      }
    }
  }

  fun deleteMoodLog(logId: String) {
    if (logId.isBlank()) return
    viewModelScope.launch {
      repository.deleteMoodLog(logId)
    }
  }

  fun submitClinicalAssessment(
    assessmentType: String,
    totalScore: Int,
    severityLabel: String,
    recommendation: String
  ) {
    val lang = _appLanguage.value
    viewModelScope.launch {
      val result = repository.addAssessment(
        assessmentType = assessmentType,
        totalScore = totalScore,
        severityLabel = severityLabel,
        recommendation = recommendation
      )
      if (result.isSuccess) {
        _statusBannerMessage.value = stringsFor(lang).assessmentSavedToast
      } else {
        _statusBannerMessage.value = stringsFor(lang).genericErrorToast
      }
    }
  }

  fun savePsychSessionReport(
    problemTitle: String,
    prescription: String
  ) {
    val lang = _appLanguage.value
    viewModelScope.launch {
      val result = repository.addAssessment(
        assessmentType = "PSYCH_SESSION",
        totalScore = 100,
        severityLabel = problemTitle,
        recommendation = prescription
      )
      if (result.isSuccess) {
        _statusBannerMessage.value = if (lang == AppLanguage.BN) {
          "ড. সিফাত খানের সাইকোলজিকাল সেশন রিপোর্ট সংরক্ষিত হয়েছে"
        } else {
          "Dr. Sifat Khan's psychological session report saved"
        }
      } else {
        _statusBannerMessage.value = stringsFor(lang).genericErrorToast
      }
    }
  }

  fun deleteAssessment(assessmentId: String) {
    if (assessmentId.isBlank()) return
    viewModelScope.launch {
      repository.deleteAssessment(assessmentId)
    }
  }

  suspend fun resetAllUserData(): Boolean {
    val lang = _appLanguage.value
    val result = repository.resetAllUserData()
    return if (result.isSuccess) {
      _statusBannerMessage.value = if (lang == AppLanguage.BN) {
        "আপনার অ্যাকাউন্টের সকল ডাটা সফলভাবে রিসেট করা হয়েছে"
      } else {
        "All data under your account has been reset"
      }
      true
    } else {
      _statusBannerMessage.value = stringsFor(lang).genericErrorToast
      false
    }
  }

  private companion object {
    const val TAG = "ThanatoXVM"
    const val STOP_TIMEOUT_MILLIS = 5000L
  }
}
