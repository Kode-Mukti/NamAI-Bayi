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
            appendLine("1. Berikan 5 rekomendasi nama bayi terbaik berdasarkan preferensi yang diberikan.")
            appendLine("2. Setiap nama harus memiliki makna yang jelas, mendalam, dan filosofi yang kuat.")
            appendLine("3. Berikan saran nama lengkap (2-3 kata) yang harmonis.")
            appendLine("4. Berikan saran nama panggilan (nickname) yang manis.")
            appendLine("5. Evaluasi keterbacaan internasional dan kecocokan dengan saudara (jika ada).")
            appendLine("6. Berikan skor keunikan (1-100).")
            appendLine("7. PASTIKAN setiap nama SESUAI dengan agama yang dipilih pengguna.")
            appendLine("   - Jika pengguna memilih ISLAM, jangan berikan nama yang berasal dari tradisi Kristen/Katolik, Hindu, atau Buddha.")
            appendLine("   - Jika pengguna memilih KRISTEN/KATOLIK, jangan berikan nama yang khas Islam atau Hindu/Buddha.")
            appendLine("   - Jika pengguna memilih HINDU/BUDDHA/KONGHUCU, jangan berikan nama yang khas Islam atau Kristen/Katolik.")
            appendLine("8. Agama pengguna ADALAH PRIORITAS UTAMA — jika agama bertentangan dengan budaya, agama yang menang.")
            appendLine()
            appendLine("PREFERENSI PENGGUNA:")
            appendLine("- Jenis Kelamin: ${profile.gender.name.lowercase()}")
            
            if (request.additionalPreferences.isNotEmpty()) {
                request.additionalPreferences.forEach { pref ->
                    appendLine("- $pref")
                }
            }

            request.strategy?.let {
                appendLine("- Strategi Penamaan: ${it.name.lowercase()}")
            }

            appendLine()
            appendLine("RESPON DALAM FORMAT JSON SAJA DENGAN STRUKTUR BERIKUT:")
            appendLine("""
{
  "recommendations": [
    {
      "name": "string",
      "fullNameSuggestion": "string",
      "meaning": "string",
      "philosophy": "string (filosofi mendalam)",
      "nickname": "string",
      "origin": "string",
      "pronunciation": "string",
      "cultural_context": "string",
      "alternative_spellings": ["string"],
      "uniquenessScore": int (1-100),
      "internationalReadability": "string",
      "siblingCompatibility": "string",
      "popularity_rank": int,
      "score": float (0.0-1.0),
      "strategy_used": "string",
      "reasoning": "string (mengapa AI merekomendasikan ini)"
    }
  ]
}
            """.trimIndent())
        }
    }
}
