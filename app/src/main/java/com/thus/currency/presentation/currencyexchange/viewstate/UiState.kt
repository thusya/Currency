package com.thus.currency.presentation.currencyexchange.viewstate

sealed class UiState{
    object Loading : UiState()
    object Normal : UiState()
    data class Error(val exception: Exception) : UiState()
}
