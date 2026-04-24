package com.example.rootup.model


import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {

    val allPlants: Flow<List<Plant>> = plantDao.getAllPlants()

    suspend fun insert(plant: Plant) {
        plantDao.insertPlant(plant)
    }

    suspend fun update(plant: Plant) {
        plantDao.updatePlant(plant)
    }

    suspend fun delete(plantId: Int) {
        plantDao.deleteById(plantId)
    }
}
