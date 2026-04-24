package com.example.rootup.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rootup.model.AppDatabase
import com.example.rootup.model.PlantRepository
import com.example.rootup.view.LoginScreen
import com.example.rootup.view.MainDin
import com.example.rootup.view.MainWin
import com.example.rootup.view.OfficeScreen
import com.example.rootup.view.RegistrationScreen
import com.example.rootup.viewmodel.PlantViewModel
import com.example.rootup.viewmodel.PlantViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val database = AppDatabase.getDatabase(context)
    val repository = PlantRepository(database.plantDao())

    val plantViewModel: PlantViewModel = viewModel(
        factory = PlantViewModelFactory(repository)
    )

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
                    navController.navigate(Login) { popUpTo(0) }
                }
            } else {
                MainWin(viewModel = plantViewModel)
            }
        }

        composable<Office> {
            if (auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Login) { popUpTo(0) }
                }
            } else {
                OfficeScreen()
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
                MainDin(viewModel = plantViewModel)
            }
        }
    }
}