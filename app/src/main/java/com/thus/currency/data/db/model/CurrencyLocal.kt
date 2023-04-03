package com.thus.currency.data.db.model

import com.google.gson.annotations.SerializedName
import com.thus.currency.domain.model.CurrencyDomain

data class CurrencyLocal(
    @field:SerializedName("currencyId") val currencyId: String,
    @field:SerializedName("rate") val rate: Double?,
    @field:SerializedName("currency_name") val currencyName: String?,
    @field:SerializedName("star_time") val starTime: Long?
)

fun CurrencyLocal.toCurrencyDomain() =
    CurrencyDomain(
        currencyId = currencyId,
        currencyName = currencyName.orEmpty(),
        rate = rate ?: 0.0,
        star = (starTime ?: 0L) > 0L
    )