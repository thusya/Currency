package com.thus.currency.data.repository

import android.content.SharedPreferences
import com.thus.currency.data.constants.Constants
import com.thus.currency.data.db.NCurrencyDAO
import com.thus.currency.data.remote.OpenExchangeRatesService
import com.thus.currency.invokeMethod
import com.thus.currency.setProperty
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyExchangeRepositoryImplTest {
    val mockOpenExchangeRatesService: OpenExchangeRatesService = mockk(relaxed = true)
    val mockNCurrencyDAO: NCurrencyDAO = mockk(relaxed = true)
    val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
    val mockSharedPreferencesEditor: SharedPreferences.Editor = mockk(relaxed = true)
    val mockCurrencyId = "USD"

    lateinit var instance: CurrencyExchangeRepositoryImpl

    @BeforeEach
    fun beforeEachTest() {
        instance = spyk(
            CurrencyExchangeRepositoryImpl(
                mockOpenExchangeRatesService,
                mockNCurrencyDAO,
                mockSharedPreferences
            ),
            recordPrivateCalls = true
        )
        every { mockSharedPreferences.edit() } returns mockSharedPreferencesEditor
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("getBaseCurrencyLocal()")
    @Nested
    inner class GetBaseCurrencyLocal {
        @DisplayName("then return value from sharedPreferences")
        @Test
        fun test() {
            every {
                mockSharedPreferences.getString(
                    Constants.PREF_KEY_BASE_CURRENCY,
                    any()
                )
            } returns mockCurrencyId

            val result = instance.invokeMethod("getBaseCurrencyLocal") as String

            Assertions.assertEquals(mockCurrencyId, result)
        }
    }

    @DisplayName("getTimestampLocal()")
    @Nested
    inner class GetTimestampLocal {
        @DisplayName("then return value from sharedPreferences")
        @Test
        fun test() {
            val mockTimestamp = 123L
            every {
                mockSharedPreferences.getLong(
                    Constants.PREF_KEY_TIMESTAMP,
                    any()
                )
            } returns mockTimestamp

            val result = instance.invokeMethod("getTimestampLocal") as Long

            Assertions.assertEquals(mockTimestamp, result)
        }
    }

    @DisplayName("setStar(currencyId)")
    @Nested
    inner class SetStar {
        @DisplayName("then invoke currencyDAO.insertStar")
        @Test
        fun test() = runTest {
            instance.setStar(mockCurrencyId)

            coVerify { mockNCurrencyDAO.insertStar(any()) }
        }
    }

    @DisplayName("removeStar(currencyId)")
    @Nested
    inner class RemoveStar {
        @DisplayName("then invoke currencyDAO.removeStar")
        @Test
        fun test() = runTest {
            instance.removeStar(mockCurrencyId)

            coVerify { mockNCurrencyDAO.removeStar(mockCurrencyId) }
        }
    }

    @DisplayName("setBaseCurrency(currencyId)")
    @Nested
    inner class SetBaseCurrency {
        @DisplayName("then invoke baseCurrency.value and instance.saveBaseCurrencyToLocal")
        @Test
        fun test() {
            val mockBaseCurrency: MutableStateFlow<String> = mockk(relaxed = true)
            instance.setProperty("baseCurrency", mockBaseCurrency)

            instance.setBaseCurrency(mockCurrencyId)

            verify {
                mockBaseCurrency.value = mockCurrencyId
                instance.invokeMethod("saveBaseCurrencyToLocal", mockCurrencyId)
            }
        }
    }

    @DisplayName("saveTimestampToLocal(timestamp)")
    @Nested
    inner class SaveTimestampToLocal {
        @DisplayName("then save timestamp to sharedPreferences")
        @Test
        fun test() {
            val mockTimestamp = 123L

            instance.invokeMethod("saveTimestampToLocal", mockTimestamp)

            verify {
                mockSharedPreferencesEditor.putLong(
                    Constants.PREF_KEY_TIMESTAMP,
                    mockTimestamp
                )
            }
        }
    }

    @DisplayName("saveBaseCurrencyToLocal(timestamp)")
    @Nested
    inner class SaveBaseCurrencyToLocal {
        @DisplayName("then save timestamp to sharedPreferences")
        @Test
        fun test() {
            instance.invokeMethod("saveBaseCurrencyToLocal", mockCurrencyId)

            verify {
                mockSharedPreferencesEditor.putString(
                    Constants.PREF_KEY_BASE_CURRENCY,
                    mockCurrencyId
                )
            }
        }
    }
}