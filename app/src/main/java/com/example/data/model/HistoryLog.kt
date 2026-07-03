package com.example.data.model

data class HistoryLog(
  val id: Int,
  val title: String,
  val type: String, // "CALL", "SMS"
  val status: String, // "Completed", "Delivered", "Failed"
  val time: String,
  val summary: String
)
