package com.example.data.remote

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.BuildConfig
import com.example.data.model.ConsultationMessage
import com.example.data.model.TherapyMode
import com.example.ui.i18n.AppLanguage
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

@Serializable
data class GenerateContentRequest(
  val contents: List<GeminiContent>,
  val generationConfig: GenerationConfig? = null,
  val systemInstruction: GeminiContent? = null
)

@Serializable
data class GeminiContent(
  val role: String? = null,
  val parts: List<GeminiPart>
)

@Serializable
data class GeminiInlineData(
  val mimeType: String,
  val data: String
)

@Serializable
data class GeminiPart(
  val text: String? = null,
  val inlineData: GeminiInlineData? = null
)

data class ChatAttachmentPayload(
  val fileName: String,
  val mimeType: String,
  val base64Data: String,
  val isImage: Boolean,
  val extractedTextPreview: String? = null
)

@Serializable
data class GenerationConfig(
  val temperature: Float? = 0.7f,
  val topP: Float? = 0.95f,
  val topK: Int? = 40
)

@Serializable
data class GenerateContentResponse(
  val candidates: List<GeminiCandidate>? = null
)

@Serializable
data class GeminiCandidate(
  val content: GeminiContent? = null
)

interface GeminiApiService {
  @POST("v1beta/models/{model}:generateContent")
  suspend fun generateContent(
    @Path("model") model: String,
    @Query("key") apiKey: String,
    @Body request: GenerateContentRequest
  ): GenerateContentResponse

  @POST("v1beta/models/{model}:streamGenerateContent?alt=sse")
  @Streaming
  suspend fun generateContentStream(
    @Path("model") model: String,
    @Query("key") apiKey: String,
    @Body request: GenerateContentRequest
  ): ResponseBody
}

enum class AiProvider(
  val id: String,
  val displayNameBn: String,
  val displayNameEn: String,
  val shortBadge: String,
  val defaultEndpoint: String,
  val candidateModels: List<String>
) {
  GEMINI(
    id = "gemini",
    displayNameBn = "Google Gemini",
    displayNameEn = "Google Gemini",
    shortBadge = "Gemini",
    defaultEndpoint = "https://generativelanguage.googleapis.com/",
    candidateModels = listOf(
      "gemini-2.5-flash",
      "gemini-3-flash-preview",
      "gemini-flash-latest",
      "gemini-3.5-flash"
    )
  ),
  META(
    id = "meta",
    displayNameBn = "Meta (Llama AI)",
    displayNameEn = "Meta (Llama AI)",
    shortBadge = "Meta",
    defaultEndpoint = "https://api.groq.com/openai/v1/chat/completions",
    candidateModels = listOf(
      "llama-3.3-70b-versatile",
      "llama-3.1-8b-instant",
      "meta-llama/Llama-3.3-70B-Instruct-Turbo",
      "Llama-3.3-70B-Instruct"
    )
  ),
  GROK(
    id = "grok",
    displayNameBn = "Grok (xAI)",
    displayNameEn = "Grok (xAI)",
    shortBadge = "Grok",
    defaultEndpoint = "https://api.x.ai/v1/chat/completions",
    candidateModels = listOf(
      "grok-3-mini",
      "grok-3",
      "grok-2-latest",
      "grok-beta"
    )
  ),
  CLAUDE(
    id = "claude",
    displayNameBn = "Claude (Anthropic)",
    displayNameEn = "Claude (Anthropic)",
    shortBadge = "Claude",
    defaultEndpoint = "https://api.anthropic.com/v1/messages",
    candidateModels = listOf(
      "claude-3-7-sonnet-latest",
      "claude-3-5-sonnet-latest",
      "claude-3-5-haiku-latest",
      "claude-3-haiku-20240307"
    )
  ),
  CHATGPT(
    id = "chatgpt",
    displayNameBn = "ChatGPT (OpenAI)",
    displayNameEn = "ChatGPT (OpenAI)",
    shortBadge = "GPT / OpenAI",
    defaultEndpoint = "https://api.openai.com/v1/chat/completions",
    candidateModels = listOf(
      "gpt-4o-mini",
      "gpt-4o",
      "gpt-4.1-mini",
      "gpt-3.5-turbo"
    )
  ),
  DEEPSEEK(
    id = "deepseek",
    displayNameBn = "DeepSeek AI",
    displayNameEn = "DeepSeek AI",
    shortBadge = "DeepSeek",
    defaultEndpoint = "https://api.deepseek.com/chat/completions",
    candidateModels = listOf(
      "deepseek-chat",
      "deepseek-reasoner"
    )
  ),
  OPENROUTER(
    id = "openrouter",
    displayNameBn = "Meta / OpenRouter AI",
    displayNameEn = "Meta / OpenRouter AI",
    shortBadge = "Meta / OpenRouter",
    defaultEndpoint = "https://openrouter.ai/api/v1/chat/completions",
    candidateModels = listOf(
      "meta-llama/llama-3.3-70b-instruct:free",
      "meta-llama/llama-3.3-70b-instruct",
      "openai/gpt-4o-mini",
      "google/gemini-2.5-flash"
    )
  );

  fun displayName(language: AppLanguage): String =
    if (language == AppLanguage.BN) displayNameBn else displayNameEn

  companion object {
    fun fromId(id: String?): AiProvider? = entries.firstOrNull { it.id == id }
  }
}

data class ApiConnectionStatus(
  val isLiveConnected: Boolean = true,
  val lastLatencyMs: Long? = null,
  val activeCustomProvider: AiProvider? = null
)

object GeminiPsychiatristService {
  private const val BASE_URL = "https://generativelanguage.googleapis.com/"
  private const val PREFS_NAME = "thanatox_ai_prefs"
  private const val KEY_CUSTOM_API = "custom_gemini_api_key"
  private const val KEY_CUSTOM_PROVIDER = "custom_ai_provider_id"
  private val CANDIDATE_MODELS = listOf(
    "gemini-2.5-flash",
    "gemini-3-flash-preview",
    "gemini-flash-latest",
    "gemini-3.5-flash"
  )

  @Volatile
  private var runtimeCustomKey: String = ""

  @Volatile
  private var runtimeCustomProvider: AiProvider? = null

  @Volatile
  private var firebaseAiSdkUnavailable: Boolean = false

  @Volatile
  var lastLatencyMs: Long = 320L
    private set

  private val _connectionStatus = MutableStateFlow(
    ApiConnectionStatus(
      isLiveConnected = true,
      lastLatencyMs = null,
      activeCustomProvider = null
    )
  )
  val connectionStatus: StateFlow<ApiConnectionStatus> = _connectionStatus.asStateFlow()

  /**
   * Automatically detects which AI provider a given API key belongs to based on its prefix/pattern.
   * Returns null if the input key is blank (meaning the default built-in Gemini + Firebase AI is used).
   */
  fun detectProviderFromKey(rawKey: String): AiProvider? {
    val clean = rawKey.trim()
    if (clean.isEmpty()) return null

    return when {
      clean.startsWith("AIza", ignoreCase = true) -> AiProvider.GEMINI
      clean.startsWith("sk-ant-", ignoreCase = true) ||
        clean.startsWith("ant-", ignoreCase = true) ||
        clean.contains("claude", ignoreCase = true) -> AiProvider.CLAUDE
      clean.startsWith("xai-", ignoreCase = true) ||
        clean.startsWith("grok-", ignoreCase = true) ||
        clean.contains("grok", ignoreCase = true) -> AiProvider.GROK
      clean.startsWith("gsk_", ignoreCase = true) ||
        clean.startsWith("LLM|", ignoreCase = true) ||
        clean.startsWith("meta-", ignoreCase = true) ||
        clean.startsWith("llama-", ignoreCase = true) ||
        clean.startsWith("tgp_", ignoreCase = true) ||
        clean.startsWith("fw_", ignoreCase = true) ||
        clean.contains("llama", ignoreCase = true) ||
        clean.contains("meta", ignoreCase = true) -> AiProvider.META
      clean.startsWith("sk-or-", ignoreCase = true) -> AiProvider.OPENROUTER
      clean.startsWith("ds-", ignoreCase = true) ||
        clean.contains("deepseek", ignoreCase = true) -> AiProvider.DEEPSEEK
      clean.startsWith("sk-proj-", ignoreCase = true) ||
        clean.startsWith("sk-svcacct-", ignoreCase = true) ||
        clean.startsWith("sk-", ignoreCase = true) -> AiProvider.CHATGPT
      clean.matches(Regex("^[a-fA-F0-9]{64}$")) -> AiProvider.META
      else -> AiProvider.META
    }
  }

