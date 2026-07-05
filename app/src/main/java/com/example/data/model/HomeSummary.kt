package com.example.data.model

data class HomeSummary(
  val headline: String,
  val intro: String,
  val districtTag: String,
  val cropTag: String,
  val weatherValue: String,
  val topAlertTitle: String,
  val supportCopy: String,
  val priorities: List<HomePriority>
)
