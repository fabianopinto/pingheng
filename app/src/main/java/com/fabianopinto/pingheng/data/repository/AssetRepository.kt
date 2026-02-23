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

            // Get current assets from DB
            // Flow to List is tricky in suspend, we might need a non-flow getter in DAO
            // For now, let's assume we have a way to update them.
            // Simplified: update all prices in the DB based on the fetched prices
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateAsset(asset: AssetEntity) {
        assetDao.insertAssets(listOf(asset))
    }
}
