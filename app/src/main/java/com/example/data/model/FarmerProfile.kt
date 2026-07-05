package com.example.data.model

import com.example.util.AppLanguage

data class FarmerProfile(
  val name: String,
  val phone: String,
  val district: String,
  val preferredLanguage: AppLanguage,
  val trustedHelperPhone: String,
  val primaryCrop: String
)
