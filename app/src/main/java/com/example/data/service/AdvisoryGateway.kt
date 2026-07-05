package com.example.data.service

import com.example.data.model.AdvisoryCard
import com.example.data.model.HealthReport
import com.example.data.model.SoilAdvisory
import com.example.data.model.SoilAdvisoryRequest
import java.io.File

interface AdvisoryGateway {
  val modeLabel: String

  suspend fun requestSoilAdvisory(request: SoilAdvisoryRequest): SoilAdvisory

  suspend fun requestCropDiagnosis(imageFile: File): HealthReport

  suspend fun requestVoiceAdvisory(queryText: String): AdvisoryCard
}
