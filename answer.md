# Kisan Alert - Android AI Integration & Security Architecture

This document contains a complete technical overview of the implementation completed for the **Kisan Alert** Android application, utilizing Jetpack Compose, Kotlin Coroutines, and the Google AI Client SDK to execute asynchronous, real-time agronomic analysis via Google Gemini.

---

## 1. Google AI Client SDK & Google Gemini Integration

### Core Architecture Overview
The mock methods inside `KisanAlertViewModel` were completely replaced with real asynchronous API calls to the `gemini-2.5-flash` model using the standalone Google AI Client SDK to avoid initialization crashes associated with the Firebase Vertex AI SDK. We implemented strict JSON schema enforcement using Moshi serialization and defensive JSON block extraction to safely parse raw structured outputs directly into local type-safe models (`SoilAdvisory` and `HealthReport`).

### Key Technologies Utilized
*   **Google AI Client SDK:** `com.google.ai.client.generativeai:generativeai:0.9.0`
*   **Model:** `gemini-2.5-flash`
*   **State Management:** StateFlow & Compose Observable State (`mutableStateOf`)
*   **Asynchronous Engine:** Kotlin Coroutines (`Dispatchers.IO` and `Dispatchers.Main` context switching)
*   **Defensive Parsing:** Regular expression-based brace locator for resilient JSON extraction.
*   **JSON Serialization:** Moshi Parser (`com.squareup.moshi`) with Kotlin Reflection Adapter

---

### Implementation 1: Dynamic Soil Advisory Engine (`runSoilAdvisory`)

```kotlin
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
      val cleanJson = extractJsonBlock(responseText)
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
```

---

### Implementation 2: Visual Crop Leaf Pathology Diagnosis (`runCropLeafDiagnosis`)

```kotlin
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
      
      val response = generativeModel.generateContent(
        content {
          image(bitmap)
          text(promptText)
        }
      )
      val responseText = response.text?.trim() ?: throw Exception("Empty response from Gemini")
      val cleanJson = extractJsonBlock(responseText)
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
```

---

## 2. Defensive JSON Block Extraction Helper

To ensure maximum resilience against markdown wrappers, stray thoughts, leading text, or surrounding descriptions sometimes generated by LLMs alongside JSON structures, we added a robust `extractJsonBlock` utility inside `KisanAlertViewModel.kt`.

```kotlin
private fun extractJsonBlock(rawInput: String): String {
  val regex = Regex("""\{.*\}""", RegexOption.DOT_MATCHES_ALL)
  val matchResult = regex.find(rawInput)
  return matchResult?.value ?: rawInput
}
```
This utility uses a greedy regular expression with `DOT_MATCHES_ALL` options to extract everything starting from the absolute first curly brace `{` to the absolute last curly brace `}`.

---

## 3. Secure API Credential Handling

API Keys are safely retrieved at runtime using the auto-generated `BuildConfig` file populated by the **Secrets Gradle Plugin** (reading from `.env` or the platform's Secrets environment context):

```kotlin
private val generativeModel by lazy {
  GenerativeModel(
    modelName = "gemini-2.5-flash",
    apiKey = com.example.BuildConfig.GEMINI_API_KEY,
    generationConfig = generationConfig {
      responseMimeType = "application/json"
    }
  )
}

init {
  val apiKey = com.example.BuildConfig.GEMINI_API_KEY
  if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
    android.util.Log.w("KisanAlertViewModel", "Secure GEMINI_API_KEY from BuildConfig is not configured yet.")
  } else {
    android.util.Log.i("KisanAlertViewModel", "Successfully retrieved secure GEMINI_API_KEY from BuildConfig.")
  }
}
```

---

## 4. Hardened Android Security & Permissions Manifest

A production-grade, highly secure `AndroidManifest.xml` was written to enforce explicit hardware features and granular runtime permission requests, limiting access strictly to essential system services.

### Permissions Declared
1.  **Internet Connectivity:** Necessary for executing Google Gemini network queries.
2.  **Camera Hardware:** Necessary for snapping live crop leaves for immediate disease diagnosis.
3.  **Microphone Access:** Necessary for recording voice-guided queries from regional farmers.
4.  **Geographic Location:** Necessary for capturing block-level climate, soil distribution, and regional weather parameters.

### Security Configurations
*   **Cleartext Traffic Prohibited:** Explicitly forces secure HTTPS/SSL-only network traffic with `android:usesCleartextTraffic="false"`.

### Manifest File Configuration (`AndroidManifest.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Internet Access -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <!-- Camera Access -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" android:required="true" />

    <!-- Audio Recording -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />

    <!-- Location Access -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication"
        android:usesCleartextTraffic="false">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MyApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 5. UI Polish & Visual State Synchronizations

*   **Real-time Analysis Indicator:** Integrated a modern Material 3 `CircularProgressIndicator` with detailed visual feedback when executing plant pathology requests.
*   **High-Fidelity Programmatic Leaf Generator:** Implemented custom rendering inside `KisanAlertViewModel` to simulate visual leaf structures (Healthy, Rice Blast, and Bacterial Leaf Blight lesions) onto standard `Bitmap` files, allowing offline testing and visual verification before Gemini classification triggers.

---

## 6. Phase 4 Upgrades: Voice-Based Accessibility, Phone/OTP Sign-in, and Settings

To optimize the application for rural India where illiteracy rates are high, we introduced visual-first and voice-first interactions:

### A. Phone & OTP Authentication Flow
- **Direct Input:** Implemented simple, large form-fields in `LoginScreen.kt` for mobile number inputs.
- **Verification:** Verification logic uses a simple simulated 4-digit OTP callback (`verifyPhoneAndLogin`) directly tied to `KisanAlertViewModel` to bypass complex email/password registrations.
- **Routing:** Unauthenticated users are cleanly intercepted on startup within `MainActivity.kt`.

### B. Text-to-Speech (TTS) & Speech-to-Text (STT) Voice Advisory
- **Voice Manager:** Built a decoupled, runtime-safe wrapper `VoiceManager.kt` around Android's native `TextToSpeech` API. It maps the active user language (English, Hindi, Telugu, Tamil, Kannada) to local voice locales dynamically.
- **Speech Recognizer Launcher:** MainActivity registers an activity result contract for `RecognizerIntent.ACTION_RECOGNIZE_SPEECH` to capture and translate farmer spoken crop symptoms, querying Gemini directly with the transcribed string.
- **TTS Readback:** Added speaker buttons (`VolumeUp` icon) next to all diagnostic recommendations and crop recommendations so the details can be read aloud.

### C. Voice Calling & Replay Callbacks
- Added a "Voice Call Me" button to the RSK Outbound logs and dashboard emergency alerts. When clicked, it activates a TTS simulation of a regional automated call reading the warning to the farmer.

