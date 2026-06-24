package com.kodemukti.namaibayi.ui.screens.generate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.ui.viewmodel.GenerateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onNavigateToResult: (String) -> Unit = {},
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val profileId = viewModel.generatedProfileId.value
            if (profileId != null) {
                onNavigateToResult(profileId)
                viewModel.resetState()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(8.dp))

        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Konsultasi Nama",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Semua kolom di bawah ini opsional",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )

        Spacer(Modifier.height(16.dp))

        // Nama Ayah & Ibu
        PreferenceTextField(
            label = "Nama Ayah",
            value = formState.fatherName,
            onValueChange = viewModel::updateFatherName,
            placeholder = "Contoh: Pratama"
        )

        PreferenceTextField(
            label = "Nama Ibu",
            value = formState.motherName,
            onValueChange = viewModel::updateMotherName,
            placeholder = "Contoh: Aisyah"
        )

        PreferenceTextField(
            label = "Nama Bayi (Opsional)",
            value = formState.name,
            onValueChange = viewModel::updateName,
            placeholder = "Contoh: Arkana"
        )

        // Jenis Kelamin
        Text(
            text = "Jenis Kelamin",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
        )
        Row(
            Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GenderOption("Laki-laki", formState.gender == Gender.MALE) { viewModel.updateGender(Gender.MALE) }
            GenderOption("Perempuan", formState.gender == Gender.FEMALE) { viewModel.updateGender(Gender.FEMALE) }
            GenderOption("Netral", formState.gender == Gender.NEUTRAL) { viewModel.updateGender(Gender.NEUTRAL) }
        }

        Spacer(Modifier.height(16.dp))

        // Agama Dropdown
        val religionOptions = listOf("Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu", "Lainnya")
        PreferenceDropdown(
            label = "Agama",
            options = religionOptions,
            selectedOption = formState.religion,
            onOptionSelected = viewModel::updateReligion
        )

        // Budaya Dropdown
        val cultureOptions = listOf("Jawa", "Sunda", "Bali", "Batak", "Minang", "Betawi", "Melayu", "Bugis", "Tionghoa", "Lainnya")
        PreferenceDropdown(
            label = "Budaya / Suku",
            options = cultureOptions,
            selectedOption = formState.culture,
            onOptionSelected = viewModel::updateCulture
        )

        // Provinsi Dropdown (Short list for MVP)
        val provinceOptions = listOf("DKI Jakarta", "Jawa Barat", "Jawa Tengah", "Jawa Timur", "Banten", "Bali", "Sumatera Utara", "Sulawesi Selatan", "Lainnya")
        PreferenceDropdown(
            label = "Provinsi",
            options = provinceOptions,
            selectedOption = formState.province,
            onOptionSelected = viewModel::updateProvince
        )

        // Makna
        PreferenceTextField(
            label = "Makna yang Diinginkan",
            value = formState.desiredMeaning,
            onValueChange = viewModel::updateMeaning,
            placeholder = "Contoh: Cahaya, Pembawa rezeki, Kuat"
        )

        // Kepribadian
        PreferenceTextField(
            label = "Kepribadian yang Diinginkan",
            value = formState.personality,
            onValueChange = viewModel::updatePersonality,
            placeholder = "Contoh: Pemimpin, Lembut, Cerdas"
        )

        // Tingkat Keunikan
        Text(
            text = "Tingkat Keunikan",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Populer",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Unik",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Slider(
            value = formState.uniquenessLevel,
            onValueChange = viewModel::updateUniquenessLevel,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Spacer(Modifier.height(8.dp))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.generate() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = uiState !is UiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = "Cari Nama",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        if (uiState is UiState.Error) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = (uiState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(80.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Medium,
    )
    Spacer(Modifier.height(8.dp))
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            placeholder = { Text("Pilih $label") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}

@Composable
fun PreferenceTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Medium,
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
fun GenderOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
