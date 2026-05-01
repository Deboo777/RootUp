package com.example.rootup.model

import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {

    val allPlants: Flow<List<Plant>> = plantDao.getAllPlants()

    suspend fun insertPlant(plant: Plant) {
        plantDao.insertPlant(plant)
    }

    suspend fun updatePlant(plant: Plant) {
        plantDao.updatePlant(plant)
    }

    suspend fun deleteById(plantId: Int) {
        plantDao.deleteById(plantId)
    }
}
