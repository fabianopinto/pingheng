package com.fabianopinto.pingheng.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabianopinto.pingheng.data.local.AssetEntity
import com.fabianopinto.pingheng.data.model.Asset
import com.fabianopinto.pingheng.data.repository.AssetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val repository: AssetRepository
) : ViewModel() {

    val assets: StateFlow<List<Asset>> = repository.allAssets
        .map { entities ->
            entities.map { it.toDomainModel() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalValuation: StateFlow<Double> = assets
        .map { list -> list.sumOf { it.currentValuation } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun addAsset(symbol: String, targetPercentage: Double) {
        viewModelScope.launch {
            repository.addAsset(
                AssetEntity(
                    symbol = symbol,
                    targetPercentage = targetPercentage / 100.0
                )
            )
        }
    }

    fun deleteAsset(symbol: String) {
        viewModelScope.launch {
            repository.deleteAsset(symbol)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.refreshData()
        }
    }

    fun executeRebalancing() {
        viewModelScope.launch {
            repository.executeRebalancing()
        }
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
