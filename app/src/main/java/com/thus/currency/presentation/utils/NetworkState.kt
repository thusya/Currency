package com.thus.currency.presentation.utils

sealed class NetworkState {
    object Loaded : NetworkState()
    object Loading : NetworkState()
    data class Error(val error: String): NetworkState()
}