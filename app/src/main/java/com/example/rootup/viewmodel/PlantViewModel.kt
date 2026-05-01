package com.example.rootup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rootup.model.Plant
import com.example.rootup.model.PlantRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantViewModel(private val repository: PlantRepository) : ViewModel() {
    val allPlants: StateFlow<List<Plant>> = repository.allPlants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val catalogPlants: StateFlow<List<Plant>> = repository.allPlants
        .map { list -> list.filter { it.isAddedToDiary == 0 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val diaryPlants: StateFlow<List<Plant>> = repository.allPlants
        .map { list -> list.filter { it.isAddedToDiary == 1 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getDaysPassed(timestamp: Long?): Int {
        if (timestamp == null || timestamp < 1000000000000L) return 0
        val diff = System.currentTimeMillis() - timestamp
        val days = (diff / (1000 * 60 * 60 * 24)).toInt()
        return if (days > 0) days else 0
    }

    fun insertNewTypeToCatalog(name: String, interval: String, description: String) {
        val newPlant = Plant(
            name = name,
            description = description,
            photo_path = null,
            water_interval_days = interval.toIntOrNull() ?: 3,
            last_watered_timestamp = 0L,
            isAddedToDiary = 0
        )
        viewModelScope.launch {
            repository.insertPlant(newPlant)
        }
    }

    fun addCustomPlant(plant: Plant, customName: String) {
        viewModelScope.launch {
            val newPlant = plant.copy(
                id = null,
                name = customName,
                isAddedToDiary = 1,
                last_watered_timestamp = System.currentTimeMillis()
            )
            repository.insertPlant(newPlant)
        }
    }


    fun waterPlant(plant: Plant) {
        viewModelScope.launch {
            val updatedPlant = plant.copy(last_watered_timestamp = System.currentTimeMillis())
            repository.updatePlant(updatedPlant)
        }
    }

    fun deletePlant(plantId: Int) {
        viewModelScope.launch {
            repository.deleteById(plantId)
        }
    }
    fun updatePlant(plant: Plant) {
        viewModelScope.launch {
            repository.updatePlant(plant)
        }
    }
}

class PlantViewModelFactory(private val repository: PlantRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}