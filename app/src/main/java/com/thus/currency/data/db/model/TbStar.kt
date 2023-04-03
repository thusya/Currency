package com.thus.currency.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.thus.currency.data.constants.Constants

@Entity(tableName = Constants.TB_STAR)
data class TbStar(
    @PrimaryKey
    @field:SerializedName("currency_id")
    val currencyId: String,
    @field:SerializedName("star_time")
    val starTime: Long
)