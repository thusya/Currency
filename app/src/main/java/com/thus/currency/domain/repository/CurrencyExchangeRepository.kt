package com.thus.currency.domain.repository

import androidx.paging.PagingData
import com.thus.currency.domain.model.CurrencyDomain
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangeRepository {
    fun getCurrencyPagingSource(): Flow<PagingData<CurrencyDomain>>
    fun getServerUpdatedTimestamp(): Flow<Long>

    suspend fun setStar(currencyId: String)
    suspend fun removeStar(currencyId: String)

    fun setBaseCurrency(currencyId: String)
    fun getBaseCurrency(): Flow<CurrencyDomain>

    suspend fun loadingData()
}