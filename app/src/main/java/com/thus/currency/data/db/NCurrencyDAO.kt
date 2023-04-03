package com.thus.currency.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thus.currency.data.constants.Constants
import com.thus.currency.data.db.model.CurrencyLocal
import com.thus.currency.data.db.model.TbCurrency
import com.thus.currency.data.db.model.TbExchangeRate
import com.thus.currency.data.db.model.TbStar
import kotlinx.coroutines.flow.Flow

@Dao
interface NCurrencyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(list: List<TbExchangeRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(list: List<TbCurrency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStar(star: TbStar)

    @Query("DELETE FROM ${Constants.TB_STAR} WHERE currencyId = :currencyId")
    suspend fun removeStar(currencyId: String)

    @Query(
        "SELECT ${Constants.TB_EXCHANGE_RATE}.currencyId AS currencyId" +
            ", ${Constants.TB_EXCHANGE_RATE}.rate AS rate" +
            ", ${Constants.TB_CURRENCY}.currencyName AS currencyName" +
            ", ${Constants.TB_STAR}.starTime AS starTime" +
            " FROM ${Constants.TB_EXCHANGE_RATE}" +
            " LEFT JOIN ${Constants.TB_CURRENCY} ON ${Constants.TB_EXCHANGE_RATE}.currencyId = ${Constants.TB_CURRENCY}.currencyId" +
            " LEFT JOIN ${Constants.TB_STAR} ON ${Constants.TB_EXCHANGE_RATE}.currencyId = ${Constants.TB_STAR}.currencyId" +
            " ORDER BY starTime DESC, currencyId ASC"
    )
    fun getCurrencyPagingSource(): PagingSource<Int, CurrencyLocal>

    @Query(
        "SELECT ${Constants.TB_EXCHANGE_RATE}.currencyId AS currencyId" +
            ", ${Constants.TB_EXCHANGE_RATE}.rate AS rate" +
            ", ${Constants.TB_CURRENCY}.currencyName AS currencyName" +
            ", ${Constants.TB_STAR}.starTime AS starTime" +
            " FROM ${Constants.TB_EXCHANGE_RATE}" +
            " LEFT JOIN ${Constants.TB_CURRENCY} ON ${Constants.TB_EXCHANGE_RATE}.currencyId = ${Constants.TB_CURRENCY}.currencyId" +
            " LEFT JOIN ${Constants.TB_STAR} ON ${Constants.TB_EXCHANGE_RATE}.currencyId = ${Constants.TB_STAR}.currencyId" +
            " WHERE ${Constants.TB_EXCHANGE_RATE}.currencyId = :currencyId LIMIT 1"
    )
    fun getCurrencyById(currencyId: String): Flow<CurrencyLocal?>
}