# PROJECT DOCUMENTATION REPORT

---

## TITLE OF THE PROJECT
# **ThanatoX: Design and Implementation of an AI-Powered Clinical Psychiatrist Consultation, Psychometric Assessment, and Mental Wellness Sanctuary on Android**

---

### **A Project Report Submitted in Partial Fulfillment of the Requirements for the Degree of**
**Bachelor of Science in Computer Science and Engineering (B.Sc. in CSE)**  
*(Or Customizable Degree / Course Title)*

---

### **SUBMITTED BY (STUDENT IDENTITY)**
* **Student Name:** Sifat Khan
* **Student ID / Roll No.:** `[Enter Student ID / Roll Number]`
* **Registration No.:** `[Enter Registration Number]`
* **Session / Academic Year:** `[Enter Session, e.g., 2021–2022 / 2025–2026]`
* **Semester / Year:** `[Enter Year / Semester]`
* **Email:** khansifatcstu@gmail.com
* **Department:** Department of Computer Science and Engineering (CSE)
* **Institute / University:** Chandpur Science and Technology University (CSTU) / `[Enter Full Institute Name]`

---

### **SUPERVISED BY (SUPERVISOR IDENTITY)**
* **Supervisor Name:** `[Enter Supervisor Full Name]`
* **Designation:** `[Professor / Associate Professor / Assistant Professor / Lecturer]`
* **Department:** Department of Computer Science and Engineering (CSE)
* **Institute / University:** Chandpur Science and Technology University (CSTU) / `[Enter Full Institute Name]`

---

**Date of Submission:** October 2026

---
---

## CERTIFICATE OF APPROVAL

This is to certify that the project report entitled **"ThanatoX: Design and Implementation of an AI-Powered Clinical Psychiatrist Consultation, Psychometric Assessment, and Mental Wellness Sanctuary on Android"** submitted by **Sifat Khan** (Student ID: `[Enter Student ID]`), to the Department of Computer Science and Engineering, **Chandpur Science and Technology University (CSTU)**, is a record of bona fide project work carried out under my direct supervision and guidance.

The software system and its accompanying documentation fulfill the academic standards and requirements prescribed by the institute for the award of the degree. Neither this report nor any part of it has been submitted previously for any other degree or diploma at any other university or institution.

<br><br>

**________________________________________**  
**Signature of the Supervisor**  
**Name:** `[Enter Supervisor Name]`  
**Designation:** `[Enter Designation]`  
**Department of Computer Science and Engineering**  
**Chandpur Science and Technology University (CSTU)**  

<br><br>

**________________________________________**  
**Signature of the Head of the Department / Chairman**  
**Name:** `[Enter Chairman Name]`  
**Chairman, Department of Computer Science and Engineering**  
**Chandpur Science and Technology University (CSTU)**  

<br><br>

**________________________________________**  
**Signature of External / Internal Examiner(s)**  

---

## DECLARATION BY THE CANDIDATE

I, **Sifat Khan**, hereby declare that the project work entitled **"ThanatoX: Design and Implementation of an AI-Powered Clinical Psychiatrist Consultation, Psychometric Assessment, and Mental Wellness Sanctuary on Android"** is an original engineering work designed, implemented, and tested by me under the supervision of **`[Enter Supervisor Name]`**, `[Enter Designation]`, Department of Computer Science and Engineering.

All external libraries, clinical scales (such as PHQ-9, GAD-7, PSS-10, Big Five OCEAN, and Dark Triad), cloud services (Google Firebase, Gemini API), and scholarly sources referenced in this project have been duly acknowledged and cited.

<br><br>

**________________________________________**  
**Signature of the Candidate**  
**Name:** Sifat Khan  
**Student ID:** `[Enter Student ID]`  
**Date:** `____________________`

---

## ACKNOWLEDGMENT

First and foremost, all praise and gratitude belong to the Almighty for granting me the patience, intellect, and resilience required to conceptualize, architect, and complete the **ThanatoX** project and this comprehensive technical report.

I would like to express my deepest gratitude and sincere appreciation to my honorable supervisor, **`[Enter Supervisor Name]`** (`[Enter Designation]`, Department of Computer Science and Engineering), whose insightful guidance, constructive feedback, architectural critiques, and continuous encouragement shaped every phase of this project—from initial clinical requirement analysis to final Android performance optimization.

I also extend my respectful thanks to the respected Chairman and all faculty members of the Department of Computer Science and Engineering at **Chandpur Science and Technology University (CSTU)** for providing a rigorous academic environment that fostered software engineering excellence.

Finally, I am profoundly grateful to my parents, family members, and peers for their unwavering moral support throughout the research, development, and documentation of **ThanatoX**.

---

## ABSTRACT

Mental health disorders—including Major Depressive Disorder (MDD), Generalized Anxiety Disorder (GAD), chronic perceived stress, and academic semester burnout—affect a substantial proportion of university students and working adults worldwide. In developing nations such as Bangladesh, a critical shortage of trained psychiatrists, high out-of-pocket consultation costs, geographical barriers, and pervasive social stigma prevent over 80% of affected individuals from seeking timely psychological evaluation and therapeutic intervention. While generic conversational AI tools and simple mood-tracking applications exist, they lack structured clinical intake protocols, validated psychometric scoring engines, native bilingual accessibility (`বাংলা` and `English`), and strict patient data isolation.

To bridge this gap, this project presents **ThanatoX** (`com.aistudio.thanatox.psychm`), an end-to-end, cloud-synchronized Android mobile application engineered in **Kotlin** using **Jetpack Compose (Material Design 3)** and the **Model-View-ViewModel (MVVM)** architectural pattern. **ThanatoX** functions as a comprehensive 24/7 digital psychiatric sanctuary built around five integrated pillars:

1. **Ergonomic Zero-Scroll 4-Box Home Sanctuary & Floating Chat Head:** A proportionally weighted single-screen dashboard providing immediate access to clinical orientation, one-tap mood synchronization, a 12-article clinical psychology library, and diagnostic test previews, augmented by a physics-based, Messenger-style draggable floating consultation bubble (`DraggableDoctorChatBubble`).
2. **24/7 Live AI Psychiatrist Consultation (`Dr. Sifat Khan` Clinical Persona):** A multi-turn, multimodal (text, camera, image, and PDF/document) consultation engine offering four specialized clinical modes—*Cognitive Behavioral Therapy (CBT)*, *Acute Crisis De-escalation*, *Semester Burnout Recovery*, and *Sleep & Insomnia Hygiene*—with intelligent session lifecycle management that opens a fresh session on app launch while preserving ongoing conversations across tab navigation.
3. **Interactive 5-Step AI Psychological Session (`Mind Care Hub`):** A structured clinical chamber workflow where the user selects a presenting complaint (or enters a custom issue), undergoes a 5-stage adaptive diagnostic Q&A conducted by the AI psychiatrist, and receives a formal, cloud-saved **`Rx` Therapeutic Prescription & Behavioral Guideline** with single-tap report management.
4. **9-Scale Clinical & Psychometric Assessment Engine:** A dual-category testing suite comprising **4 Clinical Diagnostic Scales** (*PHQ-9 Depression*, *GAD-7 Anxiety*, *PSS-10 Perceived Stress*, and *Semester Burnout*) and **5 Psychological & Personality Tests** (*Dark Triad*, *Big Five OCEAN*, *Emotional Intelligence EQ*, *Adult Attachment Style*, and *Cognitive Distortion Screening*), complete with automated severity grading, **Dr. Sifat Khan's AI Psychological Analysis**, persistent Firestore report archiving, and direct card-level deletion.
5. **Somatic Therapy Toolkit & Cloud Mood/Sleep Diary:** Interactive physiological self-regulation modules including an animated **4-7-8 Autonomic Breathing Pacer**, **5-4-3-2-1 Sensory Grounding**, **Progressive Muscle Relaxation (PMR)**, and a longitudinal Mood & Sleep Diary backed by **Cloud Firestore** under **Zero-Trust Security Rules (`firestore.rules`)** and **Google Sign-In via Jetpack Credential Manager**.

Furthermore, the application features a **Multi-Provider AI Routing Engine** supporting Google **Gemini 2.5 Flash** by default alongside user-configurable API endpoints for **OpenAI (GPT-4o), Groq, DeepSeek, OpenRouter, and Anthropic Claude**, as well as an asynchronous **Off-Thread SRGB Bitmap Cache (`ThanatoXBitmapCache`)** that eliminates main-thread UI frame drops (`0 Skipped Frames`).

**Keywords:** *Mobile Health (mHealth), AI Psychiatrist, Cognitive Behavioral Therapy (CBT), Psychometric Screening (PHQ-9, GAD-7, Big Five), Android Jetpack Compose, Kotlin Coroutines, Cloud Firestore Zero-Trust Security, Large Language Models (Gemini 2.5 Flash).*

---

## TABLE OF CONTENTS

* **Title Page**
* **Certificate of Approval**
* **Declaration by the Candidate**
* **Acknowledgment**
* **Abstract**
* **Table of Contents**
* **List of Tables**
* **List of Figures & Architectural Diagrams**
* **List of Abbreviations**

### **CHAPTER 1: INTRODUCTION**
* 1.1 Background of the Study
* 1.2 Motivation and Context in University & Youth Mental Health
* 1.3 Problem Statement
* 1.4 Objectives of the Project
* 1.5 Scope of the Application
* 1.6 Core Innovations and Contributions of ThanatoX
* 1.7 Organization of the Report

