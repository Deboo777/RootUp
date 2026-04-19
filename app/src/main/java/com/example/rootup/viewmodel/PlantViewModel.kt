package com.example.rootup.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel


class PlantViewModel : ViewModel() {

    var completedDays by mutableIntStateOf(3)
    val totalDays = 7

    fun addProgress() {
        if (completedDays < totalDays) completedDays++
        else completedDays = 0
    }

    fun resetProgress() {
        completedDays = 0
    }


}