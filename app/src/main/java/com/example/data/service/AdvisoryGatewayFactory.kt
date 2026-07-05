package com.example.data.service

object AdvisoryGatewayFactory {
  fun create(apiKey: String): AdvisoryGateway {
    return if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      LocalFallbackAdvisoryGateway()
    } else {
      GeminiAdvisoryGateway(apiKey)
    }
  }
}
