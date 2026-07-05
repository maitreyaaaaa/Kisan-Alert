package com.example.data.service

import com.example.data.model.FarmerProfile
import com.example.util.AppLanguage

class LocalDemoAuthGateway : AuthGateway {
  override val modeLabel: String = "local_demo_otp"

  override suspend fun verifyOtp(
    phone: String,
    otp: String,
    farmerName: String,
    district: String,
    language: AppLanguage
  ): FarmerProfile {
    if (phone.length != 10 || otp != "1234") {
      throw IllegalArgumentException("Use the demo OTP 1234 for now.")
    }

    val normalizedName = farmerName.ifBlank { "Farmer" }
    val normalizedDistrict = district.ifBlank { "Anantapur" }
    val primaryCrop = when (normalizedDistrict.lowercase()) {
      "anantapur" -> "Groundnut"
      "guntur" -> "Chilli"
      "nizamabad" -> "Turmeric"
      "mysuru" -> "Sugarcane"
      else -> "Millet"
    }

    return FarmerProfile(
      name = normalizedName,
      phone = phone,
      district = normalizedDistrict,
      preferredLanguage = language,
      trustedHelperPhone = "+919000011223",
      primaryCrop = primaryCrop
    )
  }
}
