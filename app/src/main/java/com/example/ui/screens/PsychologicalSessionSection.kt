package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.remote.GeminiPsychiatristService
import com.example.ui.components.DoctorSifatAvatar
import com.example.ui.components.GradientGlassCard
import com.example.ui.components.StylishApronDoctorAvatar
import com.example.ui.i18n.AppLanguage
import com.example.ui.theme.DeepForestEmerald
import com.example.ui.theme.LocalIsDarkTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

data class PsychSessionProblemCategory(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val tagBn: String,
  val tagEn: String
) {
  fun title(language: AppLanguage): String = if (language == AppLanguage.BN) titleBn else titleEn
  fun subtitle(language: AppLanguage): String = if (language == AppLanguage.BN) subtitleBn else subtitleEn
  fun tag(language: AppLanguage): String = if (language == AppLanguage.BN) tagBn else tagEn
}

data class PsychClinicalStepQuestion(
  val stepNumber: Int,
  val stepLabelBn: String,
  val stepLabelEn: String,
  val questionBn: String,
  val questionEn: String,
  val optionsBn: List<String>,
  val optionsEn: List<String>
) {
  fun stepLabel(language: AppLanguage): String = if (language == AppLanguage.BN) stepLabelBn else stepLabelEn
  fun question(language: AppLanguage): String = if (language == AppLanguage.BN) questionBn else questionEn
  fun options(language: AppLanguage): List<String> = if (language == AppLanguage.BN) optionsBn else optionsEn
}

private val sessionProblemCategories = listOf(
  PsychSessionProblemCategory(
    id = "anxiety_panic",
    titleBn = "তীব্র উদ্বেগ, অস্থিরতা ও প্যানিক",
    titleEn = "Severe Anxiety, Restlessness & Panic",
    subtitleBn = "বুক ধড়ফড় করা, অজানা ভয়, হাত-পা কাঁপা বা হঠাৎ আতঙ্কিত বোধ করা।",
    subtitleEn = "Heart racing, unexplained fear, trembling, or sudden panic episodes.",
    tagBn = "অ্যাংজাইটি ও প্যানিক",
    tagEn = "Anxiety & Panic"
  ),
  PsychSessionProblemCategory(
    id = "depression_emptiness",
    titleBn = "বিষণ্ণতা, শূন্যতা ও আগ্রহহীনতা",
    titleEn = "Depression, Emptiness & Low Motivation",
    subtitleBn = "সবকিছুতে আনন্দ হারিয়ে ফেলা, ভেতরে শূন্যতা, কান্না পাওয়া ও একা লাগা।",
    subtitleEn = "Loss of interest in daily life, inner emptiness, tearfulness, or isolation.",
    tagBn = "ডিপ্রেশন কেয়ার",
    tagEn = "Depression Care"
  ),
  PsychSessionProblemCategory(
    id = "overthinking_focus",
    titleBn = "অতিরিক্ত দুশ্চিন্তা (Overthinking) ও মনোযোগহীনতা",
    titleEn = "Chronic Overthinking & Lack of Focus",
    subtitleBn = "মাথায় একের পর এক নেতিবাচক চিন্তা ঘুরপাক খাওয়া ও কাজে মনোযোগ না থাকা।",
    subtitleEn = "Racing negative thought loops, indecisiveness, and difficulty concentrating.",
    tagBn = "ওভারথিংকিং ও ফোকাস",
    tagEn = "Overthinking & Focus"
  ),
  PsychSessionProblemCategory(
    id = "insomnia_exhaustion",
    titleBn = "অনিদ্রা, দুঃস্বপ্ন ও মানসিক ক্লান্তি",
    titleEn = "Insomnia, Sleep Disturbance & Burnout",
    subtitleBn = "রাতে ঘুম না আসা, বারবার ঘুম ভেঙে যাওয়া এবং সারাদিন মানসিক অবসাদ।",
    subtitleEn = "Difficulty falling asleep, broken sleep, nightmares, and chronic exhaustion.",
    tagBn = "ঘুম ও বার্নআউট",
    tagEn = "Sleep & Burnout"
  ),
  PsychSessionProblemCategory(
    id = "relationship_trauma",
    titleBn = "সম্পর্কজনিত ট্রমা, বিচ্ছেদ ও মানসিক আঘাত",
    titleEn = "Relationship Trauma, Breakup & Emotional Grief",
    subtitleBn = "প্রিয়জনের অবহেলা, বিশ্বাসঘাতকতা, বিচ্ছেদ বা অতীতের কষ্টকর স্মৃতি।",
    subtitleEn = "Heartbreak, betrayal, family conflict, or lingering painful memories.",
    tagBn = "ট্রমা ও সম্পর্ক",
    tagEn = "Trauma & Grief"
  ),
  PsychSessionProblemCategory(
    id = "anger_self_esteem",
    titleBn = "রাগ নিয়ন্ত্রণহীনতা, অপরাধবোধ ও আত্মবিশ্বাসের ঘাটতি",
    titleEn = "Anger Outbursts, Guilt & Low Self-Esteem",
    subtitleBn = "হঠাৎ মেজাজ হারানো, পরে নিজেকে দোষারোপ করা এবং হীনম্মন্যতায় ভোগা।",
    subtitleEn = "Sudden irritability, self-blame, shame, and feeling inadequate.",
    tagBn = "আবেগ ও আত্মবিশ্বাস",
    tagEn = "Emotion & Self-Worth"
  )
)

