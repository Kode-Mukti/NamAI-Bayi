package com.kodemukti.namaibayi.core.di

import com.kodemukti.namaibayi.data.repository.FavoriteRepositoryImpl
import com.kodemukti.namaibayi.data.repository.HistoryRepositoryImpl
import com.kodemukti.namaibayi.data.repository.LocalGenerateRepositoryImpl
import com.kodemukti.namaibayi.data.repository.SettingsRepositoryImpl
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // ── MODE: Generate ──────────────────────────────────────────────
    // Sekarang: LocalGenerateRepositoryImpl — offline, pakai database lokal 75+ nama
    //
    // Kalau mau pake Gemini API:
    //   1. Isi gemini.api.key=API_KEY di local.properties (atau di Constants.kt)
    //   2. Ganti binding di bawah jadi GeminiGenerateRepositoryImpl:
    //
    //     @Binds @Singleton
    //     abstract fun bindGenerateRepository(impl: GeminiGenerateRepositoryImpl): GenerateRepository
    //
    // Kalau mau pake backend custom:
    //   Ganti binding jadi GenerateRepositoryImpl (Retrofit → namAIApi):
    //
    //     @Binds @Singleton
    //     abstract fun bindGenerateRepository(impl: GenerateRepositoryImpl): GenerateRepository

    @Binds
    @Singleton
    abstract fun bindGenerateRepository(impl: LocalGenerateRepositoryImpl): GenerateRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
