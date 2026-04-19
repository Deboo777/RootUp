package com.example.rootup.model

data class AppState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userName: String = "",
    val plants: List<String> = emptyList(),
    val plantsCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)
