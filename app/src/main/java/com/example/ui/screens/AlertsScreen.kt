package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun AlertsScreen(viewModel: KisanAlertViewModel) {
  val lang = viewModel.currentLanguage
  val profile = viewModel.farmerProfile
  val page = Color(0xFFF7F4ED)
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Warning, contentDescription = "Alerts", tint = warmAccent)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = LocalizedStrings.get("alerts_screen_title", lang),
            color = brandInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
        Text(
          text = LocalizedStrings.get("alerts_screen_intro", lang),
          color = muted,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
        if (profile != null) {
          Text(
            text = LocalizedStrings.format(
              "alerts_context_hint",
              lang,
              mapOf(
                "district" to profile.district,
                "crop" to profile.primaryCrop.lowercase()
              )
            ),
            color = warmAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    viewModel.alertList.forEach { alert ->
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
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = alert.type,
              color = warmAccent,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = alert.time,
              color = muted,
              fontSize = 12.sp
            )
          }
          Text(
            text = alert.title,
            color = brandInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = alert.subtitle,
            color = muted,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
          Text(
            text = alert.detailScript,
            color = brandInk,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
          Button(
            onClick = { viewModel.onSpeakText?.invoke("${alert.title}. ${alert.detailScript}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9EEF8)),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = "Read alert aloud", tint = brandInk)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = LocalizedStrings.get("alerts_read_aloud", lang),
              color = brandInk,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))
  }
}
