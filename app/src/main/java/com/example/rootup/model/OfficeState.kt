package com.example.rootup.model

data class OfficeState(
    val userName: String = "Александр",
    val plantsCount: Int = 0,
    val plants: List<String> = emptyList(),
    val isLoading: Boolean = false
)