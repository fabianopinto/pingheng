package com.fabianopinto.pingheng.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fabianopinto.pingheng.data.local.PreferenceManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialsScreen(
    preferenceManager: PreferenceManager,
    navController: NavController
) {
    var apiKey by remember { mutableStateOf(preferenceManager.getApiKey()) }
    var apiSecret by remember { mutableStateOf(preferenceManager.getApiSecret()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Credentials") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Enter your Binance API Key and Secret. These will be stored securely on your device.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apiSecret,
                onValueChange = { apiSecret = it },
                label = { Text("API Secret") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    preferenceManager.saveCredentials(apiKey, apiSecret)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Credentials")
            }
        }
    }
}