### **CHAPTER 2: LITERATURE REVIEW & COMPARATIVE ANALYSIS**
* 2.1 Evolution of Digital Mental Health (mHealth) Interventions
* 2.2 Standardized Psychometric Screening Instruments in Clinical Practice
* 2.3 Large Language Models (LLMs) in Psychiatric Triage and CBT Delivery
* 2.4 Review of Existing Applications (Woebot, Wysa, Daylio, Headspace)
* 2.5 Critical Limitations of Existing Solutions
* 2.6 Comparative Feature Matrix: Existing Systems vs. ThanatoX

### **CHAPTER 3: SYSTEM ANALYSIS & REQUIREMENTS SPECIFICATION**
* 3.1 Feasibility Study (Technical, Operational, Economic, and Ethical)
* 3.2 Stakeholder Identification and User Personas
* 3.3 Functional Requirements Specification (FRS)
* 3.4 Non-Functional Requirements Specification (NFRS)
* 3.5 Hardware and Software Development Environment
* 3.6 Target Runtime and Android SDK Specifications

### **CHAPTER 4: SYSTEM ARCHITECTURE & DATABASE DESIGN**
* 4.1 High-Level System Architecture (MVVM + Clean Repository Pattern)
* 4.2 Unidirectional Data Flow (UDF) and Reactive State Management
* 4.3 Authentication Architecture: Jetpack Credential Manager & Ephemeral Guest Mode
* 4.4 Cloud Firestore NoSQL Database Schema Design
* 4.5 Zero-Trust Security Rules (`firestore.rules`) & Schema-Compliant Metadata Encoding
* 4.6 Multi-Provider AI Routing Architecture (`GeminiPsychiatristService`)
* 4.7 Navigation Architecture and Predictive Back Handling

### **CHAPTER 5: CLINICAL METHODOLOGY & PSYCHOMETRIC ENGINE DESIGN**
* 5.1 Design of the "Dr. Sifat Khan" AI Psychiatrist Clinical Persona
* 5.2 Prompt Engineering for the 4 Specialized Consultation Modes
* 5.3 Methodology of the 5-Step Interactive Psychological Session & `Rx` Generation
* 5.4 Mathematical Scoring & Severity Stratification of the 4 Clinical Scales (PHQ-9, GAD-7, PSS-10, Semester Burnout)
* 5.5 Profiling Methodology of the 5 Psychological & Personality Assessments (Dark Triad, Big Five OCEAN, EQ, Attachment Style, Cognitive Distortion)
* 5.6 Somatic & Autonomic Regulation Protocols (4-7-8 Breathing, 5-4-3-2-1 Grounding, PMR)
* 5.7 Bilingual Localization Architecture (`বাংলা` & `English`)

### **CHAPTER 6: IMPLEMENTATION & MODULE WALKTHROUGH**
* 6.1 Project Package Structure and Modularization
* 6.2 Theme Engine: Bioluminescent Teal Glassmorphism & Material Design 3
* 6.3 Implementation of the 4-Box Proportional Home Screen (`HomeScreen.kt`)
* 6.4 Physics-Based Messenger-Style Draggable Chat Bubble (`DraggableDoctorChatBubble`)
* 6.5 Implementation of the 24/7 Multimodal Consultation Module (`ConsultationScreen.kt`)
* 6.6 Implementation of the 9-Test Assessment Engine & Report Manager (`AssessmentScreen.kt`)
* 6.7 Implementation of the Mind Care Hub & 5-Step Psychological Session (`MindCareScreen.kt` & `PsychologicalSessionSection.kt`)
* 6.8 Implementation of the 12-Article Psychology Library (`PsychologyArticlesCatalog.kt`)
* 6.9 Implementation of the Row-Based Settings, Custom AI Router & Data Reset (`SettingsScreen.kt`)

### **CHAPTER 7: PERFORMANCE OPTIMIZATION, TESTING & VERIFICATION**
* 7.1 Android Graphics Pipeline Optimization: Off-Thread SRGB Bitmap Cache (`ThanatoXBitmapCache`)
* 7.2 JIT Compilation & Recomposition Optimization in Jetpack Compose
* 7.3 Unit and Local JVM Testing Strategy (Robolectric & Firestore Rule Validation)
* 7.4 Functional Test Cases and Verification Matrix
* 7.5 Security Auditing and Privacy Compliance Verification
* 7.6 Performance Benchmarks (Startup Time, Frame Pacing, Memory Footprint)

### **CHAPTER 8: CONCLUSION, LIMITATIONS & FUTURE WORK**
* 8.1 Summary of Achievements
* 8.2 Clinical and Practical Limitations
* 8.3 Future Enhancement Roadmap
* 8.4 Concluding Remarks

### **REFERENCES**
### **APPENDICES**
* Appendix A: Complete Firestore Security Rules (`firestore.rules`)
* Appendix B: Sample `Rx` Therapeutic Prescription Output
* Appendix C: User Manual & Installation Guide

---
---

## LIST OF TABLES

* **Table 2.1:** Comparative Matrix between Existing mHealth Apps and ThanatoX
* **Table 3.1:** Functional Requirements Traceability Matrix
* **Table 3.2:** Non-Functional Performance and Security Requirements
* **Table 3.3:** Development & Runtime Software Stack Specifications
* **Table 4.1:** Cloud Firestore Collection Hierarchy and Field Constraints
* **Table 4.2:** Supported AI Providers, Default Models, and Endpoint Architectures
* **Table 5.1:** PHQ-9, GAD-7, PSS-10, and Semester Burnout Scoring & Severity Cutoffs
* **Table 5.2:** Psychological & Personality Tests (Dark Triad, Big Five, EQ, Attachment, Cognitive Distortion) Dimension Mapping
* **Table 6.1:** Proportional Weight Distribution of the 4-Box Zero-Scroll Home Screen
* **Table 7.1:** Functional Test Cases and Execution Results
* **Table 7.2:** Runtime Performance Metrics Before vs. After `ThanatoXBitmapCache` Optimization

---

## LIST OF ABBREVIATIONS

* **AI:** Artificial Intelligence
* **API:** Application Programming Interface
* **ART:** Android Runtime
* **CBT:** Cognitive Behavioral Therapy
* **EQ / EI:** Emotional Quotient / Emotional Intelligence
* **GAD-7:** Generalized Anxiety Disorder 7-Item Scale
* **HWUI:** Hardware Accelerated UI Rendering Engine (Android)
* **JSON:** JavaScript Object Notation
* **LLM:** Large Language Model
* **MDD:** Major Depressive Disorder
* **mHealth:** Mobile Health
* **MVVM:** Model-View-ViewModel Architecture
* **OCEAN:** Openness, Conscientiousness, Extraversion, Agreeableness, Neuroticism
* **PHQ-9:** Patient Health Questionnaire 9-Item Scale
* **PMR:** Progressive Muscle Relaxation
* **PSS-10:** Perceived Stress Scale 10-Item Instrument
* **Rx:** Medical / Therapeutic Prescription
* **SDK:** Software Development Kit
* **SRGB:** Standard Red Green Blue Color Space
* **UDF:** Unidirectional Data Flow
* **UI / UX:** User Interface / User Experience

---
---

# CHAPTER 1: INTRODUCTION

## 1.1 Background of the Study
Mental health is an integral component of human well-being, directly governing cognitive performance, interpersonal relationships, emotional resilience, and physiological health. According to global epidemiological reports by the World Health Organization (WHO), depressive and anxiety disorders rank among the leading causes of disability worldwide. Among university students and young professionals aged 18 to 30, the prevalence of psychological distress is particularly acute due to academic pressure, career uncertainty, sleep deprivation, social isolation, and financial stress.

Despite the high burden of mental illness, access to professional psychiatric care remains severely constrained. In countries like Bangladesh, the ratio of qualified psychiatrists and clinical psychologists to the general population is critically low—often fewer than one mental health specialist per several hundred thousand citizens. Consequently, individuals experiencing early-stage symptoms of clinical depression, generalized anxiety, panic attacks, or academic burnout rarely receive timely diagnostic screening or structured cognitive behavioral guidance.

The proliferation of smartphones and major breakthroughs in generative Artificial Intelligence (Large Language Models) present a transformative opportunity to democratize primary mental health triage, psychometric self-assessment, and evidence-based self-regulation. However, translating clinical psychiatric workflows into a safe, empathetic, culturally resonant, and technically robust mobile application requires deliberate software engineering, validated psychological instruments, and uncompromising data security.

## 1.2 Motivation and Context in University & Youth Mental Health
During university life, students frequently experience what clinicians term **Semester Burnout**—a syndrome characterized by emotional exhaustion, academic cynicism, reduced personal efficacy, and psychosomatic sleep disturbances. When a student experiences an acute panic attack at 2:00 AM before an examination or struggles with persistent depressive thoughts, booking an in-person psychiatric appointment is often impossible or deterred by fear of social judgment.

