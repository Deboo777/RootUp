package com.example.rootup.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rootup.view.LoginScreen
import com.example.rootup.view.MainDin
import com.example.rootup.view.MainWin
import com.example.rootup.view.RegistrationScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController
) {

    val auth = FirebaseAuth.getInstance()
    val isLoggedIn = auth.currentUser != null

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Login
    ) {

        composable<Login> {

            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Registration)
                }
            )
        }

        composable<Registration> {

            RegistrationScreen(
                onSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Home> {

            if (auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Login) {
                        popUpTo(0)
                    }
                }
            } else {
                MainWin()
            }
        }

        composable<Details> {

            if (auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Login) {
                        popUpTo(0)
                    }
                }
            } else {
                MainDin()
            }
        }
    }
}