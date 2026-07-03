package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --------------------------------------------------------------------
// LOCALIZATION MANAGER (Multi-language Support for Smallholder Farmers)
// --------------------------------------------------------------------
enum class AppLanguage(val code: String, val nativeName: String) {
  ENGLISH("en", "English"),
  HINDI("hi", "हिंदी (Hindi)"),
  TELUGU("te", "తెలుగు (Telugu)"),
  KANNADA("kn", "ಕನ್ನಡ (Kannada)"),
  TAMIL("ta", "தமிழ் (Tamil)")
}

object LocalizedStrings {
  private val strings = mapOf(
    AppLanguage.ENGLISH to mapOf(
      "app_name" to "Kisan Alert",
      "tagline" to "AI & Community Warning Broadcasts",
      "lang_label" to "Language",
      "tab_home" to "Home",
      "tab_advisory" to "Advisory",
      "tab_logger" to "Logger",
      "tab_history" to "RSK Logs",
      "farm_status" to "Farm Status",
      "moisture" to "Soil Moisture",
      "groundwater" to "Groundwater",
      "weather" to "Weather",
      "recent_alerts" to "Recent Safety Alerts",
      "soil_params" to "Enter Soil Parameters",
      "soil_type" to "Soil Type",
      "nitrogen" to "Nitrogen (N)",
      "phosphorus" to "Phosphorus (P)",
      "potassium" to "Potassium (K)",
      "ph_level" to "Soil pH Level",
      "run_advisory" to "Run AI Crop Advisory",
      "advisory_result" to "AI Crop Recommendation",
      "capture_leaf" to "Capture or Upload Crop Image",
      "voice_query" to "Voice Query Help",
      "diag_report" to "Diagnostic Report",
      "disease" to "Detected Issue",
      "severity" to "Severity Level",
      "remedies" to "Recommended Action Plan",
      "rsk_banner_title" to "Raitha Sahayaka Kendra (RSK) Logs",
      "rsk_banner_desc" to "Broadcasting real-time regional guidelines, market pricing, and voice warnings directly to farmers' standard mobile phones.",
      "moisture_status" to "38% (Optimal)",
      "groundwater_status" to "18m (Stable)",
      "weather_status" to "Sunny, 32°C",
      "advisory_placeholder" to "Input your soil nutrients above and tap 'Run AI Crop Advisory' to generate crop recommendations suited for your field.",
      "logger_placeholder" to "Upload an image of the plant leaf or hold the mic to describe the symptoms. Visual & spoken AI diagnostics will display here."
    ),
    AppLanguage.HINDI to mapOf(
      "app_name" to "किसान अलर्ट",
      "tagline" to "एआई और सामुदायिक सुरक्षा चेतावनी",
      "lang_label" to "भाषा",
      "tab_home" to "मुख्य",
      "tab_advisory" to "फसल सलाह",
      "tab_logger" to "स्वास्थ्य लॉगर",
      "tab_history" to "आरएसके लॉग्स",
      "farm_status" to "खेत की स्थिति",
      "moisture" to "मिट्टी की नमी",
      "groundwater" to "भूजल स्तर",
      "weather" to "मौसम",
      "recent_alerts" to "हालिया सुरक्षा चेतावनी",
      "soil_params" to "मिट्टी के मापदंड दर्ज करें",
      "soil_type" to "मिट्टी का प्रकार",
      "nitrogen" to "नाइट्रोजन (N)",
      "phosphorus" to "फास्फोरस (P)",
      "potassium" to "पोटेशियम (K)",
      "ph_level" to "मिट्टी का पीएच (pH)",
      "run_advisory" to "एआई फसल सलाह चलाएं",
      "advisory_result" to "एआई फसल अनुशंसा",
      "capture_leaf" to "पत्ती की फोटो खींचें या अपलोड करें",
      "voice_query" to "आवाज द्वारा प्रश्न पूछें",
      "diag_report" to "जांच रिपोर्ट",
      "disease" to "पहचानी गई बीमारी",
      "severity" to "गंभीरता का स्तर",
      "remedies" to "अनुशंसित उपचार योजना",
      "rsk_banner_title" to "रायतु भरोसा केंद्र / आरएसके लॉग्स",
      "rsk_banner_desc" to "किसानों के साधारण मोबाइल फोन पर वास्तविक समय में क्षेत्रीय सलाह, बाजार भाव और वॉयस कॉल चेतावनी भेजना।",
      "moisture_status" to "38% (सामान्य)",
      "groundwater_status" to "18 मीटर (स्थिर)",
      "weather_status" to "धूप, 32°C",
      "advisory_placeholder" to "ऊपर मिट्टी के पोषक तत्व भरें और अपने खेत के अनुकूल फसल सुझाव प्राप्त करने के लिए 'एआई फसल सलाह चलाएं' पर टैप करें।",
      "logger_placeholder" to "पौधे की पत्ती की तस्वीर अपलोड करें या लक्षणों को बताने के लिए माइक बटन दबाएं। विज़ुअल और वॉयस रिपोर्ट यहाँ दिखाई देगी।"
    ),
    AppLanguage.TELUGU to mapOf(
      "app_name" to "కిసాన్ అలర్ట్",
      "tagline" to "AI & కమ్యూనిటీ హెచ్చరికలు",
      "lang_label" to "భాష",
      "tab_home" to "హోమ్",
      "tab_advisory" to "పంట సలహా",
      "tab_logger" to "ఆరోగ్య లాగర్",
      "tab_history" to "RSK లాగ్స్",
      "farm_status" to "పంట క్షేత్ర స్థితి",
      "moisture" to "నేల తేమ",
      "groundwater" to "భూగర్భ జల మట్టం",
      "weather" to "వాతావరణం",
      "recent_alerts" to "ఇటీవలి హెచ్చరికలు",
      "soil_params" to "నేల కొలతలను నమోదు చేయండి",
      "soil_type" to "నేల రకం",
      "nitrogen" to "నత్రజని (N)",
      "phosphorus" to "భాస్వరం (P)",
      "potassium" to "పొటాషియం (K)",
      "ph_level" to "నేల pH విలువ",
      "run_advisory" to "AI పంట సలహా రన్ చేయి",
      "advisory_result" to "AI పంట సిఫార్సు",
      "capture_leaf" to "ఆకు ఫోటో తీయండి లేదా అప్‌లోడ్ చేయండి",
      "voice_query" to "వాయిస్ ప్రశ్న సలహా",
      "diag_report" to "పంట వ్యాధి నిర్ధారణ నివేదిక",
      "disease" to "గుర్తించిన సమస్య",
      "severity" to "తీవ్రత స్థాయి",
      "remedies" to "సూచించిన నివారణ చర్యలు",
      "rsk_banner_title" to "రైతు సేవా కేంద్రం (RSK) లాగ్స్",
      "rsk_banner_desc" to "రైతుల సాధారణ మొబైల్ ఫోన్‌లకు నేరుగా రియల్ టైమ్ వాయిస్ కాల్స్ మరియు ఎస్ఎంఎస్ ద్వారా సమాచారం చేరవేయడం.",
      "moisture_status" to "38% (అనుకూలం)",
      "groundwater_status" to "18 మీటర్లు (స్థిరంగా)",
      "weather_status" to "ఎండగా ఉంది, 32°C",
      "advisory_placeholder" to "పైన మీ నేల పోషకాలను నమోదు చేసి, మీ పొలానికి సరిపోయే పంట సిఫార్సులను పొందడానికి 'AI పంట సలహా రన్ చేయి' పై నొక్కండి.",
      "logger_placeholder" to "మొక్క ఆకు చిత్రాన్ని అప్‌లోడ్ చేయండి లేదా మైక్ నొక్కి సమస్యను వివరించండి. వ్యాధి నిర్ధారణ ఇక్కడ చూపబడుతుంది."
    ),
    AppLanguage.KANNADA to mapOf(
      "app_name" to "ಕಿಸಾನ್ ಅಲರ್ಟ್",
      "tagline" to "AI ಮತ್ತು ಸಮುದಾಯ ಎಚ್ಚರಿಕೆ ವ್ಯವಸ್ಥೆ",
      "lang_label" to "ಭಾಷೆ",
      "tab_home" to "ಮುಖಪುಟ",
      "tab_advisory" to "ಬೆಳೆ ಸಲಹೆ",
      "tab_logger" to "ಆರೋಗ್ಯ ಲಾಗರ್",
      "tab_history" to "RSK ಇತಿಹಾಸ",
      "farm_status" to "ಫಾರ್ಮ್ ಸ್ಥಿತಿ",
      "moisture" to "ಮಣ್ಣಿನ ತೇವಾಂಶ",
      "groundwater" to "ಅಂತರ್ಜಲ ಮಟ್ಟ",
      "weather" to "ಹವಾಮಾನ",
      "recent_alerts" to "ಇತ್ತೀಚಿನ ಎಚ್ಚರಿಕೆಗಳು",
      "soil_params" to "ಮಣ್ಣಿನ ನಿಯತಾಂಕಗಳನ್ನು ನಮೂದಿಸಿ",
      "soil_type" to "ಮಣ್ಣಿನ ಪ್ರಕಾರ",
      "nitrogen" to "ಸಾರಜನಕ (N)",
      "phosphorus" to "ರಂಜಕ (P)",
      "potassium" to "ಪೊಟ್ಯಾಸಿಯಮ್ (K)",
      "ph_level" to "ಮಣ್ಣಿನ pH ಮಟ್ಟ",
      "run_advisory" to "AI ಬೆಳೆ ಸಲಹೆ ರನ್ ಮಾಡಿ",
      "advisory_result" to "AI ಬೆಳೆ ಶಿಫಾರಸು",
      "capture_leaf" to "ಎಲೆಯ ಚಿತ್ರವನ್ನು ತೆಗೆಯಿರಿ ಅಥವಾ ಅಪ್‌ಲೋಡ್ ಮಾಡಿ",
      "voice_query" to "ಧ್ವನಿ ಮೂಲಕ ಪ್ರಶ್ನಿಸಿ",
      "diag_report" to "ರೋಗ ಪತ್ತೆ ವರದಿ",
      "disease" to "ಪತ್ತೆಯಾದ ರೋಗ",
      "severity" to "ರೋಗದ ತೀವ್ರತೆ",
      "remedies" to "ಶಿಫಾರಸು ಮಾಡಿದ ಪರಿಹಾರಗಳು",
      "rsk_banner_title" to "ರೈತ ಸಂಪರ್ಕ ಕೇಂದ್ರ (RSK) ಇತಿಹಾಸ",
      "rsk_banner_desc" to "ರೈತರ ಸಾಮಾನ್ಯ ಮೊಬೈಲ್ ಫೋನ್‌ಗಳಿಗೆ ನೇರವಾಗಿ ಧ್ವನಿ ಕರೆ ಮತ್ತು SMS ಮೂಲಕ ತುರ್ತು ಕೃಷಿ ಮಾಹಿತಿಯನ್ನು ಕಳುಹಿಸುವುದು.",
      "moisture_status" to "38% (ಸೂಕ್ತ)",
      "groundwater_status" to "18 ಮೀ (ಸ್ಥಿರ)",
      "weather_status" to "ಬಿಸಿಲು, 32°C",
      "advisory_placeholder" to "ದಯವಿಟ್ಟು ನಿಮ್ಮ ಮಣ್ಣಿನ ಪೌಷ್ಟಿಕಾಂಶಗಳನ್ನು ನಮೂದಿಸಿ ಮತ್ತು ನಿಮ್ಮ ಮಣ್ಣಿಗೆ ಹೊಂದುವ ಬೆಳೆ ಸಲಹೆಗಳನ್ನು ಪಡೆಯಲು 'AI ಬೆಳೆ ಸಲಹೆ ರನ್ ಮಾಡಿ' ಟ್ಯಾಪ್ ಮಾಡಿ.",
      "logger_placeholder" to "ಸಸ್ಯದ ಎಲೆಯ ಚಿತ್ರವನ್ನು ಅಪ್‌ಲೋಡ್ ಮಾಡಿ ಅಥವಾ ಲಕ್ಷಣಗಳನ್ನು ವಿವರಿಸಲು ಮೈಕ್ ಒತ್ತಿರಿ. ರೋಗ ಪತ್ತೆ ಮಾಹಿತಿಯು ಇಲ್ಲಿ ಮೂಡಿಬರುತ್ತದೆ."
    ),
    AppLanguage.TAMIL to mapOf(
      "app_name" to "கிசான் அலர்ட்",
      "tagline" to "AI மற்றும் சமூக எச்சரிக்கை சேவை",
      "lang_label" to "மொழி",
      "tab_home" to "முகப்பு",
      "tab_advisory" to "பயிர் ஆலோசனை",
      "tab_logger" to "சுகாதார லாகர்",
      "tab_history" to "RSK பதிவுகள்",
      "farm_status" to "பண்ணை நிலை",
      "moisture" to "மண் ஈரப்பதம்",
      "groundwater" to "நிலத்தடி நீர்",
      "weather" to "வானிலை",
      "recent_alerts" to "சமீபத்திய எச்சரிக்கைகள்",
      "soil_params" to "மண் அளவீடுகளை உள்ளிடவும்",
      "soil_type" to "மண் வகை",
      "nitrogen" to "நைட்ரஜன் (N)",
      "phosphorus" to "பாஸ்பரஸ் (P)",
      "potassium" to "பொட்டாசியம் (K)",
      "ph_level" to "மண் pH அளவு",
      "run_advisory" to "AI பயிர் ஆலோசனையை இயக்கு",
      "advisory_result" to "AI பயிர் பரிந்துரை",
      "capture_leaf" to "இலையைப் படம் பிடிக்கவும் அல்லது பதிவேற்றவும்",
      "voice_query" to "குரல் வழி கேள்வி கேட்க",
      "diag_report" to "நோய் கண்டறிதல் அறிக்கை",
      "disease" to "கண்டறியப்பட்ட நோய்",
      "severity" to "தீவிரத்தன்மை",
      "remedies" to "பரிந்துரைக்கப்பட்ட தீர்வுகள்",
      "rsk_banner_title" to "உழவர் உதவி மையம் (RSK) பதிவுகள்",
      "rsk_banner_desc" to "விவசாயிகளின் சாதாரண மொபைல் போன்களுக்கு நேரடியாக குரல் அழைப்புகள் மற்றும் SMS மூலமாக எச்சரிக்கைகளை அனுப்புதல்.",
      "moisture_status" to "38% (சிறந்தது)",
      "groundwater_status" to "18மீ (நிலையானது)",
      "weather_status" to "வெயில், 32°C",
      "advisory_placeholder" to "மண் அளவீடுகளை மேலே உள்ளிட்டு, உங்களது நிலத்திற்கு ஏற்ற சிறந்த பயிர்களைப் பற்றி அறிய 'AI பயிர் ஆலோசனையை இயக்கு' என்பதைத் தட்டவும்.",
      "logger_placeholder" to "பயிரின் இலை புகைப்படத்தை பதிவேற்றவும் அல்லது அறிகுறிகளை விளக்க மைக் பொத்தானை அழுத்தவும். நோய் கண்டறிதல் அறிக்கை இங்கே காட்டப்படும்."
    )
  )

