package com.example.viewmodel

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
import com.example.data.model.HealthReport
import com.example.data.model.HistoryLog
import com.example.data.model.SoilAdvisory
import com.example.util.AppLanguage
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
  var simulatedImageName by mutableStateOf<String?>(null)
  var isRecordingVoice by mutableStateOf(false)
  var recordedQueryText by mutableStateOf<String?>(null)

  // Moshi & Gemini SDK Config
  private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  private val generativeModel by lazy {
    Firebase.vertexAI.generativeModel(
      modelName = "gemini-2.5-flash",
      generationConfig = generationConfig {
        responseMimeType = "application/json"
      }
    )
  }

  init {
    // Demonstration of secure API Key retrieval from Secrets panel via BuildConfig.
    // In production apps utilizing Vertex AI for Firebase, backend authentication is preferred,
    // but the API key is retrieved here for local prototyping verification.
    val apiKey = com.example.BuildConfig.GEMINI_API_KEY
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
      android.util.Log.w("KisanAlertViewModel", "Secure GEMINI_API_KEY from BuildConfig is not configured yet.")
    } else {
      android.util.Log.i("KisanAlertViewModel", "Successfully retrieved secure GEMINI_API_KEY from BuildConfig.")
    }
  }

  // Alerts & History Lists
  val alertList = listOf(
    AlertItem(
      1,
      "Dry-spell warning dispatched via Voice Call",
      "Regional IVR system broadcast to all registered smallholders in block.",
      "VOICE",
      "10 mins ago",
      "IVR Broadcast Script: 'Alert! Low rainfall forecast for next 14 days in your village. Prepare drip channels and conserve ground water. Tap 1 to repeat.'"
    ),
    AlertItem(
      2,
      "Pest Outbreak: Fall Armyworm detected",
      "Preemptive SMS advice sent to fields in 5km radius.",
      "SMS",
      "2 hours ago",
      "SMS Broadcast: 'RSK ALERT: Fall Armyworm pest detected in neighboring cotton fields. Check leaf undersides for white egg sacks. Spray Neem oil immediately.'"
    ),
    AlertItem(
      3,
      "Market Price Alert: Wheat Price Up",
      "Mandiga update sent to farmers.",
      "SMS",
      "Yesterday",
      "SMS Broadcast: 'Mandiga Market Price update: Wheat price increased by ₹50 to ₹2,450 per quintal. Demand remains strong.'"
    )
  )

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
      }
      try {
        val prompt = """
          You are an expert agronomist. Analyze the following soil and environmental conditions and recommend the most suitable crop:
          - Soil Type: $soilType
          - Nitrogen (N): $N ppm
          - Phosphorus (P): $P ppm
          - Potassium (K): $K ppm
          - Soil pH: $pH
          - Current/Forecasted Weather: $weather
          
          Return a JSON object matching this schema:
          {
            "cropName": "Name of the crop recommended",
            "matchPercentage": "Match percentage (e.g., '95% Match')",
            "plantingWindow": "Sowing window (e.g., 'Next 10 Days')",
            "details": "A detailed explanation of why this crop fits these parameters",
            "guidelines": ["Guideline 1", "Guideline 2", "Guideline 3"]
          }
          
          Ensure the response contains ONLY the valid raw JSON object. Do not wrap it in markdown code blocks or add any other text.
        """.trimIndent()
        
        val response = generativeModel.generateContent(prompt)
        val responseText = response.text?.trim() ?: throw Exception("Empty response from Gemini")
        val cleanJson = responseText.removePrefix("```json").removeSuffix("```").trim()
        val advisory = moshi.adapter(SoilAdvisory::class.java).fromJson(cleanJson)
        
        withContext(Dispatchers.Main) {
          advisoryResult = advisory
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          advisoryResult = SoilAdvisory(
            cropName = "Offline Recommendations (Error: ${e.localizedMessage ?: "Empty API response"})",
            matchPercentage = "Fallback Mode",
            plantingWindow = "Verify Credentials",
            details = "Unable to contact the Google Gemini API. Please make sure that your GEMINI_API_KEY is entered in the Secrets panel of Google AI Studio and that your device has active internet access.",
            guidelines = listOf(
              "Add GEMINI_API_KEY in Google AI Studio secrets.",
              "Verify Internet access on the device/emulator.",
              "Ensure correct model name configuration."
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
        diagnosticReport = null
      }
      try {
        val bitmap = android.graphics.BitmapFactory.decodeFile(imageFile.absolutePath)
          ?: throw Exception("Failed to decode image file")
          
        val promptText = """
          Analyze this crop leaf image. Identify any disease, assess the severity, and recommend organic/chemical remedies.
          
          Return a JSON object matching this schema:
          {
            "diseaseName": "The detected disease or 'Healthy Leaf'",
            "severityPercent": 0.0, // a float representing severity between 0.0 (healthy) and 1.0 (extremely severe)
            "remediesList": ["Remedy 1", "Remedy 2", "Remedy 3"],
            "imageType": "HEALTHY" or "BLAST" or "BLIGHT" // select the closest match based on visual symptoms
          }
          
          Ensure the response contains ONLY the valid raw JSON object. Do not wrap it in markdown code blocks or add any other text.
        """.trimIndent()
        
        val inputContent = content {
          image(bitmap)
          text(promptText)
        }
        
        val response = generativeModel.generateContent(inputContent)
        val responseText = response.text?.trim() ?: throw Exception("Empty response from Gemini")
        val cleanJson = responseText.removePrefix("```json").removeSuffix("```").trim()
        val report = moshi.adapter(HealthReport::class.java).fromJson(cleanJson)
        
        withContext(Dispatchers.Main) {
          diagnosticReport = report
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          diagnosticReport = HealthReport(
            diseaseName = "Error: ${e.localizedMessage ?: "Failed to contact Gemini"}",
            severityPercent = 0.0f,
            remediesList = listOf(
              "Please check that your GEMINI_API_KEY is configured in the AI Studio Secrets panel.",
              "Ensure your device has active internet connectivity.",
              "If the problem persists, verify the model name and network configurations."
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

  /**
   * Helper to simulate farmer voice query.
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
        recordedQueryText = "How do I protect my newly planted maize crops from wild boars and root rot during early heavy rain?"
        isDiagnosticLoading = true
      }
      
      try {
        val prompt = """
          Answer the following farmer spoken query:
          "How do I protect my newly planted maize crops from wild boars and root rot during early heavy rain?"
          
          Return a JSON object matching this schema:
          {
            "diseaseName": "Spoken Query: Maize Root Rot & Wildlife Protection",
            "severityPercent": 0.25,
            "remediesList": ["Remedy 1", "Remedy 2", "Remedy 3"],
            "imageType": "HEALTHY"
          }
          
          Ensure the response contains ONLY the valid raw JSON object. Do not wrap it in markdown code blocks or add any other text.
        """.trimIndent()
        
        val response = generativeModel.generateContent(prompt)
        val responseText = response.text?.trim() ?: throw Exception("Empty response from Gemini")
        val cleanJson = responseText.removePrefix("```json").removeSuffix("```").trim()
        val report = moshi.adapter(HealthReport::class.java).fromJson(cleanJson)
        
        withContext(Dispatchers.Main) {
          diagnosticReport = report
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          diagnosticReport = HealthReport(
            diseaseName = "Voice Advisory (Offline Mode)",
            severityPercent = 0.25f,
            remediesList = listOf(
              "Ensure trench drainage is cleared immediately to prevent pooling water.",
              "Construct a simple reflective solar ribbon fence to humanely deter wild boars.",
              "Apply a light neem cake soil dressing to strengthen root structure naturally."
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
