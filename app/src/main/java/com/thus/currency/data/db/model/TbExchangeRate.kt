package com.thus.currency.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.thus.currency.data.constants.Constants

@Entity(tableName = Constants.TB_EXCHANGE_RATE)
data class TbExchangeRate(
    @PrimaryKey
    @field:SerializedName("currency_id")
    val currencyId: String,
    @field:SerializedName("rate")
    val rate: Double,
)

fun Map<String?, Double?>?.toExchangeRates() = this?.map {
    TbExchangeRate(
        currencyId = it.key.orEmpty(),
        rate = it.value ?: 0.0
    )
} ?: emptyList()