package com.thus.currency.presentation.utils

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun currencyFormat(amount: Double): String {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount)
    }

    @SuppressLint("SimpleDateFormat")
    fun dateTimeFormat(time: Long): String {
        if (time > 0) {
            val simpleDateFormat = SimpleDateFormat("MMM dd HH:mm:ss")
            return simpleDateFormat.format(Date(time))
        }
        return ""
    }
}