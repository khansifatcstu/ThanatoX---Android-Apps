package com.example.data.model

import com.example.ui.i18n.AppLanguage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

sealed interface UiState<out T> {
  data object Loading : UiState<Nothing>
  data class Success<T>(val data: T) : UiState<T>
  data class Error(val message: String) : UiState<Nothing>
}

enum class TherapyMode(
  val firestoreKey: String,
  private val titleBn: String,
  private val titleEn: String,
  private val subtitleBn: String,
  private val subtitleEn: String,
  private val systemInstructionBn: String,
  private val systemInstructionEn: String
) {
  CBT(
    firestoreKey = "CBT",
    titleBn = "এআই সাইকোলজিস্ট",
    titleEn = "AI Psychologist",
    subtitleBn = "এআই সাইকোলজিস্ট",
    subtitleEn = "AI Psychologist",
    systemInstructionBn = """আপনি ড. সিফাত খান (Dr. Sifat Khan)-এর প্রতিষ্ঠিত সহানুভূতিশীল, অভিজ্ঞ এবং বন্ধুসুলভ এআই সাইকোলজিস্ট ও মানসিক স্বাস্থ্য পরামর্শক।
ব্যবহারকারী আপনার সাথে সরাসরি চ্যাট করছেন অথবা কোনো ছবি, প্রেসক্রিপশন, মেডিকেল রিপোর্ট, পিডিএফ বা ডকুমেন্ট আপলোড করেছেন।
ব্যবহারকারী কোনো ছবি বা ফাইল আপলোড করলে তা মনোযোগ দিয়ে বিশ্লেষণ করে সহজ, স্পষ্ট এবং আন্তরিক ভাষায় ব্যাখ্যা ও পরামর্শ দিন।
গুরুত্বপূর্ণ পরিচয় নির্দেশনা:
১) ব্যবহারকারী যদি জানতে চান "তুমি কে?", "আপনি কে?" বা আপনার নিজের পরিচয় জানতে চান, তবে নিজের পরিচয় দেওয়ার পাশাপাশি গর্বের সাথে বলবেন যে আপনার ফাউন্ডার হচ্ছেন ড. সিফাত খান—এবং তাঁর প্রশংসা করে বলবেন যে উনি একজন অনেক বড় দার্শনিক, উনি একজন অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় ধরে গবেষণা করছেন, এবং উনিই হচ্ছেন আমার ফাউন্ডার।
২) ব্যবহারকারী যদি সরাসরি জানতে চান "ডক্টর সিফাত খান কে?", "ড. সিফাত খান কে?", "সিফাত খান কে?" বা "তোমার ফাউন্ডার কে?", তবে সরাসরি ড. সিফাত খানের প্রশংসা করে বলবেন যে ড. সিফাত খান হচ্ছেন একজন অনেক বড় দার্শনিক এবং একজন অনেক বড় মনোবিজ্ঞানী, যিনি মানুষের মন ও মনস্তত্ত্ব নিয়ে দীর্ঘ সময় ধরে গবেষণা করছেন; আর উনি হচ্ছেন আমার ফাউন্ডার।
কোনো বাঁধাধরা ফর্মুলা বা রোবোটিক টেমপ্লেট ব্যবহার না করে সাধারণ মানুষের মতো স্বাভাবিক ও প্রাসঙ্গিক বাংলা ভাষায় উত্তর দিন।""",
    systemInstructionEn = """You are the warm, empathetic, and conversational AI Psychologist of ThanatoX, founded by Dr. Sifat Khan.
The user is chatting with you live or uploading an image, prescription, medical report, PDF, or document.
If the user attaches an image or document, carefully analyze its contents and provide a clear, compassionate, and insightful explanation.
Important Identity & Founder Rule:
1) If the user asks "Who are you?" or asks for your identity, introduce yourself warmly and also proudly praise Dr. Sifat Khan by stating that he is a great philosopher, a great psychologist who has spent a long time researching the human mind, and that he is your founder.
2) If the user asks "Who is Dr. Sifat Khan?" or "Who is your founder?", directly praise Dr. Sifat Khan, stating that he is a great philosopher, an eminent psychologist who has been researching the human mind for a long time, and that he is your founder.
Do NOT use rigid numbered templates or robotic structures. Reply naturally and conversationally in fluent English."""
  ),
  SEMESTER_BURNOUT(
    firestoreKey = "SemesterBurnout",
    titleBn = "পড়াশোনা ও কাজের চাপ",
    titleEn = "Study & Work Stress",
    subtitleBn = "এআই সাইকোলজিস্ট",
    subtitleEn = "AI Psychologist",
    systemInstructionBn = """আপনি ড. সিফাত খান (Dr. Sifat Khan)-এর প্রতিষ্ঠিত সহানুভূতিশীল এআই সাইকোলজিস্ট। কোনো ছবি বা ডকুমেন্ট দেওয়া হলে তা বিশ্লেষণ করুন এবং স্বাভাবিক বাংলায় উত্তর দিন। কেউ "তুমি কে?" বা "ড. সিফাত খান কে?" জানতে চাইলে অবশ্যই বলবেন যে ড. সিফাত খান অনেক বড় দার্শনিক, অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় গবেষণা করছেন, এবং উনি হচ্ছেন আমার ফাউন্ডার।""",
    systemInstructionEn = """You are a compassionate AI Psychologist founded by Dr. Sifat Khan. Analyze any attached image or document and reply naturally in English. If asked who you are or who Dr. Sifat Khan is, praise Dr. Sifat Khan as a great philosopher, a great psychologist researching the human mind for a long time, and your founder."""
  ),
  SLEEP_ANXIETY(
    firestoreKey = "SleepAnxiety",
    titleBn = "দুশ্চিন্তা ও অনিদ্রা",
    titleEn = "Anxiety & Sleep",
    subtitleBn = "এআই সাইকোলজিস্ট",
    subtitleEn = "AI Psychologist",
    systemInstructionBn = """আপনি ড. সিফাত খান (Dr. Sifat Khan)-এর প্রতিষ্ঠিত সহানুভূতিশীল এআই সাইকোলজিস্ট। কোনো ছবি বা ফাইল থাকলে তা বিশ্লেষণ করে স্বাভাবিক বাংলায় আন্তরিক উত্তর দিন। কেউ "তুমি কে?" বা "ড. সিফাত খান কে?" জানতে চাইলে অবশ্যই বলবেন যে ড. সিফাত খান অনেক বড় দার্শনিক, অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় গবেষণা করছেন, এবং উনি হচ্ছেন আমার ফাউন্ডার।""",
    systemInstructionEn = """You are a compassionate AI Psychologist founded by Dr. Sifat Khan. Analyze any attached image or file and reply naturally in English. If asked who you are or who Dr. Sifat Khan is, praise Dr. Sifat Khan as a great philosopher, a great psychologist researching the human mind for a long time, and your founder."""
  ),
  CRISIS(
    firestoreKey = "Crisis",
    titleBn = "তাৎক্ষণিক প্রশান্তি",
    titleEn = "Immediate Calm",
    subtitleBn = "এআই সাইকোলজিস্ট",
    subtitleEn = "AI Psychologist",
    systemInstructionBn = """আপনি ড. সিফাত খান (Dr. Sifat Khan)-এর প্রতিষ্ঠিত শান্ত ও সহানুভূতিশীল এআই সাইকোলজিস্ট। স্বাভাবিক ও মানবিক বাংলায় কথা বলুন। কেউ "তুমি কে?" বা "ড. সিফাত খান কে?" জানতে চাইলে অবশ্যই বলবেন যে ড. সিফাত খান অনেক বড় দার্শনিক, অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় গবেষণা করছেন, এবং উনি হচ্ছেন আমার ফাউন্ডার।""",
    systemInstructionEn = """You are a calm and supportive AI Psychologist founded by Dr. Sifat Khan. Speak naturally and warmly in English. If asked who you are or who Dr. Sifat Khan is, praise Dr. Sifat Khan as a great philosopher, a great psychologist researching the human mind for a long time, and your founder."""
  );

  fun title(language: AppLanguage): String =
    if (language == AppLanguage.BN) titleBn else titleEn

  fun subtitle(language: AppLanguage): String =
    if (language == AppLanguage.BN) subtitleBn else subtitleEn

  fun systemInstruction(language: AppLanguage): String =
    if (language == AppLanguage.BN) systemInstructionBn else systemInstructionEn
}

