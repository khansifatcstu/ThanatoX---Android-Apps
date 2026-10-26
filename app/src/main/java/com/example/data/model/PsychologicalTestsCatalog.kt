package com.example.data.model

import com.example.ui.i18n.AppLanguage

data class PsychChoiceOption(
  val score: Int,
  val textBn: String,
  val textEn: String
) {
  fun label(language: AppLanguage): String =
    if (language == AppLanguage.BN) textBn else textEn
}

data class PsychTestQuestion(
  val id: Int,
  val dimensionBn: String,
  val dimensionEn: String,
  val questionBn: String,
  val questionEn: String,
  val options: List<PsychChoiceOption>
) {
  fun question(language: AppLanguage): String =
    if (language == AppLanguage.BN) questionBn else questionEn

  fun dimension(language: AppLanguage): String =
    if (language == AppLanguage.BN) dimensionBn else dimensionEn
}

data class PsychConditionAnalysis(
  val testKey: String,
  val testTitle: String,
  val normalizedScore: Int,
  val rawScore: Int,
  val maxRawScore: Int,
  val conditionName: String,
  val conditionSummary: String,
  val deepExplanation: String,
  val keyPatterns: List<String>,
  val actionableSteps: List<String>,
  val aiPersonalAnalysis: String? = null
)

data class PsychologicalTestDefinition(
  val key: String,
  val titleBn: String,
  val titleEn: String,
  val shortTitleBn: String,
  val shortTitleEn: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val defaultBadgeBn: String,
  val defaultBadgeEn: String,
  val questions: List<PsychTestQuestion>,
  val category: TestCategory = TestCategory.PSYCHOLOGICAL_TESTS,
  val maxPossibleScore: Int = 21
) {
  fun title(language: AppLanguage): String =
    if (language == AppLanguage.BN) titleBn else titleEn

  fun shortTitle(language: AppLanguage): String =
    if (language == AppLanguage.BN) shortTitleBn else shortTitleEn

  fun subtitle(language: AppLanguage): String =
    if (language == AppLanguage.BN) subtitleBn else subtitleEn

  fun defaultBadge(language: AppLanguage): String =
    if (language == AppLanguage.BN) defaultBadgeBn else defaultBadgeEn
}

object PsychologicalTestsCatalog {

  val clinicalTests: List<PsychologicalTestDefinition> = ClinicalScalesCatalog.clinicalTests

