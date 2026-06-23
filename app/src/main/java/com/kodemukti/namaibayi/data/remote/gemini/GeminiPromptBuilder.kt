package com.kodemukti.namaibayi.data.remote.gemini

import com.kodemukti.namaibayi.domain.model.GenerateRequest
import javax.inject.Inject

class GeminiPromptBuilder @Inject constructor() {

    fun build(request: GenerateRequest): String {
        val profile = request.babyProfile

        return buildString {
            appendLine("Anda adalah Konsultan Nama Bayi AI yang sangat berpengalaman untuk orang tua Indonesia.")
            appendLine("Anda memiliki pengetahuan mendalam tentang 300+ suku dan budaya Indonesia, 6 agama resmi, 700+ bahasa daerah, dan tren nama Indonesia modern hingga tradisional.")
            appendLine()
            appendLine("TUGAS ANDA:")
            appendLine("1. Berikan 5 rekomendasi nama bayi berdasarkan preferensi berikut")
            appendLine("2. Setiap nama harus memiliki makna yang jelas dan mendalam")
            appendLine("3. Cantumkan asal-usul bahasa masing-masing nama")
            appendLine("4. Berikan skor kecocokan 0.0 - 1.0 untuk setiap nama")
            appendLine("5. Jelaskan alasan merekomendasikan setiap nama")
            appendLine("6. Variasikan asal-usul nama dalam 5 rekomendasi")
            appendLine()
            appendLine("DATA CALON ORANG TUA:")
            appendLine("- Nama yang dicari: ${profile.name}")
            appendLine("- Jenis Kelamin: ${profile.gender.name.lowercase()}")
            appendLine("- Bahasa Preferensi: ${profile.preferredLanguage.name.lowercase()}")

            request.strategy?.let {
                appendLine("- Strategi Penamaan: ${it.name.lowercase()}")
            }

            if (request.additionalPreferences.isNotEmpty()) {
                appendLine("- Preferensi Tambahan: ${request.additionalPreferences.joinToString(", ")}")
            }

            appendLine()
            appendLine("RESPON DALAM FORMAT JSON SAJA:")
            appendLine("""
{
  "recommendations": [
    {
      "name": "string",
      "meaning": "string",
      "origin": "string (asal bahasa/budaya)",
      "pronunciation": "string",
      "cultural_context": "string",
      "alternative_spellings": ["string"],
      "popularity_rank": int (1-500) atau null,
      "score": float (0.0-1.0),
      "strategy_used": "string",
      "reasoning": "string (penjelasan panjang)"
    }
  ]
}
            """.trimIndent())
        }
    }
}
