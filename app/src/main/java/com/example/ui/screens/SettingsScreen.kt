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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AppLanguage
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun SettingsScreen(viewModel: KisanAlertViewModel) {
  val lang = viewModel.currentLanguage
  val profile = viewModel.farmerProfile
  val page = Color(0xFFF7F4ED)
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
  val brandBlue = Color(0xFF2D4F8F)
  val warmAccent = Color(0xFFF28C38)
  var expandedLanguageMenu by remember { mutableStateOf(false) }
  var profileName by remember(profile?.name) { mutableStateOf(profile?.name ?: "") }
  var district by remember(profile?.district) { mutableStateOf(profile?.district ?: "") }
  var primaryCrop by remember(profile?.primaryCrop) { mutableStateOf(profile?.primaryCrop ?: "") }
  var helperPhone by remember(profile?.trustedHelperPhone) { mutableStateOf(profile?.trustedHelperPhone ?: "") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(page)
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = card),
      border = BorderStroke(1.dp, border),
      shape = RoundedCornerShape(24.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Box(
          modifier = Modifier
            .size(56.dp)
            .background(Color(0xFFE9EEF8), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Account",
            tint = brandBlue,
            modifier = Modifier.size(34.dp)
          )
        }
        Column {
          Text(
            text = LocalizedStrings.get("account_title", lang),
            color = brandInk,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = if (profile == null) {
              LocalizedStrings.get("account_not_signed_in", lang)
            } else {
              "+91 ${profile.phone}"
            },
            color = muted,
            fontSize = 14.sp
          )
          Text(
            text = LocalizedStrings.get("account_intro", lang),
            color = muted,
            fontSize = 12.sp
          )
        }
      }
    }

    Card(
      colors = CardDefaults.cardColors(containerColor = card),
      border = BorderStroke(1.dp, border),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = LocalizedStrings.get("account_language_voice_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = "Voice", tint = brandBlue)
            Column {
              Text(
                text = LocalizedStrings.get("account_voice_title", lang),
                color = brandInk,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = LocalizedStrings.get("account_voice_copy", lang),
                color = muted,
                fontSize = 12.sp
              )
            }
          }
          Switch(
            checked = viewModel.isAutoSpeakEnabled,
            onCheckedChange = { viewModel.setAutoSpeakEnabled(it) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = brandBlue,
              uncheckedThumbColor = Color.White,
              uncheckedTrackColor = Color(0xFFC7CFDB)
            ),
            modifier = Modifier.testTag("auto_speak_switch")
          )
        }

        HorizontalDivider(color = border)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = "Language", tint = warmAccent)
            Column {
              Text(
                text = LocalizedStrings.get("account_language_title", lang),
                color = brandInk,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = LocalizedStrings.get("account_language_copy", lang),
                color = muted,
                fontSize = 12.sp
              )
            }
          }
          Box {
            Button(
              onClick = { expandedLanguageMenu = true },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9EEF8)),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = viewModel.currentLanguage.nativeName,
                color = brandInk,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
            DropdownMenu(
              expanded = expandedLanguageMenu,
              onDismissRequest = { expandedLanguageMenu = false },
              modifier = Modifier.background(card)
            ) {
              AppLanguage.values().forEach { language ->
                DropdownMenuItem(
                  text = { Text(language.nativeName, color = brandInk, fontSize = 13.sp) },
                  onClick = {
                    viewModel.setCurrentLanguage(language)
                    expandedLanguageMenu = false
                  }
                )
              }
            }
          }
        }
      }
    }

    Card(
      colors = CardDefaults.cardColors(containerColor = card),
      border = BorderStroke(1.dp, border),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = LocalizedStrings.get("account_profile_name", lang),
          color = brandInk,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = profileName,
          onValueChange = { profileName = it },
          singleLine = true,
          enabled = profile != null,
          colors = settingsFieldColors(card, border, brandInk, muted, brandBlue),
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = LocalizedStrings.get("account_profile_district", lang),
          color = brandInk,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = district,
          onValueChange = { district = it },
          singleLine = true,
          enabled = profile != null,
          colors = settingsFieldColors(card, border, brandInk, muted, brandBlue),
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = LocalizedStrings.get("account_profile_crop", lang),
          color = brandInk,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = primaryCrop,
          onValueChange = { primaryCrop = it },
          singleLine = true,
          enabled = profile != null,
          colors = settingsFieldColors(card, border, brandInk, muted, brandBlue),
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = LocalizedStrings.get("account_helper_label", lang),
          color = brandInk,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = helperPhone,
          onValueChange = { helperPhone = it },
          singleLine = true,
          enabled = profile != null,
          colors = settingsFieldColors(card, border, brandInk, muted, brandBlue),
          modifier = Modifier.fillMaxWidth()
        )
        if (profile != null) {
          Button(
            onClick = {
              viewModel.updateFarmerProfile(
                name = profileName,
                district = district,
                primaryCrop = primaryCrop,
                trustedHelperPhone = helperPhone
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9EEF8)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = LocalizedStrings.get("account_save_profile", lang),
              color = brandInk,
              fontWeight = FontWeight.Bold
            )
          }
        }
        HorizontalDivider(color = border)
        Text(
          text = LocalizedStrings.get("account_support_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = LocalizedStrings.get("account_support_copy", lang),
          color = muted,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
        Text(
          text = LocalizedStrings.get("account_support_warning", lang),
          color = warmAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
      onClick = { viewModel.logout() },
      colors = ButtonDefaults.buttonColors(containerColor = brandInk),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("logout_button"),
      shape = RoundedCornerShape(14.dp)
    ) {
      Icon(Icons.Default.Logout, contentDescription = "Log out", tint = Color.White)
      Text(
        text = LocalizedStrings.get("account_sign_out", lang),
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp)
      )
    }
  }
}

@Composable
private fun settingsFieldColors(
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
  disabledTextColor = muted,
  disabledContainerColor = card,
  focusedBorderColor = brandBlue,
  unfocusedBorderColor = border,
  disabledBorderColor = border
)
