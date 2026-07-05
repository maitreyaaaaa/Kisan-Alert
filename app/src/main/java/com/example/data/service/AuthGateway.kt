package com.example.data.service

import com.example.data.model.FarmerProfile
import com.example.util.AppLanguage

interface AuthGateway {
  val modeLabel: String

  suspend fun verifyOtp(
    phone: String,
    otp: String,
    farmerName: String,
    district: String,
    language: AppLanguage
  ): FarmerProfile
}
