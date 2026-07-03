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

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true) { // Dark slate color theme by default for high contrast
        KisanAlertApp()
      }
    }
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
        when (activeTab) {
          0 -> HomeDashboardScreen(viewModel)
          1 -> CropRecommendationScreen(viewModel, coroutineScope)
          2 -> CropHealthLoggerScreen(viewModel, coroutineScope)
          3 -> RSKHistoryScreen(viewModel)
        }
      }
    }
  }
}
