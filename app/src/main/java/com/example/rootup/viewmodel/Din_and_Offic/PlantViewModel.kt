package com.example.rootup.viewmodel.Din_and_Offic

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rootup.model.data_plant.Plant
import com.example.rootup.model.data_plant.PlantRepository
import com.example.rootup.model.ImgBB.RetrofitClient
import com.example.rootup.model.autho.UploadState
import com.example.rootup.viewmodel.alarm.WateringAlarmManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class PlantViewModel(private val repository: PlantRepository) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

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
    fun uploadPlantPhoto(bitmap: Bitmap, plantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uploadState.value = UploadState.Loading

            val result = runCatching {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                val byteArray = stream.toByteArray()

                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
                val imagePart = MultipartBody.Part.createFormData("image", "plant_${plantId}.jpg", requestFile)

                val apiKey = "242ac7f8c94b812068314c20aa9463f2"
                val response = RetrofitClient.api.uploadImage(apiKey, imagePart)

                if (!response.success) {
                    error("Сервер вернул ошибку: ${response.status}")
                }

                response.data?.displayUrl ?: response.data?.url ?: error("Ссылка отсутствует в ответе")
            }

            result.onSuccess { imageUrl ->
                val currentPlant = allPlants.value.find { it.id == plantId }
                currentPlant?.let { p ->
                    val currentPhotos = p.diary_photos_str ?: ""
                    val updatedPhotosStr = if (currentPhotos.isEmpty()) imageUrl else "$currentPhotos,$imageUrl"

                    val updatedPlant = p.copy(
                        diary_photos_str = updatedPhotosStr,
                        photo_path = p.photo_path ?: imageUrl
                    )
                    repository.updatePlant(updatedPlant)
                }
                _uploadState.value = UploadState.Success(imageUrl)
            }.onFailure { exception ->
                _uploadState.value = UploadState.Error(exception.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun updatePlantWateringDate(plantId: Int, selectedMillis: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentPlant = allPlants.value.find { it.id == plantId }
            currentPlant?.let { p ->
                val updatedPlant = p.copy(last_watered_timestamp = selectedMillis)
                repository.updatePlant(updatedPlant)
            }
        }
    }

    fun getDaysPassed(timestamp: Long?): Int {
        if (timestamp == null || timestamp < 1000000000000L) return 0
        val diff = System.currentTimeMillis() - timestamp
        val days = (diff / (1000 * 60 * 60 * 24)).toInt()
        return if (days > 0) days else 0
    }

    @SuppressLint("ScheduleExactAlarm")
    fun waterPlant(plant: Plant, context: Context? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedPlant = plant.copy(last_watered_timestamp = System.currentTimeMillis())
            context?.let {
                WateringAlarmManager.scheduleWateringAlarm(it, updatedPlant)
            }
            repository.updatePlant(updatedPlant)
        }
    }

    fun insertNewTypeToCatalog(name: String, interval: String, description: String) {
        val newPlant = Plant(
            name = name,
            description = description,
            photo_path = null,
            water_interval_days = interval.toIntOrNull() ?: 3,
            last_watered_timestamp = 0L,
            isAddedToDiary = 0,
            diary_photos_str = ""
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPlant(newPlant)
        }
    }

    fun addCustomPlant(plant: Plant, customName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newPlant = plant.copy(
                id = null,
                name = customName,
                isAddedToDiary = 1,
                last_watered_timestamp = System.currentTimeMillis(),
                diary_photos_str = ""
            )
            repository.insertPlant(newPlant)
        }
    }

    fun deletePlant(plantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(plantId)
        }
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
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