This reality motivated the creation of **ThanatoX** (`থেনাটক্স`). The fundamental vision behind **ThanatoX** is to provide every student and individual with an immediately accessible, stigma-free, bilingual (`বাংলা` and `English`) digital sanctuary where they can:
1. Converse 24/7 with an empathetic, clinically grounded AI Psychiatrist persona (**Dr. Sifat Khan**).
2. Undergo a structured, step-by-step **Psychological Session** that mirrors how a real psychiatrist takes a patient history and issues a personalized therapeutic guideline (`Rx`).
3. Measure their mental and personality profile objectively using **9 standardized clinical and psychological tests** with automated scoring and AI-driven interpretation.
4. Practice immediate autonomic nervous system regulation (such as **4-7-8 breathing** and **5-4-3-2-1 sensory grounding**) and log their longitudinal mood and sleep patterns in a private, cloud-secured medical diary.

## 1.3 Problem Statement
A systematic investigation of current digital mental health practices reveals five critical engineering and clinical problems:

1. **Absence of Structured Psychiatric Intake in Generic Chatbots:** Standard AI chat interfaces respond conversationally but fail to follow structured clinical intake protocols. They do not systematically gather symptom onset, intensity, triggers, and functional impairment before formulating a structured therapeutic plan.
2. **Lack of Comprehensive Psychometric Integration:** Most wellness apps either offer simple mood logging or a single basic quiz. Users lack a unified platform combining clinical diagnostic scales (**PHQ-9, GAD-7, PSS-10, Semester Burnout**) with deeper psychological and personality profiling (**Big Five OCEAN, Dark Triad, Emotional Intelligence, Attachment Style, and Cognitive Distortions**) coupled with longitudinal report saving and AI clinical commentary.
3. **Cognitive Overload and Poor UI Ergonomics for Distressed Users:** Users experiencing anxiety or depressive fatigue are easily overwhelmed by cluttered, deeply nested, scroll-heavy mobile interfaces. They require an ergonomic, zero-scroll home dashboard and an ever-present floating consultation trigger.
4. **Linguistic and Cultural Barriers:** Very few clinical-grade mental wellness applications offer seamless, instantaneous switching between native **Bengali (`বাংলা`)** and **English** across all clinical tests, psychological articles, guided sessions, and AI psychiatrist consultations.
5. **Data Privacy Risks and Vendor Lock-In:** Sensitive psychiatric transcripts and assessment scores require strict **Zero-Trust cloud isolation** so no unauthorized entity can read or tamper with a user's records, alongside the flexibility to route AI requests across multiple LLM providers.

## 1.4 Objectives of the Project
The primary engineering and clinical objectives of the **ThanatoX** project are:

1. **To design and implement an ergonomic, Zero-Scroll 4-Box Home Sanctuary** in Jetpack Compose that displays the clinical introduction, instant mood check-in, psychology articles preview, and clinical tests preview simultaneously on a single screen, accompanied by a physics-backed **Messenger-Style Draggable Floating Chat Bubble**.
2. **To develop a 24/7 Multimodal AI Psychiatrist Consultation Engine** featuring the **Dr. Sifat Khan** clinical persona, supporting four specialized therapeutic modes (*CBT, Crisis Support, Semester Burnout, and Sleep Anxiety*), multimodal attachments (images, camera photos, PDFs), and smart session management (defaulting to a fresh chat on app launch while retaining active state across tab navigation).
3. **To engineer an interactive 5-Step AI Psychological Session** inside the Mind Care module that guides users through problem selection, five progressive clinical interview questions, and the generation of a saved, downloadable/viewable **`Rx` Therapeutic Prescription & Guideline**.
4. **To build a 9-Scale Clinical & Psychological Assessment Engine** covering four clinical scales and five personality/psychological scales with deterministic scoring, **Dr. Sifat Khan's Psychological Analysis**, persistent Cloud Firestore report storage, and single-tap card deletion.
5. **To implement a Zero-Trust Cloud Persistence Layer** using **Firebase Authentication (Google Sign-In via Credential Manager)** and **Cloud Firestore (`firestore.rules`)**, ensuring complete per-user data isolation alongside an ephemeral Guest Mode and a dedicated **Reset All Data** workflow.
6. **To optimize Android runtime performance** through asynchronous off-thread SRGB bitmap decoding (`ThanatoXBitmapCache`) and modular Jetpack Compose state management to guarantee smooth 60/120 FPS rendering without frame drops.

## 1.5 Scope of the Application
The scope of **ThanatoX** encompasses native Android mobile devices (Android 8.0 API 26 through Android 16 API 36) across compact smartphones, foldables, and tablets. Functionally, the application covers:
* Primary mental health screening and psychometric self-assessment.
* AI-assisted Cognitive Behavioral Therapy (CBT) dialogue and structured 5-step psychological sessions.
* Somatic crisis grounding and breathing exercises.
* Daily mood, sleep duration, and stress trigger journaling with longitudinal analytics.
* Psychoeducation through a 12-article clinical psychology library.
* Multi-provider LLM integration (Gemini 2.5 Flash default + OpenAI, Groq, DeepSeek, OpenRouter, and Claude).

*Ethical Disclaimer Scope:* **ThanatoX** is designed as a clinical decision-support, self-assessment, and psychological wellness companion and explicitly informs users in acute life-threatening emergencies to contact national emergency hotlines (such as 999 or Kaan Pete Roi in Bangladesh).

## 1.6 Core Innovations and Contributions of ThanatoX
* **Dual-Pathway AI Psychiatry:** Combines open-ended multi-turn consultation (`ConsultationScreen`) with a deterministic 5-step clinical intake wizard (`PsychologicalSessionSection`) that outputs a structured `Rx` behavioral prescription.
* **9-Instrument Psychometric Suite with AI Interpretation:** Bridges clinical psychiatry (PHQ-9, GAD-7, PSS-10, Burnout) and personality psychology (Dark Triad, Big Five, EQ, Attachment, Cognitive Distortions) with automated scoring and personalized AI clinical synthesis.
* **Schema-Compliant Extended Metadata Encoding:** Innovates a `[TYPE:<AssessmentKey>]` encoding layer inside `ThanatoXRepository` that allows storing all 9 test categories and `"PSYCH_SESSION"` prescriptions seamlessly while strictly satisfying immutable Zero-Trust Firestore enum rules.
* **Off-Thread SRGB Hardware Acceleration (`ThanatoXBitmapCache`):** Solves Android `HWUI: Unknown dataspace 0` and main-thread frame skipping by pre-decoding custom raster assets on `Dispatchers.IO` with explicit `ColorSpace.Named.SRGB`.

## 1.7 Organization of the Report
The remainder of this report is organized as follows:
* **Chapter 2** reviews academic literature on digital mental health, standardized psychometric scales, and clinical LLMs, comparing **ThanatoX** with existing industry platforms.
* **Chapter 3** details the system analysis, feasibility study, functional and non-functional requirements, and hardware/software specifications.
* **Chapter 4** presents the software architecture (MVVM), reactive data flow, Cloud Firestore schema, Zero-Trust security rules, and Multi-Provider AI routing engine.
* **Chapter 5** explains the clinical methodology, prompt engineering, mathematical scoring models for all 9 assessments, and the 5-step psychological session protocol.
* **Chapter 6** provides a detailed code and module-by-module implementation walkthrough across all screens and components.
* **Chapter 7** covers runtime performance optimizations, unit/JVM testing, test cases, and security verification.
* **Chapter 8** concludes the report with a summary of contributions, limitations, and future research directions.

---
---

# CHAPTER 2: LITERATURE REVIEW & COMPARATIVE ANALYSIS

## 2.1 Evolution of Digital Mental Health (mHealth) Interventions
Over the past decade, Mobile Health (mHealth) applications have emerged as a scalable modality for delivering psychological support. Early-generation mHealth applications focused primarily on passive psychoeducation (digital pamphlets) or manual mood journaling. Second-generation platforms introduced rule-based decision trees and scripted chatbots inspired by Cognitive Behavioral Therapy (CBT), originally pioneered by Aaron T. Beck. While scripted bots demonstrated modest efficacy in reducing mild depressive symptoms in randomized controlled trials (Fitzpatrick et al., 2017), their rigid decision trees frequently frustrated users whose nuanced emotional expressions fell outside predefined keyword branches.

The advent of Transformer-based Large Language Models (LLMs) such as Google's Gemini family, OpenAI's GPT series, and Anthropic's Claude has ushered in third-generation mHealth systems capable of context-aware, empathetic, and culturally adaptive therapeutic dialogue.

