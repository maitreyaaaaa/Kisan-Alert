package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.LeafDiagnosticCanvas
import com.example.ui.components.borderStrokeForReadability
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CropHealthLoggerScreen(viewModel: KisanAlertViewModel, scope: CoroutineScope) {
  val lang = viewModel.currentLanguage
  val context = androidx.compose.ui.platform.LocalContext.current
  var showUploadOptionsDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Large Action Buttons Block
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
      border = BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Submit Leaf Image or Speak Symptoms",
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Camera upload button
          Button(
            onClick = { showUploadOptionsDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
            border = borderStrokeForReadability(),
            modifier = Modifier
              .weight(1.3f)
              .height(52.dp)
              .testTag("upload_leaf_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = "Camera Upload")
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Upload Leaf",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          // Voice Query microphone button with pulsing color state
          Button(
            onClick = { viewModel.startVoiceQuery(scope) },
            colors = ButtonDefaults.buttonColors(
              containerColor = if (viewModel.isRecordingVoice) Color(0xFF78350F) else Color(0xFF1E293B)
            ),
            border = BorderStroke(
              2.dp,
              if (viewModel.isRecordingVoice) Color(0xFFFBBF24) else Color(0xFF52B788)
            ),
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
              .testTag("voice_query_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = if (viewModel.isRecordingVoice) Icons.Default.MicOff else Icons.Default.Mic,
              contentDescription = "Voice mic selector",
              tint = if (viewModel.isRecordingVoice) Color(0xFFFBBF24) else Color(0xFF52B788)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (viewModel.isRecordingVoice) "Recording" else "Voice Query",
              color = if (viewModel.isRecordingVoice) Color(0xFFFBBF24) else Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }

    // Common Symptoms Quick Catalog
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
      border = BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Common Symptoms Catalog / रोग लक्षण गैलरी",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val samples = listOf(
            Triple("Maize Root Rot", "HEALTHY", "मक्का रोट"),
            Triple("Rice Blast", "BLAST", "धान ब्लास्ट"),
            Triple("Cotton Blight", "BLIGHT", "कपास ब्लाइट")
          )
          samples.forEach { sample ->
            Box(
              modifier = Modifier
                .weight(1.0f)
                .background(Color(0xFF0F172A), shape = RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF334155), shape = RoundedCornerShape(10.dp))
                .clickable {
                  viewModel.simulatedImageName = sample.second
                  viewModel.runCropLeafDiagnosis(sample.second, context)
                }
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF1E293B), shape = RoundedCornerShape(6.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  LeafDiagnosticCanvas(imageType = sample.second)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = if (lang == com.example.util.AppLanguage.HINDI) sample.third else sample.first,
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
          }
        }
      }
    }

    // Interactive graphical Leaf Diagnostics Preview
    viewModel.simulatedImageName?.let { imageType ->
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "Simulated Upload Image Preview:",
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
          modifier = Modifier
            .size(160.dp)
            .background(Color(0xFF1E293B), shape = RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFF334155), shape = RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          LeafDiagnosticCanvas(imageType = imageType)
        }
      }
    }

    // Voice query translation feedback card
    viewModel.recordedQueryText?.let { transcript ->
      Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF78350F).copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(Icons.Default.Mic, contentDescription = "Speech Transcript", tint = Color(0xFFFBBF24))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Recognized Spoken Audio Query:",
              color = Color(0xFFFBBF24),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "\"$transcript\"",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    // Diagnostic Report Card Output
    Column {
      Text(
        text = LocalizedStrings.get("diag_report", lang).uppercase(),
        color = Color(0xFF52B788),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
      )

      AnimatedVisibility(
        visible = viewModel.diagnosticReport != null,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut()
      ) {
        viewModel.diagnosticReport?.let { report ->
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(
              2.dp,
              if (report.severityPercent > 0.3f) Color(0xFFF59E0B) else Color(0xFF52B788)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = LocalizedStrings.get("disease", lang),
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = report.diseaseName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }

                androidx.compose.material3.IconButton(
                  onClick = { viewModel.speakCurrentAdvisory(report) }
                ) {
                  Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.VolumeUp,
                    contentDescription = "Read report aloud",
                    tint = Color(0xFF52B788)
                  )
                }

                if (report.severityPercent > 0f) {
                  Box(
                    modifier = Modifier
                      .background(Color(0xFF78350F), shape = RoundedCornerShape(8.dp))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "${(report.severityPercent * 100).toInt()}% Affected",
                      color = Color(0xFFFBBF24),
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                } else {
                  Box(
                    modifier = Modifier
                      .background(Color(0xFF14532D), shape = RoundedCornerShape(8.dp))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "Optimal",
                      color = Color(0xFF52B788),
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              if (report.severityPercent > 0.0f) {
                Column {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = LocalizedStrings.get("severity", lang),
                      color = Color(0xFF94A3B8),
                      fontSize = 11.sp
                    )
                    Text(
                      text = "High Attention Required",
                      color = Color(0xFFF59E0B),
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  LinearProgressIndicator(
                    progress = report.severityPercent,
                    color = Color(0xFFF59E0B),
                    trackColor = Color(0xFF334155),
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(6.dp)
                      .clip(RoundedCornerShape(3.dp))
                  )
                }
              }

              HorizontalDivider(color = Color(0xFF334155))

              Text(
                text = LocalizedStrings.get("remedies", lang),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )

              report.remediesList.forEachIndexed { idx, remedy ->
                Row(modifier = Modifier.fillMaxWidth()) {
                  Text(
                    text = "• ",
                    color = Color(0xFF52B788),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                  Text(
                    text = remedy,
                    color = Color(0xFFF1F5F9),
                    fontSize = 13.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              // Developer integration note showing hook detail
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
                    text = "/* Gemini API Hook:\nIn the production application, the crop image bytes from the Camera API and voice audio waveform bytes are bundled together. We invoke Gemini-2.5-flash-multimodal to perform real-time visual crop leaf diagnostic scans, analyze spoken symptoms, and return a structured JSON report matching this format. */",
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

      if (viewModel.isDiagnosticLoading) {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          border = BorderStroke(2.dp, Color(0xFF52B788)),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            CircularProgressIndicator(color = Color(0xFF52B788), modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Analyzing with Google Gemini...",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Performing plant pathology scan of the leaf image...",
              color = Color(0xFF94A3B8),
              fontSize = 13.sp,
              textAlign = TextAlign.Center
            )
          }
        }
      } else if (viewModel.diagnosticReport == null) {
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
              Icons.Default.CameraAlt,
              contentDescription = "Empty Report Indicator",
              tint = Color(0xFF334155),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = LocalizedStrings.get("logger_placeholder", lang),
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

  // Camera capture simulation options dialogue
  if (showUploadOptionsDialog) {
    Dialog(onDismissRequest = { showUploadOptionsDialog = false }) {
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
          Text(
            text = "Simulate Leaf Capture",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "Select one of the following diagnostic leaf samples to simulate upload and AI processing.",
            color = Color(0xFF94A3B8),
            fontSize = 13.sp
          )

          HorizontalDivider(color = Color(0xFF334155))

          // Option 1: Healthy Leaf
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.simulateUploadLeaf("HEALTHY", context, scope)
                showUploadOptionsDialog = false
              }
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Healthy", tint = Color(0xFF52B788))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Sample A: Healthy Maize Leaf", color = Color.White, fontSize = 14.sp)
          }

          // Option 2: Rice Blast
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.simulateUploadLeaf("BLAST", context, scope)
                showUploadOptionsDialog = false
              }
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Warning, contentDescription = "Blast Warning", tint = Color(0xFFF59E0B))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Sample B: Rice Leaf with Fungal Blast Spots", color = Color.White, fontSize = 14.sp)
          }

          // Option 3: Bacterial Leaf Blight
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.simulateUploadLeaf("BLIGHT", context, scope)
                showUploadOptionsDialog = false
              }
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Warning, contentDescription = "Blight Warning", tint = Color(0xFFEF4444))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Sample C: Leaf with Bacterial Wilt / Blight", color = Color.White, fontSize = 14.sp)
          }

          Button(
            onClick = { showUploadOptionsDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
            modifier = Modifier.align(Alignment.End)
          ) {
            Text("Cancel", color = Color.White)
          }
        }
      }
    }
  }
}
