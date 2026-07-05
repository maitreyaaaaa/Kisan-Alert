package com.example.data.service

import com.example.data.model.AdvisoryCard
import com.example.data.model.HealthReport
import com.example.data.model.SoilAdvisory
import com.example.data.model.SoilAdvisoryRequest
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class GeminiAdvisoryGateway(
  apiKey: String
) : AdvisoryGateway {
  override val modeLabel: String = "direct_gemini_prototype"

  private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  private val generativeModel = GenerativeModel(
    modelName = "gemini-2.5-flash",
    apiKey = apiKey,
    generationConfig = generationConfig {
      responseMimeType = "application/json"
    }
  )

  override suspend fun requestSoilAdvisory(request: SoilAdvisoryRequest): SoilAdvisory {
    val prompt = """
      You are an expert agronomist. Analyze the following soil and environmental conditions and recommend the most suitable crop:
      - Soil Type: ${request.soilType}
      - Nitrogen (N): ${request.nitrogen} ppm
      - Phosphorus (P): ${request.phosphorus} ppm
      - Potassium (K): ${request.potassium} ppm
      - Soil pH: ${request.phLevel}
      - Current/Forecasted Weather: ${request.weather}
      
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
    return moshi.adapter(SoilAdvisory::class.java).fromJson(cleanJson)
      ?: throw Exception("Unable to parse soil advisory response")
  }

  override suspend fun requestCropDiagnosis(imageFile: File): HealthReport {
    val bitmap = android.graphics.BitmapFactory.decodeFile(imageFile.absolutePath)
      ?: throw Exception("Failed to decode image file")

    val promptText = """
      Analyze this crop leaf image. Identify any disease, assess the severity, and recommend organic/chemical remedies.
      
      Return a JSON object matching this schema:
      {
        "diseaseName": "The detected disease or 'Healthy Leaf'",
        "severityPercent": 0.0,
        "remediesList": ["Remedy 1", "Remedy 2", "Remedy 3"],
        "imageType": "HEALTHY" or "BLAST" or "BLIGHT"
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
    return moshi.adapter(HealthReport::class.java).fromJson(cleanJson)
      ?: throw Exception("Unable to parse crop diagnosis response")
  }

  override suspend fun requestVoiceAdvisory(queryText: String): AdvisoryCard {
    val prompt = """
      Answer the following farmer spoken query in a short, farmer-safe format:
      "$queryText"
      
      Return a JSON object matching this schema:
      {
        "title": "Short title for the next step",
        "summary": "One short summary grounded in the likely next safe action",
        "actions": ["Action 1", "Action 2", "Action 3"],
        "riskLabel": "Plan before spending" or "Low risk" or "Medium risk" or "High risk",
        "needsHumanReview": true,
        "confidenceLabel": "Prototype guidance" or "Pattern-based guidance" or "Early guidance"
      }
      
      Ensure the response contains ONLY the valid raw JSON object. Do not wrap it in markdown code blocks or add any other text.
    """.trimIndent()

    val response = generativeModel.generateContent(prompt)
    val responseText = response.text?.trim() ?: throw Exception("Empty response from Gemini")
    val cleanJson = extractJsonBlock(responseText)
    return moshi.adapter(AdvisoryCard::class.java).fromJson(cleanJson)
      ?: throw Exception("Unable to parse voice advisory response")
  }

  private fun extractJsonBlock(rawInput: String): String {
    val regex = Regex("""\{.*\}""", RegexOption.DOT_MATCHES_ALL)
    val matchResult = regex.find(rawInput)
    return matchResult?.value ?: rawInput
  }
}