## 2.2 Standardized Psychometric Screening Instruments in Clinical Practice
Objective measurement is the cornerstone of evidence-based psychiatry. Clinical practice relies on validated psychometric instruments to quantify symptom severity and monitor treatment response:
1. **PHQ-9 (Patient Health Questionnaire-9):** Developed by Kroenke, Spitzer, and Williams (2001), the PHQ-9 scores each of the 9 DSM-IV/DSM-5 criteria for Major Depressive Disorder on a 4-point Likert scale (`0 = Not at all` to `3 = Nearly every day`), yielding a total score from 0 to 27.
2. **GAD-7 (Generalized Anxiety Disorder-7):** Developed by Spitzer et al. (2006), the GAD-7 is a 7-item instrument (score range 0–21) with high sensitivity (89%) and specificity (82%) for screening Generalized Anxiety Disorder and panic spectrum symptoms.
3. **PSS-10 (Perceived Stress Scale):** Originated by Cohen et al. (1983), measuring the degree to which life situations are appraised as unpredictable, uncontrollable, and overloading.
4. **Academic / Semester Burnout Inventories:** Derived from the Maslach Burnout Inventory-Student Survey (MBI-SS; Schaufeli et al., 2002), assessing emotional exhaustion, cynicism toward studies, and academic inefficacy.
5. **Personality & Cognitive Frameworks:**
   * **Big Five Inventory (OCEAN):** Costa and McCrae's Five-Factor Model measuring Openness, Conscientiousness, Extraversion, Agreeableness, and Neuroticism.
   * **The Dark Triad:** Paulhus and Williams (2002) framework evaluating subclinical Narcissism, Machiavellianism, and Psychopathy.
   * **Emotional Intelligence (EI/EQ):** Goleman and Mayer-Salovey models of emotional awareness, empathy, and regulation.
   * **Adult Attachment Theory:** Bowlby and Ainsworth's attachment paradigms adapted for adult interpersonal relationships (Secure, Anxious-Preoccupied, Dismissive-Avoidant, Fearful-Avoidant).
   * **Cognitive Distortions:** Beck and Burns' taxonomy of maladaptive thinking patterns (catastrophizing, black-and-white thinking, emotional reasoning, mind reading).

## 2.3 Large Language Models (LLMs) in Psychiatric Triage and CBT Delivery
Recent clinical informatics research demonstrates that properly prompted LLMs can effectively guide patients through Socratic questioning, cognitive restructuring, behavioral activation, and crisis de-escalation. However, raw unguided LLMs suffer from two clinical shortcomings:
* **Premature Advice Giving:** Without role constraints, general LLMs immediately output generic bullet points rather than asking probing diagnostic questions first.
* **Lack of Longitudinal Clinical Memory:** Without integration into a structured database, an LLM cannot correlate a user's current chat with their PHQ-9 score or sleep diary.

**ThanatoX** directly resolves both limitations through its **5-Step Psychological Session** (which enforces five sequential diagnostic questions before generating an `Rx` guideline) and its **Context-Enriched Prompt Architecture** inside `GeminiPsychiatristService`.

## 2.4 Review of Existing Applications
1. **Woebot:** A popular CBT chatbot that uses primarily pre-scripted conversational flows. It lacks open-ended bilingual (`বাংলা`) natural language comprehension, multimodal document review, and comprehensive personality testing.
2. **Wysa:** Combines scripted exercises with limited AI chat and human coaching behind a costly subscription paywall. It does not offer a 9-scale psychometric diagnostic suite or custom multi-provider LLM API routing.
3. **Daylio:** A widely used micro-diary and mood tracker. While effective for logging daily icons, Daylio contains zero psychiatric consultation capabilities, zero clinical scales, and no interactive CBT session workflows.
4. **Headspace / Calm:** Focused almost exclusively on pre-recorded audio meditations and sleep stories, lacking diagnostic assessments, interactive psychiatric Q&A, or cognitive restructuring tools.

## 2.5 Critical Limitations of Existing Solutions
* None of the mainstream applications provide **full native Bengali (`বাংলা`) and English localization** across clinical scales, psychological articles, and AI psychiatric consultations.
* Existing apps do not allow users to **save, review, and manage both psychometric assessment reports and structured 5-step session prescriptions** with dedicated AI clinical commentary in a unified cloud repository.
* High subscription fees ($60–$100/year) exclude university students in developing regions.

## 2.6 Comparative Feature Matrix: Existing Systems vs. ThanatoX

| Feature / Capability | Woebot | Wysa | Daylio | Calm | **ThanatoX (Proposed System)** |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **24/7 Generative AI Psychiatrist Consultation** | Scripted | Partial | No | No | **Yes (4 Clinical Modes + Multimodal)** |
| **5-Step Guided Psychological Session (`Rx` Output)** | No | No | No | No | **Yes (Adaptive 5-Q Interview + Saved Rx)** |
| **4 Standardized Clinical Scales (PHQ-9, GAD-7, PSS-10, Burnout)** | No | Partial | No | No | **Yes (Full Scoring + AI Clinical Analysis)** |
| **5 Personality & Cognitive Tests (Big 5, Dark Triad, EQ, etc.)** | No | No | No | No | **Yes (Integrated in Assessment Suite)** |
| **Native Bilingual Interface (`বাংলা` + `English`)** | No | No | No | No | **Yes (100% Bilingual UI, Tests & AI)** |
| **Zero-Scroll 4-Box Home + Floating Draggable Chat Bubble** | No | No | No | No | **Yes (Ergonomic Single-Viewport Fit)** |
| **Somatic Toolkit (4-7-8 Breathing, 5-4-3-2-1 Grounding, PMR)** | Partial | Yes | No | Yes | **Yes (Interactive Animated Timers)** |
| **Multi-Provider LLM Engine (Gemini, OpenAI, Groq, DeepSeek, Claude)** | No | No | No | No | **Yes (Built-in Gemini 2.5 + Custom API Router)** |
| **Zero-Trust Cloud Firestore Sync + Direct Report Deletion** | No | No | Cloud Backup | No | **Yes (Owner-Scoped Rules + Instant Delete)** |

*Table 2.1: Comparative Matrix between Existing mHealth Apps and ThanatoX.*

---
---

# CHAPTER 3: SYSTEM ANALYSIS & REQUIREMENTS SPECIFICATION

## 3.1 Feasibility Study

### 3.1.1 Technical Feasibility
The system is developed using **Kotlin** and **Jetpack Compose**, Google's modern declarative UI toolkit for Android, paired with **Firebase Authentication**, **Cloud Firestore**, and **Google Gemini 2.5 Flash REST APIs** via `OkHttp`/`HttpURLConnection` and Kotlin Coroutines. All required SDKs are mature, production-hardened, and natively supported on Android API levels 26 through 36. Therefore, the system is 100% technically feasible.

### 3.1.2 Operational Feasibility
University students and general smartphone users are already familiar with messaging interfaces (like Messenger chat heads) and card-based mobile dashboards. By designing a **Zero-Scroll 4-Box Home Screen** and a **Messenger-style draggable chat bubble**, the learning curve is virtually zero. Furthermore, full bilingual support (`বাংলা` and `English`) ensures high operational adoption across both Bengali-speaking and English-speaking users.

### 3.1.3 Economic Feasibility
**ThanatoX** leverages serverless cloud infrastructure (Firebase Spark/Blaze tiers with strict document validation) and efficient LLM token management via **Gemini 2.5 Flash**, while also allowing power users or institutions to plug in their own free or paid API keys (Groq, DeepSeek, OpenRouter, OpenAI, Claude). This keeps operational costs near zero for student deployments.

### 3.1.4 Ethical & Clinical Safety Feasibility
All clinical scales use public-domain or academically standardized scoring rubrics. The AI psychiatrist persona (**Dr. Sifat Khan**) is strictly instructed via system prompts to maintain non-judgmental empathy, avoid prescribing controlled pharmacological substances, focus on evidence-based behavioral and cognitive interventions, and display crisis helpline guidance when severe distress is detected.

## 3.2 Stakeholder Identification and User Personas
1. **Primary User — University Student (e.g., Sifat, 22, CSE Undergraduate):** Experiencing exam anxiety, sleep disruption, and semester burnout; needs quick psychometric screening, a 5-step psychological session for actionable study/stress guidelines, and a fresh chat session upon opening the app.
2. **Secondary User — Self-Discovery & Psychology Enthusiast:** Interested in reading the 12 clinical psychology articles and taking personality assessments (Big Five OCEAN, Dark Triad, Emotional Intelligence, Attachment Style).
3. **Clinical/Academic Supervisor & Evaluator:** Evaluates the architectural integrity, security rules, psychometric accuracy, and UI responsiveness of the platform.

## 3.3 Functional Requirements Specification (FRS)

| Req. ID | Module | Functional Requirement Description | Priority |
| :--- | :--- | :--- | :---: |
| **FR-01** | **Authentication** | Support Google Sign-In via Jetpack `CredentialManager` and an optional Ephemeral Guest Sanctuary Mode. | High |
| **FR-02** | **Home Screen** | Render all 4 primary functional boxes (Introduction, Mood Selector, Articles Preview, Tests Preview) within 100% of the screen viewport without vertical scrolling or bottom dead space. | High |
| **FR-03** | **Floating Chat Head** | Provide a Messenger-style circular draggable floating avatar bubble (`DraggableDoctorChatBubble`) with online status badge that snaps to screen edges and opens the Consultation tab on tap. | High |
| **FR-04** | **AI Consultation** | Default to a fresh new chat session whenever the app is launched, while preserving the active conversation when switching between bottom navigation tabs during the same app session. | High |
| **FR-05** | **Multimodal & Modes** | Support 4 clinical consultation modes (*CBT, Crisis, Semester Burnout, Sleep Anxiety*) and allow attaching camera photos, gallery images, and PDF/text documents. | High |
| **FR-06** | **9 Clinical & Psych Tests** | Provide 4 clinical scales (*PHQ-9, GAD-7, PSS-10, Semester Burnout*) and 5 psychological tests (*Dark Triad, Big Five, EQ, Attachment Style, Cognitive Distortion*) with automated scoring and **Dr. Sifat Khan's Psychological Analysis**. | High |
| **FR-07** | **5-Step Psych Session** | Conduct a 5-step guided psychological session in Mind Care (Problem selection -> 5 progressive AI questions -> Personalized `Rx` Prescription & Guideline). | High |
| **FR-08** | **Saved Reports & Delete** | Persist completed test reports and psychological session prescriptions to Cloud Firestore and provide a direct **Delete Icon** on every saved report card for instant removal. | High |
| **FR-09** | **Mind Care Toolkit** | Provide interactive 4-7-8 Breathing timer, 5-4-3-2-1 Sensory Grounding, Progressive Muscle Relaxation, and a Cloud Mood & Sleep Diary. | Medium |
| **FR-10** | **Psychology Library** | Provide 12 comprehensive bilingual clinical psychology articles accessible from the Home Screen. | Medium |
| **FR-11** | **Settings & Custom AI** | Provide a clean row-based Settings screen with Language Toggle (`বাংলা`/`English`), Theme Toggle (`Dark`/`Light`), Multi-Provider Custom AI API Key configuration, and a confirmation-guarded **Reset All Data** action separate from **Sign Out**. | High |

