package com.thus.currency.presentation.currencyexchange.model

import com.thus.currency.domain.model.CurrencyDomain
import com.thus.currency.presentation.utils.Utils

data class OneRowItemUI(
    val endText: String = "",
    val middleText: String = "",
    val startText: String = "",
    val star: Boolean,
    val data: Any
)

fun CurrencyDomain.toOneRowItemUI() = OneRowItemUI(
    endText = currencyId,
    middleText = Utils.currencyFormat(rate),
    startText = currencyName,
    star = star,
    data = this
)