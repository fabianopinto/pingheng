package com.fabianopinto.pingheng.data.repository

import com.fabianopinto.pingheng.data.local.AssetDao
import com.fabianopinto.pingheng.data.local.AssetEntity
import com.fabianopinto.pingheng.data.remote.BinanceApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val assetDao: AssetDao,
    private val binanceApi: BinanceApi
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

                // In a real app, we'd also fetch the balance from Binance API here.
                // For this scaffolding, we update the price.
                assetDao.updateBalanceAndPrice(asset.symbol, asset.currentBalance, newPrice)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateAsset(asset: AssetEntity) {
        assetDao.insertAssets(listOf(asset))
    }
}
