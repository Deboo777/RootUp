package com.example.rootup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rootup.model.OfficeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class Office : ViewModel() {

    private val _state = MutableStateFlow(OfficeState())
    val state: StateFlow<OfficeState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        _state.value = OfficeState(
            userName = "Пользователь растиневод",
            plantsCount = 5,
            plants = listOf("Монстера", "Фикус", "Кактус", "Алоэ", "Орхидея"),
            isLoading = false
        )
    }
}