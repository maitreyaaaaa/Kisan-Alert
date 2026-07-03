package com.example.ui.components

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

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF1E293B)) // Slate card
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // High-contrast leafy icon representing agriculture and safety
        Box(
          modifier = Modifier
            .size(36.dp)
            .background(Color(0xFF1B4332), shape = CircleShape) // forest green
            .border(2.dp, Color(0xFF52B788), shape = CircleShape), // soft mint
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Agriculture,
            contentDescription = "App Leaf Logo",
            tint = Color(0xFF52B788),
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = LocalizedStrings.get("app_name", selectedLanguage),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.testTag("app_title")
          )
          Text(
            text = LocalizedStrings.get("tagline", selectedLanguage),
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Indicator language selector button (high touch readability)
      Box {
        OutlinedButton(
          onClick = { showLangDropdown = true },
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF52B788)
          ),
          border = borderStrokeForReadability(),
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
          modifier = Modifier.background(Color(0xFF1E293B))
        ) {
          AppLanguage.values().forEach { language ->
            DropdownMenuItem(
              text = {
                Text(
                  text = language.nativeName,
                  color = if (language == selectedLanguage) Color(0xFF52B788) else Color.White,
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
