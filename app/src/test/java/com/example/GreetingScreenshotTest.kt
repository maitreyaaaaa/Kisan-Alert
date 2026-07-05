package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.FarmerProfile
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.AppLanguage
import com.example.viewmodel.KisanAlertViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun settings_screenshot() {
    val viewModel = KisanAlertViewModel().apply {
      farmerProfile = FarmerProfile(
        name = "Raju Kumar",
        phone = "9876543210",
        district = "Anantapur",
        preferredLanguage = AppLanguage.ENGLISH,
        trustedHelperPhone = "+919000011223",
        primaryCrop = "Groundnut"
      )
    }

    composeTestRule.setContent {
      MyApplicationTheme {
        SettingsScreen(viewModel)
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
