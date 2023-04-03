package com.thus.currency.presentation.currencyexchange.model

import com.thus.currency.domain.model.CurrencyDomain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OneRowItemUITest {

    val mockCurrencyId = "SGD"
    val mockRate = 9.99
    val mockCurrencyName = "Singapore Dollar"
    val mockStar = true

    @DisplayName("test convert domain model to UI model")
    @Test
    fun test() {
        val mockCurrencyDomain = CurrencyDomain(
            currencyId = mockCurrencyId,
            currencyName = mockCurrencyName,
            rate = mockRate,
            star = mockStar
        )

        val result = mockCurrencyDomain.toOneRowItemUI()

        Assertions.assertEquals(mockCurrencyId, result.endText)
        Assertions.assertEquals("9.99", result.middleText)
        Assertions.assertEquals(mockCurrencyName, result.startText)
        Assertions.assertEquals(true, result.star)
        Assertions.assertEquals(mockCurrencyDomain, result.data)
    }
}