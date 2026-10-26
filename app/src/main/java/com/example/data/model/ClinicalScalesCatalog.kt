package com.example.data.model

import com.example.ui.i18n.AppLanguage

enum class TestCategory {
  CLINICAL_SCALES,
  PSYCHOLOGICAL_TESTS
}

object ClinicalScalesCatalog {

  private fun phq9AndGad7Options(): List<PsychChoiceOption> = listOf(
    PsychChoiceOption(0, "একদমই না (০ দিন)", "Not at all (0 days)"),
    PsychChoiceOption(1, "কয়েক দিন (১-৬ দিন)", "Several days (1-6 days)"),
    PsychChoiceOption(2, "অর্ধেকের বেশি দিন (৭+ দিন)", "More than half the days (7+ days)"),
    PsychChoiceOption(3, "প্রায় প্রতিদিনই", "Nearly every day")
  )

  private fun pss10StandardOptions(): List<PsychChoiceOption> = listOf(
    PsychChoiceOption(0, "কখনোই না (Never)", "Never"),
    PsychChoiceOption(1, "প্রায় কখনোই না (Almost Never)", "Almost Never"),
    PsychChoiceOption(2, "মাঝে মাঝে (Sometimes)", "Sometimes"),
    PsychChoiceOption(3, "প্রায়ই (Fairly Often)", "Fairly Often"),
    PsychChoiceOption(4, "খুব ঘনঘন (Very Often)", "Very Often")
  )

  // Reverse-scored positive items in PSS-10 (Questions 4, 5, 7, 8): Never=4, Almost Never=3, Sometimes=2, Fairly Often=1, Very Often=0
  private fun pss10ReverseOptions(): List<PsychChoiceOption> = listOf(
    PsychChoiceOption(4, "কখনোই না (Never)", "Never"),
    PsychChoiceOption(3, "প্রায় কখনোই না (Almost Never)", "Almost Never"),
    PsychChoiceOption(2, "মাঝে মাঝে (Sometimes)", "Sometimes"),
    PsychChoiceOption(1, "প্রায়ই (Fairly Often)", "Fairly Often"),
    PsychChoiceOption(0, "খুব ঘনঘন (Very Often)", "Very Often")
  )

