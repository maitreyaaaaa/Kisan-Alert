package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LoginScreen(viewModel: KisanAlertViewModel, scope: CoroutineScope) {
  val lang = viewModel.currentLanguage
  var farmerName by remember { mutableStateOf("") }
  var district by remember { mutableStateOf("Anantapur") }
  var phoneNumber by remember { mutableStateOf("") }
  var otpCode by remember { mutableStateOf("") }
  var isOtpSent by remember { mutableStateOf(false) }

  val page = Color(0xFFF7F4ED)
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
  val brandBlue = Color(0xFF2D4F8F)
  val warmAccent = Color(0xFFF28C38)
  val success = Color(0xFF4B8B6A)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(page)
      .verticalScroll(rememberScrollState())
      .padding(20.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(18.dp),
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = LocalizedStrings.get("login_eyebrow", lang),
            color = warmAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = LocalizedStrings.get("app_name", lang),
            color = brandInk,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = LocalizedStrings.get("login_intro", lang),
            color = muted,
            fontSize = 14.sp,
            lineHeight = 20.sp
          )

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LoginPill(LocalizedStrings.get("login_pill_shared", lang), brandBlue)
            LoginPill(LocalizedStrings.get("login_pill_simple", lang), success)
          }
        }
      }

      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Text(
            text = if (isOtpSent) {
              LocalizedStrings.get("login_step_2", lang)
            } else {
              LocalizedStrings.get("login_step_1", lang)
            },
            color = warmAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = if (isOtpSent) {
              LocalizedStrings.get("login_title_otp", lang)
            } else {
              LocalizedStrings.get("login_title_phone", lang)
            },
            color = brandInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
          )

          Text(
            text = if (isOtpSent) {
              LocalizedStrings.get("login_copy_otp", lang)
            } else {
              LocalizedStrings.get("login_copy_phone", lang)
            },
            color = muted,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )

          if (!isOtpSent) {
            OutlinedTextField(
              value = farmerName,
              onValueChange = { farmerName = it },
              placeholder = { Text(LocalizedStrings.get("login_name_placeholder", lang), color = muted) },
              singleLine = true,
              colors = loginFieldColors(card, border, brandInk, muted, brandBlue),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = district,
              onValueChange = { district = it },
              placeholder = { Text(LocalizedStrings.get("login_district_placeholder", lang), color = muted) },
              singleLine = true,
              colors = loginFieldColors(card, border, brandInk, muted, brandBlue),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = phoneNumber,
              onValueChange = { if (it.length <= 10) phoneNumber = it.filter(Char::isDigit) },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = brandBlue) },
              placeholder = { Text(LocalizedStrings.get("login_phone_placeholder", lang), color = muted) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              colors = loginFieldColors(card, border, brandInk, muted, brandBlue),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("phone_input_field")
            )

            Button(
              onClick = { if (phoneNumber.length == 10 && farmerName.isNotBlank()) isOtpSent = true },
              enabled = phoneNumber.length == 10 && farmerName.isNotBlank() && !viewModel.isAuthLoading,
              colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("request_otp_button")
            ) {
              Text(LocalizedStrings.get("login_send_otp", lang), fontWeight = FontWeight.Bold, color = Color.White)
            }
          } else {
            OutlinedTextField(
              value = otpCode,
              onValueChange = { if (it.length <= 4) otpCode = it.filter(Char::isDigit) },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "OTP", tint = brandBlue) },
              placeholder = { Text(LocalizedStrings.get("login_otp_placeholder", lang), color = muted) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              colors = loginFieldColors(card, border, brandInk, muted, brandBlue),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("otp_input_field")
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = {
                  isOtpSent = false
                  otpCode = ""
                },
                border = BorderStroke(1.dp, border),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(50.dp)
              ) {
                Text(LocalizedStrings.get("login_edit_number", lang), color = brandInk, fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = {
                  viewModel.verifyPhoneAndLogin(
                    phone = phoneNumber,
                    otp = otpCode,
                    farmerName = farmerName,
                    district = district,
                    scope = scope
                  )
                },
                enabled = otpCode.length == 4 && !viewModel.isAuthLoading,
                colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .weight(1.2f)
                  .height(50.dp)
                  .testTag("verify_otp_button")
              ) {
                if (viewModel.isAuthLoading) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.height(20.dp))
                } else {
                  Text(LocalizedStrings.get("login_verify_continue", lang), fontWeight = FontWeight.Bold, color = Color.White)
                }
              }
            }
          }

          Text(
            text = LocalizedStrings.get("login_demo_otp", lang),
            color = warmAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )

          viewModel.authErrorMessage?.let { err ->
            Text(
              text = err,
              color = Color(0xFFB7492D),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              textAlign = TextAlign.Start
            )
          }
        }
      }
    }
  }
}

@Composable
private fun LoginPill(label: String, color: Color) {
  Text(
    text = label,
    color = color,
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    modifier = Modifier
      .background(color.copy(alpha = 0.10f), RoundedCornerShape(999.dp))
      .padding(horizontal = 10.dp, vertical = 6.dp)
  )
}

@Composable
private fun loginFieldColors(
  card: Color,
  border: Color,
  brandInk: Color,
  muted: Color,
  brandBlue: Color
) = OutlinedTextFieldDefaults.colors(
  focusedTextColor = brandInk,
  unfocusedTextColor = brandInk,
  focusedContainerColor = card,
  unfocusedContainerColor = card,
  focusedBorderColor = brandBlue,
  unfocusedBorderColor = border,
  focusedPlaceholderColor = muted,
  unfocusedPlaceholderColor = muted
)
