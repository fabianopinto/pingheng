package com.fabianopinto.pingheng.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val symbol: String, // e.g., "BTC"
    val targetPercentage: Double,    // e.g., 0.10 for 10%
    val pairWith: String = "USDT",   // e.g., "USDT"
    val quantityPrecision: Int = 8,
    val pricePrecision: Int = 2,
    val currentBalance: Double = 0.0,
    val currentPrice: Double = 0.0
)