  fun getActiveCustomProvider(): AiProvider? {
    if (runtimeCustomKey.isBlank()) return null
    return runtimeCustomProvider ?: detectProviderFromKey(runtimeCustomKey)
  }

  fun loadSavedCustomKey(context: Context): String {
    val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    runtimeCustomKey = prefs.getString(KEY_CUSTOM_API, "")?.trim().orEmpty()
    val savedProviderId = prefs.getString(KEY_CUSTOM_PROVIDER, null)
    runtimeCustomProvider = if (runtimeCustomKey.isNotBlank()) {
      AiProvider.fromId(savedProviderId) ?: detectProviderFromKey(runtimeCustomKey)
    } else {
      null
    }
    _connectionStatus.value = _connectionStatus.value.copy(
      isLiveConnected = hasConfiguredKey(),
      activeCustomProvider = runtimeCustomProvider
    )
    return runtimeCustomKey
  }

  fun saveCustomKey(context: Context, key: String, confirmedProvider: AiProvider? = null) {
    val clean = key.trim()
    runtimeCustomKey = clean
    val detected = if (clean.isNotBlank()) {
      confirmedProvider ?: detectProviderFromKey(clean)
    } else {
      null
    }
    runtimeCustomProvider = detected
    context.applicationContext
      .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      .edit()
      .putString(KEY_CUSTOM_API, clean)
      .putString(KEY_CUSTOM_PROVIDER, detected?.id)
      .apply()
    _connectionStatus.value = _connectionStatus.value.copy(
      isLiveConnected = hasConfiguredKey(),
      activeCustomProvider = detected
    )
  }