  fun get(key: String, lang: AppLanguage): String {
    return strings[lang]?.get(key) ?: strings[AppLanguage.ENGLISH]?.get(key) ?: key
  }
}

// --------------------------------------------------------------------
// STATE MODELS & LOGIC (Simple state modeling inside a ViewModel)
// --------------------------------------------------------------------
data class AlertItem(
  val id: Int,
  val title: String,
  val subtitle: String,
  val type: String, // "VOICE", "SMS", "WARNING"
  val time: String,
  val detailScript: String
)

data class HistoryLog(
  val id: Int,
  val title: String,
  val type: String, // "CALL", "SMS"
  val status: String, // "Completed", "Delivered", "Failed"
  val time: String,
  val summary: String
)

data class SoilAdvisory(
  val cropName: String,
  val matchPercentage: String,
  val plantingWindow: String,
  val details: String,
  val guidelines: List<String>
)

data class HealthReport(
  val diseaseName: String,
  val severityPercent: Float,
  val remediesList: List<String>,
  val imageType: String // "HEALTHY", "BLAST", "BLIGHT"
)

class KisanAlertViewModel : ViewModel() {
  // Navigation & Language
  var currentTab by mutableStateOf(0)
  var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)

  // Soil Advisor Inputs
  var selectedSoilType by mutableStateOf("Clayey")
  var nitrogen by mutableStateOf("65")
  var phosphorus by mutableStateOf("45")
  var potassium by mutableStateOf("40")
  var phLevel by mutableFloatStateOf(6.5f)

  // Soil Advisor Outputs
  var isAdvisoryLoading by mutableStateOf(false)
  var advisoryResult by mutableStateOf<SoilAdvisory?>(null)

  // Crop Health Logger State
  var diagnosticReport by mutableStateOf<HealthReport?>(null)
  var simulatedImageName by mutableStateOf<String?>(null)
  var isRecordingVoice by mutableStateOf(false)
  var recordedQueryText by mutableStateOf<String?>(null)

  // Alerts & History Lists
  val alertList = listOf(
    AlertItem(
      1,
      "Dry-spell warning dispatched via Voice Call",
      "Regional IVR system broadcast to all registered smallholders in block.",
      "VOICE",
      "10 mins ago",
      "IVR Broadcast Script: 'Alert! Low rainfall forecast for next 14 days in your village. Prepare drip channels and conserve ground water. Tap 1 to repeat.'"
    ),
    AlertItem(
      2,
      "Pest Outbreak: Fall Armyworm detected",
      "Preemptive SMS advice sent to fields in 5km radius.",
      "SMS",
      "2 hours ago",
      "SMS Broadcast: 'RSK ALERT: Fall Armyworm pest detected in neighboring cotton fields. Check leaf undersides for white egg sacks. Spray Neem oil immediately.'"
    ),
    AlertItem(
      3,
      "Market Price Alert: Wheat Price Up",
      "Mandiga update sent to farmers.",
      "SMS",
      "Yesterday",
      "SMS Broadcast: 'Mandiga Market Price update: Wheat price increased by ₹50 to ₹2,450 per quintal. Demand remains strong.'"
    )
  )

  val historyLogs = listOf(
    HistoryLog(
      1,
      "Outgoing Call Completed - Pest Warning",
      "CALL",
      "Completed",
      "3 mins ago",
      "IVR Voice warning played successfully to farmer mobile (Duration: 42s). Farmer pressed '1' to confirm receipt."
    ),
    HistoryLog(
      2,
      "SMS Sent - Market Price Update",
      "SMS",
      "Delivered",
      "1 hour ago",
      "Automated regional wholesale market pricing sent in Kannada/Telugu. System delivery confirmed by network operator."
    ),
    HistoryLog(
      3,
      "Outgoing Call - Flash Flood Broadcast",
      "CALL",
      "Completed",
      "Yesterday, 5:30 PM",
      "Emergency warning issued due to sudden canal breach upstream. Playback duration: 60s. High priority confirmation received."
    ),
    HistoryLog(
      4,
      "SMS Sent - Heavy Rain Precaution",
      "SMS",
      "Delivered",
      "2 days ago",
      "Regional weather department warning: Avoid harvesting grain during the upcoming 48 hours to prevent damp rot."
    )
  )

  fun runSoilAdvisory(scope: kotlinx.coroutines.CoroutineScope) {
    scope.launch {
      isAdvisoryLoading = true
      advisoryResult = null
      delay(1500) // Simulate processing time
      
      advisoryResult = when (selectedSoilType) {
        "Clayey" -> SoilAdvisory(
          cropName = "Paddy Rice (Basmati)",
          matchPercentage = "94% Match",
          plantingWindow = "Next 10 Days (Ideal moisture)",
          details = "Clayey soil retains water perfectly for paddy cultivation. Your soil has high water retention. Your N-P-K levels are highly favorable.",
          guidelines = listOf(
            "Prepare the soil with 5-10cm standing water.",
            "Utilize localized organic urea based on current nitrogen (65 ppm).",
            "Maintain bund height of 15cm to prevent surface runoff."
          )
        )
        "Sandy" -> SoilAdvisory(
          cropName = "Groundnut (Peanut)",
          matchPercentage = "89% Match",
          plantingWindow = "Next 15 Days",
          details = "Sandy soil drains rapidly, preventing seed rot in groundnuts. Ensure light, frequent sprinkling during pod development.",
          guidelines = listOf(
            "Sow seeds at 5cm depth with 10cm row spacing.",
            "Add organic compost to improve soil nutrient holding capacity.",
            "Apply gypsum at flowering stage for strong shell development."
          )
        )
        "Loamy" -> SoilAdvisory(
          cropName = "Maize (Hybrid Corn)",
          matchPercentage = "95% Match",
          plantingWindow = "Next 7 Days (Optimal)",
          details = "Loamy soil provides rich aeration and ideal drainage for maize roots. Your soil pH is neutral (6.5), which is ideal.",
          guidelines = listOf(
            "Incorporate initial nitrogen dose prior to sowing.",
            "Keep soil consistently moist but never waterlogged.",
            "Maintain optimal weed-free intervals for first 30 days."
          )
        )
        "Red" -> SoilAdvisory(
          cropName = "Millets (Ragi / Sorghum)",
          matchPercentage = "92% Match",
          plantingWindow = "Flexible (Highly drought resistant)",
          details = "Red soil has high iron content but lower moisture storage. Millets are robust and require minimal water, matching current groundwater status.",
          guidelines = listOf(
            "Prepare light ridges for optimal soil water conservation.",
            "Minimal potassium supplementation is needed.",
            "Highly adaptive to low soil moisture conditions."
          )
        )
        "Black" -> SoilAdvisory(
          cropName = "Cotton (Bt Cotton)",
          matchPercentage = "96% Match",
          plantingWindow = "Next 5 Days",
          details = "Deep black soil is rich in clay minerals and retains moisture beautifully for long-cycle crops like Cotton. pH of 6.5 is excellent.",
          guidelines = listOf(
            "Ensure deep plowing before planting seeds.",
            "Maintain spacing of 90cm between rows.",
            "Monitor closely for bollworm outbreaks during humid spells."
          )
        )
        else -> SoilAdvisory(
          cropName = "Sorghum",
          matchPercentage = "88% Match",
          plantingWindow = "Next 14 Days",
          details = "An excellent robust crop that thrives in a wide range of soil conditions with medium inputs.",
          guidelines = listOf(
            "Excellent resistance to low rain.",
            "Avoid deep watering.",
            "Apply balanced nitrogen."
          )
        )
      }
      isAdvisoryLoading = false
    }
  }

  fun simulateUploadLeaf(type: String) {
    simulatedImageName = type
    diagnosticReport = when (type) {
      "HEALTHY" -> HealthReport(
        diseaseName = "Healthy Leaf (No disease detected)",
        severityPercent = 0.0f,
        remediesList = listOf(
          "Maintain current watering schedule.",
          "Check weekly for early signs of fungal spots.",
          "Keep fertilizer application organic and balanced."
        ),
        imageType = "HEALTHY"
      )
      "BLAST" -> HealthReport(
        diseaseName = "Rice Blast (Fungal Disease)",
        severityPercent = 0.38f,
        remediesList = listOf(
          "Apply recommended organic copper fungicide or Tricyclazole 75 WP.",
          "Avoid flooding fields excessively; maintain optimal moisture levels.",
          "Remove and burn highly infested plant residue to check the spread of spores."
        ),
        imageType = "BLAST"
      )
      "BLIGHT" -> HealthReport(
        diseaseName = "Bacterial Leaf Blight (Xanthomonas oryzae)",
        severityPercent = 0.65f,
        remediesList = listOf(
          "Drain field immediately to stop standing water transmission of bacteria.",
          "Apply customized stable bleaching powder (100g/acre) or consulting agricultural scientist.",
          "Reduce nitrogenous fertilizer application until field fully recovers."
        ),
        imageType = "BLIGHT"
      )
      else -> null
    }
  }

  fun simulateVoiceRecording(scope: kotlinx.coroutines.CoroutineScope) {
    scope.launch {
      isRecordingVoice = true
      recordedQueryText = null
      delay(2000) // Simulate recording time
      isRecordingVoice = false
      recordedQueryText = "How do I protect my newly planted maize crops from wild boars and root rot during early heavy rain?"
      
      diagnosticReport = HealthReport(
        diseaseName = "Spoken Query Advisory: Maize Root Rot Prevention",
        severityPercent = 0.25f,
        remediesList = listOf(
          "Ensure trench drainage is cleared immediately to prevent pooling water.",
          "Construct a simple reflective solar ribbon fence to humanely deter wild boars.",
          "Apply a light neem cake soil dressing to strengthen root structure naturally."
        ),
        imageType = "HEALTHY"
      )
    }
  }
}

