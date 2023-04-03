package com.thus.currency.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.thus.currency.data.constants.Constants
import com.thus.currency.data.db.NCurrencyDB
import com.thus.currency.data.remote.OpenExchangeRatesService
import com.thus.currency.data.repository.CurrencyExchangeRepositoryImpl
import com.thus.currency.domain.repository.CurrencyExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideCurrencyExchangeRepository(
        openExchangeRatesService: OpenExchangeRatesService,
        currencyDB: NCurrencyDB,
        sharedPreferences: SharedPreferences
    ): CurrencyExchangeRepository =
        CurrencyExchangeRepositoryImpl(
            openExchangeRatesService,
            currencyDB.currencyDAO(),
            sharedPreferences
        )

    @Provides
    fun provideOpenExchangeRatesService(retrofit: Retrofit): OpenExchangeRatesService =
        retrofit.create(OpenExchangeRatesService::class.java)

    @Singleton
    @Provides
    fun provideNCurrencyDB(context: Context): NCurrencyDB =
        NCurrencyDB.create(context)

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE)
}
