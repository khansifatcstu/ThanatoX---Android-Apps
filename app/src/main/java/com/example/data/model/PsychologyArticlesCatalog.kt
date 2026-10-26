package com.example.data.model

import com.example.ui.i18n.AppLanguage

data class PsychologyArticleSection(
  val subheadingBn: String,
  val subheadingEn: String,
  val bodyBn: String,
  val bodyEn: String
) {
  fun subheading(language: AppLanguage): String =
    if (language == AppLanguage.BN) subheadingBn else subheadingEn

  fun body(language: AppLanguage): String =
    if (language == AppLanguage.BN) bodyBn else bodyEn
}

data class PsychologyArticle(
  val id: String,
  val headingBn: String,
  val headingEn: String,
  val writerNameBn: String,
  val writerNameEn: String,
  val topicTitleBn: String,
  val topicTitleEn: String,
  val introBn: String,
  val introEn: String,
  val sections: List<PsychologyArticleSection>,
  val keyTakeawaysBn: List<String>,
  val keyTakeawaysEn: List<String>
) {
  fun heading(language: AppLanguage): String =
    if (language == AppLanguage.BN) headingBn else headingEn

  fun writerName(language: AppLanguage): String =
    if (language == AppLanguage.BN) writerNameBn else writerNameEn

  fun topicTitle(language: AppLanguage): String =
    if (language == AppLanguage.BN) topicTitleBn else topicTitleEn

  fun intro(language: AppLanguage): String =
    if (language == AppLanguage.BN) introBn else introEn

  fun keyTakeaways(language: AppLanguage): List<String> =
    if (language == AppLanguage.BN) keyTakeawaysBn else keyTakeawaysEn
}

object PsychologyArticlesCatalog {