*Table 3.1: Functional Requirements Traceability Matrix.*

## 3.4 Non-Functional Requirements Specification (NFRS)

| NFR ID | Category | Specification | Target Metric |
| :--- | :--- | :--- | :--- |
| **NFR-01** | **UI Performance** | Smooth frame pacing during transitions and image rendering via off-thread SRGB bitmap caching. | 60/120 FPS (`0 Skipped Frames` on startup) |
| **NFR-02** | **Security & Privacy** | Zero-Trust Firestore security rules (`request.auth.uid == userId`) preventing cross-user read/write access. | 100% Rule Enforcement |
| **NFR-03** | **Usability & Ergonomics** | Minimum 48dp touch targets, Material 3 dynamic typography, and zero-scroll home dashboard fit. | WCAG AA / M3 Compliant |
| **NFR-04** | **Availability & Resilience** | Graceful fallback between server-side Gemini API, user custom API keys, and deterministic offline clinical guidance if network drops. | 99.9% Functional Continuity |
| **NFR-05** | **Localization** | Instantaneous runtime language switching between Bengali (`বাংলা`) and English without app restart. | < 16ms State Recomposition |

*Table 3.2: Non-Functional Performance and Security Requirements.*

## 3.5 Hardware and Software Development Environment

| Component | Specification |
| :--- | :--- |
| **Programming Language** | Kotlin 2.x (Coroutines, StateFlow, Serialization) |
| **UI Framework** | Jetpack Compose with Material Design 3 (`androidx.compose.material3`) |
| **Build System** | Gradle (Kotlin DSL `.gradle.kts`) with Version Catalog (`libs.versions.toml`) |
| **Application ID** | `com.aistudio.thanatox.psychm` (Namespace: `com.example`) |
| **Target / Compile SDK** | Android SDK 36 (Minimum SDK: 26) |
| **Cloud Backend** | Google Firebase Authentication & Cloud Firestore (`firebase-bom`) |
| **Identity Management** | Jetpack Credential Manager (`androidx.credentials` + `googleid`) |
| **AI / LLM Integration** | Gemini 2.5 Flash + Multi-Provider REST Client (OpenAI, Groq, DeepSeek, OpenRouter, Claude) |

*Table 3.3: Development & Runtime Software Stack Specifications.*

---
---

# CHAPTER 4: SYSTEM ARCHITECTURE & DATABASE DESIGN

## 4.1 High-Level System Architecture (MVVM + Clean Repository Pattern)
**ThanatoX** is architected strictly according to Google's recommended **Model-View-ViewModel (MVVM)** pattern combined with a **Single-Source-of-Truth Repository Layer**. This separation of concerns ensures testability, lifecycle safety, and maintainability:

```
+-----------------------------------------------------------------------------------+
|                              UI LAYER (Jetpack Compose)                           |
|  MainActivity -> HomeScreen | ConsultationScreen | AssessmentScreen | MindCare    |
|  (Observes immutable StateFlow via collectAsStateWithLifecycle())                 |
+-----------------------------------------+-----------------------------------------+
                                          | User Intents / Actions
                                          v
+-----------------------------------------------------------------------------------+
|                        PRESENTATION LAYER (ThanatoXViewModel)                     |
|  - Manages UI StateFlows (user, consultations, assessments, moodLogs, language)   |
|  - Coordinates Coroutine launches in viewModelScope                               |
+--------------------+-----------------------------------------+--------------------+
                     |                                         |
                     v                                         v
+----------------------------------------+   +--------------------------------------+
|     DATA LAYER (ThanatoXRepository)    |   |   AI LAYER (GeminiPsychiatristService)|
| - Firestore Snapshot Listeners         |   | - Server-side Gemini 2.5 Flash       |
| - Zero-Trust CRUD Operations           |   | - Multi-Provider Custom API Router   |
| - [TYPE:...] Schema Encoding/Decoding  |   | - Multimodal Base64 Payload Builder  |
| - SharedPreferences (Theme/Lang/API)   |   | - 5-Step Psych Session Rx Generator  |
+--------------------+-------------------+   +--------------------------------------+
                     |
                     v
+----------------------------------------+
|   CLOUD BACKEND (Firebase Firestore)   |
|  /users/{userId}/consultations/{id}    |
|  /users/{userId}/mood_logs/{id}        |
|  /users/{userId}/assessments/{id}      |
+----------------------------------------+
```

## 4.2 Unidirectional Data Flow (UDF) and Reactive State Management
In **ThanatoX**, state flows strictly downward and events flow strictly upward:
1. **State Exposure:** `ThanatoXViewModel` exposes immutable `StateFlow<T>` streams (`consultations`, `moodLogs`, `assessments`, `currentUser`, `isGuestMode`, `language`, `isDarkMode`, `customApiConfig`).
2. **Lifecycle-Aware Collection:** Composables collect state via `collectAsStateWithLifecycle()`, preventing wasted background recompositions when the app goes into the background.
3. **Real-Time Firestore Synchronization:** `ThanatoXRepository` attaches real-time `addSnapshotListener` callbacks to the authenticated user's Firestore subcollections and converts them into Kotlin `callbackFlow` streams. Whenever a consultation message, mood entry, or assessment report is added or deleted, the UI updates instantaneously within milliseconds.

## 4.3 Authentication Architecture: Jetpack Credential Manager & Ephemeral Guest Mode
In compliance with modern Android security standards, **ThanatoX** implements two authentication pathways:
1. **Google Sign-In via Jetpack `CredentialManager`:** Uses `GetSignInWithGoogleOption` to authenticate the user with their Google account and exchange the ID token for a `FirebaseAuth` credential (`GoogleAuthProvider.getCredential(idToken, null)`).
2. **Ephemeral Guest Sanctuary Mode:** Users who wish to explore the app immediately or maintain complete anonymity without signing in can enter Guest Mode (`enterGuestMode()`). In Guest Mode, consultations, assessments, and mood entries are stored in reactive in-memory state for the session duration, while signed-in users enjoy permanent multi-device Cloud Firestore persistence.

## 4.4 Cloud Firestore NoSQL Database Schema Design
All user data in Cloud Firestore is organized hierarchically under a root `users` collection, keyed by the authenticated Firebase UID (`{userId}`):

| Subcollection Path | Document ID | Key Fields & Data Types | Purpose |
| :--- | :--- | :--- | :--- |
| `/users/{userId}/consultations/{consultationId}` | Auto/Custom ID | `userId` (String), `sessionId` (String), `sender` (`"user"` \| `"ai"`), `message` (String, 1–8000 chars), `clinicalMode` (`"CBT"` \| `"Crisis"` \| `"SemesterBurnout"` \| `"SleepAnxiety"`), `timestamp` (Long) | Stores multi-turn chat transcripts between the user and Dr. Sifat Khan. |
| `/users/{userId}/mood_logs/{logId}` | Auto ID | `userId` (String), `moodLabel` (String), `moodScore` (Int, 1–10), `sleepHours` (Double, 0–24), `stressTriggers` (String), `journalNote` (String, <= 4000 chars), `timestamp` (Long) | Stores daily mood scores, sleep duration, triggers, and journal notes. |
| `/users/{userId}/assessments/{assessmentId}` | Auto ID | `userId` (String), `assessmentType` (`"PHQ-9"` \| `"GAD-7"` \| `"Semester-Burnout"`), `totalScore` (Int, 0–100), `maxScore` (Int, 1–100), `severityLabel` (String, 1–500 chars), `clinicalSummary` (String, <= 4000 chars), `timestamp` (Long) | Stores all 9 clinical/psychological test reports and 5-Step Psychological Session `Rx` reports. |

*Table 4.1: Cloud Firestore Collection Hierarchy and Field Constraints.*

