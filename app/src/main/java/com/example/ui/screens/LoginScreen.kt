package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.borderStrokeForReadability
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LoginScreen(viewModel: KisanAlertViewModel, scope: CoroutineScope) {
  val lang = viewModel.currentLanguage
  var phoneNumber by remember { mutableStateOf("") }
  var otpCode by remember { mutableStateOf("") }
  var isOtpSent by remember { mutableStateOf(false) }

  val softMint = Color(0xFF52B788)
  val darkSlate = Color(0xFF0F172A)
  val cardSlate = Color(0xFF1E293B)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(darkSlate)
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      // 1. Beautiful visual Canvas illustration representing a farm (high-contrast, highly appealing)
      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
          .border(borderStrokeForReadability(), RoundedCornerShape(16.dp))
      ) {
        val width = size.width
        val height = size.height

        // Draw sky gradient
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
          ),
          size = size
        )

        // Draw golden sun
        drawCircle(
          color = Color(0xFFF59E0B),
          radius = 35f,
          center = Offset(width * 0.7f, height * 0.4f)
        )

        // Draw farm hills using path
        val hillPath1 = Path().apply {
          moveTo(0f, height * 0.9f)
          quadraticTo(width * 0.3f, height * 0.6f, width, height * 0.8f)
          lineTo(width, height)
          lineTo(0f, height)
          close()
        }
        drawPath(
          path = hillPath1,
          color = Color(0xFF1B4332)
        )

        val hillPath2 = Path().apply {
          moveTo(0f, height * 0.95f)
          quadraticTo(width * 0.6f, height * 0.7f, width, height * 0.9f)
          lineTo(width, height)
          lineTo(0f, height)
          close()
        }
        drawPath(
          path = hillPath2,
          color = Color(0xFF2D6A4F)
        )

        // Draw visual rays
        drawCircle(
          color = Color(0xFF10B981),
          radius = 12f,
          center = Offset(width * 0.2f, height * 0.85f)
        )
      }

      // App Title & Tagline
      Text(
        text = LocalizedStrings.get("app_name", lang),
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Text(
        text = "Simplified Secure Sign-In for Smallholders",
        color = Color(0xFF94A3B8),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
      )

      // Main Card for Login Inputs
      Card(
        colors = CardDefaults.cardColors(containerColor = cardSlate),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(borderStrokeForReadability(), RoundedCornerShape(16.dp))
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (!isOtpSent) {
            // Step 1: Mobile Number Input
            Text(
              text = "Enter Mobile Number",
              color = Color.White,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
              value = phoneNumber,
              onValueChange = { if (it.length <= 10) phoneNumber = it },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone icon", tint = softMint) },
              placeholder = { Text("10-digit mobile number", color = Color(0xFF64748B)) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkSlate,
                unfocusedContainerColor = darkSlate,
                focusedBorderColor = softMint,
                unfocusedBorderColor = Color(0xFF475569)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("phone_input_field")
            )

            // Submit Button
            Button(
              onClick = {
                if (phoneNumber.length == 10) {
                  isOtpSent = true
                }
              },
              enabled = phoneNumber.length == 10 && !viewModel.isAuthLoading,
              colors = ButtonDefaults.buttonColors(
                containerColor = softMint,
                disabledContainerColor = Color(0xFF334155)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("request_otp_button"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Request OTP via SMS", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          } else {
            // Step 2: OTP Verification Input
            Text(
              text = "Enter 4-Digit OTP sent to +91 $phoneNumber",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
              value = otpCode,
              onValueChange = { if (it.length <= 4) otpCode = it },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "OTP lock icon", tint = softMint) },
              placeholder = { Text("Enter OTP (e.g. 1234)", color = Color(0xFF64748B)) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkSlate,
                unfocusedContainerColor = darkSlate,
                focusedBorderColor = softMint,
                unfocusedBorderColor = Color(0xFF475569)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("otp_input_field")
            )

            // Verification Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Back/Edit Button
              OutlinedButton(
                onClick = { isOtpSent = false; otpCode = "" },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = borderStrokeForReadability(),
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Edit Phone")
              }

              // Verify Button
              Button(
                onClick = { viewModel.verifyPhoneAndLogin(phoneNumber, otpCode, scope) },
                enabled = otpCode.length >= 4 && !viewModel.isAuthLoading,
                colors = ButtonDefaults.buttonColors(containerColor = softMint),
                modifier = Modifier
                  .weight(1.2f)
                  .height(48.dp)
                  .testTag("verify_otp_button"),
                shape = RoundedCornerShape(12.dp)
              ) {
                if (viewModel.isAuthLoading) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                  Text("Verify & Login", fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          // Error Messages display
          viewModel.authErrorMessage?.let { err ->
            Text(
              text = err,
              color = Color(0xFFEF4444),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(top = 8.dp),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}
