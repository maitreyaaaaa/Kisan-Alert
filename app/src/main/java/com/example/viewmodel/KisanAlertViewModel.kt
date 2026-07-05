package com.example.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.data.model.AlertItem
import com.example.data.model.AdvisoryCard
import com.example.data.model.FarmerProfile
import com.example.data.model.HealthReport
import com.example.data.model.HistoryLog
import com.example.data.model.HomePriority
import com.example.data.model.HomeSummary
import com.example.data.model.SoilAdvisory
import com.example.data.model.SoilAdvisoryRequest
import com.example.data.service.AdvisoryGateway
import com.example.data.service.AdvisoryGatewayFactory
import com.example.data.service.AuthGateway
import com.example.data.service.AuthGatewayFactory
import com.example.data.service.LocalSessionStore
import com.example.util.AppLanguage
import com.example.util.LocalizedStrings
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KisanAlertViewModel : ViewModel() {
  // Navigation & Language
  var currentTab by mutableStateOf(0)
  var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)

  // Auth State
  var farmerProfile by mutableStateOf<FarmerProfile?>(null)
  var isAuthLoading by mutableStateOf(false)
  var authErrorMessage by mutableStateOf<String?>(null)
  val isLoggedIn: Boolean
    get() = farmerProfile != null

  // Settings State
  var isAutoSpeakEnabled by mutableStateOf(true)
  var isDarkTheme by mutableStateOf(false)


  // Soil Advisor Inputs
  var selectedSoilType by mutableStateOf("Clayey")
  var nitrogen by mutableStateOf("65")
  var phosphorus by mutableStateOf("45")
  var potassium by mutableStateOf("40")
  var phLevel by mutableFloatStateOf(6.5f)

  // Soil Advisor Outputs
  var isAdvisoryLoading by mutableStateOf(false)
  var advisoryResult by mutableStateOf<SoilAdvisory?>(null)

  // Crop Health Logger State
  var isDiagnosticLoading by mutableStateOf(false)
  var diagnosticReport by mutableStateOf<HealthReport?>(null)
  var voiceAdvisoryCard by mutableStateOf<AdvisoryCard?>(null)
  var simulatedImageName by mutableStateOf<String?>(null)
  var isRecordingVoice by mutableStateOf(false)
  var recordedQueryText by mutableStateOf<String?>(null)

  // Advisory boundary for prototype vs production-ready service routing.
  private val advisoryGateway: AdvisoryGateway =
    AdvisoryGatewayFactory.create(com.example.BuildConfig.GEMINI_API_KEY)
  private val authGateway: AuthGateway = AuthGatewayFactory.create()
  private var sessionStore: LocalSessionStore? = null

  init {
    val apiKey = com.example.BuildConfig.GEMINI_API_KEY
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      android.util.Log.w(
        "KisanAlertViewModel",
        "Prototype advisory gateway is running in ${advisoryGateway.modeLabel} mode."
      )
    } else {
      android.util.Log.i(
        "KisanAlertViewModel",
        "Prototype advisory gateway is running in ${advisoryGateway.modeLabel} mode."
      )
    }
  }

  // Alerts & History Lists
  /* legacy val alertList = listOf(
    AlertItem(
      1,
      "Heavy rain likely tomorrow evening",
      "Keep harvested crop covered and check drainage before sunset.",
      "WEATHER",
      "20 mins ago",
      "Rainfall is expected in your area tomorrow evening. Move harvested crop under cover, clear drainage channels, and delay spraying if possible."
    ),
    AlertItem(
      2,
      "Leaf spot risk after two wet mornings",
      "Walk the field early and use the camera flow if new spots appear.",
      "CROP",
      "2 hours ago",
      "Nearby fields reported fresh leaf spots after repeated moisture. Inspect the lower leaves in the morning and capture a photo if symptoms spread."
    ),
    AlertItem(
      3,
      "Tomato mandi price up by Rs 2 per kg",
      "Small price movement today. Check transport cost before selling.",
      "MARKET",
      "Yesterday",
      "SMS Broadcast: 'Mandiga Market Price update: Wheat price increased by ₹50 to ₹2,450 per quintal. Demand remains strong.'"
    )
  ) */

  val alertList: List<AlertItem>
    get() = buildAlertList()

  val homeSummary: HomeSummary
    get() = buildHomeSummary()

  val currentAskCard: AdvisoryCard?
    get() = when {
      voiceAdvisoryCard != null -> voiceAdvisoryCard
      diagnosticReport != null -> buildHealthAdvisoryCard(diagnosticReport!!, currentLanguage)
      advisoryResult != null -> buildSoilAdvisoryCard(advisoryResult!!, currentLanguage)
      else -> null
    }

  val historyLogs = listOf(
    HistoryLog(
      1,
      "Outgoing Call Completed - Pest Warning",
      "CALL",
      "Completed",
      "3 mins ago",
      "IVR Voice warning played successfully to farmer mobile (Duration: 42s). Farmer pressed '1' to confirm receipt."
    ),
    HistoryLog(
      2,
      "SMS Sent - Market Price Update",
      "SMS",
      "Delivered",
      "1 hour ago",
      "Automated regional wholesale market pricing sent in Kannada/Telugu. System delivery confirmed by operator."
    ),
    HistoryLog(
      3,
      "Outgoing Call - Flash Flood Broadcast",
      "CALL",
      "Completed",
      "Yesterday, 5:30 PM",
      "Emergency warning issued due to sudden canal breach upstream. Playback duration: 60s. High priority confirmation received."
    ),
    HistoryLog(
      4,
      "SMS Sent - Heavy Rain Precaution",
      "SMS",
      "Delivered",
      "2 days ago",
      "Regional weather department warning: Avoid harvesting grain during the upcoming 48 hours to prevent damp rot."
    )
  )

  private fun buildHomeSummary(): HomeSummary {
    val profile = farmerProfile
    val district = profile?.district ?: "your district"
    val crop = profile?.primaryCrop ?: "Millet"
    val name = profile?.name ?: "Farmer"

    return HomeSummary(
      headline = if (profile != null) {
        LocalizedStrings.format("home_signed_in_title", currentLanguage, mapOf("name" to name))
      } else {
        LocalizedStrings.get("home_headline", currentLanguage)
      },
      intro = if (profile != null) {
        LocalizedStrings.format(
          "home_signed_in_copy",
          currentLanguage,
          mapOf(
            "district" to district,
            "crop" to crop
          )
        )
      } else {
        LocalizedStrings.get("home_intro", currentLanguage)
      },
      districtTag = district,
      cropTag = crop,
      weatherValue = when (district.lowercase()) {
        "anantapur" -> LocalizedStrings.get("home_weather_anantapur", currentLanguage)
        "guntur" -> LocalizedStrings.get("home_weather_guntur", currentLanguage)
        "nizamabad" -> LocalizedStrings.get("home_weather_nizamabad", currentLanguage)
        "mysuru" -> LocalizedStrings.get("home_weather_mysuru", currentLanguage)
        else -> LocalizedStrings.get("home_weather_default", currentLanguage)
      },
      topAlertTitle = alertList.firstOrNull()?.title ?: "No active alerts",
      supportCopy = LocalizedStrings.format(
        "home_support_copy",
        currentLanguage,
        mapOf("helper" to (profile?.trustedHelperPhone ?: "+91 90000 11223"))
      ),
      priorities = listOf(
        HomePriority(
          title = LocalizedStrings.format(
            "home_priority_patch_title",
            currentLanguage,
            mapOf("crop" to crop.lowercase())
          ),
          detail = LocalizedStrings.get("home_priority_patch_detail", currentLanguage)
        ),
        HomePriority(
          title = LocalizedStrings.get("home_priority_voice_title", currentLanguage),
          detail = LocalizedStrings.get("home_priority_voice_detail", currentLanguage)
        ),
        HomePriority(
          title = LocalizedStrings.format(
            "home_priority_market_title",
            currentLanguage,
            mapOf("district" to district)
          ),
          detail = LocalizedStrings.get("home_priority_market_detail", currentLanguage)
        )
      )
    )
  }

  private fun buildAlertList(): List<AlertItem> {
    val district = farmerProfile?.district ?: "your district"
    val crop = farmerProfile?.primaryCrop ?: "Millet"

    return listOf(
      AlertItem(
        1,
        "Heavy rain likely in $district tonight",
        "Keep harvested crop covered and clear the nearest drain before sunset.",
        "WEATHER",
        "20 mins ago",
        "Weather risk is highest in the next 8 hours. Move harvested produce off the floor, reopen blocked drainage, and delay spraying if leaf wetness remains high."
      ),
      AlertItem(
        2,
        "$crop disease risk rising in wet pockets",
        "Walk the shadiest side of the field and inspect lower leaves before treatment.",
        "CROP",
        "2 hours ago",
        "Repeated moisture can make early disease spread look like a nutrient issue. Inspect two affected plants and compare them with a healthy patch before spending on inputs."
      ),
      AlertItem(
        3,
        "$district mandi signal improving for $crop",
        "Watch transport cost before sending the next small lot.",
        "MARKET",
        "Yesterday",
        "Market movement is positive, but not urgent. Confirm the evening rate and compare it with loading and travel cost before making a sale decision."
      )
    )
  }

  private fun buildHealthAdvisoryCard(report: HealthReport, language: AppLanguage): AdvisoryCard {
    val severityPercent = (report.severityPercent * 100).toInt()
    val riskLabel = when {
      report.severityPercent >= 0.6f -> LocalizedStrings.get("ask_risk_high", language)
      report.severityPercent >= 0.25f -> LocalizedStrings.get("ask_risk_medium", language)
      else -> LocalizedStrings.get("ask_risk_low", language)
    }
    val needsHumanReview = report.severityPercent >= 0.25f
    val confidenceLabel = if (report.imageType == "HEALTHY") {
      LocalizedStrings.get("ask_confidence_early", language)
    } else {
      LocalizedStrings.get("ask_confidence_pattern", language)
    }

    return AdvisoryCard(
      title = report.diseaseName,
      summary = if (report.imageType == "HEALTHY") {
        LocalizedStrings.get("ask_summary_healthy", language)
      } else {
        LocalizedStrings.get("ask_summary_detected", language)
          .replace("{risk}", riskLabel)
          .replace("{percent}", severityPercent.toString())
      },
      actions = report.remediesList,
      riskLabel = riskLabel,
      needsHumanReview = needsHumanReview,
      confidenceLabel = confidenceLabel
    )
  }

  private fun buildSoilAdvisoryCard(advisory: SoilAdvisory, language: AppLanguage): AdvisoryCard {
    val needsHumanReview = true
    val confidenceLabel = if (advisory.matchPercentage.contains("Prototype", ignoreCase = true)) {
      LocalizedStrings.get("ask_confidence_prototype", language)
    } else {
      LocalizedStrings.get("ask_confidence_soil_fit", language)
    }

    return AdvisoryCard(
      title = advisory.cropName,
      summary = advisory.details,
      actions = advisory.guidelines,
      riskLabel = LocalizedStrings.get("ask_risk_plan", language),
      needsHumanReview = needsHumanReview,
      confidenceLabel = confidenceLabel
    )
  }

  /**
   * 1. runSoilAdvisory: Construct a prompt and query Gemini model for a JSON output.
   */
  fun runSoilAdvisory(
    scope: CoroutineScope,
    soilType: String = selectedSoilType,
    N: String = nitrogen,
    P: String = phosphorus,
    K: String = potassium,
    pH: Float = phLevel,
    weather: String = "Sunny, 32°C"
  ) {
    scope.launch(Dispatchers.IO) {
      withContext(Dispatchers.Main) {
        isAdvisoryLoading = true
        advisoryResult = null
        diagnosticReport = null
        voiceAdvisoryCard = null
      }
      try {
        val advisory = advisoryGateway.requestSoilAdvisory(
          SoilAdvisoryRequest(
            soilType = soilType,
            nitrogen = N,
            phosphorus = P,
            potassium = K,
            phLevel = pH,
            weather = weather
          )
        )
        
        withContext(Dispatchers.Main) {
          advisoryResult = advisory
          if (isAutoSpeakEnabled) {
            speakAdvisoryCard(buildSoilAdvisoryCard(advisory, currentLanguage))
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          advisoryResult = SoilAdvisory(
            cropName = "Offline Recommendations (Error: ${e.localizedMessage ?: "Empty API response"})",
            matchPercentage = "Fallback Mode",
            plantingWindow = "Review Service Setup",
            details = "The prototype advisory flow could not complete. Production should move this path to a server-side advisory service instead of relying on client-side model access.",
            guidelines = listOf(
              "Keep this app usable with local fallbacks while backend work is in progress.",
              "Move model access behind an approved server-side agronomy service.",
              "Verify connectivity and prototype configuration only for local testing."
            )
          )
        }
      } finally {
        withContext(Dispatchers.Main) {
          isAdvisoryLoading = false
        }
      }
    }
  }

  /**
   * 2. runCropLeafDiagnosis: Converts the captured crop leaf image file to a Bitmap
   * and sends it with a plant pathology prompt to the gemini-2.5-flash model.
   */
  fun runCropLeafDiagnosis(imageFile: File, scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
      withContext(Dispatchers.Main) {
        isDiagnosticLoading = true
        advisoryResult = null
        diagnosticReport = null
        voiceAdvisoryCard = null
      }
      try {
        val report = advisoryGateway.requestCropDiagnosis(imageFile)
        
        withContext(Dispatchers.Main) {
          diagnosticReport = report
          if (isAutoSpeakEnabled) {
            speakAdvisoryCard(buildHealthAdvisoryCard(report, currentLanguage))
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          diagnosticReport = HealthReport(
            diseaseName = "Prototype advisory error: ${e.localizedMessage ?: "Service unavailable"}",
            severityPercent = 0.0f,
            remediesList = listOf(
              "Treat this as a prototype limitation, not a final production path.",
              "Use image-free local fallback guidance until the backend advisory service exists.",
              "Move crop diagnosis to a server-side service before production release."
            ),
            imageType = "HEALTHY"
          )
        }
      } finally {
        withContext(Dispatchers.Main) {
          isDiagnosticLoading = false
        }
      }
    }
  }

  /**
   * Helper to simulate taking/uploading a leaf image. Saves a high-fidelity drawn leaf bitmap
   * into a File in the cache directory, and calls runCropLeafDiagnosis on it.
   */
  fun simulateUploadLeaf(
    type: String,
    context: android.content.Context? = null,
    scope: CoroutineScope? = null
  ) {
    simulatedImageName = type
    advisoryResult = null
    voiceAdvisoryCard = null
    if (context != null && scope != null) {
      scope.launch(Dispatchers.IO) {
        try {
          val file = createSimulatedLeafFile(type, context)
          runCropLeafDiagnosis(file, scope)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    } else {
      // Fallback offline mapping if context or scope is omitted
      diagnosticReport = when (type) {
        "HEALTHY" -> HealthReport(
          diseaseName = "Healthy Leaf (No disease detected)",
          severityPercent = 0.0f,
          remediesList = listOf(
            "Maintain current watering schedule.",
            "Check weekly for early signs of fungal spots.",
            "Keep fertilizer application organic and balanced."
          ),
          imageType = "HEALTHY"
        )
        "BLAST" -> HealthReport(
          diseaseName = "Rice Blast (Fungal Disease)",
          severityPercent = 0.38f,
          remediesList = listOf(
            "Apply recommended organic copper fungicide or Tricyclazole 75 WP.",
            "Avoid flooding fields excessively; maintain optimal moisture levels.",
            "Remove and burn highly infested plant residue to check the spread of spores."
          ),
          imageType = "BLAST"
        )
        "BLIGHT" -> HealthReport(
          diseaseName = "Bacterial Leaf Blight (Xanthomonas oryzae)",
          severityPercent = 0.65f,
          remediesList = listOf(
            "Drain field immediately to stop standing water transmission of bacteria.",
            "Apply customized stable bleaching powder (100g/acre) or consulting agricultural scientist.",
            "Reduce nitrogenous fertilizer application until field fully recovers."
          ),
          imageType = "BLIGHT"
        )
        else -> null
      }
    }
  }

  // Callback hooks for MainActivity voice integration
  var onStartSpeechRecognizer: (() -> Unit)? = null
  var onSpeakText: ((String) -> Unit)? = null

  fun startVoiceQuery(scope: CoroutineScope) {
    if (onStartSpeechRecognizer != null) {
      onStartSpeechRecognizer?.invoke()
    } else {
      simulateVoiceRecording(scope)
    }
  }

  fun onSpeechRecognized(queryText: String, scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
      withContext(Dispatchers.Main) {
        recordedQueryText = queryText
        isDiagnosticLoading = true
        diagnosticReport = null
        advisoryResult = null
        voiceAdvisoryCard = null
      }
      
      try {
        val card = advisoryGateway.requestVoiceAdvisory(queryText)
        
        withContext(Dispatchers.Main) {
          voiceAdvisoryCard = card
          if (isAutoSpeakEnabled) {
            speakAdvisoryCard(card)
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          val fallbackCard = AdvisoryCard(
            title = "Next step for your question",
            summary = "Start with one local field check before spending money on treatment or fertilizer for: $queryText",
            actions = listOf(
              "Keep field moisture monitored.",
              "Consult local extension officer if symptoms spread.",
              "Apply organic bio-pesticides or compost to boost immunity."
            ),
            riskLabel = LocalizedStrings.get("ask_risk_plan", currentLanguage),
            needsHumanReview = true,
            confidenceLabel = LocalizedStrings.get("ask_confidence_prototype", currentLanguage)
          )
          voiceAdvisoryCard = fallbackCard
          if (isAutoSpeakEnabled) {
            speakAdvisoryCard(fallbackCard)
          }
        }
      } finally {
        withContext(Dispatchers.Main) {
          isDiagnosticLoading = false
        }
      }
    }
  }

  fun speakCurrentAdvisory(report: HealthReport) {
    speakAdvisoryCard(buildHealthAdvisoryCard(report, currentLanguage))
  }

  fun speakSoilAdvisory(advisory: SoilAdvisory) {
    speakAdvisoryCard(buildSoilAdvisoryCard(advisory, currentLanguage))
  }

  fun speakAdvisoryCard(card: AdvisoryCard) {
    val textToSpeak = buildString {
      append(LocalizedStrings.get("tts_answer_intro", currentLanguage))
      append(" ")
      append(card.title)
      append(". ")
      append(card.summary)
      append(" ")
      append(LocalizedStrings.get("tts_actions_intro", currentLanguage))
      append(" ")
      card.actions.forEachIndexed { index, action ->
        append("Action ${index + 1}: $action. ")
      }
    }
    onSpeakText?.invoke(textToSpeak)
  }

  /**
   * Helper to simulate farmer voice query (fallback).
   */
  fun simulateVoiceRecording(scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
      withContext(Dispatchers.Main) {
        isRecordingVoice = true
        recordedQueryText = null
      }
      delay(2000) // Simulate recording time
      withContext(Dispatchers.Main) {
        isRecordingVoice = false
      }
      onSpeechRecognized(
        "How do I protect my newly planted maize crops from wild boars and root rot during early heavy rain?",
        scope
      )
    }
  }

  fun verifyPhoneAndLogin(phone: String, otp: String, scope: CoroutineScope) {
    verifyPhoneAndLogin(
      phone = phone,
      otp = otp,
      farmerName = "Farmer",
      district = "Anantapur",
      scope = scope
    )
  }

  fun verifyPhoneAndLogin(
    phone: String,
    otp: String,
    farmerName: String,
    district: String,
    scope: CoroutineScope
  ) {
    scope.launch(Dispatchers.IO) {
      withContext(Dispatchers.Main) {
        isAuthLoading = true
        authErrorMessage = null
      }

      try {
        delay(1500) // Simulate network delay
        val profile = authGateway.verifyOtp(
          phone = phone,
          otp = otp,
          farmerName = farmerName,
          district = district,
          language = currentLanguage
        )

        withContext(Dispatchers.Main) {
          farmerProfile = profile
          currentLanguage = profile.preferredLanguage
          currentTab = 0
          authErrorMessage = null
          persistSessionState()
        }
      } catch (e: IllegalArgumentException) {
        withContext(Dispatchers.Main) {
          authErrorMessage = e.message ?: "Use the demo OTP 1234 for now."
        }
      } finally {
        withContext(Dispatchers.Main) {
          isAuthLoading = false
        }
      }
    }
  }

  fun setCurrentLanguage(language: AppLanguage) {
    currentLanguage = language
    farmerProfile = farmerProfile?.copy(preferredLanguage = language)
    persistSessionState()
  }

  fun setAutoSpeakEnabled(enabled: Boolean) {
    isAutoSpeakEnabled = enabled
    persistSessionState()
  }

  fun updateFarmerProfile(
    name: String,
    district: String,
    primaryCrop: String,
    trustedHelperPhone: String
  ) {
    farmerProfile = farmerProfile?.copy(
      name = name.ifBlank { farmerProfile?.name ?: "Farmer" },
      district = district.ifBlank { farmerProfile?.district ?: "Anantapur" },
      primaryCrop = primaryCrop.ifBlank { farmerProfile?.primaryCrop ?: "Millet" },
      trustedHelperPhone = trustedHelperPhone.ifBlank { farmerProfile?.trustedHelperPhone ?: "+919000011223" }
    )
    persistSessionState()
  }

  fun logout() {
    farmerProfile = null
    currentTab = 0
    advisoryResult = null
    diagnosticReport = null
    voiceAdvisoryCard = null
    recordedQueryText = null
    simulatedImageName = null
    authErrorMessage = null
    sessionStore?.clear()
  }

  fun attachSessionStore(context: Context) {
    if (sessionStore != null) return

    sessionStore = LocalSessionStore(context.applicationContext)
    val storedState = sessionStore?.loadState() ?: return
    farmerProfile = storedState.profile
    currentLanguage = storedState.profile?.preferredLanguage ?: storedState.currentLanguage
    isAutoSpeakEnabled = storedState.isAutoSpeakEnabled
  }

  private fun persistSessionState() {
    sessionStore?.saveState(
      profile = farmerProfile,
      currentLanguage = currentLanguage,
      isAutoSpeakEnabled = isAutoSpeakEnabled
    )
  }


  /**
   * Helper function to programmatically render a plant leaf with anomalies to a Bitmap,
   * then save it to a temporary JPG image file in the cache directory.
   */
  private fun createSimulatedLeafFile(type: String, context: android.content.Context): File {
    val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply { isAntiAlias = true }
    
    // Fill background (same dark slate color as the app UI for consistency)
    canvas.drawColor(0xFF1E293B.toInt())
    
    // Draw leaf shape outline
    val leafPath = AndroidPath().apply {
      moveTo(200f, 60f)
      cubicTo(340f, 140f, 300f, 320f, 200f, 340f)
      cubicTo(100f, 320f, 60f, 140f, 200f, 60f)
    }
    
    val leafColor = when (type) {
      "HEALTHY" -> 0xFF2D6A4F.toInt()
      "BLAST" -> 0xFF40916C.toInt()
      "BLIGHT" -> 0xFF52B788.toInt()
      else -> 0xFF1B4332.toInt()
    }
    paint.color = leafColor
    paint.style = Paint.Style.FILL
    canvas.drawPath(leafPath, paint)
    
    // Draw main leaf stem/vein
    paint.color = 0xFFD8F3DC.toInt()
    paint.strokeWidth = 5f
    canvas.drawLine(200f, 60f, 200f, 340f, paint)
    
    // Draw secondary veins
    paint.strokeWidth = 3f
    canvas.drawLine(200f, 140f, 270f, 180f, paint)
    canvas.drawLine(200f, 140f, 130f, 180f, paint)
    canvas.drawLine(200f, 220f, 280f, 260f, paint)
    canvas.drawLine(200f, 220f, 120f, 260f, paint)
    
    // Add visual anomalies/lesions for pathology scanning
    if (type == "BLAST") {
      // Brown and grey blast lesion spots
      paint.color = 0xFF78350F.toInt() // Brown
      paint.style = Paint.Style.FILL
      canvas.drawCircle(170f, 190f, 15f, paint)
      paint.color = 0xFF94A3B8.toInt() // Grey inner
      canvas.drawCircle(170f, 190f, 8f, paint)
      
      paint.color = 0xFF78350F.toInt()
      canvas.drawCircle(240f, 230f, 20f, paint)
      paint.color = 0xFF94A3B8.toInt()
      canvas.drawCircle(240f, 230f, 10f, paint)
    } else if (type == "BLIGHT") {
      // Golden yellow margins/stripes
      paint.color = 0xFFF59E0B.toInt()
      paint.style = Paint.Style.STROKE
      paint.strokeWidth = 12f
      canvas.drawLine(100f, 140f, 120f, 260f, paint)
      canvas.drawLine(300f, 140f, 280f, 260f, paint)
    }
    
    val file = File(context.cacheDir, "simulated_leaf_${type.lowercase()}.jpg")
    FileOutputStream(file).use { out ->
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file
  }
}
