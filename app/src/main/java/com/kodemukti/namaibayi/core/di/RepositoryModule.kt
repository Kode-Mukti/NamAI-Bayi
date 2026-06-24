package com.kodemukti.namaibayi.core.di

import com.kodemukti.namaibayi.data.repository.FallbackGenerateRepositoryImpl
import com.kodemukti.namaibayi.data.repository.FavoriteRepositoryImpl
import com.kodemukti.namaibayi.data.repository.HistoryRepositoryImpl
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

    // Fallback: coba Gemini API dulu; kalau gagal, pakai data lokal 75+ nama

    @Binds
    @Singleton
    abstract fun bindGenerateRepository(impl: FallbackGenerateRepositoryImpl): GenerateRepository

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