// --------------------------------------------------------------------
// NATIVE ACTIVITY INITIALIZATION
// --------------------------------------------------------------------
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
  val deepGreen = Color(0xFF1B4332)
  val softMint = Color(0xFF52B788)
  val warningGold = Color(0xFFF59E0B)

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

// --------------------------------------------------------------------
// REUSABLE UPPER APP HEADER (AppName + Language Dropdown)
// --------------------------------------------------------------------
@Composable
fun KisanHeader(
  selectedLanguage: AppLanguage,
  onLanguageSelected: (AppLanguage) -> Unit
) {
  var showLangDropdown by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF1E293B)) // Slate card
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // High-contrast leafy icon representing agriculture and safety
        Box(
          modifier = Modifier
            .size(36.dp)
            .background(Color(0xFF1B4332), shape = CircleShape) // forest green
            .border(2.dp, Color(0xFF52B788), shape = CircleShape), // soft mint
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Agriculture,
            contentDescription = "App Leaf Logo",
            tint = Color(0xFF52B788),
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = LocalizedStrings.get("app_name", selectedLanguage),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.testTag("app_title")
          )
          Text(
            text = LocalizedStrings.get("tagline", selectedLanguage),
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Indicator language selector button (high touch readability)
      Box {
        OutlinedButton(
          onClick = { showLangDropdown = true },
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF52B788)
          ),
          border = borderStrokeForReadability(),
          modifier = Modifier.testTag("language_selector")
        ) {
          Icon(Icons.Default.Language, contentDescription = "Language", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = selectedLanguage.nativeName,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
        }

        DropdownMenu(
          expanded = showLangDropdown,
          onDismissRequest = { showLangDropdown = false },
          modifier = Modifier.background(Color(0xFF1E293B))
        ) {
          AppLanguage.values().forEach { language ->
            DropdownMenuItem(
              text = {
                Text(
                  text = language.nativeName,
                  color = if (language == selectedLanguage) Color(0xFF52B788) else Color.White,
                  fontWeight = if (language == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 14.sp
                )
              },
              onClick = {
                onLanguageSelected(language)
                showLangDropdown = false
              }
            )
          }
        }
      }
    }
  }
}

