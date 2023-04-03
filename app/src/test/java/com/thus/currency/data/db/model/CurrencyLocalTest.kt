package com.thus.currency.data.db.model

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyLocalTest {

    val mockCurrencyId = "SGD"
    val mockRate = 1.3
    val mockCurrencyName = "Singapore Dollar"
    val mockStarTime = 1000L

    lateinit var instance: CurrencyLocal

    @BeforeEach
    fun beforeEachTest() {
        instance = CurrencyLocal(
            currencyId = mockCurrencyId,
            rate = mockRate,
            currencyName = mockCurrencyName,
            starTime = mockStarTime
        )
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("test convert to domain model")
    @Test
    fun test() {
        val result = instance.toCurrencyDomain()

        Assertions.assertEquals(mockCurrencyId, result.currencyId)
        Assertions.assertEquals(mockRate, result.rate)
        Assertions.assertEquals(mockCurrencyName, result.currencyName)
        Assertions.assertEquals(true, result.star)
    }
}