const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read or write consultations", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("consultations").get());
});

test("Authenticated user: can create and read their own consultation message", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const docRef = aliceDb.collection("users").doc(ALICE_UID).collection("consultations").doc("msg_1");
  await assertSucceeds(
    docRef.set({
      userId: ALICE_UID,
      role: "user",
      therapyMode: "CBT",
      text: "Feeling overwhelmed with semester exams.",
      moodTag: "Anxious",
      createdAt: new Date(),
    })
  );
  await assertSucceeds(docRef.get());
});

test("Cross-user isolation: Bob cannot read Alice's mood logs or consultations", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).collection("mood_logs").doc("log_1").set({
      userId: ALICE_UID,
      moodScore: 7,
      emotionLabel: "Calm",
      sleepHours: 8,
      stressLevel: 3,
      journalNote: "Studied steadily today.",
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("mood_logs").doc("log_1").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("mood_logs").where("userId", "==", ALICE_UID).get()
  );
});

test("Schema validation: rejects ghost fields and invalid moodScore bounds", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  // Ghost field injection
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("mood_logs").doc("log_bad_1").set({
      userId: ALICE_UID,
      moodScore: 5,
      emotionLabel: "Okay",
      sleepHours: 7,
      stressLevel: 4,
      journalNote: "Normal day",
      createdAt: new Date(),
      isAdmin: true,
    })
  );
  // Out of bounds moodScore (15 > 10)
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("mood_logs").doc("log_bad_2").set({
      userId: ALICE_UID,
      moodScore: 15,
      emotionLabel: "Okay",
      sleepHours: 7,
      stressLevel: 4,
      journalNote: "Normal day",
      createdAt: new Date(),
    })
  );
});
