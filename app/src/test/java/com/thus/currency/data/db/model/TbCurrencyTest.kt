package com.thus.currency.data.db.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TbCurrencyTest {

    val mockCurrencyId = "SGD"
    val mockCurrencyName = "Singapore Dollar"

    @DisplayName("test function Map<String?, String?>?.toTbCurrencies()")
    @Test
    fun test() {
        val mockMap: Map<String?, String?> = mapOf(
            mockCurrencyId to mockCurrencyName
        )
        val result = mockMap.toTbCurrencies().first()

        Assertions.assertEquals(mockCurrencyId, result.currencyId)
        Assertions.assertEquals(mockCurrencyName, result.currencyName)
    }
}