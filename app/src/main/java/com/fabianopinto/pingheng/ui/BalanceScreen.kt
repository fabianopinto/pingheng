package com.fabianopinto.pingheng.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fabianopinto.pingheng.data.model.Asset
import com.fabianopinto.pingheng.data.model.TradeRecommendation
import com.fabianopinto.pingheng.ui.theme.PínghéngTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    viewModel: AssetViewModel,
    onNavigateToSetup: () -> Unit
) {
    val assets by viewModel.assets.collectAsState()
    val totalValuation by viewModel.totalValuation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pínghéng Balance") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onNavigateToSetup) {
                        Icon(Icons.Default.Settings, contentDescription = "Setup")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.executeRebalancing() },
                icon = { Icon(Icons.Default.Settings, "Execute") },
                text = { Text("Execute") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TotalBalanceCard(totalValuation)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    BalanceHeader()
                }
                items(assets) { asset ->
                    AssetRow(asset, totalValuation)
                }
            }
        }
    }
}

@Composable
fun TotalBalanceCard(totalValuation: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total Portfolio Valuation", style = MaterialTheme.typography.labelMedium)
            Text(
                text = "$${String.format(Locale.US, "%.2f", totalValuation)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BalanceHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Asset", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Slice", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Target", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Recommendation", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AssetRow(asset: Asset, totalValuation: Double) {
    val currentSlice = if (totalValuation > 0) (asset.currentValuation / totalValuation) else 0.0
    val recommendation = asset.getTradeRecommendation(totalValuation)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(asset.symbol, fontWeight = FontWeight.Bold)
                Text("$${String.format(Locale.US, "%.2f", asset.currentPrice)}", fontSize = 12.sp)
            }

            Text(
                text = "${String.format(Locale.US, "%.1f", currentSlice * 100)}%",
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${String.format(Locale.US, "%.1f", asset.targetPercentage * 100)}%",
                modifier = Modifier.weight(1f)
            )

            RecommendationBadge(recommendation, modifier = Modifier.weight(1.5f))
        }
    }
}

@Composable
fun RecommendationBadge(recommendation: TradeRecommendation, modifier: Modifier = Modifier) {
    val (text, color) = when (recommendation) {
        is TradeRecommendation.Buy -> "BUY ${String.format(Locale.US, "%.4f", recommendation.quantity)}" to Color(0xFF4CAF50)
        is TradeRecommendation.Sell -> "SELL ${String.format(Locale.US, "%.4f", recommendation.quantity)}" to Color(0xFFF44336)
        TradeRecommendation.None -> "HOLD" to Color.Gray
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AssetRowPreview() {
    PínghéngTheme {
        AssetRow(
            asset = Asset(
                symbol = "BTC",
                targetPercentage = 0.5,
                currentBalance = 1.0,
                currentPrice = 50000.0
            ),
            totalValuation = 100000.0
        )
    }
}
