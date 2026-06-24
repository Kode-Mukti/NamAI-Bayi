package com.kodemukti.namaibayi.ui.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _showDialog = MutableStateFlow(true)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    init {
        viewModelScope.launch {
            val acknowledged = dataStore.data.map { prefs ->
                prefs[PRIVACY_ACKED] ?: false
            }.first()
            _showDialog.value = !acknowledged
        }
    }

    fun acknowledge() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[PRIVACY_ACKED] = true
            }
            _showDialog.value = false
        }
    }

    companion object {
        private val PRIVACY_ACKED = booleanPreferencesKey("privacy_acknowledged")
    }
}
