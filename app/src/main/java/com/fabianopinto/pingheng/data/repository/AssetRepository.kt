package com.fabianopinto.pingheng.data.repository

import com.fabianopinto.pingheng.data.local.AssetDao
import com.fabianopinto.pingheng.data.local.AssetEntity
import com.fabianopinto.pingheng.data.model.Asset
import com.fabianopinto.pingheng.data.remote.BinanceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class AssetRepository @Inject constructor(
    private val assetDao: AssetDao,
    private val binanceApi: BinanceApi
) {
    val allAssets: Flow<List<AssetEntity>> = assetDao.getAllAssets()

    // These should be securely stored, e.g., in EncryptedSharedPreferences or DataStore
    private var apiKey: String = ""
    private var apiSecret: String = ""

    fun setCredentials(key: String, secret: String) {
        apiKey = key
        apiSecret = secret
    }

    suspend fun addAsset(asset: AssetEntity) {
        assetDao.insertAssets(listOf(asset))
    }

    suspend fun deleteAsset(symbol: String) {
        assetDao.deleteAsset(symbol)
    }

    suspend fun refreshData() {
        try {
            val prices = binanceApi.getPrices()
            val priceMap = prices.associateBy({ it.symbol }, { it.price.toDoubleOrNull() ?: 0.0 })

            val currentAssets = assetDao.getAllAssetsList()

            currentAssets.forEach { asset ->
                val pairSymbol = "${asset.symbol}${asset.pairWith}"
                val newPrice = priceMap[pairSymbol] ?: 0.0

                // In a real app, we'd also fetch the balance from Binance API here.
                // For this scaffolding, we update the price.
                assetDao.updateBalanceAndPrice(asset.symbol, asset.currentBalance, newPrice)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun executeRebalancing() {
        if (apiKey.isBlank() || apiSecret.isBlank()) return

        val currentEntities = assetDao.getAllAssetsList()
        val totalValuation = currentEntities.sumOf { it.currentBalance * it.currentPrice }

        if (totalValuation <= 0) return

        currentEntities.forEach { entity ->
            val asset = entity.toDomainModel()
            val recommendation = asset.getTradeRecommendation(totalValuation)

            if (recommendation.quantity > 0) {
                try {
                    val side = when (recommendation) {
                        is com.fabianopinto.pingheng.data.model.TradeRecommendation.Buy -> "BUY"
                        is com.fabianopinto.pingheng.data.model.TradeRecommendation.Sell -> "SELL"
                        else -> return@forEach
                    }

                    // Simple Market Order implementation
                    // Note: signature calculation is missing here - required for Binance API
                    val timestamp = System.currentTimeMillis()
                    val symbol = "${asset.symbol}${asset.pairWith}"

                    // binanceApi.postOrder(apiKey, symbol, side, "MARKET", recommendation.quantity.toString(), timestamp, "SIGNATURE")

                    println("Executing $side ${recommendation.quantity} of ${asset.symbol}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        refreshData()
    }

    private fun AssetEntity.toDomainModel(): Asset {
        return Asset(
            symbol = symbol,
            targetPercentage = targetPercentage,
            currentBalance = currentBalance,
            currentPrice = currentPrice,
            pairWith = pairWith,
            quantityPrecision = quantityPrecision,
            pricePrecision = pricePrecision
        )
    }
}
