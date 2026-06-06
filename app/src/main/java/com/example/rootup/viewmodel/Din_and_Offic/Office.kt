package com.example.rootup.viewmodel.Din_and_Offic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rootup.model.autho.OfficeState
import com.example.rootup.model.data_plant.PlantRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OfficeViewModel(private val repository: PlantRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val state: StateFlow<OfficeState> = repository.allPlants
        .map { allPlants ->
            val myPlants = allPlants.filter { it.isAddedToDiary == 1 }

            val currentUser = auth.currentUser
            val displayName = when {
                currentUser == null -> "Неавторизован"
                currentUser.isAnonymous -> "Аноним"
                else -> currentUser.email?.substringBefore("@") ?: "Пользователь"
            }

            OfficeState(
                userName = displayName,
                plantsCount = myPlants.size,
                plants = myPlants.map { it.name },
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OfficeState(isLoading = true)
        )
}
class OfficeViewModelFactory(private val repository: PlantRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OfficeViewModel(repository) as T
    }
}
