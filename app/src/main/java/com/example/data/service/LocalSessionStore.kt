package com.example.data.service

import android.content.Context
import com.example.data.model.FarmerProfile
import com.example.util.AppLanguage
import org.json.JSONObject

data class StoredSessionState(
  val profile: FarmerProfile?,
  val currentLanguage: AppLanguage,
  val isAutoSpeakEnabled: Boolean
)

class LocalSessionStore(context: Context) {
  private val prefs = context.getSharedPreferences("kisan_alert_session", Context.MODE_PRIVATE)

  fun loadState(): StoredSessionState {
    val profile = loadProfile()
    val languageCode = prefs.getString(KEY_LANGUAGE, profile?.preferredLanguage?.code ?: AppLanguage.ENGLISH.code)
    val currentLanguage = AppLanguage.values().firstOrNull { it.code == languageCode } ?: AppLanguage.ENGLISH
    val isAutoSpeakEnabled = prefs.getBoolean(KEY_AUTO_SPEAK, true)

    return StoredSessionState(
      profile = profile,
      currentLanguage = currentLanguage,
      isAutoSpeakEnabled = isAutoSpeakEnabled
    )
  }

  fun saveState(
    profile: FarmerProfile?,
    currentLanguage: AppLanguage,
    isAutoSpeakEnabled: Boolean
  ) {
    prefs.edit().apply {
      putString(KEY_LANGUAGE, currentLanguage.code)
      putBoolean(KEY_AUTO_SPEAK, isAutoSpeakEnabled)
      if (profile == null) {
        remove(KEY_PROFILE)
      } else {
        putString(
          KEY_PROFILE,
          JSONObject()
            .put("name", profile.name)
            .put("phone", profile.phone)
            .put("district", profile.district)
            .put("preferredLanguage", profile.preferredLanguage.code)
            .put("trustedHelperPhone", profile.trustedHelperPhone)
            .put("primaryCrop", profile.primaryCrop)
            .toString()
        )
      }
      apply()
    }
  }

  fun clear() {
    prefs.edit().clear().apply()
  }

  private fun loadProfile(): FarmerProfile? {
    val raw = prefs.getString(KEY_PROFILE, null) ?: return null
    return runCatching {
      val json = JSONObject(raw)
      FarmerProfile(
        name = json.optString("name", "Farmer"),
        phone = json.optString("phone"),
        district = json.optString("district", "Anantapur"),
        preferredLanguage = AppLanguage.values().firstOrNull {
          it.code == json.optString("preferredLanguage", AppLanguage.ENGLISH.code)
        } ?: AppLanguage.ENGLISH,
        trustedHelperPhone = json.optString("trustedHelperPhone", "+919000011223"),
        primaryCrop = json.optString("primaryCrop", "Millet")
      )
    }.getOrNull()
  }

  private companion object {
    const val KEY_PROFILE = "farmer_profile"
    const val KEY_LANGUAGE = "current_language"
    const val KEY_AUTO_SPEAK = "auto_speak_enabled"
  }
}
