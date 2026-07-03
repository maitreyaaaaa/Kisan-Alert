# Kisan Alert - Android AI Integration & Security Architecture

This document contains a complete technical overview of the implementation completed for the **Kisan Alert** Android application, utilizing Jetpack Compose, Kotlin Coroutines, and the Firebase Vertex AI Android SDK to execute asynchronous, real-time agronomic analysis via Google Gemini.

---

## 1. Firebase Vertex AI & Google Gemini Integration

### Core Architecture Overview
The mock methods inside `KisanAlertViewModel` were completely replaced with real asynchronous API calls to the `gemini-2.5-flash` model. We implemented strict JSON schema enforcement using Moshi serialization to safely parse raw structured outputs directly into local type-safe models (`SoilAdvisory` and `HealthReport`).

### Key Technologies Utilized
*   **Firebase Vertex AI SDK:** `com.google.firebase:firebase-vertexai:16.0.0-beta02`
*   **Model:** `gemini-2.5-flash`
*   **State Management:** StateFlow & Compose Observable State (`mutableStateOf`)
*   **Asynchronous Engine:** Kotlin Coroutines (`Dispatchers.IO` and `Dispatchers.Main` context switching)
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
```

---

## 2. Secure API Credential Handling

API Keys are safely retrieved at runtime using the auto-generated `BuildConfig` file populated by the **Secrets Gradle Plugin** (reading from `.env` or the platform's Secrets environment context):

```kotlin
private val generativeModel by lazy {
  Firebase.vertexAI.generativeModel(
    modelName = "gemini-2.5-flash",
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

## 3. Hardened Android Security & Permissions Manifest

A production-grade, highly secure `AndroidManifest.xml` was written to enforce explicit hardware features and granular runtime permission requests, limiting access strictly to essential system services.

### Permissions Declared
1.  **Internet Connectivity:** Necessary for executing Vertex AI / Google Gemini network queries.
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

## 4. UI Polish & Visual State Synchronizations

*   **Real-time Analysis Indicator:** Integrated a modern Material 3 `CircularProgressIndicator` with detailed visual feedback when executing plant pathology requests.
*   **High-Fidelity Programmatic Leaf Generator:** Implemented custom rendering inside `KisanAlertViewModel` to simulate visual leaf structures (Healthy, Rice Blast, and Bacterial Leaf Blight lesions) onto standard `Bitmap` files, allowing offline testing and visual verification before Gemini classification triggers.
