package com.thus.currency.data.db.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TbExchangeRateTest {

    val mockCurrencyId = "SGD"
    val mockRate = 9.9

    @DisplayName("test function Map<String?, String?>?.toTbCurrencies()")
    @Test
    fun test() {
        val mockMap: Map<String?, Double?> = mapOf(
            mockCurrencyId to mockRate
        )
        val result = mockMap.toExchangeRates().first()

        Assertions.assertEquals(mockCurrencyId, result.currencyId)
        Assertions.assertEquals(mockRate, result.rate)
    }
}