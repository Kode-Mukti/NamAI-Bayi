package com.kodemukti.namaibayi.core.common

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIErrorLogger @Inject constructor() {

    fun logProviderError(tag: String, errorType: String, cause: Throwable?) {
        val message = when (errorType) {
            "TIMEOUT" -> "AI provider request timed out"
            "INVALID_RESPONSE" -> "AI provider returned invalid/unparseable response"
            "RATE_LIMIT" -> "AI provider rate limit hit"
            "VALIDATION_FAILED" -> "AI response failed schema validation"
            "PROVIDER_DOWN" -> "AI provider unavailable"
            else -> "AI provider error: $errorType"
        }
        Log.e(tag, message, cause)
    }

    fun sanitizeForUser(errorType: String): String {
        return when (errorType) {
            "TIMEOUT" -> "Koneksi terputus. Silakan coba lagi."
            "RATE_LIMIT" -> "Terlalu banyak permintaan. Silakan tunggu sebentar."
            "PROVIDER_DOWN" -> "Layanan sedang sibuk. Silakan coba lagi nanti."
            else -> "Gagal menghasilkan nama. Silakan coba lagi."
        }
    }
}
