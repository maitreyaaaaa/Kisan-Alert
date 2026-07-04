package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.borderStrokeForReadability
import com.example.util.LocalizedStrings
import com.example.util.AppLanguage
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun SettingsScreen(viewModel: KisanAlertViewModel) {
  val lang = viewModel.currentLanguage
  val softMint = Color(0xFF52B788)
  val cardSlate = Color(0xFF1E293B)
  val darkSlate = Color(0xFF0F172A)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. visual user account header card
    Card(
      colors = CardDefaults.cardColors(containerColor = cardSlate),
      border = BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Icon(
          imageVector = Icons.Default.AccountCircle,
          contentDescription = "User Avatar",
          tint = softMint,
          modifier = Modifier.size(56.dp)
        )
        Column {
          Text(
            text = "Kisan Mobile Account",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "+91 ${viewModel.userPhoneNumber}",
            color = Color(0xFF94A3B8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Status: Smallholder Farmer Profile Verified",
            color = Color(0xFFFBBF24),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // 2. Settings preferences Card
    Card(
      colors = CardDefaults.cardColors(containerColor = cardSlate),
      border = BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "Preferences / प्राथमिकताएं",
          color = softMint,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )

        // Switch row: Auto speak
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = "TTS Icon", tint = Color(0xFF60A5FA))
            Column {
              Text(
                text = "Auto Read-Aloud",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Speak crop advice automatically",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
            }
          }
          Switch(
            checked = viewModel.isAutoSpeakEnabled,
            onCheckedChange = { viewModel.isAutoSpeakEnabled = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = softMint,
              checkedTrackColor = Color(0xFF1B4332),
              uncheckedThumbColor = Color(0xFF94A3B8),
              uncheckedTrackColor = Color(0xFF334155)
            ),
            modifier = Modifier.testTag("auto_speak_switch")
          )
        }

        HorizontalDivider(color = Color(0xFF334155))

        // Switch row: Dark Mode
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Theme Icon", tint = Color(0xFFFBBF24))
            Column {
              Text(
                text = "Dark Mode / डार्क मोड",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Switch between dark and light themes",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
            }
          }
          Switch(
            checked = viewModel.isDarkTheme,
            onCheckedChange = { viewModel.isDarkTheme = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = softMint,
              checkedTrackColor = Color(0xFF1B4332),
              uncheckedThumbColor = Color(0xFF94A3B8),
              uncheckedTrackColor = Color(0xFF334155)
            ),
            modifier = Modifier.testTag("dark_theme_switch")
          )
        }

        HorizontalDivider(color = Color(0xFF334155))

        // Language Select Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = "Language icon", tint = Color(0xFFFBBF24))
            Text(
              text = "App Language",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }

          // Select boxes
          var expandedLanguageMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
          Box {
            Button(
              onClick = { expandedLanguageMenu = true },
              colors = ButtonDefaults.buttonColors(containerColor = darkSlate),
              border = borderStrokeForReadability(),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(36.dp)
            ) {
              Text(viewModel.currentLanguage.nativeName, fontSize = 12.sp, color = Color.White)
            }
            DropdownMenu(
              expanded = expandedLanguageMenu,
              onDismissRequest = { expandedLanguageMenu = false },
              modifier = Modifier.background(cardSlate)
            ) {
              AppLanguage.values().forEach { language ->
                DropdownMenuItem(
                  text = { Text(language.nativeName, color = Color.White, fontSize = 13.sp) },
                  onClick = {
                    viewModel.currentLanguage = language
                    expandedLanguageMenu = false
                  }
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    // 3. Log out button
    Button(
      onClick = { viewModel.logout() },
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D)), // Dark Red
      border = borderStrokeForReadability(),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("logout_button"),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(Icons.Default.Logout, contentDescription = "Log out", tint = Color.White)
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Sign Out from Account",
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
