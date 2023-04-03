package com.thus.currency.presentation.currencyexchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.thus.currency.domain.repository.CurrencyExchangeRepository
import com.thus.currency.presentation.currencyexchange.viewstate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyExchangeViewModel @Inject constructor(
    private val repository: CurrencyExchangeRepository
) : ViewModel() {

    val baseCurrencyState = repository.getBaseCurrency()
    val serverUpdatedTimestampState = repository.getServerUpdatedTimestamp()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: Flow<UiState> = _uiState

    private val searchState = MutableStateFlow("")
    private val amountState = MutableStateFlow(1.0)
    val currencyState = repository.getCurrencyPagingSource()
        .cachedIn(viewModelScope)
        .flatMapLatest { pagingData ->
            searchState.map { keyword ->
                pagingData.filter {
                    val keywordProcessing = keyword.lowercase().trim()
                    keywordProcessing.isBlank() || it.currencyId.lowercase()
                        .contains(keywordProcessing) || it.currencyName.lowercase()
                        .contains(keywordProcessing)
                }
            }
        }
        .flatMapLatest { pagingData ->
            baseCurrencyState.map { base ->
                pagingData.filter {
                    it.currencyId != base.currencyId
                }.map { currencyDomain ->
                    currencyDomain.copy(rate = currencyDomain.rate / base.rate)
                }
            }
        }
        .flatMapLatest { pagingData ->
            amountState.map { amount ->
                pagingData.map { currencyDomain ->
                    currencyDomain.copy(rate = currencyDomain.rate * amount)
                }
            }
        }

    init {
        loadingData()
    }

    fun setAmountChange(amount: String) {
        amountState.value = amount.toDoubleOrNull() ?: 0.0
    }

    fun setSearchChange(keyword: String) {
        searchState.value = keyword
    }

    fun setBaseCurrency(currencyId: String) {
        repository.setBaseCurrency(currencyId)
    }

    fun setStar(currencyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setStar(currencyId)
        }
    }

    fun removeStar(currencyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeStar(currencyId)
        }
    }

    fun loadingData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {
                repository.loadingData()
                _uiState.value = UiState.Normal
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}