private fun borderStrokeForReadability() = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788))

// --------------------------------------------------------------------
// SCREEN 1: HOME DASHBOARD (Live parameter metrics & Emergency Alert Broadcast list)
// --------------------------------------------------------------------
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
              border = androidx.compose.foundation.BorderStroke(
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
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788)),
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

@Composable
fun FarmMetricCard(
  modifier: Modifier = Modifier,
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  borderColor: Color
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
    border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
    modifier = modifier,
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = iconTint,
        modifier = Modifier.size(32.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = title,
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = value,
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }
    }
  }
}

@Composable
fun DashboardCanvasIllustration() {
  Canvas(
    modifier = Modifier
      .fillMaxWidth()
      .height(110.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))
        )
      )
  ) {
    val width = size.width
    val height = size.height

    // Draw glowing sunset/sunrise in background
    drawCircle(
      color = Color(0xFFFEF08A).copy(alpha = 0.2f),
      radius = 90.dp.toPx(),
      center = Offset(width * 0.85f, height * 0.2f)
    )
    drawCircle(
      color = Color(0xFFFBBF24).copy(alpha = 0.4f),
      radius = 50.dp.toPx(),
      center = Offset(width * 0.85f, height * 0.2f)
    )

    // Draw rolling agricultural hills
    val hill1 = Path().apply {
      moveTo(0f, height)
      quadraticTo(width * 0.25f, height * 0.6f, width * 0.6f, height)
      close()
    }
    drawPath(
      path = hill1,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1B4332), Color(0xFF0F172A))
      )
    )

    val hill2 = Path().apply {
      moveTo(width * 0.4f, height)
      quadraticTo(width * 0.75f, height * 0.45f, width, height)
      lineTo(width, height)
      lineTo(width * 0.4f, height)
      close()
    }
    drawPath(
      path = hill2,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2D6A4F), Color(0xFF1B4332))
      )
    )

    // Draw grid patterns representing crop rows
    drawArc(
      color = Color(0xFF52B788).copy(alpha = 0.2f),
      startAngle = 180f,
      sweepAngle = 90f,
      useCenter = false,
      size = Size(120.dp.toPx(), 120.dp.toPx()),
      topLeft = Offset(-30.dp.toPx(), 60.dp.toPx()),
      style = Stroke(width = 2.dp.toPx())
    )

    // Glowing notification pulse overlay representing warnings
    drawCircle(
      color = Color(0xFFF59E0B).copy(alpha = 0.15f),
      radius = 25.dp.toPx(),
      center = Offset(width * 0.15f, height * 0.4f)
    )
    drawCircle(
      color = Color(0xFFF59E0B),
      radius = 4.dp.toPx(),
      center = Offset(width * 0.15f, height * 0.4f)
    )
  }
}

