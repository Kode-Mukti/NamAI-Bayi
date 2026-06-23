package com.kodemukti.namaibayi.core.di

import android.content.Context
import com.kodemukti.namaibayi.BuildConfig
import com.kodemukti.namaibayi.core.common.Constants
import com.kodemukti.namaibayi.data.local.dao.FavoriteDao
import com.kodemukti.namaibayi.data.local.dao.HistoryDao
import com.kodemukti.namaibayi.data.local.database.NamAIDatabase
import com.kodemukti.namaibayi.data.remote.api.NamAIApi
import com.kodemukti.namaibayi.data.remote.gemini.GeminiApiKey
import com.kodemukti.namaibayi.data.repository.FavoriteRepositoryImpl
import com.kodemukti.namaibayi.data.repository.HistoryRepositoryImpl
import com.kodemukti.namaibayi.data.repository.SettingsRepositoryImpl
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNamAIApi(retrofit: Retrofit): NamAIApi {
        return retrofit.create(NamAIApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NamAIDatabase {
        return NamAIDatabase.getInstance(context)
    }

    @Provides
    fun provideHistoryDao(database: NamAIDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideFavoriteDao(database: NamAIDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    @GeminiApiKey
    fun provideGeminiApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }
}
