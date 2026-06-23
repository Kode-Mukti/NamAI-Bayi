package com.kodemukti.namaibayi.core.common

object Constants {
    const val BASE_URL = "https://api.namaibayi.com/"
    const val DATABASE_NAME = "namaibayi.db"
    const val PREF_NAME = "namaibayi_prefs"

    const val DEFAULT_TEMPERATURE = 0.7f
    const val DEFAULT_MAX_TOKENS = 1024

    const val PAGE_SIZE = 20
    const val DEBOUNCE_DELAY = 300L

    // Gemini API
    // Baca dari local.properties —> BuildConfig.GEMINI_API_KEY
    // Cara setting: buka file local.properties (di root project), tambah baris:
    //   gemini.api.key=AIzaSy...
    const val GEMINI_API_KEY = ""
    const val GEMINI_MODEL = "gemma-4-31b-it"
    const val GEMINI_MAX_TOKENS = 2048
}