private fun buildClinicalQuestionsForProblem(problemTitle: String): List<PsychClinicalStepQuestion> {
  return listOf(
    PsychClinicalStepQuestion(
      stepNumber = 1,
      stepLabelBn = "ধাপ ১/৫ • প্রধান লক্ষণ ও অনুভূতির ধরন",
      stepLabelEn = "Step 1/5 • Primary Symptom & Emotional Pattern",
      questionBn = "আসসালামু আলাইকুম, আমি ড. সিফাত খান। আপনি \"$problemTitle\" বিষয়টি নিয়ে সেশন শুরু করেছেন। এই মুহূর্তে আপনার মনের ভেতর সবচেয়ে তীব্রভাবে কোন অনুভূতি বা লক্ষণটি কাজ করছে?",
      questionEn = "Hello, I am Dr. Sifat Khan. You started this session regarding \"$problemTitle\". Right now, which symptom or feeling is affecting you most intensely?",
      optionsBn = listOf(
        "সারাক্ষণ মনের ভেতর অস্থিরতা ও ভয় কাজ করে",
        "ভেতর থেকে তীব্র শূন্যতা, মন খারাপ ও কান্না পায়",
        "মাথায় একই চিন্তা বারবার ঘুরে আসে, থামাতে পারি না",
        "প্রচণ্ড মানসিক ক্লান্তি ও কোনো কিছুতেই উৎসাহ পাই না"
      ),
      optionsEn = listOf(
        "Constant inner restlessness, fear, or tension",
        "Deep emptiness, sadness, and urge to cry",
        "Repetitive racing thoughts I cannot switch off",
        "Severe mental exhaustion and zero motivation"
      )
    ),
    PsychClinicalStepQuestion(
      stepNumber = 2,
      stepLabelBn = "ধাপ ২/৫ • সমস্যার সময়কাল ও ট্রিগার",
      stepLabelEn = "Step 2/5 • Duration & Triggering Factors",
      questionBn = "এই মানসিক অবস্থাটি আপনি কতদিন ধরে অনুভব করছেন এবং বিশেষ কোনো ঘটনা বা পরিস্থিতির পর কি এটি বেশি বেড়ে যায়?",
      questionEn = "How long have you been experiencing this state, and does it intensify after specific situations or triggers?",
      optionsBn = listOf(
        "গত কয়েক সপ্তাহ ধরে, বিশেষ করে একা থাকলে বা রাতে বাড়ে",
        "কয়েক মাস ধরে চলছে, পড়াশোনা বা ক্যারিয়ারের চাপে বাড়ে",
        "নির্দিষ্ট একটি সম্পর্ক বা কষ্টকর ঘটনার পর থেকে শুরু হয়েছে",
        "দীর্ঘদিন ধরেই মাঝে মাঝে এমন হয়, এখন প্রায় প্রতিদিনই হচ্ছে"
      ),
      optionsEn = listOf(
        "For the past few weeks, especially worse at night or when alone",
        "For several months, triggered by academic/career pressure",
        "Started after a specific relationship conflict or painful event",
        "On and off for a long time, but now happening almost daily"
      )
    ),
    PsychClinicalStepQuestion(
      stepNumber = 3,
      stepLabelBn = "ধাপ ৩/৫ • ঘুম ও শারীরিক প্রভাব",
      stepLabelEn = "Step 3/5 • Sleep, Appetite & Somatic Impact",
      questionBn = "একজন সাইকিয়াট্রিস্ট হিসেবে জানতে চাই—এই মানসিক চাপের কারণে আপনার ঘুম, খাদ্যাভ্যাস বা শরীরে কোনো পরিবর্তন (যেমন বুক ধড়ফড়, মাথাব্যথা, অবসাদ) লক্ষ্য করছেন কি?",
      questionEn = "As your psychiatrist, I want to check your physical markers—how is this affecting your sleep, appetite, or body (e.g., palpitations, headaches, fatigue)?",
      optionsBn = listOf(
        "রাতে সহজে ঘুম আসে না বা অনেক রাত পর্যন্ত জেগে থাকতে হয়",
        "বুক ধড়ফড় করে, শ্বাস নিতে চাপ লাগে বা মাথাব্যথা হয়",
        "ঘুম হলেও সকালে ওঠার পর শরীর ও মন প্রচণ্ড ক্লান্ত লাগে",
        "খাবারের রুচি কমে গেছে এবং শরীরে কোনো শক্তি পাই না"
      ),
      optionsEn = listOf(
        "Difficulty falling asleep or staying awake late into the night",
        "Heart palpitations, chest tightness, or frequent headaches",
        "Waking up exhausted even after sleeping",
        "Reduced appetite and constant low physical energy"
      )
    ),
    PsychClinicalStepQuestion(
      stepNumber = 4,
      stepLabelBn = "ধাপ ৪/৫ • দৈনন্দিন জীবন ও চিন্তার প্যাটার্ন",
      stepLabelEn = "Step 4/5 • Cognitive Pattern & Daily Functioning",
      questionBn = "যখন এই সমস্যাটি তীব্র হয়, তখন নিজের বা ভবিষ্যৎ সম্পর্কে আপনার মনে কোন ধরনের চিন্তা সবচেয়ে বেশি আসে এবং দৈনন্দিন কাজে এর প্রভাব কতটুকু?",
      questionEn = "When this issue peaks, what kind of thoughts about yourself or the future dominate your mind, and how much does it disrupt daily work?",
      optionsBn = listOf(
        "মনে হয় আমার দ্বারা কিছুই হবে না বা ভবিষ্যৎ অন্ধকার",
        "অতীতের ভুল ও কষ্টের স্মৃতি মনে পড়ে নিজেকে দোষী মনে হয়",
        "কাজে বা পড়াশোনায় একেবারেই মনোযোগ ধরে রাখতে পারছি না",
        "মানুষের সাথে কথা বলতে বা মিশতে ইচ্ছা করে না, নিজেকে গুটিয়ে নিই"
      ),
      optionsEn = listOf(
        "I feel hopeless about the future or fear I will fail",
        "Past regrets and painful memories make me blame myself",
        "I cannot focus on studies or daily responsibilities at all",
        "I withdraw from people and avoid social interaction"
      )
    ),
    PsychClinicalStepQuestion(
      stepNumber = 5,
      stepLabelBn = "ধাপ ৫/৫ • বর্তমান মোকাবিলা ও সেশনের লক্ষ্য",
      stepLabelEn = "Step 5/5 • Current Coping & Therapeutic Goal",
      questionBn = "সেশনের শেষ ধাপ: বর্তমানে মন খুব খারাপ বা অস্থির হলে আপনি কী করেন, এবং আজকের এই সাইকোলজিকাল সেশন থেকে আপনি সবচেয়ে বেশি কোন পরিবর্তনটি চান?",
      questionEn = "Final step: How do you currently cope when distress peaks, and what primary relief or change are you hoping for from today's session?",
      optionsBn = listOf(
        "নীরবে সহ্য করি; আমি মনের অস্থিরতা ও অতিরিক্ত চিন্তা কমাতে চাই",
        "ফোন চালিয়ে ভুলে থাকার চেষ্টা করি; আমি স্বাভাবিক ঘুম ও মনোযোগ চাই",
        "কান্না করি বা একা থাকি; আমি মানসিক ট্রমা ও বিষণ্ণতা কাটিয়ে উঠতে চাই",
        "সুনির্দিষ্ট ডেইলি রুটিন ও থেরাপি গাইডলাইন চাই যা প্রতিদিন মেনে চলব"
      ),
      optionsEn = listOf(
        "Ibottle it up silently; I want to calm my anxiety and racing thoughts",
        "I distract myself with my phone; I want healthy sleep and focus back",
        "I cry or isolate myself; I want to heal from sadness and trauma",
        "I want a structured daily routine and practical therapy prescription"
      )
    )
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PsychologicalSessionSection(
  language: AppLanguage,
  savedSessions: List<PsychAssessment> = emptyList(),
  onSaveSessionReport: (String, String) -> Unit = { _, _ -> },
  onDeleteSessionReport: (String) -> Unit = {},
  onOpenLiveChatbot: () -> Unit = {}
) {
  val isDark = LocalIsDarkTheme.current
  val scope = rememberCoroutineScope()

  // Session State:
  // selectedProblemTitle == null -> Stage 1 (Problem Selection)
  // currentStepIndex in 0..4 and prescriptionText == null -> Stage 2 (Step-by-step Psychiatrist Q&A)
  // prescriptionText != null or isGeneratingPrescription -> Stage 3 (Final Prescription & Guideline)
  var selectedProblemTitle by rememberSaveable { mutableStateOf<String?>(null) }
  var customProblemInput by rememberSaveable { mutableStateOf("") }
  var currentStepIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentCustomAnswer by rememberSaveable { mutableStateOf("") }
  var selectedOptionForCurrentStep by rememberSaveable { mutableStateOf("") }

  val recordedQuestions = remember { mutableStateListOf<String>() }
  val recordedAnswers = remember { mutableStateListOf<String>() }

  var isGeneratingPrescription by rememberSaveable { mutableStateOf(false) }
  var generatedPrescription by rememberSaveable { mutableStateOf<String?>(null) }

  val resetSession: () -> Unit = {
    selectedProblemTitle = null
    customProblemInput = ""
    currentStepIndex = 0
    currentCustomAnswer = ""
    selectedOptionForCurrentStep = ""
    recordedQuestions.clear()
    recordedAnswers.clear()
    isGeneratingPrescription = false
    generatedPrescription = null
  }

  // STAGE 1: Problem Selection Screen
  if (selectedProblemTitle == null) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .testTag("psych_session_problem_selection_list"),
      contentPadding = PaddingValues(top = 6.dp, bottom = 28.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 24.dp,
          isHighlighted = true
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              StylishApronDoctorAvatar(
                modifier = Modifier.size(52.dp),
                isCircular = true
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Surface(
                  shape = RoundedCornerShape(50),
                  color = DeepForestEmerald,
                  border = BorderStroke(1.dp, Color(0xFF5EEAD4).copy(alpha = 0.45f))
                ) {
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "এআই সাইকিয়াট্রিস্ট সেশন • ড. সিফাত খান"
                    } else {
                      "AI Psychiatrist Session • Dr. Sifat Khan"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                  text = if (language == AppLanguage.BN) {
                    "সাইকোলজিকাল সেশন (Psychological Session)"
                  } else {
                    "Clinical Psychological Session"
                  },
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = if (language == AppLanguage.BN) {
                "একজন সাইকিয়াট্রিস্ট যেভাবে ধাপে ধাপে রোগীর সমস্যা শোনেন ও বিশ্লেষণ করেন—এই সেশনে প্রথমে আপনার প্রধান মানসিক সমস্যাটি নির্বাচন করুন। এরপর ৫টি ক্লিনিক্যাল ধাপে প্রশ্নোত্তরের মাধ্যমে সেশন শেষে আপনার জন্য পূর্ণাঙ্গ মনস্তাত্ত্বিক প্রেসক্রিপশন ও গাইডলাইন তৈরি করা হবে।"
              } else {
                "Just like a structured psychiatrist consultation—first select your primary concern below. Dr. Sifat Khan will guide you through 5 progressive clinical steps and generate a personalized therapeutic prescription and daily guideline at the end."
              },
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 18.5.sp
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      item {
        Text(
          text = if (language == AppLanguage.BN) {
            "ধাপ ১: আপনার প্রধান মানসিক সমস্যাটি নির্বাচন করুন"
          } else {
            "Step 1: Select Your Primary Psychological Concern"
          },
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
      }

      itemsIndexed(sessionProblemCategories) { index, category ->
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.80f else 0.75f),
          border = BorderStroke(
            width = 1.1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.65f else 0.55f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
              recordedQuestions.clear()
              recordedAnswers.clear()
              currentStepIndex = 0
              selectedOptionForCurrentStep = ""
              currentCustomAnswer = ""
              generatedPrescription = null
              selectedProblemTitle = category.title(language)
            }
            .testTag("psych_session_problem_card_${category.id}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Psychology,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Surface(
                  shape = RoundedCornerShape(50),
                  color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
                ) {
                  Text(
                    text = category.tag(language),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.5.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = category.title(language),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = category.subtitle(language),
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.2.sp,
                    lineHeight = 16.5.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = CircleShape,
              color = DeepForestEmerald,
              modifier = Modifier.size(34.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(17.dp)
                )
              }
            }
          }
        }
      }

      // Custom problem input option
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 20.dp
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (language == AppLanguage.BN) {
                "অথবা আপনার নিজের ভাষায় সমস্যাটি লিখে সেশন শুরু করুন"
              } else {
                "Or Describe Your Own Specific Issue to Start Session"
              },
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = customProblemInput,
              onValueChange = { customProblemInput = it },
              placeholder = {
                Text(
                  text = if (language == AppLanguage.BN) {
                    "যেমন: পরীক্ষার ভয়, পারিবারিক চাপ, একাকীত্ব..."
                  } else {
                    "e.g., Exam fear, family stress, social anxiety..."
                  },
                  fontSize = 13.sp
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("psych_session_custom_problem_input"),
              shape = RoundedCornerShape(14.dp),
              singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = {
                val trimmed = customProblemInput.trim()
                if (trimmed.isNotEmpty()) {
                  recordedQuestions.clear()
                  recordedAnswers.clear()
                  currentStepIndex = 0
                  selectedOptionForCurrentStep = ""
                  currentCustomAnswer = ""
                  generatedPrescription = null
                  selectedProblemTitle = trimmed
                }
              },
              enabled = customProblemInput.trim().isNotEmpty(),
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = DeepForestEmerald),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("psych_session_start_custom_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (language == AppLanguage.BN) "সেশন শুরু করুন" else "Start Clinical Session",
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Saved Psychological Session Reports History Section (with Dr. Sifat Khan's Analysis & Direct Delete Icon)
      item {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = if (language == AppLanguage.BN) {
            "সংরক্ষিত সাইকোলজিকাল সেশন রিপোর্ট (${savedSessions.size})"
          } else {
            "Saved Psychological Session Reports (${savedSessions.size})"
          },
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = Color(0xFFF0FDFA)
        )
      }

      if (savedSessions.isEmpty()) {
        item {
          GradientGlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 18.dp
          ) {
            Text(
              text = if (language == AppLanguage.BN) {
                "এখনো কোনো সাইকোলজিকাল সেশন রিপোর্ট সংরক্ষিত হয়নি। ওপরে একটি সমস্যা নির্বাচন করে ৫-ধাপের সেশন সম্পন্ন করলে ড. সিফাত খানের বিশ্লেষণসহ রিপোর্ট এখানে সংরক্ষিত থাকবে।"
              } else {
                "No psychological session reports saved yet. Complete a 5-step session above to save Dr. Sifat Khan's analysis and prescription here."
              },
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      } else {
        items(savedSessions, key = { it.id.ifEmpty { it.hashCode().toString() } }) { sessionItem ->
          SavedPsychSessionReportCard(
            item = sessionItem,
            language = language,
            onDelete = { onDeleteSessionReport(sessionItem.id) }
          )
        }
      }
    }
    return
  }

  val activeProblem = selectedProblemTitle ?: ""
  val questions = remember(activeProblem) { buildClinicalQuestionsForProblem(activeProblem) }

  // STAGE 3: Final Prescription / Guideline Screen
  if (isGeneratingPrescription || generatedPrescription != null) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .testTag("psych_session_prescription_view"),
      contentPadding = PaddingValues(top = 6.dp, bottom = 28.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        GradientGlassCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 24.dp,
          isHighlighted = true
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                StylishApronDoctorAvatar(
                  modifier = Modifier.size(48.dp),
                  isCircular = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "Rx • মনস্তাত্ত্বিক প্রেসক্রিপশন ও গাইডলাইন"
                    } else {
                      "Rx • Psychological Prescription & Guideline"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = if (language == AppLanguage.BN) {
                      "ড. সিফাত খান • ক্লিনিক্যাল সাইকোলজিকাল সেশন রিপোর্ট"
                    } else {
                      "Dr. Sifat Khan • Clinical Session Evaluation"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.MedicalServices,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (language == AppLanguage.BN) {
                    "নির্বাচিত সমস্যা: $activeProblem"
                  } else {
                    "Evaluated Concern: $activeProblem"
                  },
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  ),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
          }
        }
      }

      if (isGeneratingPrescription) {
        item {
          GradientGlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 22.dp
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(38.dp)
              )
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = if (language == AppLanguage.BN) {
                  "ড. সিফাত খান আপনার ৫টি ধাপের উত্তর গভীরভাবে বিশ্লেষণ করে মনস্তাত্ত্বিক প্রেসক্রিপশন ও গাইডলাইন তৈরি করছেন..."
                } else {
                  "Dr. Sifat Khan is analyzing your 5 clinical responses to prepare your personalized psychological prescription..."
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      } else {
        // Main Prescription Content Card
        item {
          GradientGlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            isHighlighted = true
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color(0xFF22C55E),
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (language == AppLanguage.BN) {
                    "আপনার জন্য কাস্টমাইজড থেরাপিউটিক গাইডলাইন"
                  } else {
                    "Your Personalized Therapeutic Guideline"
                  },
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.5.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(12.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = generatedPrescription.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 13.8.sp,
                  lineHeight = 21.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("psych_session_prescription_text")
              )
            }
          }
        }

        // Session Q&A Summary Accordion/Card
        item {
          GradientGlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = if (language == AppLanguage.BN) {
                  "সেশনের প্রশ্নোত্তর সারসংক্ষেপ (৫টি ধাপ)"
                } else {
                  "Session Clinical Q&A Summary (5 Steps)"
                },
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(8.dp))
              recordedAnswers.forEachIndexed { idx, ans ->
                Text(
                  text = if (language == AppLanguage.BN) {
                    "• ধাপ ${idx + 1}: $ans"
                  } else {
                    "• Step ${idx + 1}: $ans"
                  },
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.2.sp,
                    lineHeight = 17.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(vertical = 2.dp)
                )
              }
            }
          }
        }

        // Action Buttons: Talk to Dr. Sifat Khan in Live Chat OR Start New Session
        item {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
              onClick = onOpenLiveChatbot,
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = DeepForestEmerald),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("psych_session_open_chat_button")
            ) {
              Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (language == AppLanguage.BN) {
                  "ড. সিফাত খানের সাথে সরাসরি চ্যাটে বিস্তারিত কথা বলুন"
                } else {
                  "Discuss Further in Live Chat with Dr. Sifat Khan"
                },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
              )
            }

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { resetSession() }
                .testTag("psych_session_restart_button")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Refresh,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (language == AppLanguage.BN) {
                    "নতুন সাইকোলজিকাল সেশন শুরু করুন"
                  } else {
                    "Start a New Psychological Session"
                  },
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                  ),
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }

        // Also show Saved Session Reports below the completed report so user can view/delete right away
        if (savedSessions.isNotEmpty()) {
          item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (language == AppLanguage.BN) {
                "সংরক্ষিত সাইকোলজিকাল সেশন রিপোর্ট (${savedSessions.size})"
              } else {
                "Saved Psychological Session Reports (${savedSessions.size})"
              },
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = Color(0xFFF0FDFA)
            )
          }

          items(savedSessions, key = { it.id.ifEmpty { it.hashCode().toString() } }) { sessionItem ->
            SavedPsychSessionReportCard(
              item = sessionItem,
              language = language,
              onDelete = { onDeleteSessionReport(sessionItem.id) }
            )
          }
        }
      }
    }
    return
  }

  // STAGE 2: Step-by-Step Psychiatrist Clinical Q&A (Steps 1 to 5)
  val safeStepIdx = currentStepIndex.coerceIn(0, questions.lastIndex)
  val currentQuestion = questions[safeStepIdx]
  val progressFraction = (safeStepIdx + 1).toFloat() / questions.size.toFloat()

  val submitCurrentStepAnswer: (String) -> Unit = { finalAnswer ->
    val cleanAnswer = finalAnswer.trim()
    if (cleanAnswer.isNotEmpty()) {
      recordedQuestions.add(currentQuestion.question(language))
      recordedAnswers.add(cleanAnswer)
      selectedOptionForCurrentStep = ""
      currentCustomAnswer = ""

      if (safeStepIdx < questions.lastIndex) {
        currentStepIndex = safeStepIdx + 1
      } else {
        // Completed all 5 steps -> Generate AI Psychiatrist Prescription & Automatically Save Report
        val qaPairs = recordedQuestions.zip(recordedAnswers)
        isGeneratingPrescription = true
        scope.launch {
          val prescription = GeminiPsychiatristService.generateStructuredPsychSessionPrescription(
            problemTitle = activeProblem,
            stepQaPairs = qaPairs,
            language = language
          )
          generatedPrescription = prescription
          isGeneratingPrescription = false
          onSaveSessionReport(activeProblem, prescription)
        }
      }
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("psych_session_qa_step_view"),
    contentPadding = PaddingValues(top = 6.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Top Progress & Session Topic Header
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = currentQuestion.stepLabel(language),
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              ),
              color = MaterialTheme.colorScheme.primary
            )

            Text(
              text = if (language == AppLanguage.BN) "পরিবর্তন করুন" else "Change Topic",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { resetSession() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .testTag("psych_session_change_topic_button")
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
              .fillMaxWidth()
              .height(7.dp)
              .clip(RoundedCornerShape(50)),
            color = DeepForestEmerald,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (language == AppLanguage.BN) {
              "বিষয়: $activeProblem"
            } else {
              "Topic: $activeProblem"
            },
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }

    // Active Psychiatrist Question Card
    item {
      GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        isHighlighted = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            StylishApronDoctorAvatar(
              modifier = Modifier.size(44.dp),
              isCircular = true
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (language == AppLanguage.BN) {
                  "ড. সিফাত খান (এআই সাইকিয়াট্রিস্ট)"
                } else {
                  "Dr. Sifat Khan (AI Psychiatrist)"
                },
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (language == AppLanguage.BN) {
                  "ক্লিনিক্যাল মূল্যায়ন প্রশ্ন • ধাপ ${safeStepIdx + 1}"
                } else {
                  "Clinical Assessment Question • Step ${safeStepIdx + 1}"
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = currentQuestion.question(language),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 14.5.sp,
              lineHeight = 21.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = if (language == AppLanguage.BN) {
              "নিচের যেকোনো একটি উত্তর নির্বাচন করুন অথবা নিজের ভাষায় লিখুন:"
            } else {
              "Select an option below or write in your own words:"
            },
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(8.dp))

          currentQuestion.options(language).forEachIndexed { optIdx, optionText ->
            val isSelected = selectedOptionForCurrentStep == optionText
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f)
              } else {
                MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.78f else 0.72f)
              },
              border = BorderStroke(
                width = if (isSelected) 1.6.dp else 1.dp,
                color = if (isSelected) {
                  MaterialTheme.colorScheme.primary
                } else {
                  MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable {
                  selectedOptionForCurrentStep = optionText
                  submitCurrentStepAnswer(optionText)
                }
                .testTag("psych_session_option_$optIdx")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = optionText,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.2.sp,
                    lineHeight = 18.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                )
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(17.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = currentCustomAnswer,
            onValueChange = { currentCustomAnswer = it },
            placeholder = {
              Text(
                text = if (language == AppLanguage.BN) {
                  "অথবা আপনার অনুভূতির কথা নিজের ভাষায় এখানে লিখুন..."
                } else {
                  "Or write your response in your own words here..."
                },
                fontSize = 12.8.sp
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("psych_session_custom_answer_input"),
            shape = RoundedCornerShape(14.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = {
              val ans = currentCustomAnswer.trim().ifEmpty { selectedOptionForCurrentStep }
              submitCurrentStepAnswer(ans)
            },
            enabled = currentCustomAnswer.trim().isNotEmpty() || selectedOptionForCurrentStep.isNotEmpty(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepForestEmerald),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("psych_session_next_step_button")
          ) {
            Text(
              text = if (safeStepIdx < questions.lastIndex) {
                if (language == AppLanguage.BN) "উত্তর জমা দিন ও পরবর্তী ধাপে যান" else "Submit & Next Step"
              } else {
                if (language == AppLanguage.BN) "সেশন সম্পন্ন করুন ও প্রেসক্রিপশন দেখুন" else "Complete Session & Get Prescription"
              },
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SavedPsychSessionReportCard(
  item: PsychAssessment,
  language: AppLanguage,
  onDelete: () -> Unit
) {
  val dateStr = remember(item.createdAt) {
    val ts = item.createdAt ?: Timestamp.now()
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(ts.toDate())
  }

  GradientGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("saved_psych_session_card_${item.id}"),
    cornerRadius = 20.dp
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
            text = item.severityLabel.ifBlank {
              if (language == AppLanguage.BN) "সাইকোলজিকাল সেশন" else "Psychological Session"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        // Direct Delete Icon Button on each Saved Session Report Card (matching Mood Journal)
        if (item.id.isNotBlank()) {
          IconButton(
            onClick = onDelete,
            modifier = Modifier
              .size(36.dp)
              .testTag("delete_psych_session_${item.id}")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = if (language == AppLanguage.BN) "সেশন রিপোর্ট মুছে ফেলুন" else "Delete Session Report",
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
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = if (language == AppLanguage.BN) {
              "Rx • ক্লিনিক্যাল সেশন গাইডলাইন"
            } else {
              "Rx • Clinical Session Guideline"
            },
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Text(
          text = dateStr,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          softWrap = false
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Highlighted Dr. Sifat Khan's Analysis & Prescription Box inside Saved Session Card
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.74f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            DoctorSifatAvatar(modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.BN) {
                "ড. সিফাত খানের বিশ্লেষণ ও প্রেসক্রিপশন"
              } else {
                "Dr. Sifat Khan's Analysis & Prescription"
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
            text = item.recommendation,
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
