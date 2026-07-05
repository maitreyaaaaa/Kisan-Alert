package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.screens.AlertsScreen
import com.example.ui.screens.AskAssistantScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.AppLanguage
import com.example.util.LocalizedStrings
import com.example.util.VoiceManager
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
  private lateinit var voiceManager: VoiceManager
  private var activeViewModel: KisanAlertViewModel? = null
  private var activeScope: CoroutineScope? = null

  private val speechLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == RESULT_OK) {
      val results = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
      val spokenText = results?.firstOrNull()
      if (spokenText != null && activeViewModel != null && activeScope != null) {
        activeViewModel?.onSpeechRecognized(spokenText, activeScope!!)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    voiceManager = VoiceManager(this)
    enableEdgeToEdge()

    setContent {
      val viewModel: KisanAlertViewModel = viewModel()
      viewModel.attachSessionStore(applicationContext)
      MyApplicationTheme(darkTheme = viewModel.isDarkTheme) {
        val coroutineScope = rememberCoroutineScope()

        activeViewModel = viewModel
        activeScope = coroutineScope

        viewModel.onSpeakText = { text ->
          voiceManager.speak(text, viewModel.currentLanguage)
        }
        viewModel.onStartSpeechRecognizer = {
          val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
              android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
              android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
              android.speech.RecognizerIntent.EXTRA_LANGUAGE,
              when (viewModel.currentLanguage) {
                AppLanguage.ENGLISH -> "en-US"
                AppLanguage.HINDI -> "hi-IN"
                AppLanguage.TELUGU -> "te-IN"
                AppLanguage.KANNADA -> "kn-IN"
                AppLanguage.TAMIL -> "ta-IN"
              }
            )
            putExtra(
              android.speech.RecognizerIntent.EXTRA_PROMPT,
              LocalizedStrings.get("voice_recognizer_prompt", viewModel.currentLanguage)
            )
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

  val pageBackground = Color(0xFFF7F4ED)
  val navBackground = Color(0xFFFFFBF5)
  val brandBlue = Color(0xFF2D4F8F)
  val mutedInk = Color(0xFF7A8597)
  val warmAccent = Color(0xFFF28C38)

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(pageBackground),
    containerColor = pageBackground,
    bottomBar = {
      if (viewModel.isLoggedIn) {
        NavigationBar(
          containerColor = navBackground,
          modifier = Modifier.testTag("bottom_nav_bar")
        ) {
          NavigationBarItem(
            selected = activeTab == 0,
            onClick = { viewModel.currentTab = 0 },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = {
              Text(
                LocalizedStrings.get("tab_home", selectedLanguage),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = brandBlue,
              selectedTextColor = brandBlue,
              unselectedIconColor = mutedInk,
              unselectedTextColor = mutedInk,
              indicatorColor = Color(0xFFEAE2D4)
            ),
            modifier = Modifier.testTag("nav_tab_home")
          )
          NavigationBarItem(
            selected = activeTab == 1,
            onClick = { viewModel.currentTab = 1 },
            icon = { Icon(Icons.Default.Mic, contentDescription = "Ask") },
            label = {
              Text(
                LocalizedStrings.get("tab_ask", selectedLanguage),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = brandBlue,
              selectedTextColor = brandBlue,
              unselectedIconColor = mutedInk,
              unselectedTextColor = mutedInk,
              indicatorColor = Color(0xFFEAE2D4)
            ),
            modifier = Modifier.testTag("nav_tab_ask")
          )
          NavigationBarItem(
            selected = activeTab == 2,
            onClick = { viewModel.currentTab = 2 },
            icon = { Icon(Icons.Default.Warning, contentDescription = "Alerts") },
            label = {
              Text(
                LocalizedStrings.get("tab_alerts", selectedLanguage),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = warmAccent,
              selectedTextColor = warmAccent,
              unselectedIconColor = mutedInk,
              unselectedTextColor = mutedInk,
              indicatorColor = Color(0xFFFFE8D1)
            ),
            modifier = Modifier.testTag("nav_tab_alerts")
          )
          NavigationBarItem(
            selected = activeTab == 3,
            onClick = { viewModel.currentTab = 3 },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Account") },
            label = {
              Text(
                LocalizedStrings.get("tab_account", selectedLanguage),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = brandBlue,
              selectedTextColor = brandBlue,
              unselectedIconColor = mutedInk,
              unselectedTextColor = mutedInk,
              indicatorColor = Color(0xFFEAE2D4)
            ),
            modifier = Modifier.testTag("nav_tab_account")
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(pageBackground)
        .padding(innerPadding)
    ) {
      KisanHeader(
        selectedLanguage = selectedLanguage,
        onLanguageSelected = { viewModel.setCurrentLanguage(it) }
      )

      if (!viewModel.isLoggedIn) {
        LoginScreen(viewModel, coroutineScope)
      } else {
        when (activeTab) {
          0 -> HomeDashboardScreen(
            viewModel = viewModel,
            onOpenAsk = { viewModel.currentTab = 1 },
            onOpenAlerts = { viewModel.currentTab = 2 },
            onOpenAccount = { viewModel.currentTab = 3 }
          )
          1 -> AskAssistantScreen(viewModel, coroutineScope)
          2 -> AlertsScreen(viewModel)
          3 -> SettingsScreen(viewModel)
        }
      }
    }
  }
}