  val psychologicalTests: List<PsychologicalTestDefinition> = listOf(
    // 1. মনের অবসাদ ও ভেতরের একাকীত্ব পরীক্ষা (Inner Loneliness & Emotional Drain Test)
    PsychologicalTestDefinition(
      key = "emotional_drain",
      titleBn = "মনের অবসাদ ও ভেতরের একাকীত্ব পরীক্ষা",
      titleEn = "Inner Loneliness & Emotional Drain Test",
      shortTitleBn = "অবসাদ ও একাকীত্ব",
      shortTitleEn = "Emotional Drain",
      subtitleBn = "মানুষের ভিড়েও একা লাগা, আবেগীয় ক্লান্তি এবং মনের না বলা শূন্যতা মূল্যায়ন।",
      subtitleEn = "Evaluate hidden emotional exhaustion, feeling misunderstood, and inner emptiness.",
      defaultBadgeBn = "৭টি মনস্তাত্ত্বিক প্রশ্ন",
      defaultBadgeEn = "7 Psychologist Questions",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "মানসিক বিচ্ছিন্নতা",
          dimensionEn = "Emotional Disconnection",
          questionBn = "বন্ধুবান্ধব বা পরিবারের সবার সাথে বসে কথা বলার সময়ও কি আপনার হঠাৎ মনে হয় আপনি ভেতর থেকে সম্পূর্ণ একা?",
          questionEn = "Even when surrounded by friends or family, do you suddenly feel completely alone inside?",
          options = listOf(
            PsychChoiceOption(0, "না, সবার সাথে থাকলে আমার মন প্রফুল্ল ও সংযুক্ত থাকে", "No, I feel connected and present around others"),
            PsychChoiceOption(1, "মাঝে মাঝে মন খারাপ থাকলে এমন মনে হয়", "Sometimes, mainly when I am already feeling low"),
            PsychChoiceOption(2, "প্রায়ই মনে হয় আমার আসল অনুভূতি কেউ জানে না", "Often, I feel no one truly sees my real feelings"),
            PsychChoiceOption(3, "সবসময়ই মনে হয় আমি সবার থেকে আলাদা ও একা", "Almost always, I feel deeply isolated even in a crowd")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "আনন্দহীনতা (Anhedonia)",
          dimensionEn = "Loss of Joy",
          questionBn = "আগে যে কাজগুলো—যেমন গান শোনা, প্রিয় খাবার, ঘোরাঘুরি বা শখের কাজ—আপনাকে আনন্দ দিত, সেগুলো করতে এখন কেমন লাগে?",
          questionEn = "How do you feel now about hobbies, music, or activities that used to bring you genuine joy?",
          options = listOf(
            PsychChoiceOption(0, "আগের মতোই আনন্দ ও উৎসাহ পাই", "I still enjoy them just as much as before"),
            PsychChoiceOption(1, "আগ্রহ কিছুটা কমেছে, তবে করলে ভালো লাগে", "My interest has dipped slightly, though I still try"),
            PsychChoiceOption(2, "করতে ইচ্ছে করে না, জোর করে করতে হয়", "I rarely feel like doing them and have to force myself"),
            PsychChoiceOption(3, "কোনো কিছুতেই আর কোনো আনন্দ বা অনুভূতি কাজ করে না", "Nothing brings me excitement or pleasure anymore")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "আবেগ লুকিয়ে রাখা (Masking)",
          dimensionEn = "Emotional Masking",
          questionBn = "কেউ যখন জিজ্ঞেস করে 'কেমন আছো?', তখন ভেতরে কষ্ট থাকা সত্ত্বেও কি আপনি হাসিমুখে বলেন 'ভালো আছি'?",
          questionEn = "When someone asks how you are, do you smile and say 'I'm fine' even when you are hurting inside?",
          options = listOf(
            PsychChoiceOption(0, "না, কাছের মানুষদের কাছে আমি মনের কথা খুলে বলি", "No, I can openly share how I feel with close ones"),
            PsychChoiceOption(1, "কখনো কখনো পরিস্থিতি বুঝে চুপ থাকি", "Occasionally, depending on the situation"),
            PsychChoiceOption(2, "বেশিরভাগ সময়ই বলি না, কারণ বলে লাভ নেই মনে হয়", "Most of the time, because I feel explaining won't help"),
            PsychChoiceOption(3, "সবসময় নিজের কষ্ট হাসির আড়ালে লুকিয়ে রাখি", "Always, I hide all my pain behind a normal smile")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "মানসিক শক্তির ক্ষয়",
          dimensionEn = "Mental Fatigue",
          questionBn = "রাতে ঘুমানোর পরও সকালে ঘুম থেকে ওঠার সাথে সাথেই কি শরীর ও মনে প্রচণ্ড ক্লান্তি বা ভারী ভাব অনুভব করেন?",
          questionEn = "Even after sleeping at night, do you wake up feeling mentally heavy and exhausted right away?",
          options = listOf(
            PsychChoiceOption(0, "না, ঘুমের পর বেশ সতেজ লাগে", "No, I usually wake up feeling refreshed"),
            PsychChoiceOption(1, "কাজের চাপ বেশি থাকলে মাঝে মাঝে ক্লান্ত লাগে", "Sometimes, when I have had a busy week"),
            PsychChoiceOption(2, "প্রায় প্রতিদিনই সকালে বিছানা ছাড়তে কষ্ট হয়", "Most mornings it feels heavy and hard to start the day"),
            PsychChoiceOption(3, "ঘুমালেও মনের ক্লান্তি একটুও কমে না, সবসময় অবসন্ন লাগে", "Sleep never fixes my mental exhaustion anymore")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "মানসিক ভারাক্রান্ততা",
          dimensionEn = "Daily Overwhelm",
          questionBn = "দৈনন্দিন ছোটখাটো কাজ (যেমন ঘর গোছানো, কারো মেসেজের উত্তর দেওয়া বা গোসল করা) করতে কি এখন পাহাড় সমান কঠিন মনে হয়?",
          questionEn = "Do small daily tasks (like replying to messages or organizing your room) feel exhausting?",
          options = listOf(
            PsychChoiceOption(0, "না, স্বাভাবিক নিয়মেই সব কাজ করতে পারি", "No, I handle daily tasks smoothly"),
            PsychChoiceOption(1, "মাঝে মাঝে আলসেমি বা ক্লান্তি কাজ করে", "Sometimes I procrastinate when tired"),
            PsychChoiceOption(2, "ছোট কাজগুলোতেও অনেক মানসিক শক্তি খরচ হয়", "Even small tasks drain a lot of my mental energy"),
            PsychChoiceOption(3, "সাধারণ কাজগুলো করতেও প্রচণ্ড কষ্ট হয় এবং ফেলে রাখি", "Basic tasks feel overwhelming and I avoid them")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "অব্যক্ত শূন্যতা",
          dimensionEn = "Unspoken Emptiness",
          questionBn = "কোনো নির্দিষ্ট কারণ ছাড়াই কি হঠাৎ বুকের ভেতরটা ফাঁকা লাগে বা নীরবে কান্না পায়?",
          questionEn = "Do you ever feel a sudden hollow ache in your chest or feel like crying without a clear reason?",
          options = listOf(
            PsychChoiceOption(0, "না, এমনটা সাধারণত হয় না", "No, that rarely happens to me"),
            PsychChoiceOption(1, "খুব খারাপ কিছু ঘটলে তখনই শুধু মন খারাপ হয়", "Only when something genuinely upsetting happens"),
            PsychChoiceOption(2, "প্রায়ই একা থাকলে অকারণে মনটা খুব ভারী হয়ে ওঠে", "Frequently when I am alone, a heavy sadness sets in"),
            PsychChoiceOption(3, "খুব ঘনঘন ভেতরটা শূন্য লাগে এবং সব অনুভূতি অবশ মনে হয়", "Very often I feel hollow or emotionally numb inside")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "বোঝাপড়ার অভাব",
          dimensionEn = "Feeling Misunderstood",
          questionBn = "আপনার কি মনে হয় আপনার জীবনের সংগ্রাম বা মনের ভেতরের যুদ্ধটা অন্য কেউ সত্যিকার অর্থে বুঝতে পারছে না?",
          questionEn = "Do you feel that no one truly understands the silent battles you are fighting inside?",
          options = listOf(
            PsychChoiceOption(0, "আমার জীবনে অন্তত একজন মানুষ আছে যে আমাকে বোঝে", "I have at least one person who truly understands me"),
            PsychChoiceOption(1, "সবাই সবকিছু না বুঝলেও কিছুটা সাপোর্ট পাই", "People may not get everything, but I feel some support"),
            PsychChoiceOption(2, "মনে হয় সবাই শুধু ওপরের আমাকে দেখে, ভেতরের কষ্টটা দেখে না", "People only see my surface, not what I carry inside"),
            PsychChoiceOption(3, "মনে হয় এই পৃথিবীতে আমার মনের ভাষা বোঝার মতো কেউ নেই", "I feel completely unseen and misunderstood by everyone")
          )
        )
      )
    ),

    // 2. অতিরিক্ত চিন্তা ও মানসিক অস্থিরতা পরীক্ষা (Overthinking & Restlessness Test)
    PsychologicalTestDefinition(
      key = "overthinking_anxiety",
      titleBn = "অতিরিক্ত চিন্তা ও মানসিক অস্থিরতা পরীক্ষা",
      titleEn = "Overthinking & Inner Restlessness Test",
      shortTitleBn = "অতিরিক্ত চিন্তা",
      shortTitleEn = "Overthinking",
      subtitleBn = "রাতের বেলা মাথার ভেতর অবিরাম চিন্তা, অজানা ভয় এবং সিদ্ধান্তহীনতার মনস্তাত্ত্বিক পরীক্ষা।",
      subtitleEn = "Assess racing thoughts at night, worst-case thinking, and inner restlessness.",
      defaultBadgeBn = "৭টি মনস্তাত্ত্বিক প্রশ্ন",
      defaultBadgeEn = "7 Psychologist Questions",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "রাতের চিন্তার লুপ",
          dimensionEn = "Nighttime Racing Mind",
          questionBn = "রাতে ঘুমানোর জন্য চোখ বন্ধ করলে কি সারাদিনের কথা, পুরনো স্মৃতি বা ভবিষ্যতের চিন্তা সিনেমার মতো মাথায় ঘুরতে থাকে?",
          questionEn = "When you close your eyes to sleep, does your mind replay conversations, memories, or future worries nonstop?",
          options = listOf(
            PsychChoiceOption(0, "না, শুয়ে পড়ার কিছুক্ষণের মধ্যেই ঘুমিয়ে পড়ি", "No, my mind calms down and I fall asleep easily"),
            PsychChoiceOption(1, "বিশেষ কোনো টেনশন থাকলে মাঝে মাঝে এমন হয়", "Occasionally, if I have a major event or exam ahead"),
            PsychChoiceOption(2, "প্রায় প্রতিদিনই চিন্তার কারণে ঘুমাতে দেরি হয়", "Most nights my thoughts keep me awake for a long time"),
            PsychChoiceOption(3, "মস্তিষ্ক এক মুহূর্তের জন্যও থামে না, চিন্তা বন্ধ করাই যায় না", "My brain feels like it has no off-switch at night")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "নেতিবাচক আশঙ্কা (Catastrophizing)",
          dimensionEn = "Worst-Case Thinking",
          questionBn = "প্রিয় কেউ ফোন না ধরলে বা ছোট কোনো সমস্যার মুখোমুখি হলে কি আপনার মাথায় সবার আগে সবচেয়ে খারাপ চিন্তাটাই আসে?",
          questionEn = "When someone doesn't reply or a small problem arises, does your mind immediately jump to the worst-case scenario?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি স্বাভাবিকভাবেই বিষয়টা নিই", "No, I stay calm and assume normal reasons"),
            PsychChoiceOption(1, "হালকা চিন্তা হয়, তবে নিজেকে সামলে নিই", "A brief worry crosses my mind, then I rationalize it"),
            PsychChoiceOption(2, "প্রায়ই খারাপ কিছু ঘটেছে ভেবে বুক ধড়ফড় শুরু হয়", "Often, I start panicking that something went wrong"),
            PsychChoiceOption(3, "সবসময়ই সবচেয়ে ভয়াবহ পরিণতির কথা ভেবে তীব্র আতঙ্কে থাকি", "Almost always, I spiral into worst-case scenarios")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "অতীত কথোপকথন বিশ্লেষণ",
          dimensionEn = "Conversation Rumination",
          questionBn = "কারো সাথে কথা বলার পর কি আপনি বারবার চিন্তা করেন—'আমি কি কথাটা ভুল বললাম?' বা 'সে কি কিছু মনে করল?'",
          questionEn = "After talking to someone, do you repeatedly replay the conversation wondering if you said something wrong?",
          options = listOf(
            PsychChoiceOption(0, "না, কথা শেষ হলে তা নিয়ে আর ভাবি না", "No, once a conversation ends, I move on"),
            PsychChoiceOption(1, "গুরুত্বপূর্ণ মিটিং বা কথার পর কখনো কখনো ভাবি", "Only after very important or sensitive conversations"),
            PsychChoiceOption(2, "প্রায়ই নিজের বলা কথাগুলো মনে করে অস্বস্তিতে ভুগি", "Frequently, I dissect my own words and feel uneasy"),
            PsychChoiceOption(3, "প্রতিটি ছোট কথাও ঘণ্টার পর ঘণ্টা মাথায় বিশ্লেষণ করি", "I constantly over-analyze every word and tone for hours")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "অবসরেও অস্থিরতা",
          dimensionEn = "Inability to Relax",
          questionBn = "যখন আপনার হাতে কোনো কাজ থাকে না বা বিশ্রাম নেন, তখনও কি ভেতরে এক ধরনের অস্থিরতা বা অপরাধবোধ কাজ করে?",
          questionEn = "Even when you have free time to rest, do you feel an inner restlessness or guilt that you should be doing something?",
          options = listOf(
            PsychChoiceOption(0, "না, অবসর সময়ে আমি শান্তিতে বিশ্রাম নিতে পারি", "No, I can genuinely relax and enjoy my downtime"),
            PsychChoiceOption(1, "মাঝে মাঝে কাজের চিন্তা মাথায় চলে আসে", "Sometimes work thoughts intrude during rest"),
            PsychChoiceOption(2, "বিশ্রাম নিলেও মনটা শান্ত হয় না, ভেতরে টেনশন চলে", "Even while resting, my mind stays tense and alert"),
            PsychChoiceOption(3, "এক মুহূর্তের জন্যও মানসিক প্রশান্তি বা স্বস্তি পাই না", "I can never fully relax; my nervous system feels constantly on edge")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "সিদ্ধান্তহীনতা ও সংশয়",
          dimensionEn = "Decision Paralysis",
          questionBn = "কোনো সিদ্ধান্ত নেওয়ার সময় কি প্রচণ্ড দ্বিধাদ্বন্দ্ব কাজ করে এবং সিদ্ধান্ত নেওয়ার পরও মনে হয় ভুল হয়ে গেল?",
          questionEn = "Do you struggle with intense self-doubt when making decisions, and second-guess yourself afterward?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি ভেবেচিন্তে আত্মবিশ্বাসের সাথে সিদ্ধান্ত নিই", "No, I decide with reasonable confidence"),
            PsychChoiceOption(1, "বড় সিদ্ধান্তের ক্ষেত্রে কিছুটা চিন্তা হয়", "Only when making major life decisions"),
            PsychChoiceOption(2, "ছোট-বড় সব সিদ্ধান্তেই অনেক সময় নষ্ট হয় ও ভয় লাগে", "Even everyday choices make me anxious and hesitant"),
            PsychChoiceOption(3, "সিদ্ধান্ত নেওয়ার ভয়ে প্রায়ই কাজ আটকে থাকে এবং পরে পস্তাই", "I freeze up over choices and constantly regret them later")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "শারীরিক উদ্বেগ লক্ষণ",
          dimensionEn = "Somatic Anxiety",
          questionBn = "চিন্তা বা চাপের সময় কি আপনার বুক ধড়ফড় করা, হাত-পা ঠান্ডা হওয়া, ঘাড় ব্যথা বা লম্বা শ্বাস নিতে কষ্ট হওয়ার মতো লক্ষণ দেখা দেয়?",
          questionEn = "When you are worried, do you feel physical symptoms like a racing heart, tight chest, shallow breathing, or tense shoulders?",
          options = listOf(
            PsychChoiceOption(0, "না, শারীরিক এমন কোনো অস্বস্তি হয় না", "No, I rarely experience physical tension from worry"),
            PsychChoiceOption(1, "খুব বেশি ভয় পেলে বা চাপে থাকলে হালকা হয়", "Mildly, only during high-stress situations"),
            PsychChoiceOption(2, "প্রায়ই দুশ্চিন্তার সাথে বুক ভারী বা ধড়ফড় করে", "Often, my chest feels tight or my heart races when anxious"),
            PsychChoiceOption(3, "প্রতিদিনই দুশ্চিন্তার কারণে শরীরে তীব্র অস্থিরতা ও ক্লান্তি টের পাই", "Daily, my body feels the physical weight of constant anxiety")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "ভবিষ্যৎ নিয়ন্ত্রণের চেষ্টা",
          dimensionEn = "Future Hyper-Control",
          questionBn = "আগামীকাল বা ভবিষ্যতে কী ঘটবে তা আগে থেকেই শতভাগ নিশ্চিত না হতে পারলে কি আপনার ভেতরে ভয় কাজ করে?",
          questionEn = "Does uncertainty about tomorrow or the future trigger a strong need to mentally prepare for every possibility?",
          options = listOf(
            PsychChoiceOption(0, "না, সময়ের সাথে যা হবে তা মেনে নেওয়ার মানসিকতা আছে", "No, I accept that some things are naturally uncertain"),
            PsychChoiceOption(1, "পরিকল্পনা করতে পছন্দ করি, তবে অতিরিক্ত ভয় পাই না", "I like planning, but uncertainty doesn't overwhelm me"),
            PsychChoiceOption(2, "অনিশ্চয়তা একদম সহ্য করতে পারি না, সব আগে থেকে জানতে চাই", "Uncertainty makes me very uncomfortable and anxious"),
            PsychChoiceOption(3, "ভবিষ্যতের অজানা ভয়ে বর্তমান মুহূর্তে বাঁচতেই ভুলে যাই", "Fear of the unknown consumes my present peace completely")
          )
        )
      )
    ),

    // 3. সম্পর্ক ও ইমোশনাল অ্যাটাচমেন্ট পরীক্ষা (Relationship & Attachment Psychology Test)
    PsychologicalTestDefinition(
      key = "attachment_relationship",
      titleBn = "সম্পর্ক ও ইমোশনাল অ্যাটাচমেন্ট পরীক্ষা",
      titleEn = "Relationship & Attachment Psychology Test",
      shortTitleBn = "সম্পর্ক ও অ্যাটাচমেন্ট",
      shortTitleEn = "Attachment Style",
      subtitleBn = "ভালোবাসা ও বন্ধুত্বে আপনার নির্ভরশীলতা, হারানোর ভয় এবং বিশ্বাসের মনস্তাত্ত্বিক ধরন নির্ণয়।",
      subtitleEn = "Understand your attachment style, fear of abandonment, boundaries, and trust patterns.",
      defaultBadgeBn = "৭টি মনস্তাত্ত্বিক প্রশ্ন",
      defaultBadgeEn = "7 Psychologist Questions",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "হারানোর ভয় (Abandonment Fear)",
          dimensionEn = "Fear of Abandonment",
          questionBn = "আপনার প্রিয় মানুষ বা কাছের বন্ধু যদি হঠাৎ আগের চেয়ে একটু কম কথা বলে বা ব্যস্ত থাকে, তখন আপনার মনে কী চলে?",
          questionEn = "If someone close to you becomes slightly distant or busy, what happens inside your mind?",
          options = listOf(
            PsychChoiceOption(0, "মনে করি সে হয়তো নিজের কাজে ব্যস্ত, এতে আমার খারাপ লাগে না", "I assume they are just busy and feel secure"),
            PsychChoiceOption(1, "একটু খারাপ লাগে, তবে নিজে থেকে খোঁজ নিই", "I notice it slightly, but check in calmly"),
            PsychChoiceOption(2, "মনে হয় আমি কি কোনো ভুল করলাম? সে কি আমাকে অবহেলা করছে?", "I start worrying if I did something wrong or if they lost interest"),
            PsychChoiceOption(3, "প্রচণ্ড অস্থির হয়ে পড়ি এবং মনে হয় সে আমাকে ছেড়ে চলে যাবে", "I feel intense panic and fear that they are abandoning me")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "অন্যকে খুশি রাখা (People-Pleasing)",
          dimensionEn = "Self-Sacrifice & Pleasing",
          questionBn = "সম্পর্ক বা বন্ধুত্ব টিকিয়ে রাখার জন্য কি আপনি নিজের পছন্দ, মতামত বা কষ্টকে চেপে রেখে অন্যের মন জুগিয়ে চলেন?",
          questionEn = "Do you often suppress your own needs or hurt feelings just to keep others happy and avoid conflict?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি শ্রদ্ধার সাথে নিজের মতামত ও প্রয়োজন জানাতে পারি", "No, I express my needs and boundaries respectfully"),
            PsychChoiceOption(1, "কখনো কখনো ঝামেলা এড়াতে ছাড় দিই", "Sometimes I compromise to keep the peace"),
            PsychChoiceOption(2, "প্রায়ই নিজের কষ্ট চেপে রাখি যাতে মানুষটি রাগ না করে", "Often, I silence my own feelings so others won't be upset"),
            PsychChoiceOption(3, "সবসময় অন্যকে খুশি করতে গিয়ে নিজেকে পুরোপুরি হারিয়ে ফেলি", "Always; I prioritize everyone else at the cost of myself")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "বিশ্বাসের দেয়াল (Trust Guard)",
          dimensionEn = "Emotional Vulnerability",
          questionBn = "কারো সাথে ঘনিষ্ঠ হওয়ার সময় কি আপনার মনে ভয় কাজ করে যে—'আমার দুর্বলতা জানলে সে পরে আমাকে আঘাত করবে'?",
          questionEn = "When getting close to someone, do you fear that opening up about your vulnerabilities will eventually be used against you?",
          options = listOf(
            PsychChoiceOption(0, "না, সঠিক মানুষকে আমি মন খুলে বিশ্বাস করতে পারি", "No, I can trust dependable people openly"),
            PsychChoiceOption(1, "সময় নিই, তবে ধীরে ধীরে বিশ্বাস করি", "It takes time, but I open up gradually"),
            PsychChoiceOption(2, "খুব ভয় লাগে, তাই মনের গভীর কথা কাউকে বলি না", "I keep a strong guard up and rarely share deep feelings"),
            PsychChoiceOption(3, "কাউকেই বিশ্বাস করতে পারি না, কেউ কাছে এলেই নিজেকে গুটিয়ে নিই", "I pull away whenever someone gets too close to protect myself")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "মানসিক নির্ভরশীলতা",
          dimensionEn = "Emotional Dependency",
          questionBn = "আপনার সারাদিনের মেজাজ বা ভালো থাকা কি একটি নির্দিষ্ট মানুষের ব্যবহার বা মেসেজের ওপর পুরোপুরি নির্ভর করে?",
          questionEn = "Does your entire mood for the day depend heavily on how one specific person treats or texts you?",
          options = listOf(
            PsychChoiceOption(0, "না, আমার মানসিক শান্তি আমার নিজের নিয়ন্ত্রণেও থাকে", "No, my emotional balance remains relatively steady on my own"),
            PsychChoiceOption(1, "প্রিয় মানুষের ব্যবহার প্রভাব ফেলে, তবে কাজকর্ম থেমে থাকে না", "It affects me somewhat, but doesn't derail my whole day"),
            PsychChoiceOption(2, "সে একটু অন্যরকম আচরণ করলেই আমার পুরো দিনটা নষ্ট হয়ে যায়", "A slight shift in their tone ruins my entire day"),
            PsychChoiceOption(3, "তার একটু মনোযোগ ছাড়া আমি মানসিকভাবে অচল হয়ে পড়ি", "My emotional survival feels completely tied to their attention")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "সীমারেখা ও না বলা (Boundaries)",
          dimensionEn = "Setting Boundaries",
          questionBn = "কেউ যদি আপনার সাথে অন্যায় আচরণ করে বা অতিরিক্ত চাপ দেয়, আপনি কি সরাসরি তাকে 'না' বলতে পারেন?",
          questionEn = "When someone crosses a line or asks for too much, can you firmly say 'no' without feeling guilty?",
          options = listOf(
            PsychChoiceOption(0, "হ্যাঁ, আমি শান্ত কিন্তু দৃঢ়ভাবে নিজের সীমারেখা জানাতে পারি", "Yes, I can set clear boundaries without guilt"),
            PsychChoiceOption(1, "বলতে পারি, তবে পরে একটু খারাপ লাগে", "I can say no, though I feel a little guilty afterward"),
            PsychChoiceOption(2, "মুখের ওপর 'না' বলতে খুব কষ্ট হয়, তাই অনিচ্ছা সত্ত্বেও মেনে নিই", "I struggle to say no and end up agreeing against my will"),
            PsychChoiceOption(3, "কখনোই প্রতিবাদ করতে পারি না, নীরবে কষ্ট সহ্য করি", "I never speak up for myself and silently absorb the hurt")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "মনোমালিন্য ও প্রতিক্রিয়া",
          dimensionEn = "Conflict Response",
          questionBn = "কাছের মানুষের সাথে ঝগড়া বা ভুল বোঝাবুঝি হলে আপনার প্রথম মানসিক প্রতিক্রিয়া কী হয়?",
          questionEn = "When a misunderstanding or conflict happens with someone close, what is your immediate inner reaction?",
          options = listOf(
            PsychChoiceOption(0, "শান্তভাবে কথা বলে সমস্যাটি সমাধান করার চেষ্টা করি", "I communicate calmly to understand and resolve it"),
            PsychChoiceOption(1, "কিছুটা সময় নিই, তারপর কথা বলে মিটিয়ে ফেলি", "I take a short breather, then talk it through"),
            PsychChoiceOption(2, "তাৎক্ষণিকভাবে সমাধান না হওয়া পর্যন্ত তীব্র অস্থিরতায় ছটফট করি", "I feel frantic and anxious until they reassure me immediately"),
            PsychChoiceOption(3, "সম্পূর্ণ চুপ হয়ে যাই এবং মনের দরজা বন্ধ করে দূরে সরে যাই", "I shut down emotionally and distance myself completely")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "ভালোবাসার যোগ্যতা নিয়ে সংশয়",
          dimensionEn = "Feeling Worthy of Love",
          questionBn = "মনের গভীরে কি কখনো মনে হয়—'আমাকে যেভাবে আছি সেভাবে সত্যিকারের ভালোবাসার মতো মানুষ হয়তো কেউ হবে না'?",
          questionEn = "Deep down, do you ever feel that no one will truly love and accept the real, unfiltered version of you?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি বিশ্বাস করি আমি ভালোবাসা ও সম্মানের যোগ্য", "No, I believe I am worthy of genuine love and respect"),
            PsychChoiceOption(1, "মন খারাপ থাকলে কখনো কখনো এমন সন্দেহ উঁকি দেয়", "Rarely, only in moments of deep sadness"),
            PsychChoiceOption(2, "প্রায়ই মনে হয় আমাকে ভালোবাসা কঠিন বা আমি যথেষ্ট ভালো নই", "Often, I feel I am too much or not enough to be loved"),
            PsychChoiceOption(3, "সবসময় মনে হয় কেউ আমার আসল রূপ দেখলে আমাকে ছেড়ে চলে যাবে", "Always; I fear that anyone who knows the real me will leave")
          )
        )
      )
    ),

    // 4. আত্মবিশ্বাস ও নিজেকে মূল্যায়ন পরীক্ষা (Self-Worth & Inner Critic Test)
    PsychologicalTestDefinition(
      key = "self_worth_confidence",
      titleBn = "আত্মবিশ্বাস ও নিজেকে মূল্যায়ন পরীক্ষা",
      titleEn = "Self-Worth & Inner Critic Psychology Test",
      shortTitleBn = "আত্মবিশ্বাস ও মূল্যবোধ",
      shortTitleEn = "Self-Worth",
      subtitleBn = "নিজেকে দোষারোপ করা, অন্যের সাথে তুলনা এবং আত্মবিশ্বাসের প্রকৃত অবস্থা যাচাই।",
      subtitleEn = "Measure self-criticism, social comparison, imposter syndrome, and self-esteem.",
      defaultBadgeBn = "৭টি মনস্তাত্ত্বিক প্রশ্ন",
      defaultBadgeEn = "7 Psychologist Questions",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "আত্ম-সমালোচনা (Inner Critic)",
          dimensionEn = "Harsh Inner Critic",
          questionBn = "আপনার দ্বারা যদি ছোট কোনো ভুল হয়ে যায়, তখন আপনি নিজের সাথে মনে মনে কীভাবে কথা বলেন?",
          questionEn = "When you make a small mistake in life or work, how does your inner voice speak to you?",
          options = listOf(
            PsychChoiceOption(0, "ভুল হতেই পারে মেনে নিয়ে সেখান থেকে শেখার চেষ্টা করি", "I treat myself kindly and view it as a learning step"),
            PsychChoiceOption(1, "একটু মন খারাপ হয়, তবে দ্রুত নিজেকে সামলে নিই", "I feel briefly annoyed, then move forward"),
            PsychChoiceOption(2, "নিজেকে খুব বকাঝকা করি এবং দীর্ঘক্ষণ অপরাধবোধে ভুগি", "I scold myself harshly and replay the mistake for days"),
            PsychChoiceOption(3, "মনে হয় আমি কোনো কাজেরই যোগ্য নই, সবসময় নিজেকে ব্যর্থ ভাবি", "I immediately feel like a complete failure at everything")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "অন্যের সাথে তুলনা",
          dimensionEn = "Social Comparison",
          questionBn = "সোশ্যাল মিডিয়ায় বা আশেপাশে সমবয়সীদের সাফল্য দেখলে আপনার মনের ভেতর কী অনুভূতি তৈরি হয়?",
          questionEn = "When you see peers succeeding on social media or in life, what do you feel inside?",
          options = listOf(
            PsychChoiceOption(0, "অনুপ্রাণিত হই এবং নিজের গতির ওপর ভরসা রাখি", "I feel inspired while trusting my own timeline"),
            PsychChoiceOption(1, "মাঝে মাঝে একটু চিন্তা হয়, তবে হতাশ হই না", "A passing thought of comparison, but I stay grounded"),
            PsychChoiceOption(2, "মনে হয় সবাই এগিয়ে যাচ্ছে, শুধু আমিই পিছিয়ে পড়ে আছি", "I feel like everyone is moving ahead while I am stuck behind"),
            PsychChoiceOption(3, "নিজেকে চরম ব্যর্থ ও মূল্যহীন মনে হয় এবং প্রচণ্ড হীনম্মন্যতায় ভুগি", "I feel deeply inadequate and worthless compared to others")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "প্রশংসা গ্রহণে জড়তা (Imposter Syndrome)",
          dimensionEn = "Imposter Syndrome",
          questionBn = "কেউ যখন আপনার কোনো কাজের বা গুণের প্রশংসা করে, তখন কি আপনার মনে হয় 'আমি আসলে এতটা যোগ্য নই'?",
          questionEn = "When someone praises your work or character, do you secretly feel you don't truly deserve it?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি আনন্দের সাথে প্রশংসা গ্রহণ করি", "No, I accept compliments with gratitude and confidence"),
            PsychChoiceOption(1, "একটু লাজুক লাগে, তবে ভালো লাগে", "I feel a bit shy, but I appreciate it"),
            PsychChoiceOption(2, "মনে হয় তারা হয়তো ভদ্রতা করে বলছে বা আসল সত্যটা জানে না", "I feel they are just being polite or overestimating me"),
            PsychChoiceOption(3, "সবসময় ভয় লাগে যে একদিন সবাই বুঝে যাবে আমি আসলে কতটা অযোগ্য", "I constantly fear people will discover I am not good enough")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "নিজেকে বোঝা মনে করা",
          dimensionEn = "Feeling Like a Burden",
          questionBn = "অসুস্থ হলে বা বিপদে পড়ে কারো কাছে সাহায্য চাইতে গেলে কি মনে হয় আপনি অন্যের ওপর বোঝা হয়ে যাচ্ছেন?",
          questionEn = "When you need help or emotional support, do you hesitate because you feel like a burden to others?",
          options = listOf(
            PsychChoiceOption(0, "না, প্রয়োজনে কাছের মানুষদের কাছে স্বাভাবিকভাবেই সাহায্য চাই", "No, I feel comfortable reaching out when I need help"),
            PsychChoiceOption(1, "একটু ইতস্তত বোধ করি, তবে প্রয়োজন হলে বলি", "I hesitate slightly, but ask when necessary"),
            PsychChoiceOption(2, "কাউকে বিরক্ত করতে চাই না ভেবে নিজের কষ্ট একাই সহ্য করি", "I avoid asking because I don't want to trouble anyone"),
            PsychChoiceOption(3, "সবসময় মনে হয় আমার উপস্থিতি বা সমস্যাই অন্যদের জন্য একটা বোঝা", "I deeply feel that my needs and presence are a burden")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "নতুন কাজে ভয়",
          dimensionEn = "Fear of Failure & Judgment",
          questionBn = "নতুন কোনো উদ্যোগ বা সুযোগ সামনে এলে কি 'আমি পারব না' বা 'মানুষ কী বলবে' ভেবে পিছিয়ে যান?",
          questionEn = "When a new opportunity appears, do you hold yourself back out of fear of failing or being judged?",
          options = listOf(
            PsychChoiceOption(0, "না, ভয় থাকলেও আমি সাহস করে চেষ্টা করি", "No, even if nervous, I step forward and try"),
            PsychChoiceOption(1, "শুরুতে একটু দ্বিধা থাকে, পরে শুরু করি", "I hesitate at first, then push myself to try"),
            PsychChoiceOption(2, "ব্যর্থ হওয়ার ভয়ে অনেক ভালো সুযোগ হাতছাড়া করে ফেলি", "Fear of failure often stops me from taking good opportunities"),
            PsychChoiceOption(3, "নিজের ওপর কোনো ভরসাই পাই না, তাই চেষ্টা করার সাহসই হারিয়ে ফেলেছি", "I have zero faith in my abilities, so I give up before starting")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "নিজের মতামতকে গুরুত্ব দেওয়া",
          dimensionEn = "Self-Trust",
          questionBn = "একটি বিষয়ে আপনার মতামত এবং অন্যের মতামত ভিন্ন হলে কি আপনি সহজেই ধরে নেন যে আপনিই ভুল?",
          questionEn = "When someone disagrees with you, do you immediately assume that you must be the one who is wrong?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি যুক্তি দিয়ে উভয় দিক বিবেচনা করি", "No, I trust my perspective while listening to others"),
            PsychChoiceOption(1, "মাঝে মাঝে নিজের সিদ্ধান্তের ওপর একটু সন্দেহ হয়", "Sometimes I question my judgment briefly"),
            PsychChoiceOption(2, "প্রায়ই নিজের বুদ্ধির চেয়ে অন্যের কথাকেই বেশি সঠিক মনে করি", "I usually doubt my own mind and defer to others"),
            PsychChoiceOption(3, "নিজের বিচারবুদ্ধির ওপর আমার বিন্দুমাত্র আস্থা নেই", "I never trust my own thoughts or instincts")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "আত্ম-গ্রহণযোগ্যতা (Self-Acceptance)",
          dimensionEn = "Self-Compassion",
          questionBn = "দিনশেষে নিজের দিকে তাকালে কি নিজের গুণগুলোর চেয়ে নিজের অপূর্ণতা ও ত্রুটিগুলোই বেশি চোখে পড়ে?",
          questionEn = "At the end of the day, do you focus mostly on your flaws and shortcomings rather than your strengths?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি নিজের ভালো দিক ও প্রচেষ্টাকে সম্মান করি", "No, I appreciate my efforts and good qualities"),
            PsychChoiceOption(1, "মাঝে মাঝে নিজের ত্রুটিগুলো নিয়ে চিন্তা হয়", "Occasionally I dwell on what I could improve"),
            PsychChoiceOption(2, "বেশিরভাগ সময় নিজের খামতিগুলো নিয়েই মন খারাপ করে থাকি", "Most of the time I only see what is lacking in me"),
            PsychChoiceOption(3, "নিজেকে নিয়ে প্রচণ্ড অসন্তুষ্টি ও ঘৃণা কাজ করে", "I feel constant dissatisfaction and harshness toward myself")
          )
        )
      )
    ),

    // 5. অতীত আঘাত ও ইমোশনাল ট্রমা পরীক্ষা (Unhealed Trauma & Emotional Triggers Test)
    PsychologicalTestDefinition(
      key = "unhealed_trauma",
      titleBn = "অতীত আঘাত ও ইমোশনাল ট্রমা পরীক্ষা",
      titleEn = "Unhealed Trauma & Emotional Triggers Test",
      shortTitleBn = "অতীত ট্রমা ও ক্ষত",
      shortTitleEn = "Trauma & Triggers",
      subtitleBn = "অতীতের না শুকানো মানসিক ক্ষত, হঠাৎ ট্রিগার হওয়া এবং আত্মরক্ষামূলক আচরণের গভীর মূল্যায়ন।",
      subtitleEn = "Assess unhealed emotional wounds, sudden triggers, hyper-vigilance, and emotional numbing.",
      defaultBadgeBn = "৭টি মনস্তাত্ত্বিক প্রশ্ন",
      defaultBadgeEn = "7 Psychologist Questions",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "ইমোশনাল ট্রিগার",
          dimensionEn = "Sudden Emotional Triggers",
          questionBn = "কারো গলার উঁচু স্বর, কোনো নির্দিষ্ট কথা বা আচরণ দেখলে কি হঠাৎ আপনার পুরনো কোনো কষ্টের স্মৃতি মনে পড়ে বুক কেঁপে ওঠে?",
          questionEn = "Does a raised voice, a specific tone, or a small gesture suddenly trigger painful memories or intense fear inside you?",
          options = listOf(
            PsychChoiceOption(0, "না, অতীতের কোনো স্মৃতি আমাকে এভাবে নাড়িয়ে দেয় না", "No, past memories rarely trigger sudden fear in me"),
            PsychChoiceOption(1, "খুব বিরল ক্ষেত্রে পুরনো কথা মনে পড়ে মন খারাপ হয়", "Rarely, certain reminders make me briefly sad"),
            PsychChoiceOption(2, "প্রায়ই ছোটখাটো ঘটনায় অতীতের কষ্টের অনুভূতি ফিরে আসে", "Frequently, small triggers bring back old emotional pain"),
            PsychChoiceOption(3, "হঠাৎ প্রচণ্ড ভয়, শ্বাসকষ্ট বা অস্থিরতা শুরু হয় যা নিয়ন্ত্রণ করা কঠিন", "Triggers immediately cause intense panic or emotional flooding")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "অতি-সতর্কতা (Hyper-Vigilance)",
          dimensionEn = "Hyper-Vigilance",
          questionBn = "আপনি কি সবসময় আশেপাশের মানুষের মুখের ভাব, কথা বলার ধরন বা আচরণের সূক্ষ্ম পরিবর্তন খুব সতর্কভাবে খেয়াল করেন যাতে আগেভাগেই বিপদ বুঝতে পারেন?",
          questionEn = "Do you constantly scan people's facial expressions, tone, and mood shifts to protect yourself from getting hurt?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি মানুষের সাথে স্বাভাবিক ও নিশ্চিন্তভাবেই মিশি", "No, I interact naturally without being on high alert"),
            PsychChoiceOption(1, "অপরিচিত পরিবেশে কিছুটা সতর্ক থাকি", "Only slightly when in unfamiliar environments"),
            PsychChoiceOption(2, "প্রায়ই মানুষের মুড বোঝার জন্য ভেতরে ভেতরে তটস্থ থাকি", "Often, I stay on guard watching for signs of anger or change"),
            PsychChoiceOption(3, "সবসময় মস্তিষ্ক হাই-অ্যালার্টে থাকে, মনে হয় যেকোনো মুহূর্তে আঘাত আসবে", "Always; my nervous system is constantly scanning for emotional danger")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "আনন্দেও অজানা ভয়",
          dimensionEn = "Foreboding Joy",
          questionBn = "জীবনে যখন খুব ভালো কিছু ঘটে বা আপনি খুব খুশি থাকেন, তখনও কি মনের ভেতর ভয় কাজ করে যে—'এই সুখ বুঝি বেশিক্ষণ টিকবে না, কিছু একটা খারাপ ঘটবে'?",
          questionEn = "Even when something wonderful happens in your life, do you feel an underlying dread that something bad will soon ruin it?",
          options = listOf(
            PsychChoiceOption(0, "না, খুশির মুহূর্তগুলো আমি মন খুলে উপভোগ করি", "No, I allow myself to fully enjoy happy moments"),
            PsychChoiceOption(1, "কখনো কখনো হালকা চিন্তা উঁকি দেয়", "Occasionally a fleeting worry passes through"),
            PsychChoiceOption(2, "বেশি খুশি হলেই ভয় লাগে যে এরপরই হয়তো বড় কোনো কষ্ট আসবে", "Often, happiness makes me nervous that pain will follow"),
            PsychChoiceOption(3, "ভালো সময়েও আতঙ্কে থাকি, মনে হয় সুখ আমার কপালে সয় না", "I cannot enjoy peace because I am always bracing for the next blow")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "আবেগ অবশ করে রাখা (Emotional Numbing)",
          dimensionEn = "Protective Numbing",
          questionBn = "পুনরায় কষ্ট পাওয়ার ভয়ে কি আপনি নিজের আবেগ, অনুভূতি বা কাউকে গভীরভাবে ভালোবাসার ক্ষমতাকে ভেতর থেকে বন্ধ করে রেখেছেন?",
          questionEn = "To avoid getting hurt again, have you unconsciously shut down your emotions or kept your heart walled off?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি এখনো মানুষকে ভালোবাসতে ও অনুভব করতে পারি", "No, my heart remains open to feeling and connecting"),
            PsychChoiceOption(1, "আগের চেয়ে কিছুটা সাবধানী হয়েছি", "I am more cautious than before, but still feel deeply"),
            PsychChoiceOption(2, "প্রায়ই নিজের অনুভূতিগুলোকে পাথর করে রাখি যাতে কষ্ট না লাগে", "I often numb my feelings so nothing can hurt me deeply"),
            PsychChoiceOption(3, "মনটা পুরোপুরি অবশ হয়ে গেছে—ভালো বা খারাপ কোনো কিছুই আর স্পর্শ করে না", "I feel completely emotionally detached and walled off inside")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "অতীতের অমীমাংসিত ক্ষত",
          dimensionEn = "Unresolved Past Pain",
          questionBn = "অতীতের এমন কোনো ঘটনা, অবহেলা বা বিশ্বাসঘাতকতা কি আছে যা অনেক দিন পার হওয়ার পরও আজও আপনাকে ভেতর থেকে পোড়ায়?",
          questionEn = "Is there a past betrayal, neglect, or painful event that still aches inside you as if it happened yesterday?",
          options = listOf(
            PsychChoiceOption(0, "না, অতীতের কষ্টগুলো আমি সময়ের সাথে কাটিয়ে উঠেছি", "No, I have made peace with my past experiences"),
            PsychChoiceOption(1, "মনে পড়লে খারাপ লাগে, তবে তা বর্তমানকে থামিয়ে রাখে না", "Memories hurt occasionally, but don't control my present"),
            PsychChoiceOption(2, "প্রায়ই সেই পুরনো আঘাতগুলো মনে পড়ে এবং খুব অসহায় লাগে", "Old wounds resurface often and make me feel helpless"),
            PsychChoiceOption(3, "অতীতের সেই ক্ষত আজও আমার প্রতিটি দিন ও বিশ্বাসকে ক্ষতবিক্ষত করে", "That past trauma still haunts my daily thoughts and trust")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "অকারণ অপরাধবোধ (Self-Blame)",
          dimensionEn = "Trauma Self-Blame",
          questionBn = "অতীতে আপনার সাথে ঘটে যাওয়া খারাপ আচরণ বা অন্যায়ের জন্য কি আপনি আজও ঘুরেফিরে নিজেকেই দোষী মনে করেন?",
          questionEn = "Do you still blame yourself for past painful events or mistreatment that was actually not your fault?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি জানি অন্যের ভুলের দায় আমার নয়", "No, I recognize what was not my fault"),
            PsychChoiceOption(1, "কখনো কখনো মনে হয় আমি হয়তো আরও সতর্ক হতে পারতাম", "Sometimes I wish I had known better back then"),
            PsychChoiceOption(2, "প্রায়ই মনে হয় আমারই হয়তো কোনো দোষ ছিল, তাই এমন হয়েছে", "Often I feel it happened because something was wrong with me"),
            PsychChoiceOption(3, "সবসময় তীব্র অপরাধবোধ ও লজ্জার বোঝা বয়ে বেড়াই", "I carry constant heavy guilt and shame over the past")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "মানসিক শাটডাউন (Freeze Response)",
          dimensionEn = "Freeze or Escape Response",
          questionBn = "যখন মানসিক চাপ বা আবেগ খুব তীব্র হয়ে ওঠে, তখন কি আপনার শরীর-মন একদম স্থবির (Freeze) হয়ে যায় এবং সবকিছু ছেড়ে পালিয়ে যেতে ইচ্ছে করে?",
          questionEn = "When emotional stress peaks, does your mind freeze up completely or make you want to disappear from everyone?",
          options = listOf(
            PsychChoiceOption(0, "না, আমি ধৈর্য ধরে পরিস্থিতি মোকাবিলা করার চেষ্টা করি", "No, I try to work through stressful moments step by step"),
            PsychChoiceOption(1, "কিছুক্ষণ একা থাকতে ইচ্ছে করে, তারপর স্বাভাবিক হই", "I need a little quiet time, then I recover"),
            PsychChoiceOption(2, "মাথা পুরোপুরি ফাঁকা বা অবশ হয়ে যায়, কোনো কথা বলতে পারি না", "My mind goes blank and I shut down completely"),
            PsychChoiceOption(3, "সব যোগাযোগ বিচ্ছিন্ন করে নিজেকে সবার আড়ালে সরিয়ে ফেলি", "I feel an overwhelming urge to cut off everyone and vanish")
          )
        )
      )
    )
  )

