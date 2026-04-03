package com.example.rootup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.rootup.ui.theme.RootUpTheme
import com.example.rootup.view.MainWin
import com.example.rootup.view.navigation.AppNavHost
import com.example.rootup.view.navigation.Details
import com.example.rootup.view.navigation.Home


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootUpTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF2196F3),

                        ) {
                            NavigationBarItem(
                                selected = true,
                                onClick = { navController.navigate(Home) },
                                icon = { },
                                label = { Text("Главная") },

                            )
                            NavigationBarItem(
                                selected = true,
                                onClick = {navController.navigate(Details) },
                                icon = { },
                                label = { Text("Дневник Растений") },

                            )
                        }
                    }

                ) { innerPadding ->
                    AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
                }
            }
        }
    }
}