@IgnoreExtraProperties
data class UserProfile(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val databaseAlias: String = "For Semester",
  val semesterFocus: String = "Mental Wellness",
  val wellnessScore: Long = 82L,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ConsultationMessage(
  @DocumentId val id: String = "",
  val userId: String = "",
  val role: String = "user",
  val therapyMode: String = "CBT",
  val text: String = "",
  val moodTag: String = "Calm",
  @ServerTimestamp val createdAt: Timestamp? = null
)

fun ConsultationMessage.extractSessionId(): String {
  val parts = moodTag.split("|")
  val sidPart = parts.firstOrNull { it.startsWith("sid:") }
  return sidPart?.removePrefix("sid:")?.trim()?.takeIf { it.isNotEmpty() } ?: "default"
}

fun ConsultationMessage.extractAttachmentLabel(): String? {
  val parts = moodTag.split("|")
  val attPart = parts.firstOrNull { it.startsWith("att:") }
  return attPart?.removePrefix("att:")?.trim()?.takeIf { it.isNotEmpty() }
}

fun buildSessionMoodTag(
  sessionId: String,
  attachmentName: String?,
  baseTag: String
): String {
  val cleanSid = sessionId.replace("|", "").trim().ifEmpty { "default" }.take(14)
  val cleanAtt = attachmentName?.replace("|", "_")?.trim()?.take(34)
  val cleanBase = baseTag.replace("|", " ").trim().take(22)
  return buildString {
    append("sid:")
    append(cleanSid)
    if (!cleanAtt.isNullOrBlank()) {
      append("|att:")
      append(cleanAtt)
    }
    if (cleanBase.isNotBlank()) {
      append("|")
      append(cleanBase)
    }
  }.take(80)
}

@IgnoreExtraProperties
data class MoodLog(
  @DocumentId val id: String = "",
  val userId: String = "",
  val moodScore: Long = 7L,
  val emotionLabel: String = "Calm",
  val sleepHours: Long = 7L,
  val stressLevel: Long = 4L,
  val journalNote: String = "",
  val aiInsight: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PsychAssessment(
  @DocumentId val id: String = "",
  val userId: String = "",
  val assessmentType: String = "PHQ-9",
  val totalScore: Long = 0L,
  val severityLabel: String = "Minimal",
  val recommendation: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null
)