// --------------------------------------------------------------------
// SCREEN 2: CROP RECOMMENDATION (Form metrics & AI recommendations mockup)
// --------------------------------------------------------------------
@Composable
fun CropRecommendationScreen(viewModel: KisanAlertViewModel, scope: kotlinx.coroutines.CoroutineScope) {
  val lang = viewModel.currentLanguage
  val soilTypes = listOf("Clayey", "Sandy", "Loamy", "Red", "Black")
  var showSoilDropdown by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Soil Parameters Form Card
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Info, contentDescription = "Soil Parameters", tint = Color(0xFF52B788))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = LocalizedStrings.get("soil_params", lang),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }

        HorizontalDivider(color = Color(0xFF334155))

        // Soil Type Input (High contrast touch dropdown)
        Text(
          text = LocalizedStrings.get("soil_type", lang),
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )

        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = viewModel.selectedSoilType,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showSoilDropdown = true }
              .testTag("soil_type_dropdown"),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
              disabledTextColor = Color.White,
              disabledBorderColor = Color(0xFF52B788),
              disabledTrailingIconColor = Color(0xFF52B788)
            ),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown") }
          )

          DropdownMenu(
            expanded = showSoilDropdown,
            onDismissRequest = { showSoilDropdown = false },
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .background(Color(0xFF1E293B))
          ) {
            soilTypes.forEach { type ->
              DropdownMenuItem(
                text = { Text(type, color = Color.White, fontSize = 16.sp) },
                onClick = {
                  viewModel.selectedSoilType = type
                  showSoilDropdown = false
                }
              )
            }
          }
        }

        // NPK Inputs (Side-by-side with high target sizing)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = viewModel.nitrogen,
            onValueChange = { viewModel.nitrogen = it },
            label = { Text(LocalizedStrings.get("nitrogen", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("nitrogen_input")
          )

          OutlinedTextField(
            value = viewModel.phosphorus,
            onValueChange = { viewModel.phosphorus = it },
            label = { Text(LocalizedStrings.get("phosphorus", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("phosphorus_input")
          )

          OutlinedTextField(
            value = viewModel.potassium,
            onValueChange = { viewModel.potassium = it },
            label = { Text(LocalizedStrings.get("potassium", lang), fontSize = 11.sp, maxLines = 1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF52B788),
              unfocusedBorderColor = Color(0xFF334155),
              focusedLabelColor = Color(0xFF52B788),
              unfocusedLabelColor = Color(0xFF94A3B8),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("potassium_input")
          )
        }

        // pH Level Slider Input (large and visual)
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = LocalizedStrings.get("ph_level", lang),
              color = Color(0xFF94A3B8),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = String.format("%.1f", viewModel.phLevel),
              color = Color(0xFF52B788),
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
          Slider(
            value = viewModel.phLevel,
            onValueChange = { viewModel.phLevel = it },
            valueRange = 4.0f..9.0f,
            colors = SliderDefaults.colors(
              thumbColor = Color(0xFF52B788),
              activeTrackColor = Color(0xFF52B788),
              inactiveTrackColor = Color(0xFF334155)
            ),
            modifier = Modifier.testTag("ph_slider")
          )
        }

        // ACTION BUTTON
        Button(
          onClick = { viewModel.runSoilAdvisory(scope) },
          enabled = !viewModel.isAdvisoryLoading,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1B4332), // Forest green
            contentColor = Color.White
          ),
          border = borderStrokeForReadability(),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("run_advisory_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (viewModel.isAdvisoryLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Analyzing Soil...", fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.Agriculture, contentDescription = "Run Advisory", tint = Color(0xFFFBBF24))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = LocalizedStrings.get("run_advisory", lang),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp
            )
          }
        }
      }
    }

    // AI Crop Recommendation Placeholder Output Container
    Column {
      Text(
        text = LocalizedStrings.get("advisory_result", lang).uppercase(),
        color = Color(0xFF52B788),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
      )

      AnimatedVisibility(
        visible = viewModel.advisoryResult != null,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut()
      ) {
        viewModel.advisoryResult?.let { advisory ->
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Match Success",
                    tint = Color(0xFF52B788),
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = advisory.cropName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }
                Box(
                  modifier = Modifier
                    .background(Color(0xFF14532D), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = advisory.matchPercentage,
                    color = Color(0xFF52B788),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Text(
                text = "Optimal Planting Window: ${advisory.plantingWindow}",
                color = Color(0xFFFBBF24), // Harvest gold highlight
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )

              Text(
                text = advisory.details,
                color = Color(0xFFF1F5F9),
                fontSize = 14.sp
              )

              HorizontalDivider(color = Color(0xFF334155))

              Text(
                text = "Scientific Local Guidelines:",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )

              advisory.guidelines.forEachIndexed { i, rule ->
                Row(modifier = Modifier.fillMaxWidth()) {
                  Text(
                    text = "${i + 1}. ",
                    color = Color(0xFF52B788),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                  Text(
                    text = rule,
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))
              
              // Educational block demonstrating API integration
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
                    text = "/* Gemini API Hook:\nIn production, we package Soil Type (${viewModel.selectedSoilType}), N: ${viewModel.nitrogen}, P: ${viewModel.phosphorus}, K: ${viewModel.potassium}, and pH: ${viewModel.phLevel} into a structured prompt and invoke the Gemini-2.5-flash model via Retrofit/Ktor to generate dynamic localized advisory summaries in the selected regional dialect. */",
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

      if (viewModel.advisoryResult == null) {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              Icons.Default.Agriculture,
              contentDescription = "Empty Advisory Indicator",
              tint = Color(0xFF334155),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = LocalizedStrings.get("advisory_placeholder", lang),
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
}

// --------------------------------------------------------------------
// SCREEN 3: CROP HEALTH LOGGER (Image visual diagnostics & Audio voice transcriptions)
// --------------------------------------------------------------------
@Composable
fun CropHealthLoggerScreen(viewModel: KisanAlertViewModel, scope: kotlinx.coroutines.CoroutineScope) {
  val lang = viewModel.currentLanguage
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
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
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
            onClick = { viewModel.simulateVoiceRecording(scope) },
            colors = ButtonDefaults.buttonColors(
              containerColor = if (viewModel.isRecordingVoice) Color(0xFF78350F) else Color(0xFF1E293B)
            ),
            border = androidx.compose.foundation.BorderStroke(
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
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)),
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
            border = androidx.compose.foundation.BorderStroke(
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
                Column {
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

      if (viewModel.diagnosticReport == null) {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
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
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788)),
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
                viewModel.simulateUploadLeaf("HEALTHY")
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
                viewModel.simulateUploadLeaf("BLAST")
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
                viewModel.simulateUploadLeaf("BLIGHT")
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

// Custom Leaf visualizer Canvas drawing representing visual diagnostics
@Composable
fun LeafDiagnosticCanvas(imageType: String) {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Draw main leaf outline path
    val leafPath = Path().apply {
      moveTo(w * 0.5f, h * 0.15f)
      cubicTo(w * 0.85f, h * 0.35f, w * 0.75f, h * 0.8f, w * 0.5f, h * 0.85f)
      cubicTo(w * 0.25f, h * 0.8f, w * 0.15f, h * 0.35f, w * 0.5f, h * 0.15f)
    }

    // Determine color based on plant status
    val leafColor = when (imageType) {
      "HEALTHY" -> Color(0xFF2D6A4F)
      "BLAST" -> Color(0xFF40916C)
      "BLIGHT" -> Color(0xFF52B788)
      else -> Color(0xFF1B4332)
    }

    // Draw solid leaf body
    drawPath(path = leafPath, color = leafColor)

    // Draw leaf central stem/vein
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.5f),
      start = Offset(w * 0.5f, h * 0.15f),
      end = Offset(w * 0.5f, h * 0.85f),
      strokeWidth = 3.dp.toPx()
    )

    // Draw secondary veins
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.35f),
      end = Offset(w * 0.68f, h * 0.45f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.35f),
      end = Offset(w * 0.32f, h * 0.45f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.55f),
      end = Offset(w * 0.72f, h * 0.65f),
      strokeWidth = 2.dp.toPx()
    )
    drawLine(
      color = Color(0xFFD8F3DC).copy(alpha = 0.3f),
      start = Offset(w * 0.5f, h * 0.55f),
      end = Offset(w * 0.28f, h * 0.65f),
      strokeWidth = 2.dp.toPx()
    )

    // Draw simulated spots / disease anomalies on leaf Canvas overlay
    if (imageType == "BLAST") {
      // Rice Blast distinct brown/grey elliptical lesion spots
      drawCircle(
        color = Color(0xFF78350F),
        radius = 8.dp.toPx(),
        center = Offset(w * 0.42f, h * 0.48f)
      )
      drawCircle(
        color = Color(0xFF94A3B8),
        radius = 4.dp.toPx(),
        center = Offset(w * 0.42f, h * 0.48f)
      )

      drawCircle(
        color = Color(0xFF78350F),
        radius = 12.dp.toPx(),
        center = Offset(w * 0.62f, h * 0.58f)
      )
      drawCircle(
        color = Color(0xFF94A3B8),
        radius = 6.dp.toPx(),
        center = Offset(w * 0.62f, h * 0.58f)
      )

      drawCircle(
        color = Color(0xFF78350F),
        radius = 7.dp.toPx(),
        center = Offset(w * 0.38f, h * 0.32f)
      )
    }

    if (imageType == "BLIGHT") {
      // Bacterial Blight long golden yellow stripes along the margins
      val blightMarginPathRight = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        cubicTo(w * 0.72f, h * 0.35f, w * 0.68f, h * 0.7f, w * 0.5f, h * 0.85f)
        cubicTo(w * 0.64f, h * 0.72f, w * 0.68f, h * 0.42f, w * 0.5f, h * 0.15f)
      }
      drawPath(path = blightMarginPathRight, color = Color(0xFFF59E0B).copy(alpha = 0.8f))

      val blightMarginPathLeft = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        cubicTo(w * 0.28f, h * 0.35f, w * 0.32f, h * 0.7f, w * 0.5f, h * 0.85f)
        cubicTo(w * 0.36f, h * 0.72f, w * 0.32f, h * 0.42f, w * 0.5f, h * 0.15f)
      }
      drawPath(path = blightMarginPathLeft, color = Color(0xFFFBBF24).copy(alpha = 0.6f))
    }
  }
}

// --------------------------------------------------------------------
// SCREEN 4: RSK WARNING SYSTEM LOGS (Historical outreach broadcasts)
// --------------------------------------------------------------------
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
      border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788)),
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
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
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
          border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF52B788)),
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

            Button(
              onClick = { selectedLogForDialog = null },
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
}
