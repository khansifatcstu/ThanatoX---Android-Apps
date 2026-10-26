package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThanatoXRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun addMoodLog_authenticatedOwner_createsAndObservesMoodLog() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = ThanatoXRepository(firestore)

    val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.addMoodLog(
        moodScore = 8,
        emotionLabel = "Calm",
        sleepHours = 7,
        stressLevel = 3,
        journalNote = "Semester preparation going well",
        aiInsight = "Keep pacing your study blocks."
      )
    }
    assertTrue(createResult.isSuccess)
    val logId = createResult.getOrThrow()
    assertTrue(logId.isNotEmpty())

    val emittedLogs = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeMoodLogs(uid).first { list -> list.any { it.id == logId } }
    }
    assertTrue(emittedLogs.any { it.id == logId && it.moodScore == 8L })
  }

  @Test
  fun addConsultationMessage_authenticatedOwner_createsAndObservesMessage() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = ThanatoXRepository(firestore)

    val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.addConsultationMessage(
        role = "user",
        therapyMode = "SemesterBurnout",
        text = "Feeling stressed about upcoming finals.",
        moodTag = "Stressed"
      )
    }
    assertTrue(createResult.isSuccess)
    val msgId = createResult.getOrThrow()

    val emittedMessages = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeConsultations(uid).first { list -> list.any { it.id == msgId } }
    }
    assertTrue(emittedMessages.any { it.id == msgId && it.therapyMode == "SemesterBurnout" })
  }

  @Test
  fun getMoodLogById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val aliceRepo = ThanatoXRepository(firestore)
    val logId = withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceRepo.addMoodLog(
        moodScore = 6,
        emotionLabel = "Focused",
        sleepHours = 6,
        stressLevel = 5,
        journalNote = "Private reflection"
      ).getOrThrow()
    }

    signInTestUser(BOB_EMAIL)
    val bobRepo = ThanatoXRepository(firestore)
    val result = withTimeout(DEFAULT_TIMEOUT_MS) {
      bobRepo.getMoodLogById(aliceUid, logId)
    }
    assertTrue(result.isFailure)
    val firestoreException = result.exceptionOrNull()?.unwrapFirestoreException()
    assertNotNull(firestoreException)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
  }

  @Test
  fun observeMoodLogs_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
    auth.signOut()
    val repository = ThanatoXRepository(firestore)

    var caughtException: Throwable? = null
    try {
      withTimeout(FLOW_TIMEOUT_MS) {
        repository.observeMoodLogs("some_user_123").first()
      }
    } catch (e: Throwable) {
      caughtException = e
    }
    assertNotNull(caughtException)
    val firestoreException = caughtException?.unwrapFirestoreException()
    assertNotNull("Expected nested FirebaseFirestoreException, got $caughtException", firestoreException)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
  }

  private companion object {
    const val ALICE_EMAIL = "alice@thanatox.test"
    const val BOB_EMAIL = "bob@thanatox.test"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3500L
  }
}
