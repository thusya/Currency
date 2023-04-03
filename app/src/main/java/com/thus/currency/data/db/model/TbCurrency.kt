package com.thus.currency.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.thus.currency.data.constants.Constants

@Entity(tableName = Constants.TB_CURRENCY)
data class TbCurrency(
    @PrimaryKey
    @field:SerializedName("currency_id")
    val currencyId: String,
    @field:SerializedName("currency_name")
    val currencyName: String
)

fun Map<String?, String?>?.toTbCurrencies() = this?.map {
    TbCurrency(
        currencyId = it.key.orEmpty(),
        currencyName = it.value.orEmpty()
    )
} ?: emptyList()