  val clinicalTests: List<PsychologicalTestDefinition> = listOf(
    // 1. PHQ-9 (Patient Health Questionnaire-9 — Exact 9 Clinical Questions, Score 0–27)
    PsychologicalTestDefinition(
      key = "PHQ-9",
      category = TestCategory.CLINICAL_SCALES,
      maxPossibleScore = 27,
      titleBn = "PHQ-9 বিষণ্ণতা নির্ণয় স্কেল (Patient Health Questionnaire-9)",
      titleEn = "PHQ-9 Clinical Depression Scale (9 Items)",
      shortTitleBn = "PHQ-9 (বিষণ্ণতা)",
      shortTitleEn = "PHQ-9 (Depression)",
      subtitleBn = "গত ২ সপ্তাহে নিচের সমস্যাগুলো আপনাকে কতটুকু ভুগিয়েছে? (আন্তর্জাতিক DSM-5 স্ট্যান্ডার্ড ৯টি প্রশ্ন • পূর্ণমান ২৭)",
      subtitleEn = "Over the last 2 weeks, how often have you been bothered by any of the following problems? (Official 9 items • Max 27)",
      defaultBadgeBn = "৯টি প্রশ্ন • স্কেল ০-২৭",
      defaultBadgeEn = "9 Questions • Scale 0-27",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "PHQ-9 • প্রশ্ন ১ (Anhedonia)",
          dimensionEn = "PHQ-9 • Item 1 (Anhedonia)",
          questionBn = "গত ২ সপ্তাহে: কাজকর্মে খুব কম আগ্রহ বা আনন্দ পাওয়া?",
          questionEn = "Over the last 2 weeks: Little interest or pleasure in doing things?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "PHQ-9 • প্রশ্ন ২ (Depressed Mood)",
          dimensionEn = "PHQ-9 • Item 2 (Depressed Mood)",
          questionBn = "গত ২ সপ্তাহে: মনমরা লাগা, বিষণ্ণ বোধ করা অথবা আশাহীন মনে হওয়া?",
          questionEn = "Over the last 2 weeks: Feeling down, depressed, or hopeless?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "PHQ-9 • প্রশ্ন ৩ (Sleep Disturbance)",
          dimensionEn = "PHQ-9 • Item 3 (Sleep Disturbance)",
          questionBn = "গত ২ সপ্তাহে: ঘুমিয়ে পড়তে বা ঘুম ধরে রাখতে সমস্যা হওয়া, অথবা অতিরিক্ত ঘুমানো?",
          questionEn = "Over the last 2 weeks: Trouble falling or staying asleep, or sleeping too much?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "PHQ-9 • প্রশ্ন ৪ (Fatigue / Low Energy)",
          dimensionEn = "PHQ-9 • Item 4 (Fatigue / Low Energy)",
          questionBn = "গত ২ সপ্তাহে: ক্লান্ত বোধ করা বা শরীরে খুব কম শক্তি পাওয়া?",
          questionEn = "Over the last 2 weeks: Feeling tired or having little energy?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "PHQ-9 • প্রশ্ন ৫ (Appetite Change)",
          dimensionEn = "PHQ-9 • Item 5 (Appetite Change)",
          questionBn = "গত ২ সপ্তাহে: ক্ষুধামন্দা বা খাবারে অরুচি হওয়া, অথবা অতিরিক্ত খাওয়া?",
          questionEn = "Over the last 2 weeks: Poor appetite or overeating?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "PHQ-9 • প্রশ্ন ৬ (Guilt / Worthlessness)",
          dimensionEn = "PHQ-9 • Item 6 (Worthlessness)",
          questionBn = "গত ২ সপ্তাহে: নিজের সম্পর্কে খারাপ লাগা—মনে হওয়া যে আপনি একজন ব্যর্থ মানুষ বা নিজের ও পরিবারের মাথা নিচু করেছেন?",
          questionEn = "Over the last 2 weeks: Feeling bad about yourself — or that you are a failure or have let yourself or your family down?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "PHQ-9 • প্রশ্ন ৭ (Concentration)",
          dimensionEn = "PHQ-9 • Item 7 (Concentration)",
          questionBn = "গত ২ সপ্তাহে: কোনো বিষয়ে মনোযোগ দিতে সমস্যা হওয়া, যেমন পত্রিকা পড়া বা টেলিভিশন দেখা?",
          questionEn = "Over the last 2 weeks: Trouble concentrating on things, such as reading the newspaper or watching television?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 8,
          dimensionBn = "PHQ-9 • প্রশ্ন ৮ (Psychomotor Change)",
          dimensionEn = "PHQ-9 • Item 8 (Psychomotor)",
          questionBn = "গত ২ সপ্তাহে: এত ধীরগতিতে চলাফেরা বা কথা বলা যা অন্যদের চোখে পড়েছে? অথবা এর উল্টোটা—অতিরিক্ত ছটফট করা বা অস্থির হয়ে এদিক-ওদিক পায়চারি করা?",
          questionEn = "Over the last 2 weeks: Moving or speaking so slowly that other people could have noticed? Or the opposite — being so fidgety or restless that you have been moving around a lot more than usual?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 9,
          dimensionBn = "PHQ-9 • প্রশ্ন ৯ (Self-Harm Ideation)",
          dimensionEn = "PHQ-9 • Item 9 (Self-Harm Ideation)",
          questionBn = "গত ২ সপ্তাহে: এমন চিন্তা আসা যে বেঁচে না থাকলেই ভালো হতো অথবা কোনোভাবে নিজের ক্ষতি করার চিন্তা মাথায় আসা?",
          questionEn = "Over the last 2 weeks: Thoughts that you would be better off dead, or of hurting yourself in some way?",
          options = phq9AndGad7Options()
        )
      )
    ),

    // 2. GAD-7 (Generalized Anxiety Disorder-7 — Exact 7 Clinical Questions, Score 0–21)
    PsychologicalTestDefinition(
      key = "GAD-7",
      category = TestCategory.CLINICAL_SCALES,
      maxPossibleScore = 21,
      titleBn = "GAD-7 উদ্বেগ নির্ণয় স্কেল (Generalized Anxiety Disorder-7)",
      titleEn = "GAD-7 Clinical Anxiety Scale (7 Items)",
      shortTitleBn = "GAD-7 (উদ্বেগ)",
      shortTitleEn = "GAD-7 (Anxiety)",
      subtitleBn = "গত ২ সপ্তাহে নিচের উদ্বেগজনিত লক্ষণগুলো আপনাকে কতটুকু ভুগিয়েছে? (আন্তর্জাতিক স্ট্যান্ডার্ড ৭টি প্রশ্ন • পূর্ণমান ২১)",
      subtitleEn = "Over the last 2 weeks, how often have you been bothered by the following anxiety problems? (Official 7 items • Max 21)",
      defaultBadgeBn = "৭টি প্রশ্ন • স্কেল ০-২১",
      defaultBadgeEn = "7 Questions • Scale 0-21",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "GAD-7 • প্রশ্ন ১ (Nervousness)",
          dimensionEn = "GAD-7 • Item 1 (Nervousness)",
          questionBn = "গত ২ সপ্তাহে: নার্ভাস বা ভীত লাগা, উদ্বেগ অনুভব করা কিংবা মনের ভেতর টানটান উত্তেজনা বোধ করা?",
          questionEn = "Over the last 2 weeks: Feeling nervous, anxious, or on edge?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "GAD-7 • প্রশ্ন ২ (Uncontrollable Worry)",
          dimensionEn = "GAD-7 • Item 2 (Uncontrollable Worry)",
          questionBn = "গত ২ সপ্তাহে: দুশ্চিন্তা থামাতে না পারা অথবা দুশ্চিন্তার ওপর নিজের কোনো নিয়ন্ত্রণ না থাকা?",
          questionEn = "Over the last 2 weeks: Not being able to stop or control worrying?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "GAD-7 • প্রশ্ন ৩ (Excessive Worry)",
          dimensionEn = "GAD-7 • Item 3 (Excessive Worry)",
          questionBn = "গত ২ সপ্তাহে: নানারকম ভিন্ন ভিন্ন বিষয় নিয়ে অতিরিক্ত দুশ্চিন্তা করা?",
          questionEn = "Over the last 2 weeks: Worrying too much about different things?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "GAD-7 • প্রশ্ন ৪ (Trouble Relaxing)",
          dimensionEn = "GAD-7 • Item 4 (Trouble Relaxing)",
          questionBn = "গত ২ সপ্তাহে: মন ও শরীরকে শান্ত করতে বা রিল্যাক্স করতে সমস্যা হওয়া?",
          questionEn = "Over the last 2 weeks: Trouble relaxing?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "GAD-7 • প্রশ্ন ৫ (Restlessness)",
          dimensionEn = "GAD-7 • Item 5 (Restlessness)",
          questionBn = "গত ২ সপ্তাহে: এতটাই অস্থির লাগা যে এক জায়গায় স্থির হয়ে বসে থাকা কঠিন হয়ে পড়া?",
          questionEn = "Over the last 2 weeks: Being so restless that it is hard to sit still?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "GAD-7 • প্রশ্ন ৬ (Irritability)",
          dimensionEn = "GAD-7 • Item 6 (Irritability)",
          questionBn = "গত ২ সপ্তাহে: খুব সহজেই বিরক্ত হয়ে যাওয়া বা অল্পতেই মেজাজ খিটখিটে হওয়া?",
          questionEn = "Over the last 2 weeks: Becoming easily annoyed or irritable?",
          options = phq9AndGad7Options()
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "GAD-7 • প্রশ্ন ৭ (Impending Doom)",
          dimensionEn = "GAD-7 • Item 7 (Impending Doom)",
          questionBn = "গত ২ সপ্তাহে: এমন ভয় লাগা যে এই বুঝি ভয়ংকর বা খারাপ কিছু একটা ঘটতে চলেছে?",
          questionEn = "Over the last 2 weeks: Feeling afraid, as if something awful might happen?",
          options = phq9AndGad7Options()
        )
      )
    ),

    // 3. PSS-10 (Perceived Stress Scale-10 — Exact 10 Clinical Questions, Score 0–40)
    PsychologicalTestDefinition(
      key = "PSS-10",
      category = TestCategory.CLINICAL_SCALES,
      maxPossibleScore = 40,
      titleBn = "PSS-10 মানসিক চাপ পরিমাপক স্কেল (Perceived Stress Scale-10)",
      titleEn = "PSS-10 Clinical Perceived Stress Scale (10 Items)",
      shortTitleBn = "PSS-10 (মানসিক চাপ)",
      shortTitleEn = "PSS-10 (Stress)",
      subtitleBn = "গত ১ মাসে আপনার অনুভূতি ও মানসিক চাপের মাত্রা কেমন ছিল? (শেলডন কোহেনের ১০টি অরিজিনাল প্রশ্ন • পূর্ণমান ৪০)",
      subtitleEn = "In the last month, how often have you felt or thought a certain way? (Official 10 items • Max 40)",
      defaultBadgeBn = "১০টি প্রশ্ন • স্কেল ০-৪০",
      defaultBadgeEn = "10 Questions • Scale 0-40",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "PSS-10 • প্রশ্ন ১ (Unexpected Upset)",
          dimensionEn = "PSS-10 • Item 1 (Unexpected Upset)",
          questionBn = "গত ১ মাসে কতবার এমন হয়েছে যে অপ্রত্যাশিত কোনো ঘটনা ঘটায় আপনি মানসিকভাবে খুব বিচলিত বা বিপর্যস্ত হয়ে পড়েছেন?",
          questionEn = "In the last month, how often have you been upset because of something that happened unexpectedly?",
          options = pss10StandardOptions()
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "PSS-10 • প্রশ্ন ২ (Loss of Control)",
          dimensionEn = "PSS-10 • Item 2 (Loss of Control)",
          questionBn = "গত ১ মাসে কতবার আপনার মনে হয়েছে যে জীবনের গুরুত্বপূর্ণ বিষয়গুলো আপনি নিয়ন্ত্রণ করতে পারছেন না?",
          questionEn = "In the last month, how often have you felt that you were unable to control the important things in your life?",
          options = pss10StandardOptions()
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "PSS-10 • প্রশ্ন ৩ (Nervous & Stressed)",
          dimensionEn = "PSS-10 • Item 3 (Nervous & Stressed)",
          questionBn = "গত ১ মাসে কতবার আপনি স্নায়বিক দুর্বলতা (Nervous) এবং তীব্র মানসিক চাপ (Stressed) অনুভব করেছেন?",
          questionEn = "In the last month, how often have you felt nervous and stressed?",
          options = pss10StandardOptions()
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "PSS-10 • প্রশ্ন ৪ (Coping Confidence)",
          dimensionEn = "PSS-10 • Item 4 (Coping Confidence)",
          questionBn = "গত ১ মাসে কতবার আপনার ব্যক্তিগত সমস্যাগুলো সামলানোর ক্ষমতার ওপর নিজের আত্মবিশ্বাস ছিল?",
          questionEn = "In the last month, how often have you felt confident about your ability to handle your personal problems?",
          options = pss10ReverseOptions()
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "PSS-10 • প্রশ্ন ৫ (Things Going Your Way)",
          dimensionEn = "PSS-10 • Item 5 (Things Going Well)",
          questionBn = "গত ১ মাসে কতবার আপনার মনে হয়েছে যে সবকিছু আপনার অনুকূলে বা মনের মতো চলছে?",
          questionEn = "In the last month, how often have you felt that things were going your way?",
          options = pss10ReverseOptions()
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "PSS-10 • প্রশ্ন ৬ (Task Overload)",
          dimensionEn = "PSS-10 • Item 6 (Task Overload)",
          questionBn = "গত ১ মাসে কতবার আপনার মনে হয়েছে যে আপনার যা যা করা প্রয়োজন তার সবগুলোর সাথে আপনি কুলিয়ে উঠতে পারছেন না?",
          questionEn = "In the last month, how often have you found that you could not cope with all the things that you had to do?",
          options = pss10StandardOptions()
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "PSS-10 • প্রশ্ন ৭ (Irritation Control)",
          dimensionEn = "PSS-10 • Item 7 (Irritation Control)",
          questionBn = "গত ১ মাসে কতবার আপনি জীবনের বিরক্তিকর পরিস্থিতিগুলো সফলভাবে নিয়ন্ত্রণ করতে পেরেছেন?",
          questionEn = "In the last month, how often have you been able to control irritations in your life?",
          options = pss10ReverseOptions()
        ),
        PsychTestQuestion(
          id = 8,
          dimensionBn = "PSS-10 • প্রশ্ন ৮ (On Top of Things)",
          dimensionEn = "PSS-10 • Item 8 (Mastery)",
          questionBn = "গত ১ মাসে কতবার আপনার মনে হয়েছে যে সব পরিস্থিতি আপনার নিজের নিয়ন্ত্রণে (On top of things) আছে?",
          questionEn = "In the last month, how often have you felt that you were on top of things?",
          options = pss10ReverseOptions()
        ),
        PsychTestQuestion(
          id = 9,
          dimensionBn = "PSS-10 • প্রশ্ন ৯ (Anger at Uncontrollable)",
          dimensionEn = "PSS-10 • Item 9 (Frustration)",
          questionBn = "গত ১ মাসে কতবার এমন কোনো বিষয়ের কারণে আপনার প্রচণ্ড রাগ হয়েছে যা আপনার নিয়ন্ত্রণের বাইরে ছিল?",
          questionEn = "In the last month, how often have you been angered because of things that were outside of your control?",
          options = pss10StandardOptions()
        ),
        PsychTestQuestion(
          id = 10,
          dimensionBn = "PSS-10 • প্রশ্ন ১০ (Piling Difficulties)",
          dimensionEn = "PSS-10 • Item 10 (Overwhelmed)",
          questionBn = "গত ১ মাসে কতবার আপনার মনে হয়েছে যে সমস্যাগুলো এতটাই স্তূপ হয়ে জমেছে যে তা আপনি আর কাটিয়ে উঠতে পারবেন না?",
          questionEn = "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?",
          options = pss10StandardOptions()
        )
      )
    ),

    // 4. ISI-7 (Insomnia Severity Index-7 — Exact 7 Clinical Questions, Score 0–28)
    PsychologicalTestDefinition(
      key = "ISI-7",
      category = TestCategory.CLINICAL_SCALES,
      maxPossibleScore = 28,
      titleBn = "ISI-7 অনিদ্রা ও ঘুমের প্রকৃত অবস্থা নির্ণয় স্কেল (Insomnia Severity Index)",
      titleEn = "ISI-7 Clinical Insomnia Severity Index (7 Items)",
      shortTitleBn = "ISI-7 (অনিদ্রা)",
      shortTitleEn = "ISI-7 (Insomnia)",
      subtitleBn = "গত ২ সপ্তাহে আপনার ঘুমের সমস্যা ও তার প্রভাব কেমন ছিল? (আন্তর্জাতিক ক্লিনিক্যাল ৭টি প্রশ্ন • পূর্ণমান ২৮)",
      subtitleEn = "Rate the current (last 2 weeks) severity of your insomnia problem(s). (Official 7 items • Max 28)",
      defaultBadgeBn = "৭টি প্রশ্ন • স্কেল ০-২৮",
      defaultBadgeEn = "7 Questions • Scale 0-28",
      questions = listOf(
        PsychTestQuestion(
          id = 1,
          dimensionBn = "ISI-7 • প্রশ্ন ১ (Sleep Onset)",
          dimensionEn = "ISI-7 • Item 1 (Sleep Onset)",
          questionBn = "গত ২ সপ্তাহে: বিছানায় যাওয়ার পর ঘুমিয়ে পড়তে (Difficulty falling asleep) আপনার কতটা সমস্যা হয়েছে?",
          questionEn = "Over the last 2 weeks: How severe has your difficulty falling asleep been?",
          options = listOf(
            PsychChoiceOption(0, "কোনো সমস্যা নেই (০)", "None (0)"),
            PsychChoiceOption(1, "মৃদু সমস্যা (১)", "Mild (1)"),
            PsychChoiceOption(2, "মাঝারি সমস্যা (২)", "Moderate (2)"),
            PsychChoiceOption(3, "তীব্র সমস্যা (৩)", "Severe (3)"),
            PsychChoiceOption(4, "অতি তীব্র সমস্যা (৪)", "Very Severe (4)")
          )
        ),
        PsychTestQuestion(
          id = 2,
          dimensionBn = "ISI-7 • প্রশ্ন ২ (Sleep Maintenance)",
          dimensionEn = "ISI-7 • Item 2 (Staying Asleep)",
          questionBn = "গত ২ সপ্তাহে: একটানা ঘুমিয়ে থাকতে বা মাঝরাতে ঘুম ভেঙে যাওয়ার (Difficulty staying asleep) সমস্যা কতটা ছিল?",
          questionEn = "Over the last 2 weeks: How severe has your difficulty staying asleep been?",
          options = listOf(
            PsychChoiceOption(0, "কোনো সমস্যা নেই (০)", "None (0)"),
            PsychChoiceOption(1, "মৃদু সমস্যা (১)", "Mild (1)"),
            PsychChoiceOption(2, "মাঝারি সমস্যা (২)", "Moderate (2)"),
            PsychChoiceOption(3, "তীব্র সমস্যা (৩)", "Severe (3)"),
            PsychChoiceOption(4, "অতি তীব্র সমস্যা (৪)", "Very Severe (4)")
          )
        ),
        PsychTestQuestion(
          id = 3,
          dimensionBn = "ISI-7 • প্রশ্ন ৩ (Early Awakening)",
          dimensionEn = "ISI-7 • Item 3 (Waking Too Early)",
          questionBn = "গত ২ সপ্তাহে: খুব ভোরে বা সময়ের অনেক আগেই ঘুম ভেঙে যাওয়ার এবং আর ঘুম না আসার সমস্যা কতটা ছিল?",
          questionEn = "Over the last 2 weeks: How severe have your problems waking up too early been?",
          options = listOf(
            PsychChoiceOption(0, "কোনো সমস্যা নেই (০)", "None (0)"),
            PsychChoiceOption(1, "মৃদু সমস্যা (১)", "Mild (1)"),
            PsychChoiceOption(2, "মাঝারি সমস্যা (২)", "Moderate (2)"),
            PsychChoiceOption(3, "তীব্র সমস্যা (৩)", "Severe (3)"),
            PsychChoiceOption(4, "অতি তীব্র সমস্যা (৪)", "Very Severe (4)")
          )
        ),
        PsychTestQuestion(
          id = 4,
          dimensionBn = "ISI-7 • প্রশ্ন ৪ (Sleep Satisfaction)",
          dimensionEn = "ISI-7 • Item 4 (Sleep Satisfaction)",
          questionBn = "আপনার বর্তমান ঘুমের ধরন বা ঘুমের মান নিয়ে আপনি কতটা সন্তুষ্ট বা অসন্তুষ্ট?",
          questionEn = "How satisfied or dissatisfied are you with your current sleep pattern?",
          options = listOf(
            PsychChoiceOption(0, "খুব সন্তুষ্ট (০)", "Very Satisfied (0)"),
            PsychChoiceOption(1, "মোটামুটি সন্তুষ্ট (১)", "Moderately Satisfied (1)"),
            PsychChoiceOption(2, "মাঝারি বা নিরপেক্ষ (২)", "Moderately / Neutral (2)"),
            PsychChoiceOption(3, "অসন্তুষ্ট (৩)", "Dissatisfied (3)"),
            PsychChoiceOption(4, "চরমভাবে অসন্তুষ্ট (৪)", "Very Dissatisfied (4)")
          )
        ),
        PsychTestQuestion(
          id = 5,
          dimensionBn = "ISI-7 • প্রশ্ন ৫ (Noticeable to Others)",
          dimensionEn = "ISI-7 • Item 5 (Noticeable Impact)",
          questionBn = "ঘুমের সমস্যার কারণে আপনার জীবনযাত্রার মান বা সতেজতা যতটা ক্ষতিগ্রস্ত হচ্ছে তা অন্যদের চোখে কতটা ধরা পড়ছে বলে মনে করেন?",
          questionEn = "How noticeable to others do you think your sleep problem is in terms of impairing the quality of your life?",
          options = listOf(
            PsychChoiceOption(0, "একদমই লক্ষণীয় নয় (০)", "Not at all noticeable (0)"),
            PsychChoiceOption(1, "সামান্য লক্ষণীয় (১)", "A little (1)"),
            PsychChoiceOption(2, "কিছুটা লক্ষণীয় (২)", "Somewhat (2)"),
            PsychChoiceOption(3, "অনেকখানি লক্ষণীয় (৩)", "Much (3)"),
            PsychChoiceOption(4, "অত্যন্ত প্রকটভাবে লক্ষণীয় (৪)", "Very much noticeable (4)")
          )
        ),
        PsychTestQuestion(
          id = 6,
          dimensionBn = "ISI-7 • প্রশ্ন ৬ (Sleep Worry/Distress)",
          dimensionEn = "ISI-7 • Item 6 (Sleep Distress)",
          questionBn = "আপনার বর্তমান ঘুমের সমস্যা নিয়ে আপনি কতটা চিন্তিত বা মানসিকভাবে কষ্টে (Worried/Distressed) আছেন?",
          questionEn = "How worried or distressed are you about your current sleep problem?",
          options = listOf(
            PsychChoiceOption(0, "একদমই চিন্তিত নই (০)", "Not at all worried (0)"),
            PsychChoiceOption(1, "সামান্য চিন্তিত (১)", "A little (1)"),
            PsychChoiceOption(2, "কিছুটা চিন্তিত (২)", "Somewhat (2)"),
            PsychChoiceOption(3, "অনেক বেশি চিন্তিত (৩)", "Much (3)"),
            PsychChoiceOption(4, "প্রচণ্ডভাবে চিন্তিত ও ব্যথিত (৪)", "Very much worried (4)")
          )
        ),
        PsychTestQuestion(
          id = 7,
          dimensionBn = "ISI-7 • প্রশ্ন ৭ (Daytime Interference)",
          dimensionEn = "ISI-7 • Item 7 (Daytime Functioning)",
          questionBn = "আপনার ঘুমের সমস্যাটি আপনার দৈনন্দিন কাজকর্মে (যেমন: দিনের বেলা ক্লান্তি, মেজাজ, কাজের ক্ষমতা, মনোযোগ ও স্মৃতিশক্তি) কতটা ব্যাঘাত ঘটাচ্ছে?",
          questionEn = "To what extent do you consider your sleep problem to interfere with your daily functioning (e.g., daytime fatigue, mood, concentration, memory)?",
          options = listOf(
            PsychChoiceOption(0, "একদমই ব্যাঘাত ঘটাচ্ছে না (০)", "Not at all interfering (0)"),
            PsychChoiceOption(1, "সামান্য ব্যাঘাত ঘটাচ্ছে (১)", "A little (1)"),
            PsychChoiceOption(2, "কিছুটা ব্যাঘাত ঘটাচ্ছে (২)", "Somewhat (2)"),
            PsychChoiceOption(3, "অনেক বেশি ব্যাঘাত ঘটাচ্ছে (৩)", "Much (3)"),
            PsychChoiceOption(4, "মারাত্মকভাবে ব্যাঘাত ঘটাচ্ছে (৪)", "Very much interfering (4)")
          )
        )
      )
    )
  )

  fun evaluateClinicalScale(
    test: PsychologicalTestDefinition,
    answers: List<Int>,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    val maxRaw = test.maxPossibleScore.coerceAtLeast(1)
    val rawSum = answers.sum().coerceIn(0, maxRaw)
    val normalizedScore = ((rawSum.toDouble() / maxRaw.toDouble()) * 100.0).toInt().coerceIn(0, 100)

    return when (test.key) {
      "PHQ-9" -> evaluatePhq9(test, rawSum, maxRaw, normalizedScore, answers, patterns, language)
      "GAD-7" -> evaluateGad7(test, rawSum, maxRaw, normalizedScore, patterns, language)
      "PSS-10" -> evaluatePss10(test, rawSum, maxRaw, normalizedScore, patterns, language)
      else -> evaluateIsi7(test, rawSum, maxRaw, normalizedScore, patterns, language)
    }
  }

  private fun evaluatePhq9(
    test: PsychologicalTestDefinition,
    raw: Int,
    maxRaw: Int,
    normalized: Int,
    answers: List<Int>,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    val item9Score = answers.getOrNull(8) ?: 0
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        raw in 0..4 -> ClinicalQuad(
          "বিষণ্ণতা নেই বা ন্যূনতম (None-Minimal Depression • স্কোর: $raw/২৭)",
          "আন্তর্জাতিক PHQ-9 মানদণ্ড অনুযায়ী আপনার মধ্যে ক্লিনিক্যাল বিষণ্ণতার লক্ষণ নেই।",
          "আপনার PHQ-9 স্কোর $raw (২৭-এর মধ্যে)। ক্লিনিক্যাল গাইডলাইন অনুযায়ী ০ থেকে ৪ স্কোর নির্দেশ করে যে আপনার মানসিক স্থিতিশীলতা, ঘুম এবং দৈনন্দিন কাজের আগ্রহ স্বাভাবিক পর্যায়ে রয়েছে।",
          listOf(
            "নিয়মিত ৭-৮ ঘণ্টা ঘুম, সুষম খাদ্য এবং শারীরিক সচলতা বজায় রাখুন।",
            "প্রতিদিনের অনুভূতি ট্র্যাক করতে থেনাটক্স জার্নাল ব্যবহার করুন।",
            "মানসিক চাপ বাড়লে টুলকিট সেকশনের ব্রেথিং অনুশীলন করুন।"
          )
        )
        raw in 5..9 -> ClinicalQuad(
          "মৃদু বিষণ্ণতা (Mild Depression • স্কোর: $raw/২৭)",
          "PHQ-9 স্কেলে ৫–৯ স্কোর প্রাথমিক বা মৃদু বিষণ্ণতার ইঙ্গিত দেয়।",
          "আপনার PHQ-9 স্কোর $raw/২৭। এটি বোঝায় যে সাম্প্রতিক সময়ে কিছু মানসিক চাপ বা মন খারাপের কারণে আপনার শক্তি ও আগ্রহে সামান্য ভাটা পড়েছে। এ পর্যায়ে সচেতন জীবনযাত্রা ও সাইকোলজিক্যাল সাপোর্ট নিলে দ্রুতই স্বাভাবিক অবস্থায় ফেরা সম্ভব।",
          listOf(
            "ড. সিফাত খানের সাথে CBT চ্যাট সেশনে আপনার মনের কথা শেয়ার করুন।",
            "নিজেকে ঘরবন্দী না রেখে প্রতিদিন অন্তত ২০ মিনিট হাঁটাহাঁটি ও রোদে সময় কাটান।",
            "২ সপ্তাহ পর পুনরায় PHQ-9 পরীক্ষা দিয়ে আপনার অগ্রগতি যাচাই করুন।"
          )
        )
        raw in 10..14 -> ClinicalQuad(
          "মাঝারি ক্লিনিক্যাল বিষণ্ণতা (Moderate Depression • স্কোর: $raw/২৭)",
          "PHQ-9 স্কেলে ১০–১৪ স্কোর মাঝারি মাত্রার বিষণ্ণতা নির্দেশ করে যা আপনার দৈনন্দিন জীবনকে প্রভাবিত করছে।",
          "আপনার PHQ-9 স্কোর $raw/২৭। ক্লিনিক্যাল মানদণ্ডে দেখা যাচ্ছে—কাজে আনন্দহীনতা, ক্লান্তি, ঘুমের ছন্দপতন বা নিজেকে দোষারোপ করার প্রবণতা আপনার দৈনন্দিন কার্যক্ষমতা কমিয়ে দিচ্ছে। এই অবস্থায় সক্রিয় মানসিক পরিচর্যা ও কাউন্সেলিং অত্যন্ত জরুরি।",
          listOf(
            "আজই ড. সিফাত খান (লাইভ এআই সাইকোলজিস্ট)-এর সাথে বিস্তারিত পরামর্শ সেশন শুরু করুন।",
            "বড় কাজগুলোকে ছোট ছোট অংশে ভাগ করুন এবং নিজের ওপর অতিরিক্ত চাপ কমান।",
            "ঘুম ও খাবারের রুটিন নির্দিষ্ট সময়ে আনার চেষ্টা করুন।"
          )
        )
        raw in 15..19 -> ClinicalQuad(
          "মাঝারি থেকে তীব্র বিষণ্ণতা (Moderately Severe Depression • স্কোর: $raw/২৭)",
          "PHQ-9 স্কেলে ১৫–১৯ স্কোর উচ্চ মাত্রার ক্লিনিক্যাল বিষণ্ণতার লক্ষণ প্রকাশ করে।",
          "আপনার PHQ-9 স্কোর $raw/২৭। আপনার উত্তরে স্পষ্ট হয়েছে যে বিষণ্ণতা, শক্তির অভাব, মনোযোগহীনতা এবং মানসিক ভারাক্রান্ততা আপনার প্রায় প্রতিদিনের জীবনকে গ্রাস করছে। আন্তর্জাতিক ক্লিনিক্যাল গাইডলাইন অনুযায়ী এই স্কোরে সাইকোথেরাপি ও বিশেষজ্ঞ পরামর্শ গ্রহণ করা অত্যন্ত গুরুত্বপূর্ণ।",
          listOf(
            "ড. সিফাত খানের সাথে নিয়মিত সাপোর্টিভ ও CBT সেশন গ্রহণ করুন এবং বিশ্বস্ত পরিবারের সদস্যের সাথে কথা বলুন।",
            "নিজেকে একা রাখবেন না; প্রয়োজনে সরাসরি মনোরোগ বিশেষজ্ঞের পরামর্শ নিন।",
            "টুলকিট ট্যাবের ৫-৪-৩-২-১ গ্রাউন্ডিং এবং ৪-৭-৮ শ্বাস-প্রশ্বাসের অনুশীলন প্রতিদিন করুন।"
          )
        )
        else -> ClinicalQuad(
          "তীব্র ক্লিনিক্যাল বিষণ্ণতা (Severe Depression • স্কোর: $raw/২৭)",
          "PHQ-9 স্কেলে ২০–২৭ স্কোর তীব্র ক্লিনিক্যাল বিষণ্ণতা নির্দেশ করে, যার জন্য দ্রুত পেশাদার সহায়তা প্রয়োজন।",
          "আপনার PHQ-9 স্কোর $raw/২৭। প্রায় প্রতিটি প্রশ্নেই আপনার তীব্র মানসিক কষ্ট, আনন্দহীনতা ও ক্লান্তি ফুটে উঠেছে" +
            (if (item9Score > 0) " এবং ৯ নম্বর প্রশ্নে আপনার নেতিবাচক চিন্তার ইঙ্গিত পাওয়া গেছে।" else "।") +
            " অনুগ্রহ করে মনে রাখবেন, বিষণ্ণতা একটি নিরাময়যোগ্য অবস্থা এবং সঠিক সহায়তায় আপনি অবশ্যই সুস্থ হয়ে উঠবেন।",
          listOf(
            "এখনই ড. সিফাত খানের সাথে কথা বলুন এবং আপনার প্রিয়জন বা একজন পেশাদার মনোরোগ বিশেষজ্ঞের (Psychiatrist) সহায়তা নিন।",
            "কোনো অবস্থাতেই নিজেকে একা বা বিচ্ছিন্ন রাখবেন না।",
            "আজকের দিনটির জন্য কেবল ছোট ও সহজ বিশ্রামের পদক্ষেপ নিন, নিজেকে কোনো কিছুর জন্যই দোষ দেবেন না।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        raw in 0..4 -> ClinicalQuad(
          "None-Minimal Depression (PHQ-9 Score: $raw/27)",
          "According to official PHQ-9 clinical cutoffs (0–4), you show no or minimal signs of depression.",
          "Your PHQ-9 score is $raw out of 27. This indicates healthy emotional regulation, stable energy, and normal interest in daily activities.",
          listOf(
            "Maintain consistent sleep (7–8 hours) and regular physical movement.",
            "Continue tracking your daily reflections in the Journal tab.",
            "Use the Toolkit breathing exercises whenever mild situational stress arises."
          )
        )
        raw in 5..9 -> ClinicalQuad(
          "Mild Depression (PHQ-9 Score: $raw/27)",
          "A PHQ-9 score of 5–9 indicates mild depressive symptoms that warrant mindful self-care.",
          "Your PHQ-9 score is $raw out of 27. You have been experiencing several days of low mood, fatigue, or reduced motivation over the past two weeks. Early cognitive restructuring and routine adjustments are highly effective at this stage.",
          listOf(
            "Start a CBT conversation with Dr. Sifat Khan to unpack recent stressors.",
            "Schedule one small pleasurable or outdoor activity each day (Behavioral Activation).",
            "Re-take the PHQ-9 in 2 weeks to monitor your mood trajectory."
          )
        )
        raw in 10..14 -> ClinicalQuad(
          "Moderate Clinical Depression (PHQ-9 Score: $raw/27)",
          "A PHQ-9 score of 10–14 indicates moderate clinical depression affecting daily functioning.",
          "Your PHQ-9 score is $raw out of 27. Clinical standards indicate that persistent low mood, sleep changes, fatigue, or self-criticism are actively weighing on your daily life.",
          listOf(
            "Engage with Dr. Sifat Khan in the live AI Psychiatrist chat for structured therapeutic support.",
            "Break daily responsibilities into small, manageable steps without harsh self-judgment.",
            "Reach out to a trusted friend, family member, or counselor for support."
          )
        )
        raw in 15..19 -> ClinicalQuad(
          "Moderately Severe Depression (PHQ-9 Score: $raw/27)",
          "A PHQ-9 score of 15–19 reflects moderately severe depression requiring active support.",
          "Your PHQ-9 score is $raw out of 27. You are experiencing depressive symptoms on more than half the days or nearly every day, significantly impacting your energy, concentration, and emotional well-being.",
          listOf(
            "Talk with Dr. Sifat Khan right away and strongly consider consulting a licensed mental health professional.",
            "Avoid isolating yourself—share how you are feeling with someone you trust.",
            "Use the Toolkit's grounding and relaxation exercises daily to reduce nervous system overload."
          )
        )
        else -> ClinicalQuad(
          "Severe Clinical Depression (PHQ-9 Score: $raw/27)",
          "A PHQ-9 score of 20–27 indicates severe depression; immediate compassionate care is recommended.",
          "Your PHQ-9 score is $raw out of 27" +
            (if (item9Score > 0) ", including affirmative responses on Item 9 regarding self-harm/hopelessness thoughts." else ".") +
            " You are carrying an overwhelming emotional burden right now, and you deserve immediate, caring support.",
          listOf(
            "Connect with Dr. Sifat Khan now and reach out to a mental health professional or trusted loved one today.",
            "Please do not stay alone with these heavy thoughts—stay around safe, supportive people.",
            "Focus only on getting through today gently, one breath and one moment at a time."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun evaluateGad7(
    test: PsychologicalTestDefinition,
    raw: Int,
    maxRaw: Int,
    normalized: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        raw in 0..4 -> ClinicalQuad(
          "স্বাভাবিক বা ন্যূনতম উদ্বেগ (Minimal Anxiety • স্কোর: $raw/২১)",
          "আন্তর্জাতিক GAD-7 স্কেল অনুযায়ী আপনার উদ্বেগের মাত্রা সম্পূর্ণ স্বাভাবিক (০–৪)।",
          "আপনার GAD-7 স্কোর $raw (২১-এর মধ্যে)। আপনি দৈনন্দিন দুশ্চিন্তাকে সুন্দরভাবে নিয়ন্ত্রণ করতে পারছেন এবং অকারণ অস্থিরতা আপনার শান্তিকে ব্যাহত করছে না।",
          listOf(
            "আপনার বর্তমান মানসিক স্থিরতা ও স্বাস্থ্যকর জীবনযাপন বজায় রাখুন।",
            "নিয়মিত পর্যাপ্ত ঘুম ও হালকা ব্যায়াম চালিয়ে যান।",
            "মানসিক প্রশান্তির জন্য মাঝে মাঝে ৪-৭-৮ ব্রেথিং অনুশীলন করুন।"
          )
        )
        raw in 5..9 -> ClinicalQuad(
          "মৃদু উদ্বেগ (Mild Anxiety • স্কোর: $raw/২১)",
          "GAD-7 স্কেলে ৫–৯ স্কোর মৃদু বা প্রাথমিক উদ্বেগের লক্ষণ নির্দেশ করে।",
          "আপনার GAD-7 স্কোর $raw/২১। গত ২ সপ্তাহে আপনি কয়েক দিন নার্ভাসনেস, অতিরিক্ত চিন্তা বা রিল্যাক্স হতে কিছুটা সমস্যার মুখোমুখি হয়েছেন। সঠিক শ্বাস-প্রশ্বাস ও চিন্তা নিয়ন্ত্রণের মাধ্যমে এটি সহজেই নিয়ন্ত্রণে আনা সম্ভব।",
          listOf(
            "প্রতিদিন সকাল ও রাতে টুলকিট সেকশনের ৪-৭-৮ ব্রেথিং ব্যায়াম অনুশীলন করুন।",
            "চা-কফি বা ক্যাফেইন গ্রহণ কমিয়ে দিন এবং ঘুমানোর সময় নির্দিষ্ট করুন।",
            "ড. সিফাত খানের সাথে কথা বলে দুশ্চিন্তার মূল কারণগুলো বিশ্লেষণ করুন।"
          )
        )
        raw in 10..14 -> ClinicalQuad(
          "মাঝারি ক্লিনিক্যাল উদ্বেগ (Moderate Anxiety • স্কোর: $raw/২১)",
          "GAD-7 স্কেলে ১০–১৪ স্কোর মাঝারি মাত্রার জেনারালাইজড অ্যাংজাইটি নির্দেশ করে।",
          "আপনার GAD-7 স্কোর $raw/২১। ক্লিনিক্যাল মানদণ্ড অনুযায়ী দুশ্চিন্তা থামাতে না পারা, অস্থিরতা এবং খিটখিটে মেজাজ আপনার অর্ধেকের বেশি দিনকে প্রভাবিত করছে। আপনার স্নায়ুতন্ত্র অতিরিক্ত উত্তেজিত অবস্থায় রয়েছে।",
          listOf(
            "ড. সিফাত খানের সাথে CBT ও মাইন্ডফুলনেস সেশনে যুক্ত হয়ে দুশ্চিন্তার চক্র ভাঙার কৌশল নিন।",
            "টুলকিট ট্যাবের পেশী শিথিলকরণ (PMR) ও ৫-৪-৩-২-১ গ্রাউন্ডিং প্রতিদিন অনুশীলন করুন।",
            "সারাদিনের দুশ্চিন্তাগুলো জার্নালে লিখে মাথার ভেতরের চাপ কমান।"
          )
        )
        else -> ClinicalQuad(
          "তীব্র ক্লিনিক্যাল উদ্বেগ (Severe Anxiety • স্কোর: $raw/২১)",
          "GAD-7 স্কেলে ১৫–২১ স্কোর তীব্র উদ্বেগ ও অস্থিরতা (Severe Generalized Anxiety) নির্দেশ করে।",
          "আপনার GAD-7 স্কোর $raw/২১। আপনি প্রায় প্রতিদিনই অনিয়ন্ত্রিত দুশ্চিন্তা, স্থির হয়ে বসতে না পারা এবং অজানা বিপদের আশঙ্কায় ভুগছেন। এই তীব্র উদ্বেগ আপনার শরীর ও মনকে মারাত্মকভাবে ক্লান্ত করে তুলছে।",
          listOf(
            "আজই ড. সিফাত খানের সাথে লাইভ সেশনে কথা বলুন এবং প্রয়োজনে মনোরোগ বিশেষজ্ঞের পরামর্শ নিন।",
            "হঠাৎ আতঙ্ক বা বুক ধড়ফড় শুরু হলে সাথে সাথে ৪-৭-৮ শ্বাস-প্রশ্বাসের ব্যায়াম করুন।",
            "ভবিষ্যতের সব সমস্যার সমাধান আজই করার চেষ্টা না করে বর্তমান মুহূর্তে নিজেকে নিরাপদ রাখুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        raw in 0..4 -> ClinicalQuad(
          "Minimal Anxiety (GAD-7 Score: $raw/21)",
          "According to official GAD-7 cutoffs (0–4), your anxiety level is minimal and well-regulated.",
          "Your GAD-7 score is $raw out of 21. You manage everyday worries effectively without persistent restlessness or uncontrollable tension.",
          listOf(
            "Continue your healthy sleep and mindfulness habits.",
            "Use the Journal tab to keep track of positive daily moments.",
            "Practice calm breathing whenever busy days arise."
          )
        )
        raw in 5..9 -> ClinicalQuad(
          "Mild Clinical Anxiety (GAD-7 Score: $raw/21)",
          "A GAD-7 score of 5–9 indicates mild anxiety symptoms over the past two weeks.",
          "Your GAD-7 score is $raw out of 21. You have experienced several days of nervousness, worrying about different things, or slight difficulty relaxing.",
          listOf(
            "Practice the 4-7-8 Calm Breathing exercise in the Toolkit tab twice daily.",
            "Reduce caffeine intake and write down lingering worries before bedtime.",
            "Chat with Dr. Sifat Khan to reframe anxious thoughts early."
          )
        )
        raw in 10..14 -> ClinicalQuad(
          "Moderate Generalized Anxiety (GAD-7 Score: $raw/21)",
          "A GAD-7 score of 10–14 indicates moderate clinical anxiety affecting your daily peace.",
          "Your GAD-7 score is $raw out of 21. Uncontrollable worry, restlessness, and tension are occurring on more than half the days, keeping your nervous system on alert.",
          listOf(
            "Consult Dr. Sifat Khan in CBT Mode to challenge catastrophic worry loops.",
            "Use the 5-4-3-2-1 Grounding and Progressive Muscle Relaxation (PMR) tools daily.",
            "Set aside a bounded 15-minute 'worry window' rather than fighting thoughts all day."
          )
        )
        else -> ClinicalQuad(
          "Severe Generalized Anxiety (GAD-7 Score: $raw/21)",
          "A GAD-7 score of 15–21 indicates severe clinical anxiety requiring active therapeutic care.",
          "Your GAD-7 score is $raw out of 21. Nearly every day, you are experiencing intense worry, inability to relax, physical restlessness, and a sense of impending dread.",
          listOf(
            "Start a live session with Dr. Sifat Khan immediately and consider consulting a clinical specialist.",
            "Use the 4-7-8 breathing exercise immediately whenever your heart races or panic rises.",
            "Anchor yourself in the present room using the 5-4-3-2-1 sensory grounding exercise."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun evaluatePss10(
    test: PsychologicalTestDefinition,
    raw: Int,
    maxRaw: Int,
    normalized: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        raw in 0..13 -> ClinicalQuad(
          "নিম্ন মানসিক চাপ (Low Perceived Stress • স্কোর: $raw/৪০)",
          "কোহেনের PSS-10 স্কেল অনুযায়ী (০–১৩) আপনার মানসিক চাপের মাত্রা কম এবং নিয়ন্ত্রণে রয়েছে।",
          "আপনার PSS-10 স্কোর $raw (৪০-এর মধ্যে)। গত ১ মাসে অনাকাঙ্ক্ষিত পরিস্থিতি আসলেও আপনি আত্মবিশ্বাসের সাথে তা সামলে নিতে পেরেছেন।",
          listOf(
            "সমস্যা সমাধানের এই ইতিবাচক মানসিকতা বজায় রাখুন।",
            "কাজ ও বিশ্রামের মধ্যে বর্তমানের মতোই ভারসাম্য রাখুন।",
            "নিয়মিত জার্নালিং ও পর্যাপ্ত ঘুম অব্যাহত রাখুন।"
          )
        )
        raw in 14..26 -> ClinicalQuad(
          "মাঝারি মানসিক চাপ (Moderate Perceived Stress • স্কোর: $raw/৪০)",
          "PSS-10 স্কেলে ১৪–২৬ স্কোর মাঝারি মাত্রার মানসিক চাপ নির্দেশ করে।",
          "আপনার PSS-10 স্কোর $raw/৪০। গত ১ মাসে কাজের চাপ ও অপ্রত্যাশিত কিছু ঘটনার কারণে আপনার মাঝে মাঝে মনে হয়েছে পরিস্থিতি আপনার নিয়ন্ত্রণের বাইরে চলে যাচ্ছে।",
          listOf(
            "প্রতিদিনের কাজের অগ্রাধিকার তালিকা তৈরি করুন এবং একসাথে সব কাজের চাপ নেওয়া বন্ধ করুন।",
            "দিনে ২ বার টুলকিট সেকশনের শ্বাস-প্রশ্বাস ও পেশী শিথিলকরণ (PMR) অনুশীলন করুন।",
            "ড. সিফাত খানের সাথে কথা বলে স্ট্রেস ম্যানেজমেন্ট পরিকল্পনা তৈরি করুন।"
          )
        )
        else -> ClinicalQuad(
          "উচ্চ মাত্রার মানসিক চাপ ও ওভারলোড (High Perceived Stress • স্কোর: $raw/৪০)",
          "PSS-10 স্কেলে ২৭–৪০ স্কোর অত্যন্ত উচ্চ মাত্রার মানসিক চাপ ও বার্নআউট ঝুঁকি নির্দেশ করে।",
          "আপনার PSS-10 স্কোর $raw/৪০। আপনার উত্তরগুলো বলছে যে গত ১ মাসে সমস্যার স্তূপ, নিয়ন্ত্রণহীনতার অনুভূতি ও স্নায়বিক চাপ আপনাকে চরমভাবে ক্লান্ত করে তুলেছে। দীর্ঘদিন এই মাত্রার স্ট্রেস থাকলে তা ঘুম ও শারীরিক স্বাস্থ্যের ওপর প্রভাব ফেলে।",
          listOf(
            "আজই ড. সিফাত খানের সাথে স্ট্রেস রিলিফ সেশনে কথা বলুন এবং মনের বোঝা হালকা করুন।",
            "যে বিষয়গুলো আপনার নিয়ন্ত্রণের বাইরে সেগুলো নিয়ে নিজেকে দোষারোপ করা বন্ধ করুন।",
            "অতিরিক্ত দায়িত্বকে 'না' বলুন এবং প্রতিদিন অন্তত ৩০ মিনিট সম্পূর্ণ বিশ্রামের সময় রাখুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        raw in 0..13 -> ClinicalQuad(
          "Low Perceived Stress (PSS-10 Score: $raw/40)",
          "According to official PSS-10 cutoffs (0–13), your perceived stress is low and well-managed.",
          "Your PSS-10 score is $raw out of 40. Over the past month, you have felt capable of handling life's unexpected challenges with resilience.",
          listOf(
            "Continue maintaining healthy work-life boundaries.",
            "Keep using daily reflection and restful sleep to stay balanced.",
            "Practice brief breathing exercises during busy days."
          )
        )
        raw in 14..26 -> ClinicalQuad(
          "Moderate Perceived Stress (PSS-10 Score: $raw/40)",
          "A PSS-10 score of 14–26 indicates moderate perceived stress over the past month.",
          "Your PSS-10 score is $raw out of 40. You have frequently felt stretched thin by responsibilities and occasionally overwhelmed by things outside your control.",
          listOf(
            "Prioritize your top 2 daily tasks and let go of perfectionism on minor items.",
            "Practice Progressive Muscle Relaxation (PMR) in the Toolkit tab each evening.",
            "Discuss your current workload and stressors with Dr. Sifat Khan."
          )
        )
        else -> ClinicalQuad(
          "High Perceived Stress & Overload (PSS-10 Score: $raw/40)",
          "A PSS-10 score of 27–40 indicates high perceived stress and elevated burnout risk.",
          "Your PSS-10 score is $raw out of 40. Over the past month, difficulties have felt piled so high that your sense of control and calm has been severely strained.",
          listOf(
            "Connect with Dr. Sifat Khan in the live chat to decompress and build a recovery plan.",
            "Step back from non-urgent obligations and give your nervous system permission to rest.",
            "Use the 4-7-8 breathing and 5-4-3-2-1 grounding exercises whenever stress peaks."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private fun evaluateIsi7(
    test: PsychologicalTestDefinition,
    raw: Int,
    maxRaw: Int,
    normalized: Int,
    patterns: List<String>,
    language: AppLanguage
  ): PsychConditionAnalysis {
    return if (language == AppLanguage.BN) {
      val (condName, summary, explanation, steps) = when {
        raw in 0..7 -> ClinicalQuad(
          "ক্লিনিক্যাল অনিদ্রা নেই (No Clinically Significant Insomnia • স্কোর: $raw/২৮)",
          "আন্তর্জাতিক ISI-7 স্কেল অনুযায়ী (০–৭) আপনার ঘুমের মান স্বাভাবিক ও স্বাস্থ্যকর।",
          "আপনার ISI-7 স্কোর $raw (২৮-এর মধ্যে)। আপনার ঘুমিয়ে পড়া, ঘুমের গভীরতা এবং দিনের বেলার সতেজতা স্বাভাবিক পর্যায়ে রয়েছে।",
          listOf(
            "প্রতিদিন একই সময়ে ঘুমানো এবং ঘুম থেকে ওঠার এই সুঅভ্যাস বজায় রাখুন।",
            "ঘুমানোর আগে শান্ত পরিবেশ বজায় রাখুন।",
            "থেনাটক্স জার্নালে আপনার ঘুমের ঘণ্টা নিয়মিত রেকর্ড করুন।"
          )
        )
        raw in 8..14 -> ClinicalQuad(
          "মৃদু বা প্রাথমিক অনিদ্রা (Subthreshold / Mild Insomnia • স্কোর: $raw/২৮)",
          "ISI-7 স্কেলে ৮–১৪ স্কোর প্রাথমিক বা মৃদু অনিদ্রার লক্ষণ নির্দেশ করে।",
          "আপনার ISI-7 স্কোর $raw/২৮। গত ২ সপ্তাহে ঘুমিয়ে পড়তে দেরি হওয়া বা মাঝে মাঝে ঘুম ভেঙে যাওয়ার কারণে আপনি ঘুম নিয়ে পুরোপুরি সন্তুষ্ট নন এবং দিনে কিছুটা ক্লান্তি অনুভব করছেন।",
          listOf(
            "ঘুমানোর অন্তত ৪৫ মিনিট আগে মোবাইল ও স্ক্রিন ব্যবহার বন্ধ করুন।",
            "বিছানায় শুয়ে টুলকিট সেকশনের ৪-৭-৮ প্রশান্তির শ্বাস-প্রশ্বাস ব্যায়াম করুন।",
            "বিকেলের পর চা বা কফি পান করা থেকে বিরত থাকুন।"
          )
        )
        raw in 15..21 -> ClinicalQuad(
          "মাঝারি ক্লিনিক্যাল অনিদ্রা (Moderate Clinical Insomnia • স্কোর: $raw/২৮)",
          "ISI-7 স্কেলে ১৫–২১ স্কোর মাঝারি মাত্রার ক্লিনিক্যাল অনিদ্রা নির্দেশ করে।",
          "আপনার ISI-7 স্কোর $raw/২৮। ঘুমের সমস্যাটি আপনার প্রতিদিনের মেজাজ, মনোযোগ এবং কর্মক্ষমতায় স্পষ্ট ব্যাঘাত ঘটাচ্ছে এবং ঘুম নিয়ে আপনার মনে দুশ্চিন্তা তৈরি হয়েছে।",
          listOf(
            "ড. সিফাত খানের সাথে CBT-I (Insomnia Therapy) পদ্ধতি নিয়ে আলোচনা করুন।",
            "রাতের বেলা মাথার দুশ্চিন্তাগুলো ঘুমানোর আগেই ডায়েরি বা জার্নালে লিখে রাখুন।",
            "ঘুমানোর আগে পেশী শিথিলকরণ (PMR) ও ৪-৭-৮ ব্রেথিং নিয়মিত অনুশীলন করুন।"
          )
        )
        else -> ClinicalQuad(
          "তীব্র ক্লিনিক্যাল অনিদ্রা (Severe Clinical Insomnia • স্কোর: $raw/২৮)",
          "ISI-7 স্কেলে ২২–২৮ স্কোর তীব্র ক্লিনিক্যাল অনিদ্রা নির্দেশ করে যা আপনার শরীর ও মনকে বিপর্যস্ত করছে।",
          "আপনার ISI-7 স্কোর $raw/২৮। ঘুমিয়ে পড়তে তীব্র কষ্ট, বারবার ঘুম ভেঙে যাওয়া এবং ঘুমের অভাবে দিনের বেলা প্রচণ্ড অবসাদ আপনার জীবনযাত্রার মানকে মারাত্মকভাবে ক্ষতিগ্রস্ত করছে।",
          listOf(
            "আজই ড. সিফাত খানের সাথে কথা বলুন এবং দীর্ঘস্থায়ী অনিদ্রার জন্য চিকিৎসকের পরামর্শ নিন।",
            "বিছানায় শুয়ে ঘড়ির দিকে বারবার তাকানো বন্ধ করুন এবং স্নায়ু শান্ত করতে ৪-৭-৮ ব্রেথিং করুন।",
            "দিনের বেলা লম্বা ঘুম পরিহার করে রাতের ঘুমের নির্দিষ্ট রুটিন তৈরি করুন।"
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    } else {
      val (condName, summary, explanation, steps) = when {
        raw in 0..7 -> ClinicalQuad(
          "No Clinically Significant Insomnia (ISI-7 Score: $raw/28)",
          "According to official ISI-7 cutoffs (0–7), your sleep pattern is healthy and restorative.",
          "Your ISI-7 score is $raw out of 28. You fall asleep reasonably well and your daytime functioning is not impaired by sleep difficulties.",
          listOf(
            "Keep a consistent wake-up and bedtime schedule.",
            "Continue logging your sleep hours in the Journal tab.",
            "Keep your bedroom cool, dark, and quiet."
          )
        )
        raw in 8..14 -> ClinicalQuad(
          "Subthreshold / Mild Insomnia (ISI-7 Score: $raw/28)",
          "An ISI-7 score of 8–14 indicates mild or subthreshold insomnia over the past two weeks.",
          "Your ISI-7 score is $raw out of 28. Occasional trouble falling or staying asleep is leaving you mildly dissatisfied with your sleep and slightly tired during the day.",
          listOf(
            "Turn off bright screens at least 45 minutes before bed.",
            "Practice the 4-7-8 Calm Breathing exercise in the Toolkit tab while lying in bed.",
            "Avoid caffeine in the late afternoon and evening."
          )
        )
        raw in 15..21 -> ClinicalQuad(
          "Moderate Clinical Insomnia (ISI-7 Score: $raw/28)",
          "An ISI-7 score of 15–21 indicates moderate clinical insomnia affecting daytime energy and focus.",
          "Your ISI-7 score is $raw out of 28. Sleep onset or maintenance problems are noticeably interfering with your mood, concentration, and daily stamina.",
          listOf(
            "Talk with Dr. Sifat Khan for cognitive-behavioral sleep strategies (CBT-I).",
            "Do a brain-dump of racing thoughts in the Journal tab an hour before sleep.",
            "Use Progressive Muscle Relaxation (PMR) in bed to release physical tension."
          )
        )
        else -> ClinicalQuad(
          "Severe Clinical Insomnia (ISI-7 Score: $raw/28)",
          "An ISI-7 score of 22–28 indicates severe clinical insomnia requiring dedicated care.",
          "Your ISI-7 score is $raw out of 28. Severe difficulty falling or staying asleep is causing high distress and major daytime exhaustion.",
          listOf(
            "Consult Dr. Sifat Khan in the live chat and consider speaking with a healthcare specialist.",
            "Avoid clock-watching at night; use the 4-7-8 breathing exercise to calm sleep anxiety.",
            "Reserve your bed strictly for sleep and relaxation so your brain rebuilds a restful association."
          )
        )
      }
      PsychConditionAnalysis(test.key, test.title(language), normalized, raw, maxRaw, condName, summary, explanation, patterns, steps)
    }
  }

  private data class ClinicalQuad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
  )
}
