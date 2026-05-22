package com.example.rootup.model

sealed interface UploadState {
    object Idle : UploadState
    object Loading : UploadState
    data class Success(val imageUrl: String) : UploadState
    data class Error(val message: String) : UploadState
}