package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.ui.components.borderStrokeForReadability
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CropRecommendationScreen(viewModel: KisanAlertViewModel, scope: CoroutineScope) {
  val lang = viewModel.currentLanguage
  val soilTypes = listOf("Clayey", "Sandy", "Loamy", "Red", "Black")
  var showSoilDropdown by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Soil Parameters Form Card
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
      border = BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Info, contentDescription = "Soil Parameters", tint = Color(0xFF52B788))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = LocalizedStrings.get("soil_params", lang),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }

        HorizontalDivider(color = Color(0xFF334155))

        // Soil Type Input (High contrast touch dropdown)
        Text(
          text = LocalizedStrings.get("soil_type", lang),
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )

        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = viewModel.selectedSoilType,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showSoilDropdown = true }
              .testTag("soil_type_dropdown"),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
              disabledTextColor = Color.White,
              disabledBorderColor = Color(0xFF52B788),
              disabledTrailingIconColor = Color(0xFF52B788)
            ),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown") }
          )

          DropdownMenu(
            expanded = showSoilDropdown,
            onDismissRequest = { showSoilDropdown = false },
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .background(Color(0xFF1E293B))
          ) {
            soilTypes.forEach { type ->
              DropdownMenuItem(
                text = { Text(type, color = Color.White, fontSize = 16.sp) },
                onClick = {
                  viewModel.selectedSoilType = type
                  showSoilDropdown = false
                }
              )
            }
          }
        }

        // NPK Inputs (Side-by-side with high target sizing)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = viewModel.nitrogen,
            onValueChange = { viewModel.nitrogen = it },
            label = { Text(LocalizedStrings.get("nitrogen", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("nitrogen_input")
          )

          OutlinedTextField(
            value = viewModel.phosphorus,
            onValueChange = { viewModel.phosphorus = it },
            label = { Text(LocalizedStrings.get("phosphorus", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("phosphorus_input")
          )

          OutlinedTextField(
            value = viewModel.potassium,
            onValueChange = { viewModel.potassium = it },
            label = { Text(LocalizedStrings.get("potassium", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("potassium_input")
          )
        }

        // pH Level Slider Input (large and visual)
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = LocalizedStrings.get("ph_level", lang),
              color = Color(0xFF94A3B8),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = String.format("%.1f", viewModel.phLevel),
              color = Color(0xFF52B788),
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
          Slider(
            value = viewModel.phLevel,
            onValueChange = { viewModel.phLevel = it },
            valueRange = 4.0f..9.0f,
            colors = SliderDefaults.colors(
              thumbColor = Color(0xFF52B788),
              activeTrackColor = Color(0xFF52B788),
              inactiveTrackColor = Color(0xFF334155)
            ),
            modifier = Modifier.testTag("ph_slider")
          )
        }

        // ACTION BUTTON
        Button(
          onClick = { viewModel.runSoilAdvisory(scope) },
          enabled = !viewModel.isAdvisoryLoading,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1B4332), // Forest green
            contentColor = Color.White
          ),
          border = borderStrokeForReadability(),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("run_advisory_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (viewModel.isAdvisoryLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Analyzing Soil...", fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.Agriculture, contentDescription = "Run Advisory", tint = Color(0xFFFBBF24))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = LocalizedStrings.get("run_advisory", lang),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp
            )
          }
        }
      }
    }

    // AI Crop Recommendation Placeholder Output Container
    Column {
      Text(
        text = LocalizedStrings.get("advisory_result", lang).uppercase(),
        color = Color(0xFF52B788),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
      )

      AnimatedVisibility(
        visible = viewModel.advisoryResult != null,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut()
      ) {
        viewModel.advisoryResult?.let { advisory ->
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(2.dp, Color(0xFF52B788)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Match Success",
                    tint = Color(0xFF52B788),
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = advisory.cropName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }
                Box(
                  modifier = Modifier
                    .background(Color(0xFF14532D), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = advisory.matchPercentage,
                    color = Color(0xFF52B788),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Text(
                text = "Optimal Planting Window: ${advisory.plantingWindow}",
                color = Color(0xFFFBBF24), // Harvest gold highlight
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )

              Text(
                text = advisory.details,
                color = Color(0xFFF1F5F9),
                fontSize = 14.sp
              )

              HorizontalDivider(color = Color(0xFF334155))

              Text(
                text = "Scientific Local Guidelines:",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )

              advisory.guidelines.forEachIndexed { i, rule ->
                Row(modifier = Modifier.fillMaxWidth()) {
                  Text(
                    text = "${i + 1}. ",
                    color = Color(0xFF52B788),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                  Text(
                    text = rule,
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))
              
              // Educational block demonstrating API integration
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color(0xFF0F172A), shape = RoundedCornerShape(8.dp))
                  .padding(10.dp)
              ) {
                Column {
                  Text(
                    text = "Developer Integration Note:",
                    color = Color(0xFF52B788),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "/* Gemini API Hook:\nIn production, we package Soil Type (${viewModel.selectedSoilType}), N: ${viewModel.nitrogen}, P: ${viewModel.phosphorus}, K: ${viewModel.potassium}, and pH: ${viewModel.phLevel} into a structured prompt and invoke the Gemini-2.5-flash model via Retrofit/Ktor to generate dynamic localized advisory summaries in the selected regional dialect. */",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                  )
                }
              }
            }
          }
        }
      }

      if (viewModel.advisoryResult == null) {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          border = BorderStroke(1.dp, Color(0xFF1E293B)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              Icons.Default.Agriculture,
              contentDescription = "Empty Advisory Indicator",
              tint = Color(0xFF334155),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = LocalizedStrings.get("advisory_placeholder", lang),
              color = Color(0xFF94A3B8),
              fontSize = 13.sp,
              textAlign = TextAlign.Center,
              lineHeight = 18.sp
            )
          }
        }
      }
    }
  }
}
