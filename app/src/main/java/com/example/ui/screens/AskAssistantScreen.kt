package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun AskAssistantScreen(viewModel: KisanAlertViewModel, scope: CoroutineScope) {
  val context = LocalContext.current
  val lang = viewModel.currentLanguage
  val profile = viewModel.farmerProfile
  val page = Color(0xFFF7F4ED)
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
  val brandBlue = Color(0xFF2D4F8F)
  val warmAccent = Color(0xFFF28C38)

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
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = LocalizedStrings.get("ask_title", lang),
          color = brandInk,
          fontSize = 22.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = LocalizedStrings.get("ask_intro", lang),
          color = muted,
          fontSize = 14.sp,
          lineHeight = 20.sp
        )
        if (profile != null) {
          Text(
            text = LocalizedStrings.format(
              "ask_context_hint",
              lang,
              mapOf(
                "crop" to profile.primaryCrop.lowercase(),
                "district" to profile.district
              )
            ),
            color = warmAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp
          )
        }
        Button(
          onClick = { viewModel.startVoiceQuery(scope) },
          colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Mic, contentDescription = "Speak")
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            if (viewModel.isRecordingVoice) {
              LocalizedStrings.get("ask_voice_listening", lang)
            } else {
              LocalizedStrings.get("ask_voice_button", lang)
            }
          )
        }
        OutlinedButton(
          onClick = { viewModel.simulateUploadLeaf("BLAST", context, scope) },
          border = BorderStroke(1.dp, border),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.CameraAlt, contentDescription = "Leaf photo", tint = warmAccent)
          Spacer(modifier = Modifier.width(8.dp))
          Text(LocalizedStrings.get("ask_photo_button", lang), color = brandInk)
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = LocalizedStrings.get("ask_soil_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = LocalizedStrings.get("ask_soil_intro", lang),
          color = muted,
          fontSize = 13.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          SoilPresetButton(
            modifier = Modifier.weight(1f),
            label = LocalizedStrings.get("ask_preset_rice", lang),
            onClick = {
              viewModel.selectedSoilType = "Clayey"
              viewModel.nitrogen = "72"
              viewModel.phosphorus = "42"
              viewModel.potassium = "38"
              viewModel.phLevel = 6.4f
              viewModel.runSoilAdvisory(scope)
            }
          )
          SoilPresetButton(
            modifier = Modifier.weight(1f),
            label = LocalizedStrings.get("ask_preset_cotton", lang),
            onClick = {
              viewModel.selectedSoilType = "Black"
              viewModel.nitrogen = "62"
              viewModel.phosphorus = "36"
              viewModel.potassium = "44"
              viewModel.phLevel = 7.1f
              viewModel.runSoilAdvisory(scope)
            }
          )
          SoilPresetButton(
            modifier = Modifier.weight(1f),
            label = LocalizedStrings.get("ask_preset_pulses", lang),
            onClick = {
              viewModel.selectedSoilType = "Loamy"
              viewModel.nitrogen = "48"
              viewModel.phosphorus = "34"
              viewModel.potassium = "32"
              viewModel.phLevel = 6.8f
              viewModel.runSoilAdvisory(scope)
            }
          )
        }
      }
    }

    viewModel.recordedQueryText?.let { transcript ->
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = LocalizedStrings.get("ask_last_question", lang),
            color = muted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = transcript,
            color = brandInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    if (viewModel.isDiagnosticLoading || viewModel.isAdvisoryLoading) {
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          CircularProgressIndicator(color = brandBlue)
          Text(
            text = LocalizedStrings.get("ask_loading_title", lang),
            color = brandInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = LocalizedStrings.get("ask_loading_copy", lang),
            color = muted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
          )
        }
      }
    }

    viewModel.currentAskCard?.let { advisoryCard ->
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp),
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
            Text(
              text = if (viewModel.diagnosticReport != null) {
                LocalizedStrings.get("ask_crop_answer_title", lang)
              } else if (viewModel.voiceAdvisoryCard != null) {
                LocalizedStrings.get("ask_voice_answer_title", lang)
              } else {
                LocalizedStrings.get("ask_soil_answer_title", lang)
              },
              color = brandInk,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold
            )
            OutlinedButton(
              onClick = { viewModel.speakAdvisoryCard(advisoryCard) },
              border = BorderStroke(1.dp, border)
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Read aloud", tint = brandBlue)
            }
          }
          Text(
            text = advisoryCard.title,
            color = brandInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${advisoryCard.riskLabel} | ${advisoryCard.confidenceLabel}",
            color = warmAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = advisoryCard.summary,
            color = muted,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
          if (advisoryCard.needsHumanReview) {
            Text(
              text = LocalizedStrings.get("ask_review_copy", lang),
              color = Color(0xFFB7492D),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
          HorizontalDivider(color = border)
          advisoryCard.actions.forEach { action ->
            Text(
              text = "- $action",
              color = muted,
              fontSize = 13.sp,
              lineHeight = 18.sp
            )
          }
        }
      }
    }

    if (!viewModel.isDiagnosticLoading &&
      !viewModel.isAdvisoryLoading &&
      viewModel.currentAskCard == null
    ) {
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = LocalizedStrings.get("ask_empty_title", lang),
            color = brandInk,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = LocalizedStrings.get("ask_empty_copy", lang),
            color = muted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
          )
        }
      }
    }
  }
}

@Composable
private fun SoilPresetButton(
  modifier: Modifier = Modifier,
  label: String,
  onClick: () -> Unit
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9EEF8)),
    shape = RoundedCornerShape(12.dp),
    modifier = modifier
  ) {
    Text(
      text = label,
      color = Color(0xFF1F2A44),
      fontWeight = FontWeight.Bold
    )
  }
}
