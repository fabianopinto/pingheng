package com.fabianopinto.pingheng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.fabianopinto.pingheng.ui.BalanceScreen
import com.fabianopinto.pingheng.ui.theme.PínghéngTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PínghéngTheme {
                BalanceScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToSetup = {
                        // TODO: Implement navigation to SetupScreen
                    }
                )
            }
        }
    }
}
