package com.fabianopinto.pingheng.data.repository

import com.fabianopinto.pingheng.data.local.AssetDao
import com.fabianopinto.pingheng.data.local.AssetEntity
import com.fabianopinto.pingheng.data.local.PreferenceManager
import com.fabianopinto.pingheng.data.model.Asset
import com.fabianopinto.pingheng.data.remote.BinanceApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val assetDao: AssetDao,
    private val binanceApi: BinanceApi,
    private val preferenceManager: PreferenceManager
) {
    val allAssets: Flow<List<AssetEntity>> = assetDao.getAllAssets()

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

                assetDao.updateBalanceAndPrice(asset.symbol, asset.currentBalance, newPrice)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun executeRebalancing() {
        val apiKey = preferenceManager.getApiKey()
        val apiSecret = preferenceManager.getApiSecret()

        if (apiKey.isBlank() || apiSecret.isBlank()) {
            println("API credentials are not set.")
            return
        }

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

                    val timestamp = System.currentTimeMillis()
                    val symbol = "${asset.symbol}${asset.pairWith}"

                    // Placeholder for signature calculation
                    println("Executing $side ${recommendation.quantity} of ${asset.symbol}")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        refreshData()
    }

    fun saveCredentials(apiKey: String, apiSecret: String) {
        preferenceManager.saveCredentials(apiKey, apiSecret)
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