## 4.5 Zero-Trust Security Rules (`firestore.rules`) & Schema-Compliant Metadata Encoding
To prevent unauthorized data access or malicious payload injection, `firestore.rules` enforces a strict **Zero-Trust** policy:
* Every read, create, update, and delete request must satisfy `isOwner(userId)`, defined as `request.auth != null && request.auth.uid == userId`.
* Immutable ownership fields (`request.resource.data.userId == resource.data.userId`) cannot be altered during updates.
* **Schema-Compliant Extended Metadata Encoding:** Because the deployed `firestore.rules` validates `data.assessmentType in ['PHQ-9', 'GAD-7', 'Semester-Burnout']`, `ThanatoXRepository` implements a transparent encoding mechanism to support all **9 clinical/psychological tests** plus **`PSYCH_SESSION`** prescriptions without violating the Firestore security rule:
  * **On Write (`saveAssessment`):** If `assessmentType` is one of the extended tests (e.g., `"PSS-10"`, `"Dark-Triad"`, `"Big-Five"`, `"EI-EQ"`, `"Attachment-Style"`, `"Cognitive-Distortion"`, or `"PSYCH_SESSION"`), the repository maps the Firestore `assessmentType` field to a rule-compliant enum (`"Semester-Burnout"`) and prefixes `severityLabel` with `[TYPE:<OriginalType>]` (truncated safely under 500 characters).
  * **On Read (`observeAssessments`):** The repository parses the `[TYPE:<OriginalType>]` prefix from `severityLabel`, restores the exact `assessmentType` to the domain model `ClinicalAssessmentRecord`, and strips the prefix from `severityLabel`. This guarantees 100% Firestore security compliance while supporting all 9 tests and psychological session prescriptions.

## 4.6 Multi-Provider AI Routing Architecture (`GeminiPsychiatristService`)
A standout architectural feature of **ThanatoX** is its extensible **Multi-Provider AI Routing Engine**:

| Provider Key | Display Name | Default Model | Endpoint Architecture |
| :--- | :--- | :--- | :--- |
| **`DEFAULT_GEMINI`** | Built-in Server Gemini | `gemini-2.5-flash` | Native Google Generative Language REST API (`v1beta/models/...:generateContent`) |
| **`CUSTOM_GEMINI`** | Custom Google Gemini | `gemini-2.5-flash` | Google Generative Language REST API with User Custom Key |
| **`OPENAI`** | OpenAI | `gpt-4o-mini` / `gpt-4o` | `https://api.openai.com/v1/chat/completions` |
| **`GROQ`** | Groq Cloud (Ultra-Fast LPU) | `llama-3.3-70b-versatile` | `https://api.groq.com/openai/v1/chat/completions` |
| **`DEEPSEEK`** | DeepSeek AI | `deepseek-chat` | `https://api.deepseek.com/v1/chat/completions` |
| **`OPENROUTER`** | OpenRouter | `google/gemini-2.5-flash` | `https://openrouter.ai/api/v1/chat/completions` |
| **`CLAUDE`** | Anthropic Claude | `claude-3-5-haiku-latest` | `https://api.anthropic.com/v1/messages` |

*Table 4.2: Supported AI Providers, Default Models, and Endpoint Architectures.*

## 4.7 Navigation Architecture and Predictive Back Handling
* **Bottom Sanctuary Navigation Bar:** Provides five primary destinations: **Home (`হোম`)**, **Chat (`চ্যাট`)**, **Tests (`পরীক্ষা`)**, **Mind Care (`মাইন্ড কেয়ার`)**, and **Settings (`সেটিংস`)**.
* **Hoisted Active Chat Session State (`activeChatSessionId`):** Hoisted to the root shell in `MainActivity.kt` and initialized to a unique timestamped ID (`"s_${System.currentTimeMillis().toString(36)}"`) when the app launches. This ensures that opening the app always presents a clean, fresh consultation screen, whereas switching between tabs during the same session preserves the ongoing chat.
* **Clean Back Stack Management:** Uses `BackHandler(enabled = selectedTab != SanctuaryTab.HOME)` in `MainActivity.kt` and sub-section back handlers in `MindCareScreen.kt` with `android:enableOnBackInvokedCallback="false"` in `AndroidManifest.xml` to eliminate Android `WindowOnBackDispatcher` cancellation warnings.

---
---

# CHAPTER 5: CLINICAL METHODOLOGY & PSYCHOMETRIC ENGINE DESIGN

## 5.1 Design of the "Dr. Sifat Khan" AI Psychiatrist Clinical Persona
All generative AI interactions inside **ThanatoX** are governed by the clinical persona of **Dr. Sifat Khan (`ড. সিফাত খান — সিনিয়র কনসালটেন্ট সাইকিয়াট্রিস্ট ও মনোরোগ বিশেষজ্ঞ`)**. The system prompt architecture enforces five clinical behaviors:
1. **Warm Therapeutic Alliance:** Validates the patient's emotional pain before offering cognitive reframing.
2. **Socratic Inquiry:** Asks gentle, insightful follow-up questions to uncover core beliefs and triggers.
3. **Evidence-Based Modality Alignment:** Draws exclusively from Cognitive Behavioral Therapy (CBT), Acceptance and Commitment Therapy (ACT), Dialectical Behavior Therapy (DBT), and somatic neuroscience.
4. **Cultural & Linguistic Congruence:** Responds naturally in idiomatic, compassionate Bengali (`বাংলা`) or professional clinical English matching the user's selected language.
5. **Safety Guardrails:** Never diagnoses irreversible neurological pathology without in-person examination and immediately provides crisis intervention resources when self-harm ideation is detected.

## 5.2 Prompt Engineering for the 4 Specialized Consultation Modes
Users can switch the clinical focus of their live consultation across four modes:
1. **`CBT` (Cognitive Behavioral Reframing):** Identifies automatic negative thoughts (ANTs) and guides the user through evidence-testing and balanced thought replacement.
2. **`Crisis` (Acute Emotional De-escalation):** Uses short, grounding sentences, immediate autonomic stabilization (breathing/sensory anchoring), and empathetic containment.
3. **`SemesterBurnout` (Academic & Career Recovery):** Focuses on breaking task paralysis, Pomodoro energy pacing, perfectionism dismantling, and guilt-free rest scheduling.
4. **`SleepAnxiety` (Insomnia & Circadian Hygiene):** Applies CBT-I (Cognitive Behavioral Therapy for Insomnia) principles, stimulus control, and racing-mind wind-down protocols.

## 5.3 Methodology of the 5-Step Interactive Psychological Session & `Rx` Generation
Located inside the **Mind Care Hub**, the **Psychological Session (`সাইকোলজিকাল সেশন`)** replicates a structured psychiatric chamber visit in three distinct phases:
* **Phase 1 — Presenting Complaint Selection:** The user selects from 6 clinical categories (*Depression & Hopelessness, Anxiety & Panic, Insomnia & Sleep Disorder, Relationship & Family Trauma, Academic/Career Burnout, OCD & Intrusive Thoughts*) or writes their own custom problem statement.
* **Phase 2 — 5-Step Progressive Clinical Interview:** Instead of giving an immediate generic answer, **Dr. Sifat Khan** conducts a 5-question sequential clinical interview (`Step 1/5` through `Step 5/5`):
  * *Question 1:* Onset, duration, and primary emotional manifestation.
  * *Question 2:* Specific environmental, academic, or interpersonal triggers.
  * *Question 3:* Somatic and physiological impact (sleep, appetite, energy, focus).
  * *Question 4:* Current coping mechanisms and cognitive self-talk patterns.
  * *Question 5:* Functional impact and immediate recovery goal.
  Each subsequent question is dynamically tailored by the AI based on the user's previous answers.
* **Phase 3 — Personalized `Rx` Therapeutic Prescription & Guideline:** Upon completing Question 5, the AI synthesizes the entire 5-step clinical transcript and outputs a structured **`Rx` Prescription** containing:
  1. **Clinical Formulation & Root Cause Assessment**
  2. **Immediate Daily Behavioral Prescription (Morning / Daytime / Night Routine)**
  3. **Targeted CBT Cognitive Exercise**
  4. **Recommended Somatic & Breathing Protocol**
  5. **Long-Term Relapse Prevention Guideline**
  The completed `Rx` report is automatically saved to Cloud Firestore so the user can review or delete it anytime.

## 5.4 Mathematical Scoring & Severity Stratification of the 4 Clinical Scales
For an assessment with $N$ items where each item $i \in \{1, \dots, N\}$ is scored on a Likert scale $s_i \in \{0, 1, 2, 3\}$, the total score $S_{\text{total}}$ and normalized percentage $P$ are computed as:

$$S_{\text{total}} = \sum_{i=1}^{N} s_i, \quad P = \left( \frac{S_{\text{total}}}{S_{\max}} \right) \times 100\%$$

| Scale Code | Instrument Name | Items ($N$) | Score Range | Severity Stratification Cutoffs |
| :--- | :--- | :---: | :---: | :--- |
| **`PHQ-9`** | Patient Health Questionnaire (Depression) | 9 | 0 – 27 | `0–4`: Minimal \| `5–9`: Mild \| `10–14`: Moderate \| `15–19`: Moderately Severe \| `20–27`: Severe Depression |
| **`GAD-7`** | Generalized Anxiety Disorder Scale | 7 | 0 – 21 | `0–4`: Minimal \| `5–9`: Mild \| `10–14`: Moderate \| `15–21`: Severe Anxiety |
| **`PSS-10`** | Perceived Stress Scale | 10 | 0 – 30 | `0–9`: Low Stress \| `10–19`: Moderate Stress \| `20–30`: High Perceived Stress |
| **`Semester-Burnout`** | Academic & Work Burnout Inventory | 8 | 0 – 24 | `0–6`: Healthy Balance \| `7–12`: Mild Fatigue \| `13–18`: Moderate Burnout \| `19–24`: Severe Academic Burnout |

*Table 5.1: PHQ-9, GAD-7, PSS-10, and Semester Burnout Scoring & Severity Cutoffs.*

