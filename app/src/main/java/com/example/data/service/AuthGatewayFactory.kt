package com.example.data.service

object AuthGatewayFactory {
  fun create(): AuthGateway = LocalDemoAuthGateway()
}
