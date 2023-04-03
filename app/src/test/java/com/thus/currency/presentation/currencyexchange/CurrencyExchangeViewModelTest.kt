package com.thus.currency.presentation.currencyexchange

import com.thus.currency.domain.repository.CurrencyExchangeRepository
import com.thus.currency.setProperty
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyExchangeViewModelTest {
    val mockCurrencyExchangeRepository: CurrencyExchangeRepository = mockk(relaxed = true)
    val mockCurrencyId = "USD"
    val mockUiState = MutableStateFlow<UiState>(UiState.Loading)

    lateinit var instance: CurrencyExchangeViewModel

    @BeforeEach
    fun beforeEachTest() {
        instance = spyk(
            CurrencyExchangeViewModel(mockCurrencyExchangeRepository),
            recordPrivateCalls = true
        )
        instance.setProperty("_uiState", mockUiState)
        coEvery { mockCurrencyExchangeRepository.loadingData() } just Runs
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("setAmountChange(amount)")
    @Nested
    inner class SetAmountChange {
        @DisplayName("then set amount to amountState")
        @Test
        fun test() {
            val mockAmountState: MutableStateFlow<Double> = mockk(relaxed = true)
            instance.setProperty("amountState", mockAmountState)

            instance.setAmountChange("1.234")

            verify { mockAmountState.value = 1.234 }
        }
    }

    @DisplayName("setSearchChange(keyword)")
    @Nested
    inner class SetSearchChange {
        @DisplayName("then set keyword to searchState")
        @Test
        fun test() {
            val mockSearchState: MutableStateFlow<String> = mockk(relaxed = true)
            instance.setProperty("searchState", mockSearchState)

            instance.setSearchChange("VN")

            verify { mockSearchState.value = "VN" }
        }
    }

    @DisplayName("setBaseCurrency(currencyId)")
    @Nested
    inner class SetBaseCurrency {
        @DisplayName("then invoke repository.setBaseCurrency(currencyId)")
        @Test
        fun test() {
            instance.setBaseCurrency(mockCurrencyId)

            verify { mockCurrencyExchangeRepository.setBaseCurrency(mockCurrencyId) }
        }
    }

    @DisplayName("setStar(currencyId)")
    @Nested
    inner class SetStar {
        @DisplayName("then invoke repository.setStar(currencyId)")
        @Test
        fun test() = runTest {
            instance.setStar(mockCurrencyId)

            coVerify { mockCurrencyExchangeRepository.setStar(mockCurrencyId) }
        }
    }

    @DisplayName("removeStar(currencyId)")
    @Nested
    inner class RemoveStar {
        @DisplayName("then invoke repository.removeStar(currencyId)")
        @Test
        fun test() = runTest {
            instance.removeStar(mockCurrencyId)

            coVerify { mockCurrencyExchangeRepository.removeStar(mockCurrencyId) }
        }
    }

    @DisplayName("refresh()")
    @Nested
    inner class Refresh {
        @DisplayName("then invoke repository.refreshData()")
        @Test
        fun test() = runTest {
            instance.loadingData()

            coVerify { mockCurrencyExchangeRepository.loadingData() }
        }
    }

    @DisplayName("loadingData()")
    @Nested
    inner class LoadingData {
        @DisplayName("when invoke without any crashes")
        @Nested
        inner class LoadingDataWithoutCrashes {
            @DisplayName("then invoke currencyExchangeRepository.loadingData()")
            @Test
            fun test() = runTest {
                instance.loadingData()

                coVerify {
                    mockCurrencyExchangeRepository.loadingData()
                }
                Assertions.assertEquals(UiState.Normal::class, mockUiState.value::class)
            }
        }

        @DisplayName("when invoke with crashes")
        @Nested
        inner class LoadingDataWithCrashes {
            @DisplayName("then change uiState to Error")
            @Test
            fun test() = runTest {
                val mockException = RuntimeException()
                coEvery { mockCurrencyExchangeRepository.loadingData() } throws mockException

                instance.loadingData()

                coVerify {
                    mockCurrencyExchangeRepository.loadingData()
                }
                Assertions.assertEquals(UiState.Error::class, mockUiState.value::class)

            }
        }
    }
}