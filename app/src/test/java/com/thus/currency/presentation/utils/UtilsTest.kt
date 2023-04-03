package com.thus.currency.presentation.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UtilsTest {
    @Test
    fun testCurrencyFormat() {
        val mockAmount = 1_234_567.123

        val result = Utils.currencyFormat(mockAmount)

        Assertions.assertEquals("1,234,567.12", result)
    }

    @Test
    fun testDateTimeFormat() {
        val mockTime = 1_234_567L

        val result = Utils.dateTimeFormat(mockTime)

        Assertions.assertEquals("Jan 01 08:20:34", result)
    }
}