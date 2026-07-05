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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun HomeDashboardScreen(
  viewModel: KisanAlertViewModel,
  onOpenAsk: () -> Unit,
  onOpenAlerts: () -> Unit,
  onOpenAccount: () -> Unit
) {
  val lang = viewModel.currentLanguage
  val profile = viewModel.farmerProfile
  val homeSummary = viewModel.homeSummary
  val page = Color(0xFFF7F4ED)
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
  val brandBlue = Color(0xFF2D4F8F)
  val warmAccent = Color(0xFFF28C38)
  val success = Color(0xFF4B8B6A)

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
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = LocalizedStrings.get("greeting_short", lang),
          color = warmAccent,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = homeSummary.headline,
          color = brandInk,
          fontSize = 26.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = homeSummary.intro,
          color = muted,
          fontSize = 14.sp,
          lineHeight = 20.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          HomePill(homeSummary.districtTag, brandBlue)
          HomePill(homeSummary.cropTag, success)
        }
      }
    }

    if (profile != null) {
      Card(
        colors = CardDefaults.cardColors(containerColor = card),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          HomeStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Mic,
            iconTint = brandBlue,
            label = LocalizedStrings.get("account_profile_name", lang),
            value = profile.name
          )
          HomeStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.WbSunny,
            iconTint = warmAccent,
            label = LocalizedStrings.get("account_profile_crop", lang),
            value = profile.primaryCrop
          )
        }
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      HomeStatCard(
        modifier = Modifier.weight(1f),
        icon = Icons.Default.WbSunny,
        iconTint = warmAccent,
        label = LocalizedStrings.get("home_weather_label", lang),
        value = homeSummary.weatherValue
      )
      HomeStatCard(
        modifier = Modifier.weight(1f),
        icon = Icons.Default.Warning,
        iconTint = brandBlue,
        label = LocalizedStrings.get("home_top_alert_label", lang),
        value = homeSummary.topAlertTitle
      )
    }

    Card(
      colors = CardDefaults.cardColors(containerColor = card),
      border = BorderStroke(1.dp, border),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(
          text = LocalizedStrings.get("home_next_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        homeSummary.priorities.forEach { item ->
          HomePriorityRow(
            title = item.title,
            detail = item.detail
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = LocalizedStrings.get("home_support_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Text(
          text = homeSummary.supportCopy,
          color = muted,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
        OutlinedButton(
          onClick = onOpenAccount,
          border = BorderStroke(1.dp, border),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = LocalizedStrings.get("home_support_button", lang),
            color = brandInk,
            fontWeight = FontWeight.Bold
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = LocalizedStrings.get("home_actions_title", lang),
          color = brandInk,
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Button(
          onClick = onOpenAsk,
          colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Mic, contentDescription = "Ask by voice")
          Spacer(modifier = Modifier.width(8.dp))
          Text(LocalizedStrings.get("home_action_ask", lang))
        }
        OutlinedButton(
          onClick = onOpenAsk,
          border = BorderStroke(1.dp, border),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.CameraAlt, contentDescription = "Check crop image", tint = brandInk)
          Spacer(modifier = Modifier.width(8.dp))
          Text(LocalizedStrings.get("home_action_photo", lang), color = brandInk)
        }
        OutlinedButton(
          onClick = onOpenAlerts,
          border = BorderStroke(1.dp, border),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Warning, contentDescription = "See alerts", tint = warmAccent)
          Spacer(modifier = Modifier.width(8.dp))
          Text(LocalizedStrings.get("home_action_alerts", lang), color = brandInk)
        }
      }
    }
  }
}

@Composable
private fun HomePill(label: String, color: Color) {
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
private fun HomeStatCard(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  iconTint: Color,
  label: String,
  value: String
) {
  val card = Color(0xFFFFFBF5)
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)

  Card(
    colors = CardDefaults.cardColors(containerColor = card),
    border = BorderStroke(1.dp, border),
    shape = RoundedCornerShape(20.dp),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(icon, contentDescription = label, tint = iconTint)
      Text(
        text = label,
        color = muted,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      )
      Text(
        text = value,
        color = brandInk,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun HomePriorityRow(title: String, detail: String) {
  val border = Color(0xFFE3DDD1)
  val brandInk = Color(0xFF1F2A44)
  val muted = Color(0xFF697586)
  val brandBlue = Color(0xFF2D4F8F)

  Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
    Row(verticalAlignment = Alignment.Top) {
      Text(
        text = "-",
        color = brandBlue,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = title,
          color = brandInk,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = detail,
          color = muted,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }
    }
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(border)
    )
  }
}
