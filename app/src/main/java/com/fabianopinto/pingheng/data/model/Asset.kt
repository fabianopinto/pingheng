package com.fabianopinto.pingheng.data.model

data class Asset(
    val symbol: String,
    val targetPercentage: Double,
    val currentBalance: Double,
    val currentPrice: Double,
    val pairWith: String = "USDT",
    val quantityPrecision: Int = 8,
    val pricePrecision: Int = 2
) {
    val currentValuation: Double = currentBalance * currentPrice

    fun calculateDelta(totalValuation: Double): Double {
        val targetValuation = totalValuation * targetPercentage
        return targetValuation - currentValuation
    }

    fun getTradeRecommendation(totalValuation: Double): TradeRecommendation {
        val delta = calculateDelta(totalValuation)
        val quantity = Math.abs(delta / currentPrice)
        return if (delta > 0) {
            TradeRecommendation.Buy(quantity)
        } else {
            TradeRecommendation.Sell(quantity)
        }
    }
}

sealed class TradeRecommendation {
    abstract val quantity: Double
    data class Buy(override val quantity: Double) : TradeRecommendation()
    data class Sell(override val quantity: Double) : TradeRecommendation()
    object None : TradeRecommendation() {
        override val quantity: Double = 0.0
    }
}
