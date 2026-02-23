package com.fabianopinto.pingheng.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets")
    fun getAllAssets(): Flow<List<AssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetEntity>)

    @Query("UPDATE assets SET currentBalance = :balance, currentPrice = :price WHERE symbol = :symbol")
    suspend fun updateBalanceAndPrice(symbol: String, balance: Double, price: Double)

    @Query("DELETE FROM assets WHERE symbol = :symbol")
    suspend fun deleteAsset(symbol: String)
}
