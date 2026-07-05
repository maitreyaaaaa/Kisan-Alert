package com.example

import com.example.data.model.FarmerProfile
import com.example.data.model.AdvisoryCard
import com.example.util.AppLanguage
import com.example.viewmodel.KisanAlertViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KisanAlertViewModelTest {
  private val testDispatcher = StandardTestDispatcher()
  private lateinit var viewModel: KisanAlertViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = KisanAlertViewModel()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testInitialState() {
    assertEquals(0, viewModel.currentTab)
    assertEquals(AppLanguage.ENGLISH, viewModel.currentLanguage)
    assertFalse(viewModel.isLoggedIn)
    assertNull(viewModel.farmerProfile)
    assertTrue(viewModel.isAutoSpeakEnabled)
    assertFalse(viewModel.isDarkTheme)
  }

  @Test
  fun testNavigationAndLanguageToggles() {
    viewModel.currentTab = 2
    assertEquals(2, viewModel.currentTab)

    viewModel.setCurrentLanguage(AppLanguage.HINDI)
    assertEquals(AppLanguage.HINDI, viewModel.currentLanguage)
  }

  @Test
  fun testThemeAndSpeakToggles() {
    viewModel.setAutoSpeakEnabled(false)
    assertFalse(viewModel.isAutoSpeakEnabled)

    viewModel.isDarkTheme = false
    assertFalse(viewModel.isDarkTheme)
  }

  @Test
  fun testVerifyPhoneAndLogin_Success() = runTest {
    viewModel.verifyPhoneAndLogin(
      phone = "9876543210",
      otp = "1234",
      farmerName = "Raju",
      district = "Anantapur",
      scope = this
    )
    
    // Wait for the simulated delay to complete in standard test dispatcher
    testScheduler.advanceUntilIdle()

    assertTrue(viewModel.isLoggedIn)
    assertEquals("9876543210", viewModel.farmerProfile?.phone)
    assertEquals("Raju", viewModel.farmerProfile?.name)
    assertEquals("Groundnut", viewModel.farmerProfile?.primaryCrop)
    assertNull(viewModel.authErrorMessage)
  }

  @Test
  fun testVerifyPhoneAndLogin_Failure() = runTest {
    viewModel.verifyPhoneAndLogin("98765", "invalid", this)
    
    testScheduler.advanceUntilIdle()

    assertFalse(viewModel.isLoggedIn)
    assertNull(viewModel.farmerProfile)
    assertNotNull(viewModel.authErrorMessage)
  }

  @Test
  fun testLogoutResetsState() = runTest {
    // Set logged in state
    viewModel.verifyPhoneAndLogin("9876543210", "1234", this)
    testScheduler.advanceUntilIdle()
    assertTrue(viewModel.isLoggedIn)

    // Trigger logout
    viewModel.logout()
    assertFalse(viewModel.isLoggedIn)
    assertNull(viewModel.farmerProfile)
  }

  @Test
  fun testSetCurrentLanguageUpdatesFarmerProfilePreference() {
    viewModel.farmerProfile = FarmerProfile(
      name = "Raju",
      phone = "9876543210",
      district = "Anantapur",
      preferredLanguage = AppLanguage.ENGLISH,
      trustedHelperPhone = "+919000011223",
      primaryCrop = "Groundnut"
    )

    viewModel.setCurrentLanguage(AppLanguage.TELUGU)

    assertEquals(AppLanguage.TELUGU, viewModel.currentLanguage)
    assertEquals(AppLanguage.TELUGU, viewModel.farmerProfile?.preferredLanguage)
  }

  @Test
  fun testHomeSummaryUsesFarmerProfileContext() {
    viewModel.farmerProfile = FarmerProfile(
      name = "Raju",
      phone = "9876543210",
      district = "Anantapur",
      preferredLanguage = AppLanguage.ENGLISH,
      trustedHelperPhone = "+919000011223",
      primaryCrop = "Groundnut"
    )

    val summary = viewModel.homeSummary

    assertEquals("Today for Raju", summary.headline)
    assertEquals("Anantapur", summary.districtTag)
    assertEquals("Groundnut", summary.cropTag)
    assertTrue(summary.supportCopy.contains("+919000011223"))
    assertEquals(3, summary.priorities.size)
  }

  @Test
  fun testAlertListUsesProfileDistrictAndCrop() {
    viewModel.farmerProfile = FarmerProfile(
      name = "Raju",
      phone = "9876543210",
      district = "Guntur",
      preferredLanguage = AppLanguage.ENGLISH,
      trustedHelperPhone = "+919000011223",
      primaryCrop = "Chilli"
    )

    val alerts = viewModel.alertList

    assertTrue(alerts.first().title.contains("Guntur"))
    assertTrue(alerts[1].title.contains("Chilli"))
    assertTrue(alerts[2].title.contains("Guntur"))
  }

  @Test
  fun testCurrentAskCardUsesDiagnosticReport() {
    viewModel.diagnosticReport = com.example.data.model.HealthReport(
      diseaseName = "Rice Blast",
      severityPercent = 0.65f,
      remediesList = listOf("Check two nearby plants", "Avoid whole-field spray"),
      imageType = "BLAST"
    )

    val card = viewModel.currentAskCard

    assertNotNull(card)
    assertEquals("Rice Blast", card?.title)
    assertEquals("High risk", card?.riskLabel)
    assertTrue(card?.needsHumanReview == true)
    assertEquals(2, card?.actions?.size)
  }

  @Test
  fun testCurrentAskCardUsesSoilAdvisory() {
    viewModel.advisoryResult = com.example.data.model.SoilAdvisory(
      cropName = "Paddy",
      matchPercentage = "Prototype Match",
      plantingWindow = "Review This Week",
      details = "Keep the first step small and compare with district conditions.",
      guidelines = listOf("Check field moisture", "Review with helper")
    )

    val card = viewModel.currentAskCard

    assertNotNull(card)
    assertEquals("Paddy", card?.title)
    assertEquals("Plan before spending", card?.riskLabel)
    assertTrue(card?.needsHumanReview == true)
    assertEquals("Prototype guidance", card?.confidenceLabel)
  }

  @Test
  fun testCurrentAskCardFallsBackToEnglishForNewHindiKeys() {
    viewModel.currentLanguage = AppLanguage.HINDI
    viewModel.diagnosticReport = com.example.data.model.HealthReport(
      diseaseName = "Rice Blast",
      severityPercent = 0.65f,
      remediesList = listOf("Check two nearby plants"),
      imageType = "BLAST"
    )

    val card = viewModel.currentAskCard

    assertNotNull(card)
    assertEquals("High risk", card?.riskLabel)
    assertEquals("Pattern-based guidance", card?.confidenceLabel)
    assertTrue(card?.summary?.contains("65%") == true)
  }

  @Test
  fun testCurrentAskCardUsesVoiceAdvisoryCardFirst() {
    viewModel.voiceAdvisoryCard = AdvisoryCard(
      title = "Next step for your question",
      summary = "Inspect one patch first.",
      actions = listOf("Inspect one patch first", "Use photo if spread continues"),
      riskLabel = "Plan before spending",
      needsHumanReview = true,
      confidenceLabel = "Prototype guidance"
    )

    val card = viewModel.currentAskCard

    assertNotNull(card)
    assertEquals("Next step for your question", card?.title)
    assertEquals("Prototype guidance", card?.confidenceLabel)
    assertTrue(card?.needsHumanReview == true)
  }

  @Test
  fun testUpdateFarmerProfileUpdatesEditableFields() {
    viewModel.farmerProfile = FarmerProfile(
      name = "Raju",
      phone = "9876543210",
      district = "Anantapur",
      preferredLanguage = AppLanguage.ENGLISH,
      trustedHelperPhone = "+919000011223",
      primaryCrop = "Groundnut"
    )

    viewModel.updateFarmerProfile(
      name = "Sita",
      district = "Guntur",
      primaryCrop = "Chilli",
      trustedHelperPhone = "+919111122233"
    )

    assertEquals("Sita", viewModel.farmerProfile?.name)
    assertEquals("Guntur", viewModel.farmerProfile?.district)
    assertEquals("Chilli", viewModel.farmerProfile?.primaryCrop)
    assertEquals("+919111122233", viewModel.farmerProfile?.trustedHelperPhone)
  }
}
