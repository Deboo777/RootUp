package com.example.rootup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rootup.ui.theme.RootUpTheme
import com.example.rootup.view.navigation.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootUpTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {

                        val isAuthScreen = currentDestination?.hasRoute<Login>() == true ||
                                currentDestination?.hasRoute<Registration>() == true

                        if (!isAuthScreen && currentDestination != null) {
                            NavigationBar(
                                containerColor = Color(0xFF2196F3),
                                contentColor = Color.White
                            ) {
                                NavigationBarItem(
                                    selected = currentDestination.hasRoute<Home>(),
                                    onClick = {
                                        navController.navigate(Home) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
                                    label = { Text("Главная") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        indicatorColor = Color(0xFF1976D2)
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentDestination.hasRoute<Details>(),
                                    onClick = {
                                        navController.navigate(Details) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.List, contentDescription = "Дневник") },
                                    label = { Text("Дневник") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        indicatorColor = Color(0xFF1976D2)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentDestination.hasRoute<Office>(),
                                    onClick = {
                                        navController.navigate(Office) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.List, contentDescription = "Кабинет") },
                                    label = { Text("Кабинет") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        indicatorColor = Color(0xFF1976D2)
                                    )
                                )
                            }
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