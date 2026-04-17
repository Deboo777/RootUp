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

    private val auth: FirebaseAuth = Firebase.auth

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }

        isLoading = true
        errorMessage = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isLoading = false
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = it.exception?.localizedMessage ?: "Ошибка входа"
                }
            }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank()) {
            errorMessage = "Введите email"
            return
        }

        if (password.length < 6) {
            errorMessage = "Пароль должен быть не менее 6 символов"
            return
        }

        isLoading = true
        errorMessage = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isLoading = false
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = it.exception?.localizedMessage ?: "Ошибка регистрации"
                }
            }
    }

    fun loginAsGuest(onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null

        auth.signInAnonymously()
            .addOnCompleteListener {
                isLoading = false
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = it.exception?.localizedMessage ?: "Ошибка гостевого входа"
                }
            }
    }
}