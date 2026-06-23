package com.kodemukti.namaibayi.data.remote.gemini

object GeminiConfig {
    const val BASE_URL = "https://generativelanguage.googleapis.com/"
    const val MODEL = "gemma-4-31b-it"
    const val FALLBACK_MODEL = "gemini-2.0-flash"

    const val TEMPERATURE = 0.7f
    const val MAX_OUTPUT_TOKENS = 2048
    const val TOP_P = 0.95f
}
