package com.thus.currency.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyDomain(
    val currencyId: String = "",
    val currencyName: String = "",
    val rate: Double = 0.0,
    val star: Boolean = false
): Parcelable