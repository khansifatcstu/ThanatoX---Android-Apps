# ThanatoX: AI-Powered Psychiatrist & Mental Wellness Sanctuary
## 3-Minute University Project Defense Presentation Deck & Speaker Script

---

## SLIDE 1: TITLE & ACADEMIC IDENTITY (0:00 – 0:15)

### **Project Title**
**ThanatoX: An AI-Powered Clinical Psychiatrist Consultation, Psychometric Assessment, and Mental Wellness Android Application**

### **Presented By (Student Identity)**
* **Name:** Sifat Khan
* **Student ID / Roll:** `[Enter Student ID]`
* **Session / Batch:** `[Enter Session / Batch]`
* **Department:** Department of Computer Science and Engineering (CSE)
* **University / Institute:** Chandpur Science and Technology University (CSTU) / `[Confirm Institute Name]`

### **Supervised By (Supervisor Identity)**
* **Supervisor Name:** `[Enter Supervisor Name]`
* **Designation:** `[Professor / Associate Professor / Assistant Professor / Lecturer]`
* **Department:** Department of Computer Science and Engineering (CSE)
* **University / Institute:** Chandpur Science and Technology University (CSTU) / `[Confirm Institute Name]`

> **🎤 Speaker Script (0:00 – 0:15 | 15 Seconds):**
> *"Assalamu Alaikum and good morning respected Chairman, my honorable Supervisor [Supervisor Name], and esteemed members of the examination board. I am Sifat Khan, and today I am presenting my project, **ThanatoX**—an AI-powered clinical psychiatrist consultation, psychometric testing, and mental wellness Android application."*

---

## SLIDE 2: PROBLEM STATEMENT & MOTIVATION (0:15 – 0:40)

### **Why ThanatoX is Needed**
* **Severe Mental Health Treatment Gap:** Over 80% of university students and young adults experiencing depression, anxiety, or academic burnout never receive timely clinical screening due to social stigma, high consultation fees, and shortage of psychiatrists.
* **Lack of Structured Bilingual Tools:** Existing wellness apps are either static trackers without clinical intelligence or generic chatbots lacking structured psychiatric intake protocols and native Bengali (`বাংলা`) + English accessibility.
* **Fragmented User Experience:** Users struggle with cluttered interfaces, unvalidated quizzes, and apps that lose clinical history across devices.

### **Research & Engineering Objective**
* Build a **Zero-Trust, Cloud-Synced Android Sanctuary** that combines **24/7 AI Psychiatric Consultation**, **Structured 5-Step Clinical Sessions with Rx Guidelines**, **9 Standardized Psychometric Assessments**, and **Evidence-Based CBT/Grounding Toolkits** in one unified mobile platform.

> **🎤 Speaker Script (0:15 – 0:40 | 25 Seconds):**
> *"Mental health challenges such as depression, anxiety, and semester burnout heavily impact students, yet social stigma and high costs prevent early intervention. Existing apps are either simple mood trackers or generic chatbots. Our objective with ThanatoX was to engineer a structured, bilingual (Bengali and English) mobile psychiatric platform that replicates real clinical workflows—from initial screening to guided therapy sessions and psychometric diagnostics."*

---

## SLIDE 3: PROPOSED SOLUTION & ERGONOMIC HOME ARCHITECTURE (0:40 – 1:05)

### **ThanatoX Core UX Innovations**
* **Zero-Scroll 4-Box Proportional Home Dashboard:**
  1. **Box 1 — Clinical Sanctuary Introduction:** Dynamic welcome banner detailing the platform's mission and immediate psychological assurance.
  2. **Box 2 — Instant Mood Check-In:** 1-tap emotional state synchronization directly linked to the user's clinical diary.
  3. **Box 3 — Curated Psychology Library Preview:** Instant access to 12 research-backed clinical psychology articles.
  4. **Box 4 — Standardized Clinical Tests Preview:** Direct launchpad into psychometric screening scales.
* **Messenger-Style Draggable Floating Chat Head (`DraggableDoctorChatBubble`):**
  * Physics-backed, spring-snapping circular avatar with real-time online indicator accessible from anywhere on the home screen for instant crisis or consultation access.

> **🎤 Speaker Script (0:40 – 1:05 | 25 Seconds):**
> *"To ensure zero cognitive overload for distressed users, we designed a proportional 4-Box Home Screen that fits 100% of the viewport without scrolling—giving immediate access to the clinical introduction, mood check-in, psychology articles, and diagnostic tests. Additionally, we engineered a Messenger-style draggable floating chat bubble with edge-snapping physics so users can reach Dr. Sifat Khan's AI consultation with a single tap."*

---

## SLIDE 4: AI PSYCHIATRIST CONSULTATION & 5-STEP PSYCHOLOGICAL SESSION (1:05 – 1:35)

