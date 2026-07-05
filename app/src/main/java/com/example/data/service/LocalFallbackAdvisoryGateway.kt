package com.example.data.service

import com.example.data.model.AdvisoryCard
import com.example.data.model.HealthReport
import com.example.data.model.SoilAdvisory
import com.example.data.model.SoilAdvisoryRequest
import java.io.File

class LocalFallbackAdvisoryGateway : AdvisoryGateway {
  override val modeLabel: String = "local_fallback"

  override suspend fun requestSoilAdvisory(request: SoilAdvisoryRequest): SoilAdvisory {
    val cropName = when {
      request.soilType.equals("Clayey", ignoreCase = true) -> "Paddy"
      request.soilType.equals("Black", ignoreCase = true) -> "Cotton"
      request.soilType.equals("Loamy", ignoreCase = true) -> "Pulses"
      else -> "Millet"
    }

    return SoilAdvisory(
      cropName = cropName,
      matchPercentage = "Prototype Match",
      plantingWindow = "Review This Week",
      details = "This answer is coming from the local fallback advisory path. It keeps the app usable while the production advisory service is still being built.",
      guidelines = listOf(
        "Cross-check this advice with district conditions before spending on inputs.",
        "Use the voice or image flow if the field symptoms are changing quickly.",
        "Move to a server-side agronomy service before calling this production ready."
      )
    )
  }

  override suspend fun requestCropDiagnosis(imageFile: File): HealthReport {
    val imageName = imageFile.name.lowercase()
    return when {
      "blast" in imageName -> HealthReport(
        diseaseName = "Rice Blast (Fallback Detection)",
        severityPercent = 0.38f,
        remediesList = listOf(
          "Inspect nearby leaves before spraying the whole field.",
          "Reduce standing water and improve airflow first.",
          "Use local agronomy review if spread increases by tomorrow."
        ),
        imageType = "BLAST"
      )
      "blight" in imageName -> HealthReport(
        diseaseName = "Bacterial Leaf Blight (Fallback Detection)",
        severityPercent = 0.65f,
        remediesList = listOf(
          "Drain the patch and avoid fresh nitrogen for now.",
          "Isolate the wettest area for closer checking.",
          "Escalate to a trusted agriculture advisor if symptoms spread across rows."
        ),
        imageType = "BLIGHT"
      )
      else -> HealthReport(
        diseaseName = "Healthy Leaf (Fallback Detection)",
        severityPercent = 0.0f,
        remediesList = listOf(
          "Keep monitoring the same patch over the next two days.",
          "Use a new image only if symptoms spread.",
          "Treat this as a prototype answer until the production advisory service is live."
        ),
        imageType = "HEALTHY"
      )
    }
  }

  override suspend fun requestVoiceAdvisory(queryText: String): AdvisoryCard {
    return AdvisoryCard(
      title = "Next step for your question",
      summary = "Start with one field check before spending money on treatment or fertilizer for: $queryText",
      actions = listOf(
        "Start with one local field check before spending on treatment.",
        "Use a crop image if the issue is visual or spreading.",
        "Show the result to a trusted helper if the recommendation feels risky."
      ),
      riskLabel = "Plan before spending",
      needsHumanReview = true,
      confidenceLabel = "Prototype guidance"
    )
  }
}
