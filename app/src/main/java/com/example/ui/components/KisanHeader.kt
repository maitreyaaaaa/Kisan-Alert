package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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

@Composable
fun KisanHeader(
  selectedLanguage: AppLanguage,
  onLanguageSelected: (AppLanguage) -> Unit
) {
  var showLangDropdown by remember { mutableStateOf(false) }
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val brandBlue = Color(0xFF2D4F8F)
  val muted = Color(0xFF6F7A8C)
  val warmAccent = Color(0xFFF28C38)

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(card)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .background(Color(0xFFE9EEF8), shape = CircleShape)
            .border(1.dp, border, shape = CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Agriculture,
            contentDescription = "App logo",
            tint = brandBlue,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = LocalizedStrings.get("app_name", selectedLanguage),
            color = brandInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.testTag("app_title")
          )
          Text(
            text = LocalizedStrings.get("tagline", selectedLanguage),
            color = muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Box {
        OutlinedButton(
          onClick = { showLangDropdown = true },
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = brandInk),
          border = BorderStroke(1.dp, border),
          modifier = Modifier.testTag("language_selector")
        ) {
          Icon(Icons.Default.Language, contentDescription = "Language", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = selectedLanguage.nativeName,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
        }

        DropdownMenu(
          expanded = showLangDropdown,
          onDismissRequest = { showLangDropdown = false },
          modifier = Modifier.background(card)
        ) {
          AppLanguage.values().forEach { language ->
            DropdownMenuItem(
              text = {
                Text(
                  text = language.nativeName,
                  color = if (language == selectedLanguage) warmAccent else brandInk,
                  fontWeight = if (language == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 14.sp
                )
              },
              onClick = {
                onLanguageSelected(language)
                showLangDropdown = false
              }
            )
          }
        }
      }
    }
  }
}
