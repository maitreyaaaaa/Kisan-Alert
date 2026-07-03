package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthReport(
  val diseaseName: String,
  val severityPercent: Float,
  val remediesList: List<String>,
  val imageType: String // "HEALTHY", "BLAST", "BLIGHT"
)
