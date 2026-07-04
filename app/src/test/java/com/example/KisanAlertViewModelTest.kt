package com.example

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
    assertEquals("", viewModel.userPhoneNumber)
    assertTrue(viewModel.isAutoSpeakEnabled)
    assertTrue(viewModel.isDarkTheme)
  }

  @Test
  fun testNavigationAndLanguageToggles() {
    viewModel.currentTab = 2
    assertEquals(2, viewModel.currentTab)

    viewModel.currentLanguage = AppLanguage.HINDI
    assertEquals(AppLanguage.HINDI, viewModel.currentLanguage)
  }

  @Test
  fun testThemeAndSpeakToggles() {
    viewModel.isAutoSpeakEnabled = false
    assertFalse(viewModel.isAutoSpeakEnabled)

    viewModel.isDarkTheme = false
    assertFalse(viewModel.isDarkTheme)
  }

  @Test
  fun testVerifyPhoneAndLogin_Success() = runTest {
    viewModel.verifyPhoneAndLogin("9876543210", "1234", this)
    
    // Wait for the simulated delay to complete in standard test dispatcher
    testScheduler.advanceUntilIdle()

    assertTrue(viewModel.isLoggedIn)
    assertEquals("9876543210", viewModel.userPhoneNumber)
    assertNull(viewModel.authErrorMessage)
  }

  @Test
  fun testVerifyPhoneAndLogin_Failure() = runTest {
    viewModel.verifyPhoneAndLogin("98765", "invalid", this)
    
    testScheduler.advanceUntilIdle()

    assertFalse(viewModel.isLoggedIn)
    assertEquals("", viewModel.userPhoneNumber)
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
    assertEquals("", viewModel.userPhoneNumber)
  }
}