  val allTests: List<PsychologicalTestDefinition> = clinicalTests + psychologicalTests

  fun testsForCategory(category: TestCategory): List<PsychologicalTestDefinition> =
    when (category) {
      TestCategory.CLINICAL_SCALES -> clinicalTests
      TestCategory.PSYCHOLOGICAL_TESTS -> psychologicalTests
    }

  fun findTestByKey(key: String?): PsychologicalTestDefinition {
    return allTests.find { it.key == key } ?: clinicalTests.first()
  }

  /**
   * Generates a comprehensive, realistic clinical or psychological condition explanation based on
   * the specific test and the user's exact answer pattern.
   */
  fun evaluateCondition(
    test: PsychologicalTestDefinition,
    answers: List<Int>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    val maxRaw = test.maxPossibleScore.coerceAtLeast(1)
    val rawSum = answers.sum().coerceIn(0, maxRaw)
    val normalizedScore = ((rawSum.toDouble() / maxRaw.toDouble()) * 100.0).toInt().coerceIn(0, 100)

    // Identify top dimensions where the user scored >= 2
    val elevatedDimensions = test.questions.mapIndexedNotNull { index, q ->
      val ans = answers.getOrNull(index) ?: 0
      if (ans >= 2) q.dimension(language) else null
    }.distinct()

    val patternSummaryList = if (elevatedDimensions.isNotEmpty()) {
      elevatedDimensions.map { dim ->
        if (language == AppLanguage.BN) {
          "প্রধান লক্ষণ ক্ষেত্র: $dim"
        } else {
          "Key Active Pattern: $dim"
        }
      }
    } else {
      listOf(
        if (language == AppLanguage.BN) {
          "আপনার আবেগীয় ভারসাম্য ও মানসিক সচেতনতা স্থিতিশীল পর্যায়ে রয়েছে"
        } else {
          "Your emotional regulation and self-awareness are in a healthy, balanced range"
        }
      )
    }

    if (test.category == TestCategory.CLINICAL_SCALES) {
      return ClinicalScalesCatalog.evaluateClinicalScale(
        test = test,
        answers = answers,
        patterns = patternSummaryList,
        language = language
      )
    }

    return when (test.key) {
      "emotional_drain" -> buildEmotionalDrainReport(test, normalizedScore, rawSum, maxRaw, patternSummaryList, language)
      "overthinking_anxiety" -> buildOverthinkingReport(test, normalizedScore, rawSum, maxRaw, patternSummaryList, language)
      "attachment_relationship" -> buildAttachmentReport(test, normalizedScore, rawSum, maxRaw, patternSummaryList, language)
      "self_worth_confidence" -> buildSelfWorthReport(test, normalizedScore, rawSum, maxRaw, patternSummaryList, language)
      else -> buildTraumaReport(test, normalizedScore, rawSum, maxRaw, patternSummaryList, language)
    }
  }

