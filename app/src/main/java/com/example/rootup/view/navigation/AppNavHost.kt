package com.example.rootup.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rootup.view.MainDin
import com.example.rootup.view.MainWin

@Composable
fun AppNavHost(modifier: Modifier, navController: NavHostController) {

    NavHost( navController = navController,
        startDestination = Home,
        modifier = modifier) {
        composable<Home> {
            MainWin()

        }
        composable<Details> {
            MainDin()
        }
    }
}