package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AlertItem
import com.example.ui.components.DashboardCanvasIllustration
import com.example.ui.components.FarmMetricCard
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun HomeDashboardScreen(viewModel: KisanAlertViewModel) {
  val lang = viewModel.currentLanguage
  var selectedAlertForDialog by remember { mutableStateOf<AlertItem?>(null) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Elegant dynamic Canvas illustration
    item {
      DashboardCanvasIllustration()
    }

    // A. FARM STATUS SECTION
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = LocalizedStrings.get("farm_status", lang).uppercase(),
          color = Color(0xFF52B788), // soft mint
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Card 1: Soil Moisture
          FarmMetricCard(
            modifier = Modifier.weight(1f),
            title = LocalizedStrings.get("moisture", lang),
            value = LocalizedStrings.get("moisture_status", lang),
            icon = Icons.Default.Opacity,
            iconTint = Color(0xFF60A5FA), // High contrast sky blue
            borderColor = Color(0xFF1E3A8A)
          )

          // Card 2: Groundwater Level
          FarmMetricCard(
            modifier = Modifier.weight(1f),
            title = LocalizedStrings.get("groundwater", lang),
            value = LocalizedStrings.get("groundwater_status", lang),
            icon = Icons.Default.Landscape,
            iconTint = Color(0xFF34D399), // Mint green
            borderColor = Color(0xFF065F46)
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Card 3: Weather (Full-Width for balance)
        FarmMetricCard(
          modifier = Modifier.fillMaxWidth(),
          title = LocalizedStrings.get("weather", lang),
          value = LocalizedStrings.get("weather_status", lang) + " (Humidity: 45%)",
          icon = Icons.Default.WbSunny,
          iconTint = Color(0xFFFBBF24), // Gold yellow
          borderColor = Color(0xFF78350F)
        )
      }
    }

    // B. RECENT ALERTS SECTION
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.Warning,
            contentDescription = "Alerts",
            tint = Color(0xFFF59E0B), // Harvest Gold
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = LocalizedStrings.get("recent_alerts", lang),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          viewModel.alertList.forEach { alert ->
            Card(
              colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
              border = BorderStroke(
                1.dp,
                if (alert.type == "VOICE") Color(0xFFF59E0B) else Color(0xFF2D6A4F)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedAlertForDialog = alert }
                .testTag("alert_card_${alert.id}"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Warning icon circle
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .background(
                      if (alert.type == "VOICE") Color(0xFF78350F) else Color(0xFF14532D),
                      shape = CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (alert.type == "VOICE") Icons.Default.PhoneInTalk else Icons.Default.Sms,
                    contentDescription = alert.type,
                    tint = if (alert.type == "VOICE") Color(0xFFFBBF24) else Color(0xFF52B788),
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = alert.title,
                      color = Color.White,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                    Text(
                      text = alert.time,
                      color = Color(0xFF94A3B8),
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = alert.subtitle,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Visual Detail Dialog for emergency broadcasts
  selectedAlertForDialog?.let { alert ->
    Dialog(onDismissRequest = { selectedAlertForDialog = null }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E293B),
        border = BorderStroke(2.dp, Color(0xFF52B788)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (alert.type == "VOICE") Icons.Default.PhoneInTalk else Icons.Default.Sms,
              contentDescription = "Alert Symbol",
              tint = Color(0xFFF59E0B),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Dispatch Verification",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }

          HorizontalDivider(color = Color(0xFF334155))

          Text(
            text = alert.title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "Broadcast Mode: " + if (alert.type == "VOICE") "Automated Regional IVR Voice Call" else "Regional SMS Dispatch",
            color = Color(0xFF52B788),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFF0F172A), shape = RoundedCornerShape(8.dp))
              .padding(12.dp)
          ) {
            Text(
              text = alert.detailScript,
              color = Color(0xFFF1F5F9),
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Text(
            text = "Status: Sent to 14,250 registered farmer nodes in regional cluster. 94.2% delivery success.",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
          )

          Button(
            onClick = { selectedAlertForDialog = null },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
            modifier = Modifier.align(Alignment.End)
          ) {
            Text("Close", color = Color.White)
          }
        }
      }
    }
  }
}
