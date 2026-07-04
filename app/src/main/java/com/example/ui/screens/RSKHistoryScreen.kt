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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Sms
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
import com.example.data.model.HistoryLog
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel

@Composable
fun RSKHistoryScreen(viewModel: KisanAlertViewModel) {
  val lang = viewModel.currentLanguage
  var selectedLogForDialog by remember { mutableStateOf<HistoryLog?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // RSK Information outreach banner
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1B4332)), // forest green
      border = BorderStroke(2.dp, Color(0xFF52B788)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.PhoneInTalk, contentDescription = "Call center", tint = Color(0xFFFBBF24))
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = LocalizedStrings.get("rsk_banner_title", lang),
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = LocalizedStrings.get("rsk_banner_desc", lang),
          color = Color(0xFFD8F3DC),
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }
    }

    // Call / SMS Log List
    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(viewModel.historyLogs) { log ->
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          border = BorderStroke(1.dp, Color(0xFF334155)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedLogForDialog = log }
            .testTag("history_log_${log.id}"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Icon representing channel
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(
                  if (log.type == "CALL") Color(0xFF14532D) else Color(0xFF1E3A8A),
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (log.type == "CALL") Icons.Default.Call else Icons.Default.Sms,
                contentDescription = log.type,
                tint = if (log.type == "CALL") Color(0xFF52B788) else Color(0xFF60A5FA),
                modifier = Modifier.size(18.dp)
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
                  text = log.title,
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = log.time,
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = log.summary,
                  color = Color(0xFF94A3B8),
                  fontSize = 12.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f)
                )
                Box(
                  modifier = Modifier
                    .background(Color(0xFF0F172A), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = log.status,
                    color = if (log.status == "Completed" || log.status == "Delivered") Color(0xFF52B788) else Color(0xFFFBBF24),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }

    // Historical dispatch detail dialogue
    selectedLogForDialog?.let { log ->
      Dialog(onDismissRequest = { selectedLogForDialog = null }) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFF1E293B),
          border = BorderStroke(2.dp, Color(0xFF52B788)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (log.type == "CALL") Icons.Default.Call else Icons.Default.Sms,
                contentDescription = "Log Channel",
                tint = Color(0xFF52B788),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Dispatch Report Details",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }

            HorizontalDivider(color = Color(0xFF334155))

            Text(
              text = log.title,
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )

            Text(
              text = "System Dispatch Timestamp: ${log.time}",
              color = Color(0xFF94A3B8),
              fontSize = 12.sp
            )

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A), shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
            ) {
              Text(
                text = log.summary,
                color = Color(0xFFF1F5F9),
                fontSize = 13.sp,
                lineHeight = 18.sp
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Channel: " + if (log.type == "CALL") "IVR Outbound Dialing" else "High-throughput SMS API",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
              Box(
                modifier = Modifier
                  .background(Color(0xFF14532D), shape = RoundedCornerShape(6.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = log.status,
                  color = Color(0xFF52B788),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Hook details
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
                  text = "/* Communication Hook:\nThis outreach ledger links with automated external telephony gateways (e.g. Twilio Voice / SMS IVR API, ClickSend, or localized rural government telecom hooks). The status 'Completed' confirms that the IVR service called the farmer, played the TTS audio, and parsed their confirmation keypad input via DTMF. */",
                  color = Color(0xFF94A3B8),
                  fontSize = 10.sp,
                  lineHeight = 14.sp
                )
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End,
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (log.type == "CALL") {
                var isCallRequested by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
                Button(
                  onClick = {
                    isCallRequested = true
                    viewModel.onSpeakText?.invoke(
                      "Placing outbound voice warning call to your registered phone number. Calling detail: ${log.title}. Details: ${log.summary}"
                    )
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBBF24)),
                  modifier = Modifier.padding(end = 8.dp)
                ) {
                  Text(if (isCallRequested) "Calling..." else "Voice Call Me", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
              }

              Button(
                onClick = { selectedLogForDialog = null },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332))
              ) {
                Text("Close", color = Color.White)
              }
            }
          }
        }
      }
    }
  }
}