### **1. 24/7 Live AI Consultation (`Dr. Sifat Khan` Clinical Persona)**
* **4 Specialized Psychiatric Modes:**
  * *Cognitive Behavioral Therapy (CBT)* | *Acute Crisis De-escalation* | *Semester Burnout Recovery* | *Sleep & Insomnia Hygiene*
* **Smart Session Lifecycle:** Opens a fresh consultation session on app launch while preserving active conversation state across tab switches and syncing full multi-turn history to Cloud Firestore.
* **Multimodal Clinical Input:** Supports text, camera capture, medical image attachments, and PDF/document review.

### **2. Interactive 5-Step AI Psychological Session (Mind Care Hub)**
* **Step 1:** Patient selects primary complaint (Depression, Anxiety, Sleep Disorder, Relationship/Family Stress, Academic Burnout, OCD/Intrusive Thoughts, or Custom Problem).
* **Step 2:** AI Psychiatrist conducts a **5-stage progressive clinical interview**, adapting each follow-up question to the patient's previous response.
* **Step 3:** Generates a structured **`Rx` Therapeutic Prescription & Actionable Guideline**, automatically saved to the user's cloud profile with 1-tap report management.

> **🎤 Speaker Script (1:05 – 1:35 | 30 Seconds):**
> *"At the core of ThanatoX are two AI clinical workflows. First, a 24/7 live consultation with the 'Dr. Sifat Khan' psychiatric persona supporting four specialized modes—CBT, Crisis Support, Semester Burnout, and Sleep Anxiety—along with multimodal image and document attachments. Second, inside Mind Care, we introduced a 5-Step Psychological Session that mimics a real psychiatrist's chamber: selecting a problem, answering five adaptive diagnostic questions, and receiving a saved therapeutic Rx guideline."*

---

## SLIDE 5: 9 STANDARDIZED CLINICAL & PSYCHOLOGICAL ASSESSMENTS (1:35 – 2:00)

### **Dual-Category Psychometric Engine (`AssessmentScreen`)**
* **Category A — 4 Clinical Diagnostic Scales:**
  1. **PHQ-9:** Patient Health Questionnaire (Depression Severity)
  2. **GAD-7:** Generalized Anxiety Disorder Scale
  3. **PSS-10:** Perceived Stress Scale
  4. **Semester Burnout Scale:** Academic Exhaustion & Cynicism Index
* **Category B — 5 Psychological & Personality Profiling Tests:**
  5. **Dark Triad:** Narcissism, Machiavellianism & Psychopathy
  6. **Big Five (OCEAN):** Openness, Conscientiousness, Extraversion, Agreeableness, Neuroticism
  7. **Emotional Intelligence (EQ):** Empathy & Self-Regulation
  8. **Adult Attachment Style:** Secure, Anxious, Avoidant profiling
  9. **Cognitive Distortion Screening:** Catastrophizing, All-or-Nothing Thinking

### **Automated Scoring & Dr. Sifat Khan's Clinical Analysis**
* Real-time score computation, severity stratification, personalized **Dr. Sifat Khan's Psychological Analysis**, persistent Firestore report archiving, and direct card-level deletion.

> **🎤 Speaker Script (1:35 – 2:00 | 25 Seconds):**
> *"For objective self-evaluation, ThanatoX integrates nine standardized assessments split into two categories: four clinical scales including PHQ-9, GAD-7, PSS-10, and Semester Burnout, alongside five deep psychological tests covering the Big Five OCEAN traits, Dark Triad, Emotional Intelligence, Attachment Style, and Cognitive Distortions. Every completed test generates an automated severity score alongside a personalized clinical interpretation by Dr. Sifat Khan."*

---

## SLIDE 6: MIND CARE TOOLKIT, MOOD DIARY & PSYCHOLOGY LIBRARY (2:00 – 2:20)

### **Holistic Self-Regulation Ecosystem**
* **Interactive Therapy Toolkit:**
  * **4-7-8 Autonomic Breathing Timer:** Visual animated lung-cadence pacer (4s inhale, 7s hold, 8s exhale) to activate the parasympathetic nervous system.
  * **5-4-3-2-1 Sensory Grounding:** Interactive panic-attack interruption protocol.
  * **Progressive Muscle Relaxation (PMR):** Step-by-step somatic tension release.
* **Cloud Mood & Sleep Diary:** Tracks daily emotional valence, sleep duration (hours), stress triggers, and personal reflections with AI trend insights.
* **12-Article Clinical Psychology Library:** In-depth bilingual educational articles covering neurobiology of anxiety, CBT reframing, dopamine detox, and trauma recovery.

> **🎤 Speaker Script (2:00 – 2:20 | 20 Seconds):**
> *"Beyond diagnostics, ThanatoX provides immediate self-regulation tools: an animated 4-7-8 breathing cadence timer, 5-4-3-2-1 sensory grounding for panic attacks, and Progressive Muscle Relaxation. Users also have a cloud-synced Mood and Sleep Diary with trend analytics, plus a 12-article clinical psychology library."*