  private fun buildEmotionalDrainReport(
    test: PsychologicalTestDefinition,
    score: Int,
    raw: Int,
    maxRaw: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "মানসিক সজীবতা ও আবেগীয় ভারসাম্য (Healthy Emotional Vitality)",
          "আপনার মনের ভেতরের শক্তি ও সংযোগ বেশ ভালো অবস্থায় রয়েছে।",
          "আপনার উত্তরগুলো বিশ্লেষণ করে দেখা যাচ্ছে, দৈনন্দিন জীবনের স্বাভাবিক ওঠানামা থাকলেও আপনি নিজের অনুভূতিগুলোকে চেপে রাখেন না এবং প্রিয় কাজ বা মানুষদের সাথে মানসিক সংযোগ অনুভব করতে পারেন। এটি সুস্থ মানসিক স্থিতিস্থাপকতার (Emotional Resilience) লক্ষণ।",
          listOf(
            "প্রতিদিন নিজের প্রিয় শখ বা ভালো লাগার কাজের জন্য অন্তত ৩০ মিনিট সময় বরাদ্দ রাখুন।",
            "কাছের মানুষদের সাথে মনের কথাগুলো খোলামেলা শেয়ার করার এই সুন্দর অভ্যাসটি বজায় রাখুন।",
            "নিয়মিত পর্যাপ্ত ঘুম ও জার্নাল লেখার মাধ্যমে নিজের মানসিক প্রশান্তি ধরে রাখুন।"
          )
        )
        score < 58 -> Quad(
          "নীরব মানসিক ক্লান্তি ও আবেগীয় দূরত্ব (Mild-to-Moderate Emotional Fatigue)",
          "আপনি বাইরে থেকে স্বাভাবিক থাকলেও ভেতরে ভেতরে ধীরে ধীরে মানসিক শক্তি হারাচ্ছেন।",
          "আপনার উত্তরগুলো বলছে, আপনি অনেক সময় নিজের খারাপ লাগাগুলো অন্যের কাছে প্রকাশ না করে হাসিমুখে সামলে নিচ্ছেন। এর ফলে মনের ভেতর এক ধরনের অব্যক্ত ক্লান্তি ও একাকীত্ব জমছে, যা আপনার দৈনন্দিন উৎসাহ ও ঘুমের সতেজতাকে কমিয়ে দিচ্ছে।",
          listOf(
            "সবসময় 'আমি ভালো আছি' বলার চাপ থেকে নিজেকে মুক্তি দিন; অন্তত একজন বিশ্বস্ত মানুষের কাছে মনের কথা বলুন।",
            "দিনের যে কাজগুলো আপনার শক্তি শুষে নিচ্ছে সেগুলোর মাঝে ছোট বিরতি নিন।",
            "প্রতিদিন রাতে ঘুমানোর আগে থেনাটক্স জার্নালে নিজের না বলা অনুভূতিগুলো লিখে মন হালকা করুন।"
          )
        )
        else -> Quad(
          "গভীর মানসিক অবসাদ ও অব্যক্ত একাকীত্ব (Deep Emotional Burnout & Masked Loneliness)",
          "আপনি দীর্ঘ সময় ধরে নিজের কষ্ট একা বহন করতে গিয়ে তীব্র মানসিক শূন্যতা ও অবসাদে ভুগছেন।",
          "একজন সাইকোলজিস্টের দৃষ্টিতে আপনার উত্তরগুলো অত্যন্ত গুরুত্বপূর্ণ। আপনি মানুষের ভিড়ে থেকেও নিজেকে একা অনুভব করছেন, নিজের কষ্ট হাসির আড়ালে লুকিয়ে রাখছেন (Emotional Masking) এবং প্রিয় কাজগুলোতেও আর আনন্দ পাচ্ছেন না। দীর্ঘদিন ধরে নিজের আবেগ চেপে রাখার কারণে আপনার মন ও স্নায়ু অতিরিক্ত ক্লান্ত হয়ে পড়েছে।",
          listOf(
            "আজই ড. সিফাত খান (লাইভ এআই সাইকোলজিস্ট)-এর সাথে কথা বলে আপনার মনের জমে থাকা কথাগুলো প্রকাশ করুন।",
            "নিজেকে জোর করে সব কাজে নিখুঁত রাখার চেষ্টা সাময়িকভাবে বন্ধ করুন এবং শরীর ও মনকে অপরাধবোধ ছাড়া বিশ্রাম দিন।",
            "টুলকিট সেকশনের ৪-৭-৮ ব্রেথিং ও গ্রাউন্ডিং মেডিটেশন প্রতিদিন ২ বার অনুশীলন করুন।"
          )
        )
      }
      PsychConditionAnalysis(
        testKey = test.key,
        testTitle = test.title(language),
        normalizedScore = score,
        rawScore = raw,
        maxRawScore = maxRaw,
        conditionName = condName,
        conditionSummary = summary,
        deepExplanation = explanation,
        keyPatterns = patterns,
        actionableSteps = steps
      )
    } else {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "Healthy Emotional Vitality & Connection",
          "Your emotional energy and sense of inner connection are well-balanced.",
          "Your responses indicate that while you experience normal life ups and downs, you remain connected to your feelings and still find meaning in daily activities and relationships.",
          listOf(
            "Continue dedicating at least 30 minutes daily to activities that genuinely recharge you.",
            "Keep expressing your honest feelings with trusted people in your life.",
            "Use the daily journal to maintain your emotional clarity."
          )
        )
        score < 58 -> Quad(
          "Silent Emotional Fatigue & Mild Disconnection",
          "You appear functional on the outside, but you are quietly carrying unshared emotional weight.",
          "Psychologically, your answers show signs of emotional masking—smiling through stress while feeling slightly disconnected or drained inside. Over time, holding back your true feelings reduces your daily motivation and restful energy.",
          listOf(
            "Allow yourself to drop the 'I am fine' mask with at least one safe person or in your private journal.",
            "Reduce non-essential obligations this week to let your nervous system recover.",
            "Talk with Dr. Sifat Khan whenever you feel an unspoken heaviness building up."
          )
        )
        else -> Quad(
          "Deep Emotional Exhaustion & Masked Loneliness",
          "You have been carrying heavy unspoken pain alone for too long, leading to emotional numbness and exhaustion.",
          "From a clinical psychology perspective, your responses highlight severe emotional drain and high-functioning loneliness. Even around people, you feel unseen, and things that once brought joy now feel flat. Your mind is exhausted from constantly holding everything together on the outside.",
          listOf(
            "Start a live conversation with Dr. Sifat Khan right away to safely unpack what you have been holding inside.",
            "Be gentle with yourself—pause heavy self-expectations and focus on small, restorative routines.",
            "Practice the 4-7-8 breathing exercise in the Toolkit tab before sleep to soothe your nervous system."
          )
        )
      }
      PsychConditionAnalysis(
        testKey = test.key,
        testTitle = test.title(language),
        normalizedScore = score,
        rawScore = raw,
        maxRawScore = maxRaw,
        conditionName = condName,
        conditionSummary = summary,
        deepExplanation = explanation,
        keyPatterns = patterns,
        actionableSteps = steps
      )
    }
  }

  private fun buildOverthinkingReport(
    test: PsychologicalTestDefinition,
    score: Int,
    raw: Int,
    maxRaw: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "স্থির ও নিয়ন্ত্রিত চিন্তাশক্তি (Balanced Cognitive Calm)",
          "আপনি পরিস্থিতি নিয়ে চিন্তা করলেও তা আপনার মানসিক শান্তিকে নষ্ট করে না।",
          "আপনার উত্তর অনুযায়ী, আপনি বাস্তবসম্মতভাবে চিন্তা করতে পারেন এবং অহেতুক ভয় বা পুরনো কথাবার্তা নিয়ে দীর্ঘক্ষণ মাথা ঘামান না। এটি একটি সুস্থ কগনিটিভ প্যাটার্ন।",
          listOf(
            "আপনার এই বাস্তবমুখী চিন্তাভাবনার অভ্যাসটি ধরে রাখুন।",
            "গুরুত্বপূর্ণ সিদ্ধান্তের সময় লিখিত নোট ব্যবহার করুন।",
            "নিয়মিত গভীর শ্বাস-প্রশ্বাসের অনুশীলন বজায় রাখুন।"
          )
        )
        score < 58 -> Quad(
          "অতি-বিশ্লেষণ ও প্রচ্ছন্ন উদ্বেগ (Ruminative Overthinking Pattern)",
          "আপনার মস্তিষ্ক ছোটখাটো বিষয় ও ভবিষ্যতের অনিশ্চয়তা নিয়ে প্রয়োজনের চেয়ে বেশি বিশ্লেষণ করছে।",
          "সাইকোলজিক্যাল বিশ্লেষণে দেখা যাচ্ছে, আপনি রাতে ঘুমানোর সময় বা অবসরে পুরনো কথাবার্তা এবং ভবিষ্যতের সম্ভাব্য সমস্যা নিয়ে বারবার চিন্তা করেন (Rumination)। এর ফলে আপনার মন বিশ্রাম পাচ্ছে না এবং সিদ্ধান্ত নিতে দ্বিধা তৈরি হচ্ছে।",
          listOf(
            "কোনো চিন্তা মাথায় ঘুরতে শুরু করলে নিজেকে প্রশ্ন করুন: 'এই বিষয়টি কি এখনই আমার নিয়ন্ত্রণে আছে?'",
            "ঘুমানোর ১ ঘণ্টা আগে সমস্ত চিন্তা একটি ডায়েরি বা জার্নালে লিখে মাথার বোঝা নামিয়ে রাখুন।",
            "টুলকিট ট্যাবের ৫-৪-৩-২-১ গ্রাউন্ডিং কৌশল ব্যবহার করে বর্তমান মুহূর্তে মন ফিরিয়ে আনুন।"
          )
        )
        else -> Quad(
          "তীব্র ওভারথিংকিং লুপ ও হাইপার-অ্যাংজাইটি (Chronic Overthinking & Hyper-Active Anxiety)",
          "আপনার মস্তিষ্ক সারাক্ষণ 'বিপদ সংকেত' বা সবচেয়ে খারাপ সম্ভাবনার হিসাব কষতে গিয়ে ক্লান্ত হয়ে পড়েছে।",
          "আপনার উত্তরগুলো স্পষ্ট করছে যে আপনার মস্তিষ্ক এক মুহূর্তের জন্যও থামতে পারছে না। পুরনো কথার চুলচেরা বিশ্লেষণ, ভবিষ্যৎ নিয়ে সবচেয়ে খারাপ আশঙ্কা (Catastrophizing) এবং অবসর সময়েও ভেতরের অস্থিরতা আপনার স্নায়ুতন্ত্রকে সারাক্ষণ হাই-অ্যালার্টে রাখছে, যার প্রভাব আপনার ঘুম ও শরীরেও পড়ছে।",
          listOf(
            "চিন্তা জোর করে থামানোর চেষ্টা না করে ড. সিফাত খানের সাথে CBT (Cognitive Behavioral) মোডে কথা বলুন।",
            "অনিশ্চয়তাকে ভয় না পেয়ে দিনে মাত্র ১৫ মিনিট 'চিন্তার সময়' নির্ধারণ করুন এবং বাকি সময় নিজেকে কাজে যুক্ত রাখুন।",
            "বুক ধড়ফড় বা অস্থির লাগলে সাথে সাথে ৪-৭-৮ শ্বাস-প্রশ্বাস ব্যায়াম শুরু করুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "Balanced Cognitive Calm",
          "You process thoughts thoughtfully without getting trapped in anxious loops.",
          "Your responses show healthy cognitive control. You can reflect on events and plan ahead without letting worst-case scenarios or past conversations hijack your peace of mind.",
          listOf(
            "Continue practicing mindful presence during daily routines.",
            "Keep using structured planning when making big decisions.",
            "Maintain consistent sleep habits."
          )
        )
        score < 58 -> Quad(
          "Ruminative Overthinking & Anticipatory Worry",
          "Your mind tends to replay past conversations and over-prepare for uncertain outcomes.",
          "Psychologically, you are experiencing moderate cognitive rumination. Even when you want to rest, your brain keeps analyzing what you said or what might go wrong tomorrow, creating mental fatigue and decision hesitation.",
          listOf(
            "Write down racing thoughts in the Journal tab before bed to signal your brain that it can rest.",
            "Ask yourself: 'Is this a real problem right now, or a hypothetical scenario?'",
            "Use the 5-4-3-2-1 Grounding exercise in the Toolkit tab when thoughts start looping."
          )
        )
        else -> Quad(
          "Chronic Overthinking Loop & Hyper-Active Anxiety",
          "Your mind is stuck in an exhausting cycle of worst-case thinking and nonstop analysis.",
          "Your answers reveal an overactive threat-detection system in your mind (Catastrophizing & Hyper-vigilance). Your brain tries to protect you by anticipating every possible mistake or disaster, but this leaves your nervous system constantly tense, restless, and physically drained.",
          listOf(
            "Consult Dr. Sifat Khan in CBT Mode to gently challenge and reframe catastrophic thoughts.",
            "Practice Progressive Muscle Relaxation (PMR) and 4-7-8 breathing in the Toolkit tab daily.",
            "Limit late-night screen time and practice setting a 5-minute decision timer for small choices."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun buildAttachmentReport(
    test: PsychologicalTestDefinition,
    score: Int,
    raw: Int,
    maxRaw: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "নিরাপদ ইমোশনাল অ্যাটাচমেন্ট (Secure Attachment Style)",
          "সম্পর্কে আপনি বিশ্বাস, আত্মসম্মান এবং সুস্থ সীমারেখা বজায় রাখতে জানেন।",
          "আপনার উত্তরগুলো নির্দেশ করে যে আপনি প্রিয় মানুষদের ভালোবাসলেও নিজের অস্তিত্ব বা আত্মসম্মান হারিয়ে ফেলেন না। কারো সাময়িক ব্যস্ততায় আপনি আতঙ্কিত হন না এবং নিজের প্রয়োজন স্পষ্টভাবে জানাতে পারেন।",
          listOf(
            "সম্পর্কে খোলামেলা যোগাযোগের এই সুস্থ অভ্যাস বজায় রাখুন।",
            "নিজের ব্যক্তিগত সময় ও আত্মযত্নকে সবসময় গুরুত্ব দিন।",
            "কাছের মানুষদের সাথে পারস্পরিক শ্রদ্ধার বন্ধন অটুট রাখুন।"
          )
        )
        score < 58 -> Quad(
          "সংবেদনশীল ও দ্বিধান্বিত অ্যাটাচমেন্ট (Anxious-Sensitive Attachment Pattern)",
          "আপনি মানুষকে গভীরভাবে ভালোবাসেন, কিন্তু অবহেলা বা হারানোর ভয়ে প্রায়ই নিজের কষ্ট চেপে রাখেন।",
          "মনস্তাত্ত্বিক বিশ্লেষণে দেখা যাচ্ছে, আপনি অন্যকে খুশি রাখতে গিয়ে (People-Pleasing) নিজের চাহিদাকে কম গুরুত্ব দেন এবং প্রিয় মানুষ একটু দূরে সরে গেলেই ভেতরে ভেতরে অস্থির হয়ে পড়েন। আপনি আঘাত পাওয়ার ভয়ে অনেক সময় নিজের সীমারেখা (Boundary) জানাতে পারেন না।",
          listOf(
            "মনে রাখবেন—কারো সাময়িক ব্যস্ততা বা নীরবতা মানেই আপনাকে অবহেলা বা ত্যাগ করা নয়।",
            "ছোটখাটো বিষয়ে নিজের পছন্দ ও 'না' বলার চর্চা ধীরে ধীরে শুরু করুন।",
            "নিজের ভালো থাকাকে একটিমাত্র মানুষের মেসেজ বা আচরণের ওপর শতভাগ নির্ভরশীল করবেন না।"
          )
        )
        else -> Quad(
          "তীব্র বিচ্ছেদ-আতঙ্ক ও অনিরাপদ অ্যাটাচমেন্ট (Deep Anxious-Avoidant Attachment Wound)",
          "সম্পর্কে কাছের মানুষকে হারানোর তীব্র ভয় এবং নিজের কষ্ট চেপে রাখার প্রবণতা আপনাকে মানসিকভাবে ক্ষতবিক্ষত করছে।",
          "একজন সাইকোলজিস্টের দৃষ্টিতে আপনার উত্তরগুলো বলছে—আপনি ভালোবাসা ও নিরাপত্তার জন্য তীব্রভাবে তৃষ্ণার্ত, কিন্তু একই সাথে মানুষকে বিশ্বাস করতে বা নিজের আসল রূপ দেখাতে ভয় পান। প্রিয় মানুষের সামান্য পরিবর্তনেই আপনার মনে বিচ্ছেদের আতঙ্ক (Abandonment Panic) তৈরি হয় এবং আপনি অন্যকে খুশি রাখতে গিয়ে নিজেকে সম্পূর্ণ নিঃশেষ করে ফেলছেন।",
          listOf(
            "ড. সিফাত খানের সাথে আপনার সম্পর্কের ভয় ও অতীত অভিজ্ঞতা নিয়ে খোলামেলা কথা বলুন।",
            "অন্যকে খুশি করার আগে প্রতিদিন নিজেকে প্রশ্ন করুন: 'এতে কি আমার নিজের মনের শান্তি নষ্ট হচ্ছে?'",
            "নিজের আত্মমর্যাদা বৃদ্ধির জন্য নিজের শখ, লক্ষ্য ও ব্যক্তিগত গণ্ডি শক্তিশালী করুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "Secure Emotional Attachment Style",
          "You build relationships with healthy trust, clear boundaries, and emotional balance.",
          "Your responses reflect a secure attachment pattern. You value closeness and intimacy without losing your own identity or panicking when a partner or friend needs space.",
          listOf(
            "Continue practicing honest, respectful communication in your relationships.",
            "Maintain the healthy balance between togetherness and personal space.",
            "Support others while honoring your own emotional boundaries."
          )
        )
        score < 58 -> Quad(
          "Anxious-Sensitive Attachment & People-Pleasing",
          "You care deeply about others, but fear of distance or conflict makes you suppress your own needs.",
          "Psychologically, your answers show a strong tendency toward people-pleasing and sensitivity to perceived distance. When someone close is quiet or busy, you quickly wonder if you did something wrong, and you often silence your own hurt to keep the relationship smooth.",
          listOf(
            "Remind yourself that someone's busyness or quiet mood is usually about their life, not a rejection of you.",
            "Practice expressing small preferences and boundaries without apologizing.",
            "Build daily sources of joy and validation that belong entirely to you."
          )
        )
        else -> Quad(
          "Deep Abandonment Anxiety & Attachment Vulnerability",
          "Intense fear of rejection, emotional dependency, and difficulty trusting are causing heavy inner turmoil.",
          "From an attachment psychology lens, you carry a deep fear of being left behind or misunderstood. You either over-give to the point of losing yourself, or shut down emotionally out of fear of getting hurt. Your daily peace feels dangerously tied to how others respond to you.",
          listOf(
            "Talk with Dr. Sifat Khan to heal core abandonment fears and build inner emotional safety.",
            "Pause before self-sacrificing: notice when you are saying 'yes' only out of fear of losing someone.",
            "Use grounding exercises whenever a delayed text or conflict triggers abandonment panic."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun buildSelfWorthReport(
    test: PsychologicalTestDefinition,
    score: Int,
    raw: Int,
    maxRaw: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "সুস্থ আত্মবিশ্বাস ও আত্ম-মর্যাদা (Healthy Self-Worth & Confidence)",
          "আপনি নিজের ক্ষমতা ও সীমাবদ্ধতা উভয়কেই সম্মান করতে জানেন।",
          "আপনার উত্তরগুলো প্রমাণ করে যে আপনি ভুল করলে নিজেকে শত্রুর মতো বিচার করেন না, বরং সেখান থেকে শিক্ষা নেন এবং অন্যের সাফল্যে হীনম্মন্যতায় না ভুগে নিজের পথে এগিয়ে যান।",
          listOf(
            "নিজের প্রতি এই ইতিবাচক ও সদয় দৃষ্টিভঙ্গি বজায় রাখুন।",
            "নতুন চ্যালেঞ্জ গ্রহণের সাহস অব্যাহত রাখুন।",
            "নিজের ছোট-বড় প্রতিটি অর্জনের জন্য নিজেকে ধন্যবাদ দিন।"
          )
        )
        score < 58 -> Quad(
          "অন্তর্নিহিত আত্ম-সংশয় ও তুলনা প্রবণতা (Self-Doubt & Imposter Pattern)",
          "আপনার ভেতরে যথেষ্ট যোগ্যতা থাকা সত্ত্বেও আপনি নিজের কাজের চেয়ে নিজের ত্রুটিগুলোকে বড় করে দেখেন।",
          "মনস্তাত্ত্বিক বিশ্লেষণে দেখা যাচ্ছে, আপনি অন্যের সাথে নিজের তুলনা করে মন খারাপ করেন এবং কেউ প্রশংসা করলেও তা মন থেকে বিশ্বাস করতে দ্বিধা বোধ করেন (Imposter Syndrome)। ছোট কোনো ভুল হলেই আপনি নিজেকে অতিরিক্ত দোষারোপ করেন।",
          listOf(
            "নিজের সাথে এমনভাবে কথা বলুন যেভাবে আপনি আপনার প্রিয় কোনো বন্ধুকে বিপদে সান্ত্বনা দিতেন।",
            "সোশ্যাল মিডিয়ার সাজানো সাফল্যের সাথে নিজের বাস্তব জীবনের তুলনা করা কমিয়ে দিন।",
            "প্রতিদিন জার্নালে নিজের অন্তত ১টি ভালো কাজ বা চেষ্টার কথা লিখে রাখুন।"
          )
        )
        else -> Quad(
          "তীব্র হীনম্মন্যতা ও কঠোর আত্ম-সমালোচনা (Severe Inner Critic & Low Self-Esteem)",
          "আপনার মনের ভেতরের কঠোর সমালোচক (Inner Critic) আপনাকে প্রতিনিয়ত ব্যর্থ ও মূল্যহীন মনে করাচ্ছে।",
          "সাইকোলজিক্যাল দৃষ্টিতে আপনি নিজের প্রতি অত্যন্ত কঠোর। সামান্য ভুলেই আপনি নিজেকে সম্পূর্ণ অযোগ্য মনে করেন, সাহায্য চাইতে গেলে নিজেকে অন্যের বোঝা ভাবেন এবং ব্যর্থতার ভয়ে নতুন সুযোগ থেকে পিছিয়ে আসেন। দীর্ঘদিনের সমালোচনা বা তিক্ত অভিজ্ঞতার কারণে আপনার আত্মবিশ্বাস গভীরভাবে ক্ষতিগ্রস্ত হয়েছে।",
          listOf(
            "ড. সিফাত খানের সাথে কথা বলে নিজের ভেতরের এই কঠোর আত্ম-সমালোচনার উৎসটি চিহ্নিত করুন।",
            "মনে রাখবেন—একটি ভুল মানে আপনি ব্যর্থ নন, এটি কেবল শেখার একটি অংশ।",
            "অন্যের মতামতের চেয়ে নিজের ছোট ছোট সিদ্ধান্তের ওপর আস্থা রাখার অনুশীলন শুরু করুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "Healthy Self-Worth & Grounded Confidence",
          "You treat yourself with respect and trust your own capabilities.",
          "Your responses show strong self-compassion and balanced self-esteem. You can handle mistakes without tearing yourself down and celebrate others without diminishing your own value.",
          listOf(
            "Keep practicing self-compassion when navigating new challenges.",
            "Continue celebrating your progress on your own timeline.",
            "Maintain your balanced perspective on growth and learning."
          )
        )
        score < 58 -> Quad(
          "Self-Doubt & Imposter Syndrome Pattern",
          "Despite your real strengths, an inner critic makes you question your worth and compare yourself to others.",
          "Psychologically, you show signs of imposter syndrome and social comparison. You brush off compliments, dwell on small mistakes, and often feel like everyone else has life figured out better than you.",
          listOf(
            "Speak to yourself after a mistake the way you would speak to a dear friend.",
            "Notice when social media triggers unfair comparisons and take intentional breaks.",
            "Record small daily wins in your Journal to build objective proof of your capability."
          )
        )
        else -> Quad(
          "Severe Inner Critic & Deeply Wounded Self-Esteem",
          "A harsh internal voice is constantly convincing you that you are inadequate or a burden.",
          "From a clinical psychology perspective, your answers reveal an exhausting level of self-criticism and low self-worth. You hesitate to ask for help because you feel like a burden, fear trying new things due to anticipated failure, and overlook your own strengths entirely.",
          listOf(
            "Work with Dr. Sifat Khan in the live chat to separate your true identity from your harsh inner critic.",
            "Challenge all-or-nothing thoughts like 'I made a mistake, so I am useless.'",
            "Practice asking for small support from trusted people without apologizing for existing."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun buildTraumaReport(
    test: PsychologicalTestDefinition,
    score: Int,
    raw: Int,
    maxRaw: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "মানসিক নিরাময় ও эмоционаাল নিরাপত্তা (Emotionally Integrated & Safe)",
          "অতীতের অভিজ্ঞতাগুলো আপনার বর্তমানের মানসিক শান্তি বা বিশ্বাসকে নিয়ন্ত্রণ করছে না।",
          "আপনার উত্তরগুলো বলছে, জীবনের কঠিন সময়গুলো আপনি সুস্থভাবে প্রক্রিয়া করতে পেরেছেন এবং বর্তমান মুহূর্তে নিজেকে নিরাপদ ও সংযুক্ত অনুভব করেন।",
          listOf(
            "নিজের মানসিক নিরাপত্তার এই সুস্থ পরিবেশ বজায় রাখুন।",
            "মনের যত্ন নিতে নিয়মিত মেডিটেশন ও জার্নালিং চালিয়ে যান।",
            "ইতিবাচক ও নিরাপদ মানুষদের সান্নিধ্যে থাকুন।"
          )
        )
        score < 58 -> Quad(
          "অতীতের অমীমাংসিত আঘাত ও সংবেদনশীল ট্রিগার (Lingering Emotional Wounds & Triggers)",
          "অতীতের কিছু তিক্ত স্মৃতি বা আঘাত এখনো আপনার মনের গভীরে রয়ে গেছে যা বিশেষ মুহূর্তে আপনাকে নাড়িয়ে দেয়।",
          "মনস্তাত্ত্বিক বিশ্লেষণে দেখা যাচ্ছে, আপনি বাইরে থেকে স্বাভাবিক জীবনযাপন করলেও কারো রূঢ় স্বর, অবহেলা বা বিশেষ কিছু পরিস্থিতিতে অতীতের কষ্টের অনুভূতি হঠাৎ ফিরে আসে (Emotional Triggers)। নিজেকে বাঁচাতে আপনি আগের চেয়ে অনেক বেশি সতর্ক ও রক্ষণশীল হয়ে পড়েছেন।",
          listOf(
            "কোনো ঘটনায় হঠাৎ তীব্র খারাপ লাগলে সাথে সাথে প্রতিক্রিয়া না দেখিয়ে গভীর শ্বাস নিন এবং বুঝুন এটি অতীতের ক্ষত থেকে আসছে।",
            "অতীতের যে ঘটনায় আপনার কোনো দোষ ছিল না, সেটির জন্য নিজেকে দায়ী করা বন্ধ করুন।",
            "ড. সিফাত খানের সাথে কথা বলে অতীতের সেই চাপা কষ্টগুলো নিরাপদে হালকা করুন।"
          )
        )
        else -> Quad(
          "গভীর ইমোশনাল ট্রমা ও স্নায়বিক প্রতিরক্ষা অবস্থা (Unhealed Trauma & Hyper-Vigilant Defense)",
          "অতীতের গভীর আঘাত থেকে বাঁচতে আপনার মন ও স্নায়ুতন্ত্র সারাক্ষণ একটি প্রতিরক্ষা দেয়াল (Survival Mode) তৈরি করে রেখেছে।",
          "একজন সাইকোলজিস্টের দৃষ্টিতে আপনার উত্তরগুলো অত্যন্ত সংবেদনশীল। অতীতের বিশ্বাসঘাতকতা, অবহেলা বা আঘাতের কারণে আপনার মস্তিষ্ক সারাক্ষণ বিপদের আশঙ্কায় (Hyper-vigilance) থাকে। এমনকি আনন্দের মুহূর্তেও আপনার ভয় লাগে যে 'এই বুঝি কিছু খারাপ ঘটবে', এবং তীব্র চাপে আপনার মন সম্পূর্ণ অবশ বা স্থবির (Freeze Response) হয়ে যায়। এটি আপনার দুর্বলতা নয়, বরং অতীতের আঘাত থেকে আপনার মনের আত্মরক্ষার চেষ্টা।",
          listOf(
            "আপনার এই অনুভূতিগুলো একা বয়ে বেড়াবেন না—ড. সিফাত খান চ্যাটবটে নিরাপদে ও গোপনীয়তার সাথে আপনার মনের কথা শেয়ার করুন।",
            "হঠাৎ ট্রিগার বা আতঙ্ক শুরু হলে টুলকিট সেকশনের ৫-৪-৩-২-১ গ্রাউন্ডিং মেডিটেশন করুন এবং নিজেকে বলুন: 'অতীত শেষ হয়েছে, আমি এখন নিরাপদ আছি।' ",
            "নিজের অনুভূতিগুলোকে জোর করে পাথর না করে ধীরে ধীরে ডায়েরি বা জার্নালে প্রকাশ করার চর্চা করুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        score < 28 -> Quad(
          "Emotionally Integrated & Grounded",
          "Past difficult experiences no longer control your present sense of safety or trust.",
          "Your responses show that you have processed past challenges healthily and feel emotionally safe in the present moment without constant hyper-vigilance.",
          listOf(
            "Continue nurturing safe, supportive relationships.",
            "Keep using mindfulness and journaling to stay grounded.",
            "Honor your emotional boundaries and inner peace."
          )
        )
        score < 58 -> Quad(
          "Lingering Emotional Wounds & Protective Guard",
          "Certain unresolved hurts from the past still surface when triggered by specific tones or situations.",
          "Psychologically, your answers show that while you move through daily life well, specific triggers—like raised voices, sudden changes in someone's mood, or fear of losing joy—activate old emotional pain and cause you to put up protective walls.",
          listOf(
            "When triggered, pause and remind yourself: 'This intense feeling is an echo of the past, not the present.'",
            "Release self-blame for past situations where others mistreated you.",
            "Share your reflections with Dr. Sifat Khan or in your private Journal."
          )
        )
        else -> Quad(
          "Unhealed Emotional Trauma & Survival-Mode Nervous System",
          "Your mind and nervous system are locked in protective survival mode due to deep past emotional wounds.",
          "From a trauma-psychology perspective, your responses indicate hyper-vigilance, protective emotional numbing, foreboding joy, and a freeze response under stress. Because you were deeply hurt or let down in the past, your brain stays on constant high alert to prevent future pain—even at the cost of your present peace.",
          listOf(
            "Speak gently with Dr. Sifat Khan in the live AI Psychiatrist chat to process these feelings in a safe space.",
            "Use the 5-4-3-2-1 Sensory Grounding tool immediately when a trigger makes you feel frozen or overwhelmed.",
            "Remind yourself daily: 'My protective walls helped me survive then, and I am learning to feel safe now.'"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), score, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private data class Quad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
  )
}