## 5.5 Profiling Methodology of the 5 Psychological & Personality Assessments

| Scale Code | Instrument Name | Core Psychological Dimensions Evaluated | Clinical Utility in ThanatoX |
| :--- | :--- | :--- | :--- |
| **`Dark-Triad`** | Dark Triad Personality Assessment | Subclinical Narcissism, Strategic Machiavellianism, Impulse/Callousness | Evaluates interpersonal dominance, ego defense, and manipulation tendencies. |
| **`Big-Five`** | Big Five (OCEAN) Personality Inventory | Openness, Conscientiousness, Extraversion, Agreeableness, Neuroticism | Provides comprehensive trait profiling and emotional stability baseline. |
| **`EI-EQ`** | Emotional Intelligence (EQ) Scale | Self-Awareness, Emotional Regulation, Empathy, Social Calibration | Identifies strengths and deficits in interpersonal and intrapersonal emotion processing. |
| **`Attachment-Style`** | Adult Attachment Style Inventory | Secure, Anxious-Preoccupied, Dismissive-Avoidant, Fearful-Avoidant | Uncovers root relational patterns driving relationship anxiety or emotional withdrawal. |
| **`Cognitive-Distortion`** | Cognitive Distortion Screening | Catastrophizing, All-or-Nothing Thinking, Mind Reading, Overgeneralization | Pinpoints specific CBT thinking traps for targeted cognitive restructuring. |

*Table 5.2: Psychological & Personality Tests Dimension Mapping.*

Following every completed test, **ThanatoX** displays a dedicated **"Dr. Sifat Khan's Psychological Analysis" (`ড. সিফাত খানের মনস্তাত্ত্বিক বিশ্লেষণ`)** card that translates the numerical score into compassionate, actionable clinical advice, and requests an enriched AI interpretation from `GeminiPsychiatristService`.

---
---

# CHAPTER 6: IMPLEMENTATION & MODULE WALKTHROUGH

## 6.1 Project Package Structure and Modularization
The Android codebase under `/app/src/main/java/com/example/` is cleanly modularized into domain-focused packages:
* `MainActivity.kt`: Application entry point, `enableEdgeToEdge()` configuration, SRGB bitmap cache preloading, root navigation shell, and hoisted `activeChatSessionId`.
* `model/ThanatoXModels.kt`: Immutable Kotlin data classes for `ConsultationMessage`, `MoodLogEntry`, `ClinicalAssessmentRecord`, `CustomApiConfig`, `AiProvider`, and clinical enums.
* `data/repository/ThanatoXRepository.kt`: Firebase Auth + Cloud Firestore repository, snapshot listeners, `[TYPE:...]` schema encoder/decoder, and local preference persistence.
* `data/ai/GeminiPsychiatristService.kt`: Multi-provider LLM HTTP client, multimodal image/PDF payload builder, 5-step psychological session interviewer, and `Rx` generator.
* `viewmodel/ThanatoXViewModel.kt`: Central reactive state holder coordinating UI intents, repository streams, and AI coroutines.
* `ui/theme/`: Custom Material 3 Bioluminescent Teal Glassmorphism color palette (`Color.kt`), typography (`Type.kt`), and theme wrapper (`Theme.kt`).
* `ui/components/LuxuryPsychComponents.kt`: Reusable glassmorphic cards, ambient background shaders, and `ThanatoXBitmapCache` / `rememberSrgbBitmapPainter`.
* `ui/screens/`:
  * `HomeScreen.kt`: 4-Box zero-scroll proportional dashboard, `DraggableDoctorChatBubble`, and article reader modal.
  * `PsychologyArticlesCatalog.kt`: 12 full-length bilingual clinical psychology articles.
  * `ConsultationScreen.kt`: 24/7 AI psychiatrist chat interface, session drawer, mode selector, and multimodal attachment picker.
  * `AssessmentScreen.kt` & `PsychologicalTestsCatalog.kt`: 9 clinical/psychological tests, interactive question stepper, Dr. Sifat Khan's analysis card, and saved report cards with direct delete icons.
  * `MindCareScreen.kt` & `PsychologicalSessionSection.kt`: Mind Care hub containing the 5-step AI Psychological Session, Therapy Toolkit (`ToolkitScreen.kt`), and Mood & Sleep Diary (`MoodAndCalmScreen.kt`).
  * `SettingsScreen.kt`: Clean row-based settings, custom AI API modal, and Reset All Data confirmation dialog.

## 6.2 Implementation of the 4-Box Proportional Home Screen (`HomeScreen.kt`)
A major user requirement was ensuring that **all four primary sections on the Home Screen are visible simultaneously at a single glance without vertical scrolling**, while keeping text large, legible, and free of empty bottom space.

To achieve this across varying device screen heights, `HomeScreen.kt` implements a `Column(modifier = Modifier.fillMaxSize())` utilizing precise proportional `Modifier.weight(...)` allocations totaling `4.0f` after accounting for the compact top header:

| Box Number | Functional Component | Proportional Weight | Typography Sizing |
| :---: | :--- | :---: | :--- |
| **Box 1** | **Sanctuary Introduction Box** (`HomeIntroductionBox`) | `weight(1.44f)` | Title: `15.5.sp–17.sp` (Bold) \| Body: `12.5.sp–13.8.sp` (`lineHeight = 17.5.sp`) |
| **Box 2** | **Instant Mood Check-In Box** (`HomeMoodSelectorBox`) | `weight(0.84f)` | Header: `14.8.sp–15.8.sp` \| Mood Chips: `13.sp–14.sp` |
| **Box 3** | **Psychology Articles Preview Box** (`HomeArticlesPreviewBox`) | `weight(0.88f)` | Header: `14.8.sp–15.8.sp` \| Article Titles: `13.2.sp–14.2.sp` |
| **Box 4** | **Clinical & Psych Tests Preview Box** (`HomeTestsPreviewBox`) | `weight(0.84f)` | Header: `14.8.sp–15.8.sp` \| Test Badges: `13.sp–14.sp` |

*Table 6.1: Proportional Weight Distribution of the 4-Box Zero-Scroll Home Screen.*

Furthermore, adaptive scaling via `BoxWithConstraints` dynamically tunes internal padding (`isCompactHeight = maxHeight < 680.dp`) so the 4 boxes fill 100% of the screen seamlessly on both compact phones and tall flagship displays.

## 6.3 Physics-Based Messenger-Style Draggable Chat Bubble (`DraggableDoctorChatBubble`)
Instead of cluttering Box 1 with a static consultation button, **ThanatoX** implements `DraggableDoctorChatBubble` overlaid on the Home Screen:
* Renders a circular `58.dp` glowing avatar of **Dr. Sifat Khan** with a live green online indicator badge (`14.dp`) and a subtle chat icon pill.
* Uses `Modifier.pointerInput(Unit) { detectDragGestures(...) }` to track finger drag deltas `(offsetX, offsetY)` in real time across the entire screen bounds.
* Upon drag release (`onDragEnd`), calculates the horizontal midpoint of the screen and smoothly snaps the bubble to either the left or right screen margin while clamping vertical position within safe inset bounds.
* Tapping the bubble immediately navigates the user to the live AI Psychiatrist Consultation screen.

## 6.4 Implementation of the 24/7 Multimodal Consultation Module (`ConsultationScreen.kt`)
* **Default Fresh Chat on Launch:** By hoisting `activeChatSessionId` to `MainActivity`, launching the app always initializes a brand-new session ID (`s_<timestamp>`), presenting a clean consultation canvas just like modern AI assistants. Users can open the **Session History Drawer** at any time to revisit or delete past sessions.
* **Multimodal Attachments:** Integrates `ActivityResultContracts.PickVisualMedia()`, `ActivityResultContracts.TakePicturePreview()`, and `ActivityResultContracts.OpenDocument()` so users can attach lab reports, prescriptions, handwritten notes, or images directly into their psychiatric consultation.

## 6.5 Implementation of the 9-Test Assessment Engine & Direct Report Deletion (`AssessmentScreen.kt`)
* Users switch effortlessly between **Clinical Scales (4)** and **Psychological & Personality Tests (5)**.
* Upon finishing a test, the app computes the exact score, displays the severity badge, highlights **Dr. Sifat Khan's Psychological Analysis** in a dedicated clinical card, and saves the record to Firestore.
* In the **Saved Reports (`সংরক্ষিত রিপোর্ট`)** list, every report card includes a direct **Delete IconButton (`Icons.Default.DeleteOutline`)** in the top-right corner, invoking `viewModel.deleteAssessment(record.id)` for one-tap removal.

---
---

# CHAPTER 7: PERFORMANCE OPTIMIZATION, TESTING & VERIFICATION

## 7.1 Android Graphics Pipeline Optimization: Off-Thread SRGB Bitmap Cache (`ThanatoXBitmapCache`)
During initial profiling on Android 16 (API 36), rendering high-resolution custom PNG drawables (`hero_sanctuary_banner`, `doctor_ai_avatar`, `mind_calm_illustration`, `empty_journal_art`) directly via `painterResource()` on the UI thread caused two logcat warnings:
1. `W/HWUI: Unknown dataspace 0` (caused by PNGs lacking an embedded ICC color profile header).
2. `I/Choreographer: Skipped 35 frames! / Davey! duration=726ms` (caused by synchronous main-thread bitmap decoding during initial composition).

