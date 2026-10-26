package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.ConsultationMessage
import com.example.data.model.MoodLog
import com.example.data.model.PsychAssessment
import com.example.data.model.UserProfile
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ThanatoXRepository(private val db: FirebaseFirestore) {

  // Convenience constructor: always resolves the named database ID from firebase_applet_config.xml
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth
    get() = Firebase.auth

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
  }

  fun observeUserProfile(userId: String): Flow<UserProfile?> {
    val path = "users/$userId"
    return db.collection("users").document(userId)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        } else {
          null
        }
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
        throw error
      }
  }

  suspend fun ensureUserProfile(
    displayName: String,
    semesterFocus: String = "For Semester Mental Wellness",
    wellnessScore: Int = 85
  ): Result<Unit> {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
    return try {
      val snapshot = docRef.get().await()
      val safeName = displayName.trim().ifEmpty { "ThanatoX Member" }.take(100)
      val safeFocus = semesterFocus.trim().ifEmpty { "For Semester Mental Wellness" }.take(200)
      val clampedScore = wellnessScore.coerceIn(0, 100)

      if (!snapshot.exists()) {
        val createPayload = mapOf(
          "userId" to uid,
          "displayName" to safeName,
          "databaseAlias" to "For Semester",
          "semesterFocus" to safeFocus,
          "wellnessScore" to clampedScore,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(createPayload).await()
      } else {
        val updatePayload = mapOf(
          "displayName" to safeName,
          "databaseAlias" to "For Semester",
          "semesterFocus" to safeFocus,
          "wellnessScore" to clampedScore,
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(updatePayload, SetOptions.merge()).await()
      }
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      Result.failure(e)
    }
  }

  fun observeConsultations(userId: String = auth.currentUser?.uid.orEmpty()): Flow<List<ConsultationMessage>> {
    val targetUid = userId.ifBlank { "unauthenticated_uid" }
    val path = "users/$targetUid/consultations"
    return db.collection("users").document(targetUid).collection("consultations")
      .whereEqualTo("userId", targetUid)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          ConsultationMessage::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).sortedBy { it.createdAt ?: Timestamp(0, 0) }
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  suspend fun addConsultationMessage(
    role: String,
    therapyMode: String,
    text: String,
    moodTag: String = "Calm"
  ): Result<String> {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("consultations")
    val safeRole = if (role == "psychiatrist") "psychiatrist" else "user"
    val safeMode = if (therapyMode in setOf("CBT", "Crisis", "SemesterBurnout", "SleepAnxiety")) {
      therapyMode
    } else {
      "CBT"
    }
    val safeText = text.trim().ifEmpty { "..." }.take(5000)
    val safeMood = moodTag.trim().take(80)

    val payload = mapOf(
      "userId" to uid,
      "role" to safeRole,
      "therapyMode" to safeMode,
      "text" to safeText,
      "moodTag" to safeMood,
      "createdAt" to FieldValue.serverTimestamp()
    )

    return try {
      val docRef = collectionRef.add(payload).await()
      Result.success(docRef.id)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, collectionRef.path)
      Result.failure(e)
    }
  }

  suspend fun deleteConsultationMessage(messageId: String): Result<Unit> {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("consultations").document(messageId)
    return try {
      docRef.delete().await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      Result.failure(e)
    }
  }

  fun observeMoodLogs(userId: String = auth.currentUser?.uid.orEmpty()): Flow<List<MoodLog>> {
    val targetUid = userId.ifBlank { "unauthenticated_uid" }
    val path = "users/$targetUid/mood_logs"
    return db.collection("users").document(targetUid).collection("mood_logs")
      .whereEqualTo("userId", targetUid)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          MoodLog::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  suspend fun addMoodLog(
    moodScore: Int,
    emotionLabel: String,
    sleepHours: Int,
    stressLevel: Int,
    journalNote: String,
    aiInsight: String = ""
  ): Result<String> {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("mood_logs")
    val payload = mapOf(
      "userId" to uid,
      "moodScore" to moodScore.coerceIn(1, 10),
      "emotionLabel" to emotionLabel.trim().ifEmpty { "Reflective" }.take(60),
      "sleepHours" to sleepHours.coerceIn(0, 24),
      "stressLevel" to stressLevel.coerceIn(1, 10),
      "journalNote" to journalNote.trim().ifEmpty { "Daily mood check-in." }.take(2000),
      "aiInsight" to aiInsight.trim().take(2000),
      "createdAt" to FieldValue.serverTimestamp()
    )

    return try {
      val docRef = collectionRef.add(payload).await()
      Result.success(docRef.id)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, collectionRef.path)
      Result.failure(e)
    }
  }

  suspend fun getMoodLogById(targetUserId: String, logId: String): Result<MoodLog?> {
    val docRef = db.collection("users").document(targetUserId).collection("mood_logs").document(logId)
    return try {
      val snapshot = docRef.get().await()
      Result.success(
        snapshot.toObject(MoodLog::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
      )
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.GET, docRef.path)
      Result.failure(e)
    }
  }

  suspend fun deleteMoodLog(logId: String): Result<Unit> {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("mood_logs").document(logId)
    return try {
      docRef.delete().await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      Result.failure(e)
    }
  }

  fun observeAssessments(userId: String = auth.currentUser?.uid.orEmpty()): Flow<List<PsychAssessment>> {
    val targetUid = userId.ifBlank { "unauthenticated_uid" }
    val path = "users/$targetUid/assessments"
    return db.collection("users").document(targetUid).collection("assessments")
      .whereEqualTo("userId", targetUid)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          PsychAssessment::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).map { item ->
          val rawSeverity = item.severityLabel
          if (rawSeverity.startsWith("[TYPE:") && rawSeverity.contains("]")) {
            val endBracket = rawSeverity.indexOf("]")
            val extractedType = rawSeverity.substring(6, endBracket).trim()
            val cleanSeverity = rawSeverity.substring(endBracket + 1).trim()
            item.copy(
              assessmentType = extractedType.ifEmpty { item.assessmentType },
              severityLabel = cleanSeverity.ifEmpty { rawSeverity }
            )
          } else {
            item
          }
        }.sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  suspend fun addAssessment(
    assessmentType: String,
    totalScore: Int,
    severityLabel: String,
    recommendation: String
  ): Result<String> {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("assessments")
    val cleanType = assessmentType.trim().ifEmpty { "Semester-Burnout" }
    val isAllowedFirestoreType = cleanType in setOf("PHQ-9", "GAD-7", "Semester-Burnout")
    val safeType = if (isAllowedFirestoreType) cleanType else "Semester-Burnout"
    val cleanSeverity = severityLabel.trim().ifEmpty { "Mild" }
    val encodedSeverity = if (isAllowedFirestoreType) {
      cleanSeverity.take(100)
    } else {
      "[TYPE:$cleanType] $cleanSeverity".take(100)
    }
    val payload = mapOf(
      "userId" to uid,
      "assessmentType" to safeType,
      "totalScore" to totalScore.coerceIn(0, 100),
      "severityLabel" to encodedSeverity,
      "recommendation" to recommendation.trim().ifEmpty { "Practice daily breathing and structured rest." }.take(2000),
      "createdAt" to FieldValue.serverTimestamp()
    )

    return try {
      val docRef = collectionRef.add(payload).await()
      Result.success(docRef.id)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, collectionRef.path)
      Result.failure(e)
    }
  }

  suspend fun deleteAssessment(assessmentId: String): Result<Unit> {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("assessments").document(assessmentId)
    return try {
      docRef.delete().await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      Result.failure(e)
    }
  }

  suspend fun resetAllUserData(): Result<Unit> {
    val uid = requireUserId()
    val userDoc = db.collection("users").document(uid)
    return try {
      val consultationsSnap = userDoc.collection("consultations")
        .whereEqualTo("userId", uid)
        .get()
        .await()
      for (doc in consultationsSnap.documents) {
        doc.reference.delete().await()
      }

      val moodLogsSnap = userDoc.collection("mood_logs")
        .whereEqualTo("userId", uid)
        .get()
        .await()
      for (doc in moodLogsSnap.documents) {
        doc.reference.delete().await()
      }

      val assessmentsSnap = userDoc.collection("assessments")
        .whereEqualTo("userId", uid)
        .get()
        .await()
      for (doc in assessmentsSnap.documents) {
        doc.reference.delete().await()
      }

      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, userDoc.path)
      Result.failure(e)
    }
  }
}
