package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.KisanHeader
import com.example.ui.screens.CropHealthLoggerScreen
import com.example.ui.screens.CropRecommendationScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.RSKHistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.LocalizedStrings
import com.example.viewmodel.KisanAlertViewModel
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SettingsScreen
import androidx.compose.material.icons.filled.Settings

class MainActivity : ComponentActivity() {
  private lateinit var voiceManager: com.example.util.VoiceManager
  private var activeViewModel: KisanAlertViewModel? = null
  private var activeScope: kotlinx.coroutines.CoroutineScope? = null

  private val speechLauncher = registerForActivityResult(
    androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == RESULT_OK) {
      val data = result.data
      val results = data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
      val spokenText = results?.firstOrNull()
      if (spokenText != null && activeViewModel != null && activeScope != null) {
        activeViewModel?.onSpeechRecognized(spokenText, activeScope!!)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    voiceManager = com.example.util.VoiceManager(this)
    enableEdgeToEdge()
    setContent {
      val viewModel: KisanAlertViewModel = viewModel()
      MyApplicationTheme(darkTheme = viewModel.isDarkTheme) {
        val coroutineScope = rememberCoroutineScope()
        
        activeViewModel = viewModel
        activeScope = coroutineScope
        
        viewModel.onSpeakText = { text ->
          voiceManager.speak(text, viewModel.currentLanguage)
        }
        viewModel.onStartSpeechRecognizer = {
          val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, when (viewModel.currentLanguage) {
              com.example.util.AppLanguage.ENGLISH -> "en-US"
              com.example.util.AppLanguage.HINDI -> "hi-IN"
              com.example.util.AppLanguage.TELUGU -> "te-IN"
              com.example.util.AppLanguage.KANNADA -> "kn-IN"
              com.example.util.AppLanguage.TAMIL -> "ta-IN"
            })
            putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, "Speak now / अभी बोलें...")
          }
          try {
            speechLauncher.launch(intent)
          } catch (e: Exception) {
            e.printStackTrace()
            viewModel.simulateVoiceRecording(coroutineScope)
          }
        }
        
        KisanAlertApp(viewModel)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    voiceManager.shutdown()
  }
}

@Composable
fun KisanAlertApp(viewModel: KisanAlertViewModel = viewModel()) {
  val selectedLanguage = viewModel.currentLanguage
  val activeTab = viewModel.currentTab
  val coroutineScope = rememberCoroutineScope()

  // High contrast palette values
  val slateDark = Color(0xFF0F172A)
  val softMint = Color(0xFF52B788)

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      if (viewModel.isLoggedIn) {
        NavigationBar(
          containerColor = Color(0xFF1E293B), // slateCard
          modifier = Modifier.testTag("bottom_nav_bar")
        ) {
          NavigationBarItem(
            selected = activeTab == 0,
            onClick = { viewModel.currentTab = 0 },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
            label = { Text(LocalizedStrings.get("tab_home", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = softMint,
              selectedTextColor = softMint,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8),
              indicatorColor = Color(0xFF1B4332)
            ),
            modifier = Modifier.testTag("nav_tab_home")
          )
          NavigationBarItem(
            selected = activeTab == 1,
            onClick = { viewModel.currentTab = 1 },
            icon = { Icon(Icons.Default.Agriculture, contentDescription = "Agriculture Icon") },
            label = { Text(LocalizedStrings.get("tab_advisory", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = softMint,
              selectedTextColor = softMint,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8),
              indicatorColor = Color(0xFF1B4332)
            ),
            modifier = Modifier.testTag("nav_tab_advisory")
          )
          NavigationBarItem(
            selected = activeTab == 2,
            onClick = { viewModel.currentTab = 2 },
            icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Camera Icon") },
            label = { Text(LocalizedStrings.get("tab_logger", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = softMint,
              selectedTextColor = softMint,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8),
              indicatorColor = Color(0xFF1B4332)
            ),
            modifier = Modifier.testTag("nav_tab_logger")
          )
          NavigationBarItem(
            selected = activeTab == 3,
            onClick = { viewModel.currentTab = 3 },
            icon = { Icon(Icons.Default.History, contentDescription = "Logs Icon") },
            label = { Text(LocalizedStrings.get("tab_history", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = softMint,
              selectedTextColor = softMint,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8),
              indicatorColor = Color(0xFF1B4332)
            ),
            modifier = Modifier.testTag("nav_tab_history")
          )
          NavigationBarItem(
            selected = activeTab == 4,
            onClick = { viewModel.currentTab = 4 },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings Icon") },
            label = {
              val labelText = when (selectedLanguage) {
                com.example.util.AppLanguage.ENGLISH -> "Settings"
                com.example.util.AppLanguage.HINDI -> "सेटिंग्स"
                com.example.util.AppLanguage.TELUGU -> "సెట్టింగులు"
                com.example.util.AppLanguage.KANNADA -> "ಸಂಯೋಜನೆಗಳು"
                com.example.util.AppLanguage.TAMIL -> "அமைப்புகள்"
              }
              Text(labelText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = softMint,
              selectedTextColor = softMint,
              unselectedIconColor = Color(0xFF94A3B8),
              unselectedTextColor = Color(0xFF94A3B8),
              indicatorColor = Color(0xFF1B4332)
            ),
            modifier = Modifier.testTag("nav_tab_settings")
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(slateDark)
        .padding(innerPadding)
    ) {
      // --------------------------------------------------------------------
      // HEADER WITH APP NAME & INDIC LANGUAGE DROPDOWN
      // --------------------------------------------------------------------
      KisanHeader(
        selectedLanguage = selectedLanguage,
        onLanguageSelected = { viewModel.currentLanguage = it }
      )

      // --------------------------------------------------------------------
      // ACTIVE SCREEN ROUTER
      // --------------------------------------------------------------------
      Box(modifier = Modifier.fillMaxSize()) {
        if (!viewModel.isLoggedIn) {
          LoginScreen(viewModel, coroutineScope)
        } else {
          when (activeTab) {
            0 -> HomeDashboardScreen(viewModel)
            1 -> CropRecommendationScreen(viewModel, coroutineScope)
            2 -> CropHealthLoggerScreen(viewModel, coroutineScope)
            3 -> RSKHistoryScreen(viewModel)
            4 -> SettingsScreen(viewModel)
          }
        }
      }
    }
  }
}