---

## SLIDE 7: SYSTEM ARCHITECTURE, SECURITY & TECH STACK (2:20 – 2:45)

### **Modern Android Engineering & Cloud Security**
* **Frontend & UI Layer:** 100% **Kotlin** & **Jetpack Compose (Material 3)** with custom Bioluminescent Teal Glassmorphism and Adaptive Window Size support.
* **Architecture Pattern:** **MVVM (Model-View-ViewModel)** + Clean Repository Pattern (`ThanatoXViewModel` -> `ThanatoXRepository` -> `Firestore` / `GeminiPsychiatristService`) using Kotlin Coroutines & `StateFlow`.
* **Authentication & Zero-Trust Cloud Persistence:**
  * **Google Sign-In** via Jetpack `CredentialManager` + Ephemeral Guest Sanctuary Mode.
  * **Cloud Firestore** (`users/{userId}/{consultations, mood_logs, assessments}`) protected by strict **Zero-Trust Security Rules (`firestore.rules`)** enforcing owner-only access (`request.auth.uid == userId`) and strict schema validation.
* **Multi-Provider AI Routing Engine:**
  * Built-in **Gemini 2.5 Flash** integration + extensible custom API router supporting **OpenAI (GPT-4o), Groq (Llama-3), DeepSeek, OpenRouter, and Anthropic Claude**.

> **🎤 Speaker Script (2:20 – 2:45 | 25 Seconds):**
> *"Architecturally, ThanatoX is built natively in Kotlin using Jetpack Compose Material 3 and the MVVM pattern with reactive Kotlin StateFlows. User data is isolated under Zero-Trust Firebase Firestore security rules with Google Credential Manager authentication. Our AI service layer uses Gemini 2.5 Flash by default while supporting custom API routing for OpenAI, Groq, DeepSeek, and Claude."*

---

## SLIDE 8: PERFORMANCE OPTIMIZATION, RESULTS & CONCLUSION (2:45 – 3:00)

### **Runtime Optimization & Verification**
* **Off-Thread SRGB Bitmap Cache (`ThanatoXBitmapCache`):** Asynchronous `Dispatchers.IO` image decoding with explicit `ColorSpace.Named.SRGB` preloading eliminates UI frame drops (`0 Skipped Frames`) and HWUI dataspace warnings.
* **Schema-Compliant Metadata Encoding:** Preserves all 9 test types and psychological session prescriptions inside strict Firestore rules without backend schema breakage.
* **Future Scope:** Voice-based psychiatric prosody analysis, wearable heart-rate variability (HRV) biofeedback via Health Connect, and institutional tele-psychiatry referral integration.

> **🎤 Speaker Script (2:45 – 3:00 | 15 Seconds):**
> *"We optimized runtime performance using an off-thread SRGB bitmap cache to achieve zero UI frame drops and verified our data layer with unit testing. In conclusion, ThanatoX delivers an accessible, clinically structured, and secure mental wellness sanctuary. Thank you—I now welcome any questions from the board."*

---

## BONUS: UNIVERSITY VIVA / DEFENSE Q&A CHEAT SHEET

1. **Q: How does ThanatoX differ from ChatGPT or a generic AI chatbot?**
   * **Answer:** Generic chatbots lack clinical structure, psychometric validation, and persistent psychiatric context. ThanatoX integrates 9 standardized psychometric scales (PHQ-9, GAD-7, etc.) with deterministic scoring algorithms, a structured 5-step diagnostic interview that outputs a formal `Rx` behavioral guideline, 4 specialized psychiatric prompt personas (CBT, Crisis, Burnout, Sleep), and somatic grounding tools (4-7-8 breathing, 5-4-3-2-1 grounding) backed by a Zero-Trust Firestore medical diary.
2. **Q: How is sensitive mental health data secured in your application?**
   * **Answer:** We implemented a Zero-Trust security model in `firestore.rules`. Every collection (`consultations`, `mood_logs`, `assessments`) is strictly scoped under `/users/{userId}/`. Firestore rules verify `request.auth != null && request.auth.uid == userId` on every read, write, and delete operation, alongside field-level type, length, and bounds validation. Furthermore, users can test the app in an ephemeral Guest Mode or wipe all cloud records instantly via the Reset All Data feature.
3. **Q: Why did you choose Jetpack Compose and MVVM architecture?**
   * **Answer:** Jetpack Compose enables declarative, state-driven UI rendering with Material 3 adaptive layouts, while MVVM cleanly decouples our UI screens from `ThanatoXViewModel` and `ThanatoXRepository`. Using Kotlin Coroutines and `StateFlow` ensures thread-safe asynchronous communication with both Cloud Firestore snapshot listeners and our Multi-Provider LLM HTTP client.