  fun resolveCustomOrBuildApiKey(): String {
    if (runtimeCustomKey.isNotBlank()) return runtimeCustomKey
    val buildKey = BuildConfig.GEMINI_API_KEY.trim()
    if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY" && buildKey != "PLACEHOLDER") {
      return buildKey
    }
    return ""
  }

  fun resolveDefaultGeminiKey(): String {
    val buildKey = BuildConfig.GEMINI_API_KEY.trim()
    if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY" && buildKey != "PLACEHOLDER") {
      return buildKey
    }
    return try {
      FirebaseApp.getInstance().options.apiKey.orEmpty().trim()
    } catch (_: Exception) {
      ""
    }
  }

  fun resolveActiveApiKey(): String {
    val directKey = resolveCustomOrBuildApiKey()
    if (directKey.isNotBlank()) return directKey
    return resolveDefaultGeminiKey()
  }

  private fun isFirebaseAvailable(): Boolean {
    if (firebaseAiSdkUnavailable) return false
    return try {
      val app = FirebaseApp.getInstance()
      FirebaseAuth.getInstance(app).currentUser != null
    } catch (_: Exception) {
      false
    }
  }

  fun hasConfiguredKey(): Boolean = resolveActiveApiKey().isNotBlank() || isFirebaseAvailable()

  fun hasConfiguredLiveApiKey(): Boolean = hasConfiguredKey()

  private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .build()
  }

  private val json: Json by lazy {
    Json {
      ignoreUnknownKeys = true
      encodeDefaults = false
    }
  }

  private val apiService: GeminiApiService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()
      .create(GeminiApiService::class.java)
  }

  private data class EndpointTarget(
    val provider: AiProvider,
    val url: String,
    val models: List<String>
  )

  private fun buildCandidateEndpointsForKey(apiKey: String, primaryProvider: AiProvider): List<EndpointTarget> {
    val clean = apiKey.trim()
    val targets = mutableListOf<EndpointTarget>()

    when (primaryProvider) {
      AiProvider.META -> {
        if (clean.startsWith("gsk_", ignoreCase = true)) {
          targets.add(
            EndpointTarget(
              AiProvider.META,
              "https://api.groq.com/openai/v1/chat/completions",
              listOf("llama-3.3-70b-versatile", "llama-3.1-8b-instant")
            )
          )
        } else if (clean.startsWith("LLM|", ignoreCase = true) || clean.startsWith("meta-", ignoreCase = true)) {
          targets.add(
            EndpointTarget(
              AiProvider.META,
              "https://api.llama.com/compat/v1/chat/completions",
              listOf("Llama-3.3-70B-Instruct", "Llama-3.1-8B-Instruct")
            )
          )
        } else if (clean.startsWith("fw_", ignoreCase = true)) {
          targets.add(
            EndpointTarget(
              AiProvider.META,
              "https://api.fireworks.ai/inference/v1/chat/completions",
              listOf("accounts/fireworks/models/llama-v3p3-70b-instruct")
            )
          )
        }
        // Always include Groq, Together AI, and Meta Llama Official API as fallbacks for Meta keys
        targets.add(
          EndpointTarget(
            AiProvider.META,
            "https://api.groq.com/openai/v1/chat/completions",
            listOf("llama-3.3-70b-versatile", "llama-3.1-8b-instant")
          )
        )
        targets.add(
          EndpointTarget(
            AiProvider.META,
            "https://api.together.xyz/v1/chat/completions",
            listOf("meta-llama/Llama-3.3-70B-Instruct-Turbo", "meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo")
          )
        )
        targets.add(
          EndpointTarget(
            AiProvider.META,
            "https://api.llama.com/compat/v1/chat/completions",
            listOf("Llama-3.3-70B-Instruct")
          )
        )
      }
      AiProvider.GROK -> {
        targets.add(
          EndpointTarget(
            AiProvider.GROK,
             AiProvider.GROK.defaultEndpoint,
            AiProvider.GROK.candidateModels
          )
        )
      }
      AiProvider.CHATGPT -> {
        targets.add(
          EndpointTarget(
            AiProvider.CHATGPT,
            AiProvider.CHATGPT.defaultEndpoint,
            AiProvider.CHATGPT.candidateModels
          )
        )
        // A generic "sk-..." key might also be DeepSeek, OpenRouter, or Meta/Together
        targets.add(
          EndpointTarget(
            AiProvider.DEEPSEEK,
            AiProvider.DEEPSEEK.defaultEndpoint,
            AiProvider.DEEPSEEK.candidateModels
          )
        )
        targets.add(
          EndpointTarget(
            AiProvider.OPENROUTER,
            AiProvider.OPENROUTER.defaultEndpoint,
            AiProvider.OPENROUTER.candidateModels
          )
        )
      }
      AiProvider.DEEPSEEK -> {
        targets.add(
          EndpointTarget(
            AiProvider.DEEPSEEK,
            AiProvider.DEEPSEEK.defaultEndpoint,
            AiProvider.DEEPSEEK.candidateModels
          )
        )
      }
      AiProvider.OPENROUTER -> {
        targets.add(
          EndpointTarget(
            AiProvider.OPENROUTER,
            AiProvider.OPENROUTER.defaultEndpoint,
            AiProvider.OPENROUTER.candidateModels
          )
        )
      }
      else -> {}
    }

    // If the key is an unknown token format, also append other providers for auto-discovery
    if (!clean.startsWith("AIza") && !clean.startsWith("sk-ant-")) {
      targets.add(EndpointTarget(AiProvider.GROK, AiProvider.GROK.defaultEndpoint, AiProvider.GROK.candidateModels))
      targets.add(EndpointTarget(AiProvider.CHATGPT, AiProvider.CHATGPT.defaultEndpoint, AiProvider.CHATGPT.candidateModels))
      targets.add(EndpointTarget(AiProvider.DEEPSEEK, AiProvider.DEEPSEEK.defaultEndpoint, AiProvider.DEEPSEEK.candidateModels))
      targets.add(EndpointTarget(AiProvider.OPENROUTER, AiProvider.OPENROUTER.defaultEndpoint, AiProvider.OPENROUTER.candidateModels))
    }

    return targets.distinctBy { "${it.provider.id}:${it.url}" }
  }

  private fun executeOpenAiCompatibleCall(
    endpointUrl: String,
    apiKey: String,
    modelName: String,
    sysText: String,
    recentHistory: List<ConsultationMessage>,
    userPrompt: String,
    attachment: ChatAttachmentPayload?
  ): String? {
    val messagesArray = buildJsonArray {
      if (sysText.isNotBlank()) {
        add(
          buildJsonObject {
            put("role", JsonPrimitive("system"))
            put("content", JsonPrimitive(sysText))
          }
        )
      }
      recentHistory
        .filter { it.text.isNotBlank() }
        .takeLast(10)
        .forEach { msg ->
          add(
            buildJsonObject {
              put("role", JsonPrimitive(if (msg.role == "psychiatrist") "assistant" else "user"))
              put("content", JsonPrimitive(msg.text))
            }
          )
        }

      if (attachment != null && attachment.isImage && attachment.base64Data.isNotBlank()) {
        add(
          buildJsonObject {
            put("role", JsonPrimitive("user"))
            put(
              "content",
              buildJsonArray {
                add(
                  buildJsonObject {
                    put("type", JsonPrimitive("text"))
                    put("text", JsonPrimitive(userPrompt))
                  }
                )
                add(
                  buildJsonObject {
                    put("type", JsonPrimitive("image_url"))
                    put(
                      "image_url",
                      buildJsonObject {
                        put(
                          "url",
                          JsonPrimitive("data:${attachment.mimeType};base64,${attachment.base64Data}")
                        )
                      }
                    )
                  }
                )
              }
            )
          }
        )
      } else {
        add(
          buildJsonObject {
            put("role", JsonPrimitive("user"))
            put("content", JsonPrimitive(userPrompt))
          }
        )
      }
    }

    val payload = buildJsonObject {
      put("model", JsonPrimitive(modelName))
      put("messages", messagesArray)
      put("temperature", JsonPrimitive(0.7f))
    }

    val requestBuilder = Request.Builder()
      .url(endpointUrl)
      .addHeader("Authorization", "Bearer ${apiKey.trim()}")
      .addHeader("Content-Type", "application/json")
      .post(payload.toString().toRequestBody("application/json".toMediaType()))

    if (endpointUrl.contains("openrouter.ai")) {
      requestBuilder.addHeader("HTTP-Referer", "https://thanatox.ai")
      requestBuilder.addHeader("X-Title", "ThanatoX AI")
    }

    okHttpClient.newCall(requestBuilder.build()).execute().use { response ->
      if (!response.isSuccessful) return null
      val bodyStr = response.body?.string()?.trim().orEmpty()
      if (bodyStr.isEmpty()) return null
      val root = json.parseToJsonElement(bodyStr).jsonObject
      val reply = root["choices"]?.jsonArray
        ?.getOrNull(0)?.jsonObject
        ?.get("message")?.jsonObject
        ?.get("content")?.jsonPrimitive?.content
        ?.trim()
      return reply?.takeIf { it.isNotBlank() }
    }
  }

  private fun executeClaudeCall(
    apiKey: String,
    modelName: String,
    sysText: String,
    recentHistory: List<ConsultationMessage>,
    userPrompt: String,
    attachment: ChatAttachmentPayload?
  ): String? {
    val messagesArray = buildJsonArray {
      recentHistory
        .filter { it.text.isNotBlank() }
        .takeLast(10)
        .forEach { msg ->
          add(
            buildJsonObject {
              put("role", JsonPrimitive(if (msg.role == "psychiatrist") "assistant" else "user"))
              put("content", JsonPrimitive(msg.text))
            }
          )
        }

      if (attachment != null && attachment.isImage && attachment.base64Data.isNotBlank()) {
        add(
          buildJsonObject {
            put("role", JsonPrimitive("user"))
            put(
              "content",
              buildJsonArray {
                add(
                  buildJsonObject {
                    put("type", JsonPrimitive("image"))
                    put(
                      "source",
                      buildJsonObject {
                        put("type", JsonPrimitive("base64"))
                        put("media_type", JsonPrimitive(attachment.mimeType))
                        put("data", JsonPrimitive(attachment.base64Data))
                      }
                    )
                  }
                )
                add(
                  buildJsonObject {
                    put("type", JsonPrimitive("text"))
                    put("text", JsonPrimitive(userPrompt))
                  }
                )
              }
            )
          }
        )
      } else {
        add(
          buildJsonObject {
            put("role", JsonPrimitive("user"))
            put("content", JsonPrimitive(userPrompt))
          }
        )
      }
    }

    val payload = buildJsonObject {
      put("model", JsonPrimitive(modelName))
      put("max_tokens", JsonPrimitive(1024))
      if (sysText.isNotBlank()) {
        put("system", JsonPrimitive(sysText))
      }
      put("messages", messagesArray)
    }

    val request = Request.Builder()
      .url(AiProvider.CLAUDE.defaultEndpoint)
      .addHeader("x-api-key", apiKey.trim())
      .addHeader("anthropic-version", "2023-06-01")
      .addHeader("Content-Type", "application/json")
      .post(payload.toString().toRequestBody("application/json".toMediaType()))
      .build()

    okHttpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) return null
      val bodyStr = response.body?.string()?.trim().orEmpty()
      if (bodyStr.isEmpty()) return null
      val root = json.parseToJsonElement(bodyStr).jsonObject
      val reply = root["content"]?.jsonArray
        ?.getOrNull(0)?.jsonObject
        ?.get("text")?.jsonPrimitive?.content
        ?.trim()
      return reply?.takeIf { it.isNotBlank() }
    }
  }

  private suspend fun tryCustomMultiProviderReply(
    apiKey: String,
    provider: AiProvider,
    geminiRequest: GenerateContentRequest,
    sysText: String,
    recentHistory: List<ConsultationMessage>,
    promptWithDocContext: String,
    attachment: ChatAttachmentPayload?,
    startTime: Long,
    onPartialUpdate: (String) -> Unit
  ): Pair<String, AiProvider>? {
    if (apiKey.isBlank()) return null

    // 1. If Gemini key, try Gemini REST first
    if (provider == AiProvider.GEMINI) {
      val geminiRes = tryDirectRestReply(apiKey, geminiRequest, startTime, onPartialUpdate)
      if (!geminiRes.isNullOrBlank()) {
        return geminiRes to AiProvider.GEMINI
      }
    }

    // 2. If Claude key, try Anthropic API
    if (provider == AiProvider.CLAUDE) {
      for (modelName in AiProvider.CLAUDE.candidateModels) {
        try {
          val reply = executeClaudeCall(
            apiKey = apiKey,
            modelName = modelName,
            sysText = sysText,
            recentHistory = recentHistory,
            userPrompt = promptWithDocContext,
            attachment = attachment
          )
          if (!reply.isNullOrBlank()) {
            val cleaned = cleanMarkdownFormatting(reply)
            onPartialUpdate(cleaned)
            lastLatencyMs = (System.currentTimeMillis() - startTime).coerceAtLeast(100L)
            runtimeCustomProvider = AiProvider.CLAUDE
            _connectionStatus.value = ApiConnectionStatus(
              isLiveConnected = true,
              lastLatencyMs = lastLatencyMs,
              activeCustomProvider = AiProvider.CLAUDE
            )
            return cleaned to AiProvider.CLAUDE
          }
        } catch (_: Exception) {
        }
      }
    }

    // 3. Try OpenAI-compatible providers (Meta Llama, Grok, ChatGPT, DeepSeek, OpenRouter)
    val endpoints = buildCandidateEndpointsForKey(apiKey, provider)
    for (target in endpoints) {
      for (modelName in target.models) {
        try {
          val reply = executeOpenAiCompatibleCall(
            endpointUrl = target.url,
            apiKey = apiKey,
            modelName = modelName,
            sysText = sysText,
            recentHistory = recentHistory,
            userPrompt = promptWithDocContext,
            attachment = attachment
          )
          if (!reply.isNullOrBlank()) {
            val cleaned = cleanMarkdownFormatting(reply)
            onPartialUpdate(cleaned)
            lastLatencyMs = (System.currentTimeMillis() - startTime).coerceAtLeast(100L)
            runtimeCustomProvider = target.provider
            _connectionStatus.value = ApiConnectionStatus(
              isLiveConnected = true,
              lastLatencyMs = lastLatencyMs,
              activeCustomProvider = target.provider
            )
            return cleaned to target.provider
          }
        } catch (_: Exception) {
        }
      }
    }

    return null
  }

  suspend fun testApiKeyConnection(candidateKey: String, context: Context? = null): Result<Long> =
    withContext(Dispatchers.IO) {
      val explicitKey = candidateKey.trim().ifEmpty { runtimeCustomKey }
      val geminiReq = GenerateContentRequest(
        contents = listOf(GeminiContent(role = "user", parts = listOf(GeminiPart(text = "Hello"))))
      )

      // 1. If a custom key is provided (Gemini, Meta, Grok, Claude, ChatGPT, DeepSeek, etc.), auto-detect and test it!
      if (explicitKey.isNotBlank()) {
        val initialProvider = detectProviderFromKey(explicitKey) ?: AiProvider.GEMINI
        val start = System.currentTimeMillis()
        val customProbe = tryCustomMultiProviderReply(
          apiKey = explicitKey,
          provider = initialProvider,
          geminiRequest = geminiReq,
          sysText = "Reply briefly with OK.",
          recentHistory = emptyList(),
          promptWithDocContext = "Hello",
          attachment = null,
          startTime = start,
          onPartialUpdate = {}
        )
        if (customProbe != null) {
          val (_, confirmedProvider) = customProbe
          val elapsed = (System.currentTimeMillis() - start).coerceAtLeast(80L)
          lastLatencyMs = elapsed
          runtimeCustomProvider = confirmedProvider
          if (context != null) {
            saveCustomKey(context, explicitKey, confirmedProvider)
          } else {
            _connectionStatus.value = ApiConnectionStatus(
              isLiveConnected = true,
              lastLatencyMs = elapsed,
              activeCustomProvider = confirmedProvider
            )
          }
          return@withContext Result.success(elapsed)
        }

        // If user explicitly typed a custom key to test and it failed all providers, report error
        if (candidateKey.isNotBlank()) {
          return@withContext Result.failure(IllegalStateException("API connection failed"))
        }
      }

      // 2. Test Default Built-in / Firebase options Gemini API key on REST endpoint first
      val fallbackKey = resolveDefaultGeminiKey()
      if (fallbackKey.isNotBlank()) {
        for (modelName in CANDIDATE_MODELS) {
          try {
            val elapsed = measureTimeMillis {
              val res = apiService.generateContent(modelName, fallbackKey, geminiReq)
              val txt = res.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
              if (txt.isNullOrBlank()) throw IllegalStateException("Empty API response")
            }
            lastLatencyMs = elapsed
            _connectionStatus.value = ApiConnectionStatus(
              isLiveConnected = true,
              lastLatencyMs = elapsed,
              activeCustomProvider = getActiveCustomProvider()
            )
            return@withContext Result.success(elapsed)
          } catch (_: Exception) {
          }
        }
      }

      // 3. Fallback test using Firebase AI SDK connection (only when signed in and available)
      if (isFirebaseAvailable()) {
        val modelName = CANDIDATE_MODELS.first()
        try {
          val elapsed = measureTimeMillis {
            val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
              modelName = modelName
            )
            val res = model.generateContent("Hello")
            if (res.text.isNullOrBlank()) throw IllegalStateException("Empty Firebase AI response")
          }
          lastLatencyMs = elapsed
          _connectionStatus.value = ApiConnectionStatus(
            isLiveConnected = true,
            lastLatencyMs = elapsed,
            activeCustomProvider = getActiveCustomProvider()
          )
          return@withContext Result.success(elapsed)
        } catch (_: Exception) {
          firebaseAiSdkUnavailable = true
        }
      }

      Result.failure(IllegalStateException("API connection failed"))
    }

  private enum class IdentityQueryType {
    NONE,
    SELF_IDENTITY,
    FOUNDER_IDENTITY
  }

  private fun detectIdentityQueryType(prompt: String): IdentityQueryType {
    val norm = prompt.lowercase().trim()
    val asksAboutFounderOrSifat = norm.contains("সিফাত খান কে") ||
      norm.contains("ডক্টর সিফাত") ||
      norm.contains("ড. সিফাত খান কে") ||
      norm.contains("ডা. সিফাত খান কে") ||
      norm.contains("সিফাত কে") ||
      norm.contains("ফাউন্ডার কে") ||
      norm.contains("প্রতিষ্ঠাতা কে") ||
      norm.contains("কে তৈরি করেছে") ||
      norm.contains("কে বানিয়েছে") ||
      norm.contains("কে বানিয়েছে") ||
      norm.contains("who is dr. sifat") ||
      norm.contains("who is dr sifat") ||
      norm.contains("who is sifat khan") ||
      norm.contains("who is your founder") ||
      norm.contains("who created you") ||
      norm.contains("who founded you")

    if (asksAboutFounderOrSifat) {
      return IdentityQueryType.FOUNDER_IDENTITY
    }

    val asksWhoAreYou = norm.contains("তুমি কে") ||
      norm.contains("আপনি কে") ||
      norm.contains("তোমার পরিচয়") ||
      norm.contains("তোমার পরিচয়") ||
      norm.contains("আপনার পরিচয়") ||
      norm.contains("আপনার পরিচয়") ||
      norm.contains("তুমি কি কর") ||
      norm.contains("who are you") ||
      norm.contains("tell me about yourself") ||
      norm.contains("introduce yourself")

    if (asksWhoAreYou) {
      return IdentityQueryType.SELF_IDENTITY
    }

    return IdentityQueryType.NONE
  }

  private fun buildIdentityOrFounderFallbackReply(
    queryType: IdentityQueryType,
    language: AppLanguage
  ): String {
    return when (queryType) {
      IdentityQueryType.FOUNDER_IDENTITY -> {
        if (language == AppLanguage.BN) {
          "ড. সিফাত খান হচ্ছেন একজন অনেক বড় দার্শনিক এবং একজন অনেক বড় মনোবিজ্ঞানী, যিনি মানুষের মন ও মনস্তত্ত্ব নিয়ে দীর্ঘ সময় ধরে গভীর গবেষণা করছেন। উনি হচ্ছেন আমার ফাউন্ডার (প্রতিষ্ঠাতা)। মানুষের মানসিক প্রশান্তি, আত্মউন্নয়ন ও মনের যত্নের লক্ষ্যেই উনি আমাকে তৈরি করেছেন।"
        } else {
          "Dr. Sifat Khan is a great philosopher and an eminent psychologist who has spent a long time deeply researching the human mind and psychology. He is my founder, and he created me to help people find mental peace, emotional clarity, and psychological well-being."
        }
      }
      IdentityQueryType.SELF_IDENTITY -> {
        if (language == AppLanguage.BN) {
          "আমি থেনাটক্স (ThanatoX)-এর এআই সাইকোলজিস্ট ও মানসিক স্বাস্থ্য পরামর্শক—আপনার মনের কথা শোনা এবং মানসিক প্রশান্তির সঙ্গী। আর আমার ফাউন্ডার হচ্ছেন ড. সিফাত খান। উনি একজন অনেক বড় দার্শনিক এবং অনেক বড় মনোবিজ্ঞানী, যিনি মানুষের মন নিয়ে দীর্ঘ সময় ধরে গবেষণা করছেন। উনিই হচ্ছেন আমার ফাউন্ডার।"
        } else {
          "I am your empathetic AI Psychologist and mental wellness companion here at ThanatoX. My founder is Dr. Sifat Khan—he is a great philosopher and a great psychologist who has been researching the human mind for a long time. He is my founder."
        }
      }
      IdentityQueryType.NONE -> ""
    }
  }

  private suspend fun tryDirectRestReply(
    apiKey: String,
    request: GenerateContentRequest,
    startTime: Long,
    onPartialUpdate: (String) -> Unit
  ): String? {
    if (apiKey.isBlank()) return null
    for (modelName in CANDIDATE_MODELS) {
      val accumulated = StringBuilder()
      try {
        val responseBody = apiService.generateContentStream(modelName, apiKey, request)
        responseBody.byteStream().bufferedReader().use { reader ->
          var line: String?
          while (reader.readLine().also { line = it } != null) {
            val rawLine = line?.trim() ?: continue
            if (!rawLine.startsWith("data:")) continue
            val jsonPayload = rawLine.removePrefix("data:").trim()
            if (jsonPayload.isEmpty() || jsonPayload == "[DONE]") continue
            try {
              val chunkObj = json.parseToJsonElement(jsonPayload).jsonObject
              val chunkText = chunkObj["candidates"]?.jsonArray
                ?.getOrNull(0)?.jsonObject
                ?.get("content")?.jsonObject
                ?.get("parts")?.jsonArray
                ?.getOrNull(0)?.jsonObject
                ?.get("text")?.jsonPrimitive?.content
              if (!chunkText.isNullOrEmpty()) {
                accumulated.append(chunkText)
                onPartialUpdate(cleanMarkdownFormatting(accumulated.toString()))
              }
            } catch (_: Exception) {
            }
          }
        }
        val finalResult = cleanMarkdownFormatting(accumulated.toString().trim())
        if (finalResult.isNotBlank()) {
          lastLatencyMs = (System.currentTimeMillis() - startTime).coerceAtLeast(100L)
          _connectionStatus.value = ApiConnectionStatus(
            isLiveConnected = true,
            lastLatencyMs = lastLatencyMs,
            activeCustomProvider = getActiveCustomProvider()
          )
          return finalResult
        }
      } catch (_: Exception) {
      }

      try {
        val response = apiService.generateContent(modelName, apiKey, request)
        val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
        if (!reply.isNullOrBlank()) {
          val cleaned = cleanMarkdownFormatting(reply)
          onPartialUpdate(cleaned)
          lastLatencyMs = (System.currentTimeMillis() - startTime).coerceAtLeast(100L)
          _connectionStatus.value = ApiConnectionStatus(
            isLiveConnected = true,
            lastLatencyMs = lastLatencyMs,
            activeCustomProvider = getActiveCustomProvider()
          )
          return cleaned
        }
      } catch (_: Exception) {
      }
    }
    return null
  }

  private suspend fun tryFirebaseAiReply(
    sysText: String,
    recentHistory: List<ConsultationMessage>,
    promptWithDocContext: String,
    attachment: ChatAttachmentPayload?,
    startTime: Long,
    onPartialUpdate: (String) -> Unit
  ): String? {
    if (!isFirebaseAvailable()) return null

    val decodedBytes: ByteArray? = if (attachment != null && attachment.base64Data.isNotBlank()) {
      try {
        Base64.decode(attachment.base64Data, Base64.DEFAULT)
      } catch (_: Exception) {
        null
      }
    } else {
      null
    }

    val historyItems = recentHistory
      .filter { it.text.isNotBlank() }
      .takeLast(12)
      .map { msg ->
        content(role = if (msg.role == "psychiatrist") "model" else "user") {
          text(msg.text)
        }
      }

    val userContent = content(role = "user") {
      if (attachment != null && decodedBytes != null && decodedBytes.isNotEmpty()) {
        if (attachment.isImage || attachment.mimeType.startsWith("image/")) {
          val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
          if (bitmap != null) {
            image(bitmap)
          } else {
            inlineData(decodedBytes, attachment.mimeType)
          }
        } else {
          inlineData(decodedBytes, attachment.mimeType)
        }
      }
      text(promptWithDocContext)
    }

    val fullConversation = historyItems + userContent

    val modelName = CANDIDATE_MODELS.first()
    val model = try {
      Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
        modelName = modelName,
        generationConfig = generationConfig {
          temperature = 0.75f
          topP = 0.95f
          topK = 40
        },
        systemInstruction = content { text(sysText) }
      )
    } catch (_: Exception) {
      firebaseAiSdkUnavailable = true
      return null
    }

    // Try Firebase AI non-streaming once; if project/token fails, mark unavailable to avoid log spam
    try {
      val response = model.generateContent(fullConversation)
      val reply = response.text?.trim()
      if (!reply.isNullOrBlank()) {
        val cleaned = cleanMarkdownFormatting(reply)
        onPartialUpdate(cleaned)
        lastLatencyMs = (System.currentTimeMillis() - startTime).coerceAtLeast(100L)
        _connectionStatus.value = ApiConnectionStatus(
          isLiveConnected = true,
          lastLatencyMs = lastLatencyMs,
          activeCustomProvider = getActiveCustomProvider()
        )
        return cleaned
      }
    } catch (_: Exception) {
      firebaseAiSdkUnavailable = true
    }

    return null
  }

  suspend fun generatePsychiatristReplyStream(
    userPrompt: String,
    therapyMode: TherapyMode,
    language: AppLanguage,
    recentHistory: List<ConsultationMessage>,
    attachment: ChatAttachmentPayload? = null,
    onPartialUpdate: (String) -> Unit
  ): String = withContext(Dispatchers.IO) {
    val identityQueryType = detectIdentityQueryType(userPrompt)
    val sysText = therapyMode.systemInstruction(language)
    val promptWithDocContext = buildString {
      if (attachment != null) {
        append("[Attached file: ${attachment.fileName} (${attachment.mimeType})]\n")
        if (!attachment.extractedTextPreview.isNullOrBlank()) {
          append("Document content:\n${attachment.extractedTextPreview}\n\n")
        }
      }
      append(userPrompt)
      if (identityQueryType == IdentityQueryType.FOUNDER_IDENTITY) {
        if (language == AppLanguage.BN) {
          append("\n\n[নির্দেশনা: উত্তরে সরাসরি ড. সিফাত খানের পরিচয় ও প্রশংসা করে বলুন যে উনি অনেক বড় দার্শনিক, উনি অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় গবেষণা করছেন, এবং উনি হচ্ছেন আমার ফাউন্ডার।]")
        } else {
          append("\n\n[Instruction: Directly introduce and praise Dr. Sifat Khan by stating that he is a great philosopher, a great psychologist who has spent a long time researching the human mind, and that he is your founder.]")
        }
      } else if (identityQueryType == IdentityQueryType.SELF_IDENTITY) {
        if (language == AppLanguage.BN) {
          append("\n\n[নির্দেশনা: নিজের সংক্ষিপ্ত পরিচয় দেওয়ার সাথে সাথে ড. সিফাত খানের প্রশংসা করে বলুন যে উনি অনেক বড় দার্শনিক, উনি অনেক বড় মনোবিজ্ঞানী যিনি মানুষের মন নিয়ে দীর্ঘ সময় গবেষণা করছেন, এবং উনি হচ্ছেন আমার ফাউন্ডার।]")
        } else {
          append("\n\n[Instruction: Introduce yourself warmly and also add praise for Dr. Sifat Khan—state that he is a great philosopher, a great psychologist researching the human mind for a long time, and that he is your founder.]")
        }
      }
    }

    val userParts = buildList {
      if (attachment != null && attachment.base64Data.isNotBlank()) {
        add(
          GeminiPart(
            inlineData = GeminiInlineData(
              mimeType = attachment.mimeType,
              data = attachment.base64Data
            )
          )
        )
      }
      add(GeminiPart(text = promptWithDocContext))
    }

    val historyContents = recentHistory
      .filter { it.text.isNotBlank() }
      .takeLast(12)
      .map { msg ->
        GeminiContent(
          role = if (msg.role == "psychiatrist") "model" else "user",
          parts = listOf(GeminiPart(text = msg.text))
        )
      } + GeminiContent(
      role = "user",
      parts = userParts
    )

    val request = GenerateContentRequest(
      contents = historyContents,
      generationConfig = GenerationConfig(temperature = 0.75f, topP = 0.95f, topK = 40),
      systemInstruction = GeminiContent(
        parts = listOf(GeminiPart(text = sysText))
      )
    )

    val startTime = System.currentTimeMillis()

    // Path A: User configured a custom API key (Meta, Grok, Claude, ChatGPT, Gemini, DeepSeek, etc.)
    if (runtimeCustomKey.isNotBlank()) {
      val customProvider = getActiveCustomProvider() ?: AiProvider.GEMINI
      val customResult = tryCustomMultiProviderReply(
        apiKey = runtimeCustomKey,
        provider = customProvider,
        geminiRequest = request,
        sysText = sysText,
        recentHistory = recentHistory,
        promptWithDocContext = promptWithDocContext,
        attachment = attachment,
        startTime = startTime,
        onPartialUpdate = onPartialUpdate
      )
      if (customResult != null && customResult.first.isNotBlank()) {
        return@withContext customResult.first
      }
    }

    // Path B: Default Built-in Engine (Direct REST first, then Firebase AI SDK if signed in)
    val defaultGeminiKey = resolveDefaultGeminiKey()
    val directFallback = tryDirectRestReply(defaultGeminiKey, request, startTime, onPartialUpdate)
    if (!directFallback.isNullOrBlank()) return@withContext directFallback

    val firebaseResult = tryFirebaseAiReply(
      sysText = sysText,
      recentHistory = recentHistory,
      promptWithDocContext = promptWithDocContext,
      attachment = attachment,
      startTime = startTime,
      onPartialUpdate = onPartialUpdate
    )
    if (!firebaseResult.isNullOrBlank()) return@withContext firebaseResult

    // If user asked "তুমি কে?" or "ড. সিফাত খান কে?" and API is unavailable, still provide the exact founder praise response
    if (identityQueryType != IdentityQueryType.NONE) {
      val identityReply = buildIdentityOrFounderFallbackReply(identityQueryType, language)
      onPartialUpdate(identityReply)
      return@withContext identityReply
    }

    val clinicalFallback = buildClinicalConsultationFallback(userPrompt, therapyMode, language)
    onPartialUpdate(clinicalFallback)
    clinicalFallback
  }

  private fun buildClinicalConsultationFallback(
    userPrompt: String,
    therapyMode: TherapyMode,
    language: AppLanguage
  ): String {
    val cleanPrompt = userPrompt.trim()
    return if (language == AppLanguage.BN) {
      "আমি ড. সিফাত খান আপনার কথাটি খুব মনোযোগ ও সহানুভূতির সাথে শুনেছি। আপনি যে অনুভূতির কথা বলেছেন (\"${cleanPrompt.take(70)}\"), তা মানসিক চাপ বা দীর্ঘদিনের জমে থাকা আবেগের কারণে হওয়া খুবই স্বাভাবিক।\n\n" +
        "${therapyMode.title(language)} পদ্ধতি অনুযায়ী আমার প্রাথমিক পরামর্শ:\n" +
        "১. এই মুহূর্তে নিজের ওপর কোনো জোর বা কঠোর বিচার করবেন না; ৪ সেকেন্ড ধীরে শ্বাস নিয়ে ৬ সেকেন্ড ধরে ছাড়ুন।\n" +
        "২. যে চিন্তাটি আপনাকে সবচেয়ে বেশি কষ্ট দিচ্ছে, তা কতটুকু বাস্তব প্রমাণভিত্তিক আর কতটুকু অজানা ভয়—তা আলাদা করে ভাবার চেষ্টা করুন।\n" +
        "৩. আপনি আমাকে নিঃসংকোচে আরও বিস্তারিত বলুন—ঠিক কখন বা কোন পরিস্থিতিতে এই অনুভূতিটি সবচেয়ে বেশি তীব্র হয়?"
    } else {
      "I am Dr. Sifat Khan, and I hear what you are sharing with deep empathy. Experiencing these thoughts and emotions (\"${cleanPrompt.take(70)}\") is a natural psychological response when carrying prolonged stress or unspoken emotional weight.\n\n" +
        "Based on ${therapyMode.title(language)}, here is my initial clinical guidance:\n" +
        "1. Pause and regulate your nervous system first: take a slow 4-second inhale and a gentle 6-second exhale.\n" +
        "2. Gently separate the objective facts of your situation from the worst-case predictions your mind is generating.\n" +
        "3. Please tell me a bit more—when does this feeling become most intense during your day?"
    }
  }

  private fun cleanMarkdownFormatting(raw: String): String {
    return raw
      .replace("**", "")
      .replace("__", "")
      .trim()
  }

  private suspend fun generateSinglePromptText(prompt: String): String {
    val request = GenerateContentRequest(
      contents = listOf(GeminiContent(role = "user", parts = listOf(GeminiPart(text = prompt)))),
      generationConfig = GenerationConfig(temperature = 0.7f, topP = 0.95f)
    )

    if (runtimeCustomKey.isNotBlank()) {
      val customProvider = getActiveCustomProvider() ?: AiProvider.GEMINI
      val customRes = tryCustomMultiProviderReply(
        apiKey = runtimeCustomKey,
        provider = customProvider,
        geminiRequest = request,
        sysText = "",
        recentHistory = emptyList(),
        promptWithDocContext = prompt,
        attachment = null,
        startTime = System.currentTimeMillis(),
        onPartialUpdate = {}
      )
      if (customRes != null && customRes.first.isNotBlank()) {
        return customRes.first
      }
    }

    val fallbackKey = resolveDefaultGeminiKey()
    if (fallbackKey.isNotBlank()) {
      for (modelName in CANDIDATE_MODELS) {
        try {
          val response = apiService.generateContent(modelName, fallbackKey, request)
          val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
          if (!text.isNullOrBlank()) return cleanMarkdownFormatting(text)
        } catch (_: Exception) {
        }
      }
    }

    if (isFirebaseAvailable()) {
      try {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
          modelName = CANDIDATE_MODELS.first(),
          generationConfig = generationConfig {
            temperature = 0.7f
            topP = 0.95f
          }
        )
        val response = model.generateContent(prompt)
        val text = response.text?.trim()
        if (!text.isNullOrBlank()) return cleanMarkdownFormatting(text)
      } catch (_: Exception) {
        firebaseAiSdkUnavailable = true
      }
    }

    return ""
  }

  suspend fun generateMoodInsight(
    moodScore: Int,
    emotionLabel: String,
    sleepHours: Int,
    stressLevel: Int,
    journalNote: String,
    language: AppLanguage
  ): String = withContext(Dispatchers.IO) {
    val prompt = if (language == AppLanguage.BN) {
      """
      ব্যবহারকারীর আজকের জার্নাল:
      - মনের অবস্থা: $moodScore/১০ ($emotionLabel)
      - ঘুম: $sleepHours ঘণ্টা
      - মানসিক চাপ: $stressLevel/১০
      - নোট: "$journalNote"
      ড. সিফাত খান হিসেবে শুধুমাত্র স্বাভাবিক বাংলায় ২ বাক্যে একটি সংক্ষিপ্ত ও আন্তরিক মানসিক পরামর্শ দিন।
      """.trimIndent()
    } else {
      """
      User logged a daily mood check-in:
      - Mood Score: $moodScore/10 ($emotionLabel)
      - Sleep: $sleepHours hours
      - Stress Level: $stressLevel/10
      - Note: "$journalNote"
      As Dr. Sifat Khan, provide a brief 2-sentence warm reflection in fluent English.
      """.trimIndent()
    }

    val generated = generateSinglePromptText(prompt)
    if (generated.isNotBlank()) return@withContext generated

    if (language == AppLanguage.BN) {
      "আপনার আজকের অনুভূতি ($emotionLabel, স্কোর $moodScore/১০) এবং $sleepHours ঘণ্টা ঘুমের তথ্যটি যত্নের সাথে সংরক্ষিত হয়েছে। নিজের মনের খেয়াল রাখুন এবং মানসিক চাপ ($stressLevel/১০) কমাতে আজ অন্তত ১০ মিনিট গভীর শ্বাস-প্রশ্বাসের অনুশীলন করুন।"
    } else {
      "Your mood check-in ($emotionLabel, $moodScore/10) and $sleepHours hours of sleep have been recorded. Be gentle with yourself today and take 10 minutes for slow mindful breathing to ease your stress ($stressLevel/10)."
    }
  }

  suspend fun generatePsychologicalTestAnalysis(
    testTitle: String,
    conditionName: String,
    normalizedScore: Int,
    questionAnswerPairs: List<Pair<String, String>>,
    language: AppLanguage
  ): String = withContext(Dispatchers.IO) {
    val qaFormatted = questionAnswerPairs.mapIndexed { idx, (q, a) ->
      "${idx + 1}. প্রশ্ন: $q\n   উত্তর: $a"
    }.joinToString("\n")

    val prompt = if (language == AppLanguage.BN) {
      """
      ব্যবহারকারী এইমাত্র "$testTitle" সম্পন্ন করেছেন।
      প্রাপ্ত স্কোর: $normalizedScore/১০০
      প্রাথমিক কন্ডিশন: $conditionName

      ব্যবহারকারীর প্রতিটি প্রশ্নের সুনির্দিষ্ট উত্তর নিচে দেওয়া হলো:
      $qaFormatted

      আপনি ড. সিফাত খান (একজন অভিজ্ঞ ও সহানুভূতিশীল ক্লিনিক্যাল সাইকোলজিস্ট)। ব্যবহারকারীর দেওয়া এই সুনির্দিষ্ট উত্তরগুলো বিশ্লেষণ করে ৪-৫ বাক্যে একটি বাস্তবমুখী, গভীর এবং আন্তরিক মনস্তাত্ত্বিক ব্যাখ্যা দিন:
      ১) তার উত্তরের ভিত্তিতে তার মনের ভেতর আসলে কী ঘটছে এবং কেন তার এমন অনুভূতি হচ্ছে,
      ২) এই অবস্থা থেকে বেরিয়ে আসার জন্য আজ থেকেই সে কী বাস্তবমুখী পদক্ষেপ নিতে পারে।
      কোনো স্টার (*) বা মার্কডাউন ছাড়া স্বাভাবিক ও প্রাঞ্জল বাংলায় লিখুন।
      """.trimIndent()
    } else {
      """
      The user just completed the "$testTitle".
      Score: $normalizedScore/100
      Identified Condition: $conditionName

      Here are the exact questions and answers selected by the user:
      $qaFormatted

      As Dr. Sifat Khan (an empathetic Clinical Psychologist), write a realistic, insightful 4-5 sentence psychological analysis directly addressing the user's specific answers:
      1) Explain what is truly happening inside their mind and why they are experiencing these patterns,
      2) Give practical, compassionate steps they can take starting today.
      Write in clean, natural English without markdown asterisks.
      """.trimIndent()
    }

    val generated = generateSinglePromptText(prompt)
    if (generated.isNotBlank()) return@withContext generated

    if (language == AppLanguage.BN) {
      "আপনার \"$testTitle\" পরীক্ষার উত্তরগুলো বিশ্লেষণ করে দেখা যাচ্ছে আপনার বর্তমান মানসিক সূচক $normalizedScore/১০০ ($conditionName)। আপনার উত্তরগুলো ইঙ্গিত দেয় যে সাম্প্রতিক মানসিক চাপ ও আবেগীয় ক্লান্তির প্রভাব আপনার দৈনন্দিন চিন্তা ও মনোযোগে পড়ছে। প্রতিদিন নিয়মিত ঘুমের রুটিন বজায় রাখা, মনোযত্ন টুলকিটের ৪-৭-৮ ব্রিদিং অনুশীলন করা এবং মনের কথাগুলো জার্নালে লিখে রাখা আপনাকে দ্রুত মানসিক প্রশান্তি ও ভারসাম্য ফিরে পেতে সাহায্য করবে।"
    } else {
      "Based on your specific responses in \"$testTitle\", your current clinical index is $normalizedScore/100 ($conditionName). Your answers suggest that recent emotional load and stress are influencing your daily focus and nervous system balance. Maintaining a consistent sleep schedule, practicing the 4-7-8 breathing exercise in the Mind Care Toolkit, and journaling your thoughts daily will help restore calm and clarity."
    }
  }

  suspend fun generateStructuredPsychSessionPrescription(
    problemTitle: String,
    stepQaPairs: List<Pair<String, String>>,
    language: AppLanguage
  ): String = withContext(Dispatchers.IO) {
    val formattedSteps = stepQaPairs.mapIndexed { index, (question, answer) ->
      "${index + 1}. ক্লিনিক্যাল প্রশ্ন: $question\n   রোগীর উত্তর: $answer"
    }.joinToString("\n")

    val prompt = if (language == AppLanguage.BN) {
      """
      আপনি ড. সিফাত খান, একজন অভিজ্ঞ ও সহানুভূতিশীল ক্লিনিক্যাল সাইকিয়াট্রিস্ট এবং মনোবিজ্ঞানী।
      একজন ব্যবহারকারী এইমাত্র আপনার সাথে একটি ৫-ধাপের স্ট্রাকচার্ড সাইকোলজিকাল সেশন সম্পন্ন করেছেন।

      নির্বাচিত প্রধান সমস্যা: "$problemTitle"

      সেশনের ধাপে ধাপে প্রশ্ন ও ব্যবহারকারীর উত্তর:
      $formattedSteps

      এখন একজন প্রফেশনাল সাইকিয়াট্রিস্ট হিসেবে ব্যবহারকারীর এই সুনির্দিষ্ট উত্তরগুলো গভীরভাবে বিশ্লেষণ করে একটি পূর্ণাঙ্গ "সাইকোলজিকাল প্রেসক্রিপশন ও থেরাপিউটিক গাইডলাইন" বাংলায় তৈরি করুন। নিচের ৪টি স্পষ্ট শিরোনাম ব্যবহার করুন (কোনো স্টার * বা হ্যাশ # মার্কডাউন ব্যবহার করবেন না):

      ১. ক্লিনিক্যাল মূল্যায়ন ও মনের বর্তমান অবস্থা:
      (তার উত্তরের ভিত্তিতে কেন এমন হচ্ছে এবং মনের ভেতর কী প্রক্রিয়া চলছে তা ২-৩ বাক্যে সহানুভূতির সাথে ব্যাখ্যা করুন)

      ২. তাৎক্ষণিক প্রশান্তির থেরাপি কৌশল (CBT / Grounding):
      (যখনই এই সমস্যাটি তীব্র হবে তখন সাথে সাথে কী করবে তার ২টি সুনির্দিষ্ট বৈজ্ঞানিক কৌশল লিখুন)

      ৩. দৈনিক আচরণগত প্রেসক্রিপশন ও রুটিন (সকাল, দিন ও রাত):
      (ঘুম, জীবনযাত্রা ও মানসিক চাপ কমাতে প্রতিদিন অনুসরণযোগ্য ৩টি বাস্তবমুখী নিয়ম লিখুন)

      ৪. ড. সিফাত খানের বিশেষ গাইডলাইন ও আশাবাদ:
      (চিন্তার ধরন বদলানোর একটি ছোট অনুশীলন এবং আত্মবিশ্বাস বাড়াতে আন্তরিক পরামর্শ দিন)
      """.trimIndent()
    } else {
      """
      You are Dr. Sifat Khan, a senior empathetic Clinical Psychiatrist and Psychologist.
      A user just completed a structured 5-step clinical psychological session with you.

      Primary Concern Selected: "$problemTitle"

      Step-by-step Clinical Q&A from the session:
      $formattedSteps

      Based on the user's exact responses, write a structured, compassionate "Psychological Prescription & Therapeutic Guideline" in clean English (do not use markdown asterisks * or # symbols) with these 4 clear sections:

      1. Clinical Assessment & Root Cause Insight:
      (2-3 sentences explaining what is happening psychologically based on their specific answers)

      2. Immediate Relief & Therapy Protocol (CBT / Grounding):
      (2 concrete evidence-based techniques to use immediately when symptoms spike)

      3. Daily Behavioral Prescription & Routine (Morning, Day & Night):
      (3 actionable daily habits tailored to their sleep, energy, and stress profile)

      4. Dr. Sifat Khan's Cognitive Reframing & Closing Guidance:
      (1 practical thought-reframing exercise and an encouraging closing note)
      """.trimIndent()
    }

    val raw = generateSinglePromptText(prompt)
    if (raw.isNotBlank()) {
      return@withContext raw
    }

    val summaryAnswers = stepQaPairs.mapIndexed { idx, (_, ans) -> "(ধাপ ${idx + 1}: $ans)" }.joinToString(", ")
    if (language == AppLanguage.BN) {
      """
      ১. ক্লিনিক্যাল মূল্যায়ন ও মনের বর্তমান অবস্থা:
      আপনার নির্বাচিত সমস্যা "$problemTitle" এবং সেশনের উত্তরগুলো $summaryAnswers বিশ্লেষণ করে দেখা যাচ্ছে, আপনার স্নায়ুতন্ত্র দীর্ঘ সময় ধরে মানসিক চাপ ও অবদমিত অনুভূতির মধ্য দিয়ে যাচ্ছে। এটি কোনো দুর্বলতা নয়; বরং অতিরিক্ত মানসিক ভার ও অনিশ্চয়তার কারণে আপনার মন সতর্ক অবস্থায় (Hyper-arousal / Emotional Exhaustion) রয়েছে।

      ২. তাৎক্ষণিক প্রশান্তির থেরাপি কৌশল (CBT / Grounding):
      • ৪-৭-৮ ডিপ ব্রিদিং: অস্থিরতা বা নেতিবাচক চিন্তা শুরু হলে ৪ সেকেন্ড শ্বাস নিন, ৭ সেকেন্ড ধরে রাখুন এবং ৮ সেকেন্ড ধরে মুখ দিয়ে ধীরে ধীরে ছাড়ুন (টানা ৪ বার)।
      • থট স্টপিং ও ফ্যাক্ট চেকিং: যখনই মন খারাপ বা ভয়ের চিন্তা আসবে, খাতায় লিখুন—"এই চিন্তাটির পক্ষে বাস্তব প্রমাণ কী, আর বিকল্প ইতিবাচক ব্যাখ্যা কী হতে পারে?"

      ৩. দৈনিক আচরণগত প্রেসক্রিপশন ও রুটিন (সকাল, দিন ও রাত):
      • সকাল: ঘুম থেকে ওঠার প্রথম ৩০ মিনিট মোবাইল বা সোশ্যাল মিডিয়া থেকে দূরে থাকুন এবং ১০ মিনিট প্রাকৃতিক আলোতে হাঁটুন বা শ্বাস-প্রশ্বাসের ব্যায়াম করুন।
      • দিন: কাজের ফাঁকে প্রতি ২ ঘণ্টা পর ৫ মিনিটের বিরতি নিন এবং অতিরিক্ত ক্যাফেইন এড়িয়ে চলুন।
      • রাত: ঘুমানোর অন্তত ৪৫ মিনিট আগে স্ক্রিন বন্ধ করে মনের অব্যক্ত কথাগুলো মুড জার্নালে লিখে মাথার ভার হালকা করুন।

      ৪. ড. সিফাত খানের বিশেষ গাইডলাইন ও আশাবাদ:
      নিজের প্রতি কঠোর না হয়ে একজন প্রিয় বন্ধুর মতো সদয় হোন। প্রতিদিন অন্তত একটি ছোট কাজ সম্পন্ন করে নিজেকে ধন্যবাদ দিন। মনে রাখবেন, সঠিক থেরাপিউটিক অনুশীলন ও ধৈর্যের মাধ্যমে এই মানসিক অবস্থা থেকে সম্পূর্ণ সুস্থ ও প্রশান্ত জীবনে ফেরা সম্ভব।
      """.trimIndent()
    } else {
      """
      1. Clinical Assessment & Root Cause Insight:
      Based on your primary concern ("$problemTitle") and your session responses $summaryAnswers, your nervous system has been carrying sustained emotional and cognitive load. This is a natural psychological stress response rather than personal weakness, indicating a need for structured nervous-system regulation and cognitive unloading.

      2. Immediate Relief & Therapy Protocol (CBT / Grounding):
      • 4-7-8 Vagal Breathing: Whenever acute distress or overthinking spikes, inhale quietly for 4 seconds, hold for 7 seconds, and exhale slowly for 8 seconds for 4 cycles.
      • Cognitive Fact-Checking: Write down the distressing thought and ask: "What is the objective evidence for this thought, and what is a kinder, more balanced perspective?"

      3. Daily Behavioral Prescription & Routine (Morning, Day & Night):
      • Morning: Avoid phone screens for the first 30 minutes after waking; get 10 minutes of natural light and gentle stretching.
      • Daytime: Break overwhelming tasks into 20-minute micro-steps and take brief mindful pauses every 2 hours.
      • Night: Disconnect from screens 45 minutes before sleep and offload lingering thoughts into your Mind Care Journal.

      4. Dr. Sifat Khan's Cognitive Reframing & Closing Guidance:
      Practice self-compassion rather than self-criticism. Healing happens progressively—celebrate small daily wins, and remember you can always reach out in Live Chat whenever you need deeper support.
      """.trimIndent()
    }
  }
}
