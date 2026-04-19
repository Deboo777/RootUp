package com.example.rootup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rootup.model.AppState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*

class Registration_Login : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()


    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }


    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Заполните все поля") }
            return
        }


        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        auth.signInWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userEmail = task.result?.user?.email ?: state.email
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userName = userEmail.substringBefore("@")
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Ошибка входа"
                        )
                    }
                }
            }
    }

    fun signUp(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Заполните все поля") }
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Пароли не совпадают") }
            return
        }
        if (state.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Пароль должен быть от 6 символов") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        auth.createUserWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userName = state.email.substringBefore("@")
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Ошибка регистрации"
                        )
                    }
                }
            }
    }

    fun loginAsGuest(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userName = "Гость"
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Ошибка"
                        )
                    }
                }
            }
    }

    fun logout(onNavigate: () -> Unit) {
        auth.signOut()
        _uiState.value = AppState()
        onNavigate()
    }
}