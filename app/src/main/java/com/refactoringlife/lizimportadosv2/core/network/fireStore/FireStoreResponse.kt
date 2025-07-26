package com.refactoringlife.lizimportadosv2.core.network.fireStore

sealed class FireStoreResponse<out T> {
    data object Loading : FireStoreResponse<Nothing>()
    data class Success<out T>(val data: T) : FireStoreResponse<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : FireStoreResponse<Nothing>()
}