To eliminate both issues permanently, we engineered `ThanatoXBitmapCache` and `rememberSrgbBitmapPainter` in `LuxuryPsychComponents.kt`:
* **Preloading on `Dispatchers.IO`:** In `MainActivity.onCreate()`, `ThanatoXBitmapCache.preloadAll(applicationContext)` launches background coroutines on `Dispatchers.IO`.
* **Explicit SRGB ColorSpace Assignment:** Decodes drawables using `BitmapFactory.Options().apply { inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB); inPreferredConfig = Bitmap.Config.ARGB_8888 }`.
* **Memory Caching:** Stores the decoded `ImageBitmap` instances in a thread-safe `ConcurrentHashMap<Int, ImageBitmap>`, allowing instant zero-latency rendering on the UI thread.

| Performance Metric | Before Optimization | After `ThanatoXBitmapCache` & Composable Extraction |
| :--- | :---: | :---: |
| **`W/HWUI: Unknown dataspace 0` Warnings** | 12+ warnings per screen | **0 Warnings (100% Resolved)** |
| **Startup Skipped Frames (`Choreographer`)** | 35 frames (`726ms` jank) | **0 Skipped Frames (`< 16ms` frame time)** |
| **WindowOnBackDispatcher Warnings** | Present on back press | **0 Warnings (Clean `BackHandler` lifecycle)** |
| **Consultation Top Bar JIT Allocation** | `9304 KB` single method | **Reduced via modular `@Composable` extraction** |

*Table 7.2: Runtime Performance Metrics Before vs. After Optimization.*

## 7.2 Functional Test Cases and Verification Matrix

| Test ID | Module Tested | Test Scenario / Input | Expected Output | Status |
| :--- | :--- | :--- | :--- | :---: |
| **TC-01** | **Home Screen Layout** | Launch app on standard smartphone viewport | All 4 boxes (Intro, Mood, Articles, Tests) fit 100% of screen height without scrolling or bottom gap. | **PASS** |
| **TC-02** | **Floating Chat Bubble** | Drag DraggableDoctorChatBubble across screen & tap | Bubble follows touch smoothly, snaps to nearest horizontal edge on release, and opens Chat tab on tap. | **PASS** |
| **TC-03** | **Fresh Chat on Launch** | Open app -> go to Chat tab -> switch to Home -> return to Chat | Opens fresh empty chat on app launch; preserves active chat when switching tabs within same session. | **PASS** |
| **TC-04** | **9 Assessments & AI Analysis** | Complete `PHQ-9` and `Big-Five` tests | Calculates accurate score, displays Dr. Sifat Khan's analysis, and saves report to Firestore without rule error. | **PASS** |
| **TC-05** | **5-Step Psych Session** | Select problem -> answer 5 AI questions -> complete session | Generates structured `Rx` Prescription & Guideline and saves it to Saved Session Reports. | **PASS** |
| **TC-06** | **Report Direct Deletion** | Tap Delete icon (`DeleteOutline`) on saved test or session report | Immediately deletes report from Cloud Firestore and updates UI list reactively. | **PASS** |
| **TC-07** | **Settings & Data Reset** | Configure custom API key; trigger Reset All Data | Saves custom provider config; Reset All Data wipes Firestore collections after modal confirmation without signing out. | **PASS** |

*Table 7.1: Functional Test Cases and Execution Results.*

---
---

# CHAPTER 8: CONCLUSION, LIMITATIONS & FUTURE WORK

## 8.1 Summary of Achievements
This project successfully designed, implemented, and verified **ThanatoX**, a comprehensive, bilingual (`বাংলা` and `English`) AI-powered psychiatric consultation, psychometric testing, and mental wellness sanctuary for Android. By uniting an ergonomic **Zero-Scroll 4-Box Home Screen**, a **Messenger-style floating consultation bubble**, a **24/7 Multimodal AI Psychiatrist (`Dr. Sifat Khan`)**, an interactive **5-Step Psychological Session with `Rx` Prescription generation**, **9 standardized clinical and personality assessments**, and a **Somatic Therapy Toolkit** under a **Zero-Trust Cloud Firestore architecture**, **ThanatoX** establishes a new benchmark for student-centric mobile mental healthcare.

## 8.2 Clinical and Practical Limitations
1. **Non-Replacement for Acute Inpatient Psychiatry:** While **ThanatoX** provides structured CBT guidance and validated psychometric screening, it cannot prescribe regulated psychiatric medications (e.g., SSRIs, benzodiazepines) or replace emergency psychiatric hospitalization.
2. **Internet Connectivity for Live LLM Generation:** Although offline fallback responses and local toolkit timers function without internet, real-time generative AI consultation and Cloud Firestore synchronization require an active network connection.

## 8.3 Future Enhancement Roadmap
1. **Voice Biomarker & Acoustic Prosody Analysis:** Integrating real-time speech pitch, cadence, and pause-duration analysis during voice consultations to detect vocal biomarkers of clinical depression and anxiety.
2. **Wearable Biofeedback Integration (Android Health Connect):** Reading continuous Heart Rate Variability (HRV) and sleep stages from smartwatches to trigger proactive panic-attack breathing interventions.
3. **Institutional University Counseling Bridge:** Allowing students to optionally export their encrypted **ThanatoX** PHQ-9/GAD-7 history and `Rx` session summaries directly to their university's human counseling center.

---
---

# REFERENCES

1. American Psychiatric Association. (2022). *Diagnostic and Statistical Manual of Mental Disorders* (5th ed., text rev.; DSM-5-TR). American Psychiatric Publishing.
2. Beck, A. T. (2011). *Cognitive Behavior Therapy: Basics and Beyond* (2nd ed.). Guilford Press.
3. Cohen, S., Kamarck, T., & Mermelstein, R. (1983). A global measure of perceived stress. *Journal of Health and Social Behavior*, 24(4), 385–396.
4. Costa, P. T., & McCrae, R. R. (1992). Four ways five factors are basic. *Personality and Individual Differences*, 13(6), 653–665.
5. Fitzpatrick, K. K., Darcy, A., & Vierhile, M. (2017). Delivering cognitive behavior therapy to young adults with symptoms of depression and anxiety using a fully automated conversational agent (Woebot): A randomized controlled trial. *JMIR Mental Health*, 4(2), e19.
6. Google Android Developers. (2025). *Guide to App Architecture & Jetpack Compose Material 3*. https://developer.android.com/jetpack/compose
7. Kroenke, K., Spitzer, R. L., & Williams, J. B. (2001). The PHQ-9: Validity of a brief depression severity measure. *Journal of General Internal Medicine*, 16(9), 606–613.
8. Paulhus, D. L., & Williams, K. M. (2002). The Dark Triad of personality: Narcissism, Machiavellianism, and psychopathy. *Journal of Research in Personality*, 36(6), 556–563.
9. Schaufeli, W. B., Martinez, I. M., Pinto, A. M., Salanova, M., & Bakker, A. B. (2002). Burnout and engagement in university students: A cross-national study. *Journal of Cross-Cultural Psychology*, 33(5), 464–481.
10. Spitzer, R. L., Kroenke, K., Williams, J. B., & Löwe, B. (2006). A brief measure for assessing generalized anxiety disorder: The GAD-7. *Archives of Internal Medicine*, 166(10), 1092–1097.

---
---

# APPENDICES

## Appendix A: Deployed Zero-Trust Firestore Security Rules (`firestore.rules`)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    function isSignedIn() {
      return request.auth != null;
    }
    function isOwner(userId) {
      return isSignedIn() && request.auth.uid == userId;
    }
    match /users/{userId} {
      match /consultations/{consultationId} {
        allow read, delete: if isOwner(userId);
        allow create, update: if isOwner(userId) && request.resource.data.userId == userId;
      }
      match /mood_logs/{logId} {
        allow read, delete: if isOwner(userId);
        allow create, update: if isOwner(userId) && request.resource.data.userId == userId;
      }
      match /assessments/{assessmentId} {
        allow read, delete: if isOwner(userId);
        allow create, update: if isOwner(userId) && request.resource.data.userId == userId;
      }
    }
  }
}
```

## Appendix B: Sample `Rx` Therapeutic Prescription Output from 5-Step Psychological Session
```text
====================================================================
Dr. Sifat Khan — Senior AI Consultant Psychiatrist (ThanatoX)
CLINICAL SESSION SUMMARY & Rx THERAPEUTIC GUIDELINE
====================================================================
Patient Complaint : Academic & Semester Burnout with Sleep Onset Anxiety
Session Protocol  : 5-Step Structured Psychiatric Intake Completed

1. CLINICAL FORMULATION:
   Your responses indicate high cognitive hyper-arousal driven by
   all-or-nothing academic perfectionism, leading to sympathetic nervous
   system activation at bedtime and daytime executive fatigue.

2. Rx BEHAVIORAL & COGNITIVE GUIDELINE:
   [Morning]  10 minutes of natural sunlight exposure within 30 mins of waking;
              write down only 3 non-negotiable micro-tasks for the day.
   [Daytime]  Apply 25-minute Pomodoro study blocks followed by 5-minute
              screen-free somatic stretching.
   [Evening]  Perform the ThanatoX 4-7-8 Breathing Protocol (4 cycles) and
              log your cognitive worries in the Mood & Sleep Diary 60 minutes
              before sleep to offload working memory.
====================================================================
```
