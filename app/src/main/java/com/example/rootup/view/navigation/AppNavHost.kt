package com.example.rootup.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rootup.model.AppDatabase
import com.example.rootup.model.PlantRepository
import com.example.rootup.view.*
import com.example.rootup.viewmodel.OfficeViewModel
import com.example.rootup.viewmodel.OfficeViewModelFactory
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
    val officeViewModel: OfficeViewModel = viewModel(
        factory = OfficeViewModelFactory(repository)
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
                    }
                },
                onRegisterClick = { navController.navigate(Registration) }
            )
        }

        composable<Registration> {
            RegistrationScreen(
                onSuccess = { navController.navigate(Home) { popUpTo(Login) { inclusive = true } } },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Home> {
            if (auth.currentUser == null) {
                LaunchedEffect(Unit) { navController.navigate(Login) { popUpTo(0) } }
            } else {
                MainWin(
                    viewModel = plantViewModel,
                    onPlantClick = { id -> navController.navigate(Detait(plantId = id)) },
                    onAddNewTypeClick = { navController.navigate(AddPlantRoute) }
                )
            }
        }

        composable<Detait> { backStackEntry ->
            val args: Detait = backStackEntry.toRoute()
            Detilit(
                plantId = args.plantId,
                viewModel = plantViewModel,
                onAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable<Details> {
            MainDin(
                viewModel = plantViewModel,
                onPlantClick = { id ->
                    navController.navigate(PlantStats(plantId = id))
                }
            )
        }

        composable<PlantStats> { backStackEntry ->
            val args: PlantStats = backStackEntry.toRoute()
            PlantStatsScreen(
                plantId = args.plantId,
                viewModel = plantViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Office> {
            OfficeScreen(viewModel = officeViewModel)
        }

        composable<AddPlantRoute> {
            AddPlantScreen(
                viewModel = plantViewModel,
                onSave = { navController.popBackStack() }
            )
        }
    }
}



