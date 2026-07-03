package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SoilAdvisory(
  val cropName: String,
  val matchPercentage: String,
  val plantingWindow: String,
  val details: String,
  val guidelines: List<String>
)
