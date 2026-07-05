package com.example.data.model

data class AdvisoryCard(
  val title: String,
  val summary: String,
  val actions: List<String>,
  val riskLabel: String,
  val needsHumanReview: Boolean,
  val confidenceLabel: String
)
