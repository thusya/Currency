package com.thus.currency.data.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.thus.currency.data.constants.Constants
import com.thus.currency.BuildConfig
import com.thus.currency.data.constants.Constants.PAGING_CONFIG
import com.thus.currency.data.db.NCurrencyDAO
import com.thus.currency.data.db.model.TbStar
import com.thus.currency.data.db.model.toCurrencyDomain
import com.thus.currency.data.db.model.toExchangeRates
import com.thus.currency.data.db.model.toTbCurrencies
import com.thus.currency.data.remote.OpenExchangeRatesService
import com.thus.currency.domain.model.CurrencyDomain
import com.thus.currency.domain.repository.CurrencyExchangeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.Date

class CurrencyExchangeRepositoryImpl(
    private val openExchangeRatesService: OpenExchangeRatesService,
    private val currencyDAO: NCurrencyDAO,
    private val sharedPreferences: SharedPreferences
) : CurrencyExchangeRepository {
    private val defaultBaseCurrency = "USD"
    private val baseCurrency = MutableStateFlow(getBaseCurrencyLocal() ?: defaultBaseCurrency)

    private val timestamp = MutableStateFlow(getTimestampLocal())

    private fun getBaseCurrencyLocal(): String? =
        sharedPreferences.getString(
            Constants.PREF_KEY_BASE_CURRENCY,
            defaultBaseCurrency
        )

    private fun getTimestampLocal(): Long =
        sharedPreferences.getLong(
            Constants.PREF_KEY_TIMESTAMP,
            0L
        )

    override fun getCurrencyPagingSource(): Flow<PagingData<CurrencyDomain>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = { currencyDAO.getCurrencyPagingSource() }
        ).flow.map { paging ->
            paging.map { currencyLocal -> currencyLocal.toCurrencyDomain() }
        }
    }

    override fun getServerUpdatedTimestamp(): Flow<Long> = timestamp

    override suspend fun setStar(currencyId: String) {
        currencyDAO.insertStar(
            TbStar(
                currencyId = currencyId,
                starTime = Date().time
            )
        )
    }

    override suspend fun removeStar(currencyId: String) {
        currencyDAO.removeStar(currencyId)
    }

    override fun setBaseCurrency(currencyId: String) {
        baseCurrency.value = currencyId
        saveBaseCurrencyToLocal(currencyId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBaseCurrency(): Flow<CurrencyDomain> =
        baseCurrency.flatMapLatest { currencyId ->
            currencyDAO.getCurrencyById(currencyId)
        }.map { currencyRemote ->
            currencyRemote?.toCurrencyDomain() ?: CurrencyDomain(currencyId = baseCurrency.value)
        }

    override suspend fun loadingData() {
        if (!DateUtils.isToday(timestamp.value)) {
            val currencies = openExchangeRatesService.getCurrencies()
            currencyDAO.insertCurrencies(currencies.toTbCurrencies())

            val appId = BuildConfig.OPEN_EXCHANGE_RATES_SERVICE_APP_ID
            val rateLatest = openExchangeRatesService.getLatest(appId)

            currencyDAO.insertExchangeRates(rateLatest.rates.toExchangeRates())
            rateLatest.timestamp?.let {
                val timestampConvert = it * 1000L
                timestamp.value = timestampConvert
                saveTimestampToLocal(timestampConvert)
            }
        }
    }

    private fun saveTimestampToLocal(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(Constants.PREF_KEY_TIMESTAMP, timestamp)
            .apply()
    }

    private fun saveBaseCurrencyToLocal(currencyId: String) {
        sharedPreferences.edit()
            .putString(
                Constants.PREF_KEY_BASE_CURRENCY,
                currencyId
            )
            .apply()
    }
}