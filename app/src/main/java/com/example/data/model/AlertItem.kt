package com.example.data.model

data class AlertItem(
  val id: Int,
  val title: String,
  val subtitle: String,
  val type: String, // "VOICE", "SMS", "WARNING"
  val time: String,
  val detailScript: String
)
