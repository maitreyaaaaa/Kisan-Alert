package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceManager(context: Context) : TextToSpeech.OnInitListener {
  private var tts: TextToSpeech = TextToSpeech(context, this)
  private var isInitialized = false

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      isInitialized = true
    }
  }

  fun speak(text: String, language: AppLanguage) {
    if (!isInitialized) return

    val locale = when (language) {
      AppLanguage.ENGLISH -> Locale.ENGLISH
      AppLanguage.HINDI -> Locale("hi", "IN")
      AppLanguage.TELUGU -> Locale("te", "IN")
      AppLanguage.KANNADA -> Locale("kn", "IN")
      AppLanguage.TAMIL -> Locale("ta", "IN")
    }

    tts.language = locale
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
  }

  fun stop() {
    if (isInitialized) {
      tts.stop()
    }
  }

  fun shutdown() {
    tts.shutdown()
  }
}
