package com.kodemukti.namaibayi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyDialog(onAcknowledge: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onAcknowledge) {
                Text("Saya Mengerti", fontWeight = FontWeight.SemiBold)
            }
        },
        title = {
            Text(
                text = "Kebijakan Privasi",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Terima kasih telah menggunakan NamAI Bayi. Berikut adalah informasi mengenai privasi Anda:",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "1. Informasi yang Anda berikan (nama, preferensi, agama, budaya) hanya digunakan untuk memberikan rekomendasi nama bayi yang sesuai.",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "2. Riwayat konsultasi disimpan secara lokal di perangkat Anda dan tidak dikirim ke server pihak ketiga.",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "3. Data tidak dibagikan kepada pihak manapun tanpa persetujuan Anda.",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "4. Jika menggunakan layanan AI online (Gemini API), data akan diproses oleh penyedia layanan sesuai kebijakan privasi mereka.",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Dengan menggunakan aplikasi ini, Anda menyetujui ketentuan di atas.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    )
}