  val articles: List<PsychologyArticle> = listOf(
    // Article 1
    PsychologyArticle(
      id = "stop_overthinking_guide",
      headingBn = "অতিরিক্ত চিন্তা (Overthinking) থামিয়ে মনকে শান্ত করার সহজ উপায়",
      headingEn = "How to Stop Overthinking and Calm Your Racing Mind",
      writerNameBn = "ড. সিফাত খান",
      writerNameEn = "Dr. Sifat Khan",
      topicTitleBn = "অতিরিক্ত চিন্তা ও মানসিক প্রশান্তি",
      topicTitleEn = "Overthinking & Mental Calm",
      introBn = "রাতের বেলা বালিশে মাথা রাখার পর কি আপনার মাথার ভেতর হাজারটা চিন্তা ঘুরপাক খেতে থাকে? অতীতের কোনো ভুল নিয়ে আফসোস কিংবা ভবিষ্যতে কী হবে তা নিয়ে অজানা ভয়—মনোবিজ্ঞানের ভাষায় একে বলা হয় 'ওভারথিংকিং' (Overthinking)। আমাদের মস্তিষ্ক মূলত আমাদের নিরাপদ রাখতে চায়, কিন্তু যখন সে সমাধান না খুঁজে শুধু দুশ্চিন্তার বৃত্তে ঘুরতে থাকে, তখন আমাদের মানসিক শক্তি নিঃশেষ হয়ে যায়।",
      introEn = "Do hundreds of thoughts race through your head the moment you try to rest at night? Replaying past mistakes or worrying about " +
        "what might go wrong tomorrow is known in psychology as overthinking. While your brain tries to protect you by anticipating problems, " +
        "getting trapped in endless thought loops drains your emotional energy without solving anything.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. চিন্তা এবং সমাধানের পার্থক্য বুঝুন",
          subheadingEn = "1. Distinguish Between Worrying and Problem-Solving",
          bodyBn = "অনেকেই মনে করেন বেশি চিন্তা করলে হয়তো সমস্যার সমাধান বের হবে। কিন্তু মনোবিজ্ঞান বলে—সমাধান খোঁজা মানে হলো পরবর্তী পদক্ষেপ ঠিক করা, আর ওভারথিংকিং মানে হলো একই ভয় বারবার মনের পর্দায় চালানো। যখনই দেখবেন কোনো চিন্তা আপনাকে কোনো বাস্তব পদক্ষেপ দিচ্ছে না, শুধু ভয় বাড়াচ্ছে—তখনই বুঝবেন এটি ওভারথিংকিং।",
          bodyEn = "Many people believe that thinking harder will magically solve their problems. However, problem-solving focuses on actionable steps, whereas overthinking simply replays the same fear repeatedly. Whenever a thought increases your anxiety without offering a practical action, recognize it as an overthinking loop."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. 'যদি এমন হয়' থেকে 'এখন কী করা যায়'-এ ফিরে আসুন",
          subheadingEn = "2. Shift from 'What If?' to 'What Can I Do Now?'",
          bodyBn = "ওভারথিংকিং সবসময় শুরু হয় 'যদি খারাপ কিছু হয়?' এই প্রশ্ন দিয়ে। এই চক্র ভাঙার সবচেয়ে সহজ উপায় হলো নিজেকে জিজ্ঞেস করা: 'এই মুহূর্তে বিষয়টি কি আমার নিয়ন্ত্রণে আছে?' যদি আপনার নিয়ন্ত্রণে থাকে, তবে ছোট একটি কাজ শুরু করুন। আর যদি নিয়ন্ত্রণের বাইরে হয়, তবে মস্তিষ্ককে বলুন—যা এখনো ঘটেনি তা নিয়ে আজ নিজের বর্তমান নষ্ট করব না।",
          bodyEn = "Overthinking almost always begins with 'What if something goes wrong?' The simplest way to break this habit is to ask yourself: 'Is this within my control right now?' If it is, take one small step. If it is outside your control, gently remind your mind not to sacrifice today's peace for an imagined tomorrow."
        ),
        PsychologyArticleSection(
          subheadingBn = "৩. মনের কথা খাতায় বা জার্নালে লিখে ফেলা (Brain Dump)",
          subheadingEn = "3. Write Your Thoughts Down (The Brain Dump Method)",
          bodyBn = "মাথার ভেতর চিন্তা জমে থাকলে সেগুলো অনেক বড় ও ভয়ংকর মনে হয়। কিন্তু যখন আপনি কাগজে বা অ্যাপের জার্নালে মাত্র ৫ মিনিট সময় নিয়ে মনের সব দুশ্চিন্তা লিখে ফেলবেন, তখন আপনার মস্তিষ্ক হালকা অনুভব করবে এবং আপনি সহজেই বুঝতে পারবেন কোন চিন্তাটি আসল আর কোনটি অহেতুক ভয়।",
          bodyEn = "When thoughts stay locked inside your head, they feel overwhelming and tangled. Taking just 5 minutes to write down everything worrying you transfers the burden out of working memory, allowing you to clearly see which concerns are real and which are exaggerated fears."
        )
      ),
      keyTakeawaysBn = listOf(
        "যে বিষয়টি আপনার নিয়ন্ত্রণের বাইরে, তা নিয়ে বারবার ভেবে বর্তমানের শান্তি নষ্ট করবেন না।",
        "অতিরিক্ত চিন্তা শুরু হলে গভীর শ্বাস নিন এবং চারপাশের বাস্তব কাজে মনোযোগ ফেরান।",
        "রাতে ঘুমানোর আগে মনের দুশ্চিন্তাগুলো ডায়েরি বা জার্নালে লিখে মাথা হালকা করুন।"
      ),
      keyTakeawaysEn = listOf(
        "Do not let things outside your control steal your peace in the present moment.",
        "When thought loops start, take slow deep breaths and engage in a physical activity.",
        "Write down lingering worries in a journal before bed to clear your mind."
      )
    ),

    // Article 2
    PsychologyArticle(
      id = "kahneman_two_systems_thinking",
      headingBn = "মানুষ কেন আবেগের বশে ভুল সিদ্ধান্ত নেয়? মস্তিষ্কের দুই সিস্টেমের রহস্য",
      headingEn = "Why We Make Emotional Mistakes: The Two Systems of the Human Mind",
      writerNameBn = "ড্যানিয়েল কানেমান (Daniel Kahneman)",
      writerNameEn = "Daniel Kahneman",
      topicTitleBn = "সিদ্ধান্ত গ্রহণ ও আচরণগত মনোবিজ্ঞান",
      topicTitleEn = "Decision Making & Behavioral Psychology",
      introBn = "নোবেলজয়ী মনোবিজ্ঞানী ড্যানিয়েল কানেমান তাঁর বিখ্যাত গবেষণায় দেখিয়েছেন যে আমাদের মন আসলে দুটি ভিন্ন পদ্ধতিতে কাজ করে—যাকে তিনি নাম দিয়েছেন 'সিস্টেম ১' (দ্রুত ও আবেগীয় মন) এবং 'সিস্টেম ২' (ধীর ও যুক্তিবাদী মন)। আমাদের দৈনন্দিন জীবনের রাগ, ভুল বোঝাবুঝি এবং হঠকারী সিদ্ধান্তের পেছনে এই দুই সিস্টেমের বিশাল ভূমিকা রয়েছে।",
      introEn = "Nobel Prize-winning psychologist Daniel Kahneman demonstrated that the human mind operates through two distinct modes of thought: 'System 1' (fast, automatic, and emotional) and 'System 2' (slow, deliberate, and logical). Understanding how these two systems work can protect you from impulsive regret and emotional misunderstandings.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. সিস্টেম ১: দ্রুত কিন্তু আবেগপ্রবণ মন",
          subheadingEn = "1. System 1: The Fast, Automatic, Emotional Mind",
          bodyBn = "সিস্টেম ১ কোনো পরিশ্রম ছাড়াই মুহূর্তের মধ্যে সিদ্ধান্ত নেয়। যেমন—কারো মুখের ভাব দেখে ধরে নেওয়া যে সে আমাকে অপছন্দ করে, কিংবা রেগে গিয়ে হঠাৎ কড়া কথা বলে ফেলা। বিপদের সময় সিস্টেম ১ আমাদের বাঁচায় ঠিকই, কিন্তু সম্পর্কের ক্ষেত্রে বা জীবনের বড় সিদ্ধান্তে এটি প্রায়ই ভুল অনুমান করে বসে।",
          bodyEn = "System 1 operates effortlessly and instantly. It jumps to conclusions—such as assuming someone dislikes you from a brief facial expression, or snapping in anger during an argument. While System 1 is useful in emergencies, it frequently misleads us in relationships and complex life decisions."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. সিস্টেম ২: ধীর, শান্ত ও চিন্তাশীল মন",
          subheadingEn = "2. System 2: The Slow, Calm, Logical Mind",
          bodyBn = "সিস্টেম ২ হলো আমাদের মস্তিষ্কের বিচক্ষণ অংশ। এটি কোনো সিদ্ধান্ত নেওয়ার আগে তথ্য যাচাই করে, অন্য মানুষের অবস্থান বোঝার চেষ্টা করে এবং ফলাফল নিয়ে ভাবে। তবে আমাদের মস্তিষ্ক অলস হওয়ায় সে সবসময় সিস্টেম ১-এর কথায় চলে যেতে চায়।",
          bodyEn = "System 2 is the thoughtful, analytical part of your brain. It pauses to check facts, considers the other person's perspective, and weighs long-term consequences. Because System 2 requires conscious effort, our brain often defaults to System 1 unless we intentionally slow down."
        ),
        PsychologyArticleSection(
          subheadingBn = "৩. আবেগের মুহূর্তে সঠিক সিদ্ধান্ত নেওয়ার নিয়ম",
          subheadingEn = "3. How to Make Wise Decisions When Emotions Run High",
          bodyBn = "যখনই আপনার খুব রাগ, ভয় বা অভিমান হবে—তখন বুঝবেন আপনার 'সিস্টেম ১' চালকের আসনে বসে আছে। ওই মুহূর্তে কাউকে মেসেজ পাঠানো বা বড় কোনো সিদ্ধান্ত নেওয়া থেকে মাত্র ১০ মিনিট বিরত থাকুন। একটু সময় নিলেই আপনার 'সিস্টেম ২' সক্রিয় হবে এবং আপনি অনেক শান্ত ও সঠিক সিদ্ধান্ত নিতে পারবেন।",
          bodyEn = "Whenever you feel intense anger, hurt, or panic, recognize that System 1 has taken the wheel. Pause for at least 10 minutes before sending an emotional message or making a major decision. That brief pause gives System 2 time to wake up and guide you wisely."
        )
      ),
      keyTakeawaysBn = listOf(
        "রাগ বা তীব্র কষ্টের সময় তাৎক্ষণিক কোনো বড় সিদ্ধান্ত নেবেন না।",
        "কারো আচরণের পেছনে নিজের মনগড়া ধারণা না বানিয়ে শান্তভাবে আসল কারণ জানুন।",
        "গুরুত্বপূর্ণ কথার জবাব দেওয়ার আগে ১০ সেকেন্ড থেমে চিন্তা করার অভ্যাস গড়ে তুলুন।"
      ),
      keyTakeawaysEn = listOf(
        "Never make permanent life decisions during temporary waves of anger or sadness.",
        "Verify facts calmly instead of trusting instant negative assumptions about others.",
        "Pause for 10 seconds before replying in heated conversations to activate logical thinking."
      )
    ),

    // Article 3
    PsychologyArticle(
      id = "frankl_meaning_in_hard_times",
      headingBn = "কঠিন সময়েও মনের শক্তি ধরে রাখা ও বেঁচে থাকার অর্থ খুঁজে পাওয়া",
      headingEn = "Finding Inner Strength and Meaning During Life's Hardest Moments",
      writerNameBn = "ডা. ভিক্টর ফ্র্যাঙ্কল (Dr. Viktor E. Frankl)",
      writerNameEn = "Dr. Viktor E. Frankl",
      topicTitleBn = "জীবনের অর্থ ও মানসিক দৃঢ়তা (Logotherapy)",
      topicTitleEn = "Meaning of Life & Emotional Resilience",
      introBn = "বিখ্যাত মনোরোগ বিশেষজ্ঞ ডা. ভিক্টর ফ্র্যাঙ্কল তাঁর জীবনের চরমতম প্রতিকূল অভিজ্ঞতা থেকে 'লোগোথেরাপি' (Logotherapy) তত্ত্ব প্রতিষ্ঠা করেন। তাঁর মূল কথা ছিল—মানুষের জীবনের সবচেয়ে বড় চালিকাশক্তি হলো জীবনের একটি অর্থ বা উদ্দেশ্য খুঁজে পাওয়া। যখন মানুষ জানে সে কেন বাঁচে, তখন সে যেকোনো কঠিন পরিস্থিতি অতিক্রম করতে পারে।",
      introEn = "Renowned psychiatrist Dr. Viktor E. Frankl founded Logotherapy based on a profound psychological truth: the deepest human drive is the search for meaning. When a person finds a 'why' to live for, they can endure and overcome almost any 'how'.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. পরিস্থিতি আমাদের নিয়ন্ত্রণে না থাকলেও আমাদের দৃষ্টিভঙ্গি আমাদের হাতে",
          subheadingEn = "1. You Always Have the Freedom to Choose Your Attitude",
          bodyBn = "জীবনে এমন অনেক কষ্ট বা ক্ষতি আসে যা আমরা বদলাতে পারি না। ডা. ফ্র্যাঙ্কল বলেছেন—মানুষের কাছ থেকে সবকিছু কেড়ে নেওয়া গেলেও একটি স্বাধীনতা কেউ কেড়ে নিতে পারে না, তা হলো ওই পরিস্থিতিতে সে কীভাবে সাড়া দেবে তা বেছে নেওয়ার স্বাধীনতা।",
          bodyEn = "Life inevitably brings losses and hardships we did not choose. Yet Dr. Frankl taught that between what happens to us and how we respond lies our greatest human freedom—the power to choose our attitude even in painful circumstances."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. জীবনের ছোট ছোট কাজের মধ্যেই অর্থ লুকিয়ে থাকে",
          subheadingEn = "2. Meaning Is Found in Everyday Moments and Care",
          bodyBn = "জীবনের অর্থ খুঁজে পাওয়ার জন্য বিশাল কিছু করার প্রয়োজন নেই। প্রিয় কোনো মানুষের যত্ন নেওয়া, নিজের দায়িত্বটুকু সততার সাথে পালন করা, নতুন কিছু শেখা কিংবা নিজের কষ্ট থেকে শিক্ষা নিয়ে আরও মানবিক মানুষ হয়ে ওঠা—এগুলোর প্রতিটিই আমাদের জীবনকে অর্থপূর্ণ করে তোলে।",
          bodyEn = "Finding meaning does not require grand fame. Meaning is discovered in three simple ways: by creating something or doing purposeful work, by loving and caring for someone, and by growing wiser and kinder through the challenges we face."
        )
      ),
      keyTakeawaysBn = listOf(
        "জীবনের কঠিন সময়গুলো আপনাকে ভেঙে ফেলার জন্য নয়, বরং ভেতর থেকে আরও শক্তিশালী করার জন্য আসে।",
        "প্রতিদিন অন্তত একটি ছোট কাজ করুন যা আপনার বা অন্য কারো জীবনে ইতিবাচক মূল্য যোগ করে।",
        "পরিস্থিতি যেমনই হোক, নিজের আশা ও ইতিবাচক দৃষ্টিভঙ্গি কখনো হারাবেন না।"
      ),
      keyTakeawaysEn = listOf(
        "Hardships do not define your worth; how you grow through them shapes your strength.",
        "Dedicate yourself to at least one meaningful task or act of kindness each day.",
        "Hold onto hope and personal purpose even when external circumstances feel uncertain."
      )
    ),

    // Article 4
    PsychologyArticle(
      id = "beck_negative_thoughts_cbt",
      headingBn = "মনের ভেতরের নেতিবাচক চিন্তা চেনার ও বদলানোর বৈজ্ঞানিক কৌশল (CBT)",
      headingEn = "How to Recognize and Reframe Automatic Negative Thoughts (CBT)",
      writerNameBn = "ডা. অ্যারন টি. বেক (Dr. Aaron T. Beck)",
      writerNameEn = "Dr. Aaron T. Beck",
      topicTitleBn = "কগনিটিভ বিহেভিওরাল থেরাপি ও আত্ম-উন্নয়ন",
      topicTitleEn = "Cognitive Behavioral Therapy (CBT)",
      introBn = "কগনিটিভ থেরাপির জনক ডা. অ্যারন বেক আবিষ্কার করেছিলেন যে—কোনো ঘটনা সরাসরি আমাদের মন খারাপ করে না, বরং ওই ঘটনাটিকে আমরা মনে মনে কীভাবে ব্যাখ্যা করি, তার ওপরই আমাদের অনুভূতি নির্ভর করে। আমাদের মনে অজান্তেই কিছু নেতিবাচক চিন্তা (Automatic Negative Thoughts) আসে, যা চিনতে পারলে বিষণ্ণতা ও উদ্বেগ অনেকাংশে কমানো সম্ভব।",
      introEn = "Dr. Aaron T. Beck, the father of Cognitive Behavioral Therapy (CBT), discovered a life-changing principle: events themselves do not directly cause our distress—rather, the way we interpret those events shapes how we feel. Learning to spot Automatic Negative Thoughts (ANTs) can dramatically reduce anxiety and sadness.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. সব অথবা শূন্য চিন্তা (All-or-Nothing Thinking)",
          subheadingEn = "1. All-or-Nothing Thinking",
          bodyBn = "একটি পরীক্ষায় বা কাজে একটু খারাপ করলেই মনে করা যে 'আমি পুরোপুরি ব্যর্থ, আমাকে দিয়ে কিছুই হবে না'—এটি একটি চরম চিন্তার ত্রুটি। বাস্তব জীবনে কোনো মানুষই ১০০% নিখুঁত নয়। একটি ভুল মানে কেবল একটি শেখার সুযোগ, পুরো জীবনের ব্যর্থতা নয়।",
          bodyEn = "Believing 'If I don't succeed completely, I am a total failure' is one of the most common cognitive distortions. Real life exists in shades of progress, not perfection. One setback is simply feedback for growth, never a verdict on your entire life."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. মন পড়া ও ভবিষ্যৎ গণনা (Mind Reading & Catastrophizing)",
          subheadingEn = "2. Mind Reading and Catastrophizing",
          bodyBn = "কেউ ব্যস্ততার কারণে ফোন না ধরলে আমরা ভেবে বসি 'সে নিশ্চয়ই আমাকে অবহেলা করছে' (Mind Reading), কিংবা ছোট কোনো সমস্যার কথা ভেবে ধরে নিই সবচেয়ে ভয়ংকর বিপদটাই ঘটবে (Catastrophizing)। নিজের চিন্তাকেই ধ্রুব সত্য মনে না করে নিজেকে প্রশ্ন করুন: 'আমার এই ধারণার পক্ষে বাস্তব প্রমাণ কী?'",
          bodyEn = "Assuming someone is ignoring you just because they replied late (Mind Reading), or expecting the absolute worst outcome from a minor issue (Catastrophizing), creates unnecessary pain. Always ask yourself: 'What is the actual evidence for this thought?'"
        ),
        PsychologyArticleSection(
          subheadingBn = "৩. নিজের সাথে প্রিয় বন্ধুর মতো কথা বলুন",
          subheadingEn = "3. Speak to Yourself Like a Compassionate Friend",
          bodyBn = "আপনার কোনো প্রিয় বন্ধু বিপদে পড়লে আপনি তাকে যেভাবে সাহস ও সান্ত্বনা দিতেন, নিজের ভুল বা কষ্টের সময়ও নিজেকে ঠিক সেভাবেই সহানুভূতির সাথে কথা বলুন। নিজেকে দোষারোপ করা বন্ধ করলেই মনের বোঝা অর্ধেক কমে যায়।",
          bodyEn = "When a close friend makes a mistake, you offer them encouragement—yet we often criticize ourselves harshly. Replace harsh self-blame with the same realistic kindness you would give someone you care about."
        )
      ),
      keyTakeawaysBn = listOf(
        "মনে আসা প্রতিটি নেতিবাচক চিন্তাই সত্য নয়—চিন্তাকে যাচাই করতে শিখুন।",
        "একটি ছোট ভুলের কারণে নিজের পুরো যোগ্যতাকে ছোট করে দেখবেন না।",
        "নিজের সবচেয়ে কড়া সমালোচক না হয়ে নিজের সবচেয়ে ভালো বন্ধু হোন।"
      ),
      keyTakeawaysEn = listOf(
        "Thoughts are mental events, not automatic facts—question negative assumptions.",
        "Never judge your entire capability by a single mistake or difficult day.",
        "Replace harsh self-criticism with balanced, compassionate self-talk."
      )
    ),

    // Article 5
    PsychologyArticle(
      id = "emotional_loneliness_healing",
      headingBn = "মানুষের ভিড়েও কেন নিজেকে একা লাগে এবং মনের শূন্যতা দূর করার উপায়",
      headingEn = "Why We Feel Lonely in a Crowd and How to Heal Inner Emptiness",
      writerNameBn = "ড. সিফাত খান",
      writerNameEn = "Dr. Sifat Khan",
      topicTitleBn = "একাকীত্ব ও আবেগীয় সংযোগ",
      topicTitleEn = "Loneliness & Emotional Connection",
      introBn = "আমাদের চারপাশে পরিবার, বন্ধু বা সোশ্যাল মিডিয়ার শত শত মানুষ থাকা সত্ত্বেও অনেক সময় বুকের ভেতর অদ্ভুত এক শূন্যতা ও একাকীত্ব কাজ করে। মনোবিজ্ঞানে একে বলা হয় 'ইমোশনাল লোনলিনেস' (Emotional Loneliness)। এর কারণ মানুষের অভাব নয়, বরং মনের গভীরের কথাগুলো বোঝার মতো নিরাপদ সংযোগের অভাব।",
      introEn = "Even when surrounded by family, friends, or hundreds of social media connections, many people experience a quiet ache of inner emptiness. In psychology, this is called emotional loneliness—caused not by a lack of people around you, but by a lack of feeling truly seen and understood.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. হাসিমুখের আড়ালে কষ্ট লুকিয়ে রাখার ক্লান্তি (Emotional Masking)",
          subheadingEn = "1. The Exhaustion of Emotional Masking",
          bodyBn = "আমরা অনেকেই অন্যকে বিরক্ত না করার জন্য বা নিজেকে শক্ত দেখানোর জন্য সবসময় মুখে হাসি ধরে রাখি এবং বলি 'আমি ভালো আছি'। কিন্তু দীর্ঘদিন নিজের আসল কষ্ট, ভয় বা অভিমান চেপে রাখলে মানুষ নিজের কাছ থেকেই বিচ্ছিন্ন হয়ে পড়ে এবং ভেতরে গভীর ক্লান্তি তৈরি হয়।",
          bodyEn = "Many of us wear a constant smile and say 'I'm fine' to avoid burdening others or appearing vulnerable. However, suppressing your genuine feelings for months or years disconnects you from yourself and leads to deep emotional exhaustion."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. সবার আগে নিজের অনুভূতির সাথে সংযোগ গড়ে তুলুন",
          subheadingEn = "2. Reconnect With Your Own Feelings First",
          bodyBn = "একাকীত্ব দূর করার প্রথম ধাপ হলো নিজের অনুভূতিকে অস্বীকার না করা। মন খারাপ লাগলে নিজেকে বলুন—'আজ আমার মন খারাপ লাগছে, এবং এভাবে অনুভব করাটা স্বাভাবিক।' নিজের পছন্দের ছোট কাজ, প্রকৃতির সান্নিধ্য এবং অন্তত একজন বিশ্বস্ত মানুষ বা থেরাপিস্টের কাছে মনের কথা খুলে বলা ভেতরের শূন্যতা পূরণ করতে সাহায্য করে।",
          bodyEn = "Healing loneliness starts with validating your own emotions instead of judging them. Acknowledge how you truly feel without shame, nurture small hobbies that bring peace, and share your honest thoughts in a safe, non-judgmental space."
        )
      ),
      keyTakeawaysBn = listOf(
        "সবসময় সবার কাছে নিজেকে নিখুঁত ও শক্ত দেখানোর চাপ থেকে নিজেকে মুক্তি দিন।",
        "সংখ্যার চেয়ে সম্পর্কের গভীরতাকে গুরুত্ব দিন—যাদের কাছে মন খুলে কথা বলা যায়।",
        "মনের কথা চেপে না রেখে ডায়েরিতে লিখুন বা নিরাপদ পরামর্শকের সাথে শেয়ার করুন।"
      ),
      keyTakeawaysEn = listOf(
        "Release the pressure to always appear strong and unaffected in front of everyone.",
        "Value emotional depth and authenticity over superficial social interactions.",
        "Express your unspoken feelings through journaling or confidential counseling."
      )
    ),

    // Article 6
    PsychologyArticle(
      id = "dweck_growth_mindset_confidence",
      headingBn = "ব্যর্থতার ভয় জয় করে আত্মবিশ্বাস গড়ার মনোবিজ্ঞান (Growth Mindset)",
      headingEn = "Overcoming Fear of Failure: The Psychology of Growth Mindset",
      writerNameBn = "ড. ক্যারল ডুয়েক (Dr. Carol S. Dweck)",
      writerNameEn = "Dr. Carol S. Dweck",
      topicTitleBn = "আত্মবিশ্বাস ও গ্রোথ মাইন্ডসেট",
      topicTitleEn = "Self-Confidence & Growth Mindset",
      introBn = "স্ট্যানফোর্ড বিশ্ববিদ্যালয়ের প্রখ্যাত মনোবিজ্ঞানী ড. ক্যারল ডুয়েক দীর্ঘ গবেষণার মাধ্যমে দেখিয়েছেন—মানুষের সফলতা ও মানসিক শান্তির মূল চাবিকাঠি কেবল জন্মগত মেধা নয়, বরং সে নিজের ক্ষমতাকে কীভাবে দেখে (Mindset)। যাঁরা 'গ্রোথ মাইন্ডসেট' ধারণ করেন, তাঁরা ব্যর্থতাকে ভয় না পেয়ে সেটিকে শেখার সিঁড়ি হিসেবে দেখেন।",
      introEn = "Stanford psychologist Dr. Carol S. Dweck's landmark research revealed that our resilience and fulfillment depend heavily on our mindset. People with a 'Growth Mindset' believe abilities and emotional strength can be developed through effort, turning setbacks into stepping stones.",
      sections = listOf(
        PsychologyArticleSection(
          subheadingBn = "১. ফিক্সড মাইন্ডসেট বনাম গ্রোথ মাইন্ডসেট",
          subheadingEn = "1. Fixed Mindset vs. Growth Mindset",
          bodyBn = "ফিক্সড মাইন্ডসেটের মানুষ মনে করে মেধা ও গুণাবলি জন্মগতভাবে ফিক্সড—তাই কোনো কাজে ব্যর্থ হলে তারা ভাবে 'আমি অযোগ্য'। অন্যদিকে গ্রোথ মাইন্ডসেটের মানুষ মনে করে মস্তিষ্ক পেশীর মতো—চেষ্টা ও অনুশীলনের মাধ্যমে যেকোনো দক্ষতা ও মানসিক শক্তি বাড়ানো সম্ভব।",
          bodyEn = "In a Fixed Mindset, people view setbacks as proof that they aren't good enough. In a Growth Mindset, challenges are seen as workouts for the brain—every struggle strengthens your capability and wisdom over time."
        ),
        PsychologyArticleSection(
          subheadingBn = "২. 'আমি পারছি না'-এর বদলে 'আমি এখনো শিখছি' বলার জাদু",
          subheadingEn = "2. The Power of the Word 'Yet'",
          bodyBn = "যখনই কোনো কাজ কঠিন মনে হবে, তখন নিজেকে 'আমি এটা পারব না' না বলে বলুন—'আমি এখনো এটা পুরোপুরি শিখতে পারিনি, তবে চেষ্টা করছি।' এই ছোট একটি শব্দের পরিবর্তন আপনার মস্তিষ্কের হতাশা কমিয়ে নতুনভাবে চেষ্টা করার সাহস জোগায়।",
          bodyEn = "Whenever you face a difficult goal, replace 'I can't do this' with 'I haven't mastered this yet.' Adding that single word transforms a dead-end of self-doubt into an open path of learning and hope."
        )
      ),
      keyTakeawaysBn = listOf(
        "অন্যের সাথে নিজের তুলনা না করে গতকালের আপনার চেয়ে আজ কিছুটা এগিয়ে যাওয়ার চেষ্টা করুন।",
        "ভুল বা ব্যর্থতা মানে আপনি বোকা নন—এর মানে আপনি নতুন কিছু চেষ্টা করছেন।",
        "নিজের ছোট ছোট উন্নতিগুলোকেও স্বীকৃতি দিন এবং নিজেকে উৎসাহ দিন।"
      ),
      keyTakeawaysEn = listOf(
        "Compare yourself only to who you were yesterday, never to someone else's highlight reel.",
        "Making mistakes is proof that you are trying and growing, not failing.",
        "Celebrate small daily improvements to build lasting, genuine self-confidence."
      )
    )
  )
}
