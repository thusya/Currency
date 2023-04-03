package com.thus.currency.data.remote

import com.thus.currency.data.remote.model.LatestResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenExchangeRatesService {

    @GET("latest.json")
    suspend fun getLatest(@Query("app_id") appId: String): LatestResponse

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String?, String?>
}