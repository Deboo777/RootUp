package com.example.rootup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PlantViewModel : ViewModel() {
    var completedDays by mutableIntStateOf(3)
    val totalDays = 7

    fun addProgress() {
        if (completedDays < totalDays) {
            completedDays++
        } else {
            completedDays = 0
        }
    }
    fun ProgressNull(){
        completedDays = 0
    }
}