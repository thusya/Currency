package com.thus.currency.presentation.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class NetworkStateTest {
    @Test
    fun testCreateState() {
        var networkState:NetworkState = NetworkState.Loading
        assertEquals(networkState, NetworkState.Loading)
        networkState = NetworkState.Loaded
        assertEquals(networkState, NetworkState.Loaded)
        networkState = NetworkState.Error("error")
        assertEquals((networkState as NetworkState.Error).error, "error")
    }
}