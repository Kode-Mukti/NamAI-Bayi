package com.kodemukti.namaibayi.ui.screens.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    nameRecommendationId: String,
    onBack: () -> Unit = {},
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(nameRecommendationId) {
        viewModel.loadDetail(nameRecommendationId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Detail Nama", fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                    )
                }
            },
            actions = {
                if (uiState is UiState.Success) {
                    val name = (uiState as UiState.Success<AIBabyName>).data
                    IconButton(onClick = {
                        val shareText = """
                            Rekomendasi Nama dari NamAI Bayi:
                            Nama: ${name.name}
                            Nama Lengkap: ${name.fullNameSuggestion}
                            Makna: ${name.meaning}
                            Filosofi: ${name.philosophy}
                            
                            Temukan lebih banyak di NamAI Bayi!
                        """.trimIndent()
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Bagikan Nama"))
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            is UiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    DetailContent(
                        name = state.data,
                        isFavorite = isFavorite,
                        onToggleFavorite = { viewModel.toggleFavorite(state.data) }
                    )
                }
            }

            is UiState.Idle -> {}
        }
    }
}

@Composable
private fun DetailContent(
    name: AIBabyName,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name.name,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            if (name.fullNameSuggestion.isNotBlank()) {
                Text(
                    text = name.fullNameSuggestion,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${(name.score * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Match",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Badge(name.strategyUsed.replace("_", " ").uppercase())
        if (name.nickname.isNotBlank()) {
            Badge("Panggilan: ${name.nickname}", MaterialTheme.colorScheme.tertiaryContainer)
        }
    }

    Spacer(Modifier.height(24.dp))

    SectionTitle("Makna & Filosofi")
    Text(
        text = name.meaning,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
    )
    if (name.philosophy.isNotBlank()) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = name.philosophy,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(Modifier.height(24.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow("Asal Bahasa", name.origin)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow("Pengucapan", name.pronunciationGuide)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow("Skor Keunikan", "${name.uniquenessScore}/100")
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow("Keterbacaan Int'l", name.internationalReadability.ifBlank { "-" })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow("Kocok Saudara", name.siblingCompatibility.ifBlank { "-" })
        }
    }

    Spacer(Modifier.height(24.dp))

    SectionTitle("Mengapa AI Merekomendasikan?")
    Text(
        text = name.reasoning,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(Modifier.height(24.dp))

    SectionTitle("Konteks Budaya")
    Text(
        text = name.culturalContext,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (name.alternativeSpellings.isNotEmpty()) {
        Spacer(Modifier.height(24.dp))
        SectionTitle("Ejaan Alternatif")
        Text(
            text = name.alternativeSpellings.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    Spacer(Modifier.height(32.dp))

    FilledTonalButton(
        onClick = onToggleFavorite,
        modifier = Modifier.fillMaxWidth().height(52.dp),
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(if (isFavorite) "Tersimpan di Favorit" else "Simpan ke Favorit")
    }

    Spacer(Modifier.height(80.dp))
}

@Composable
private fun Badge(text: String, containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondaryContainer) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
