package com.fabianopinto.pingheng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fabianopinto.pingheng.ui.BalanceScreen
import com.fabianopinto.pingheng.ui.SetupScreen
import com.fabianopinto.pingheng.ui.theme.PínghéngTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PínghéngTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "balance") {
                    composable("balance") {
                        BalanceScreen(
                            viewModel = hiltViewModel(),
                            onNavigateToSetup = { navController.navigate("setup") }
                        )
                    }
                    composable("setup") {
                        SetupScreen(
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
