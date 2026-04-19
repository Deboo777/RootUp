package com.example.rootup.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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