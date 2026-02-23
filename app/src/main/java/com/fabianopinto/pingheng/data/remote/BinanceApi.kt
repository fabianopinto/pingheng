package com.fabianopinto.pingheng.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

data class PriceResponse(
    val symbol: String,
    val price: String
)

data class AccountResponse(
    val balances: List<AssetBalance>
)

data class AssetBalance(
    val asset: String,
    val free: String,
    val locked: String
)

data class OrderResponse(
    val symbol: String,
    val orderId: Long,
    val status: String
)

interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getPrices(): List<PriceResponse>

    @GET("api/v3/account")
    suspend fun getAccountInfo(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("timestamp") timestamp: Long,
        @Query("signature") signature: String
    ): AccountResponse

    @POST("api/v3/order")
    suspend fun postOrder(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("symbol") symbol: String,
        @Query("side") side: String,
        @Query("type") type: String,
        @Query("quantity") quantity: String,
        @Query("timestamp") timestamp: Long,
        @Query("signature") signature: String
    ): OrderResponse
}
