package com.example.data.model

data class SoilAdvisoryRequest(
  val soilType: String,
  val nitrogen: String,
  val phosphorus: String,
  val potassium: String,
  val phLevel: Float,
  val weather: String
)
