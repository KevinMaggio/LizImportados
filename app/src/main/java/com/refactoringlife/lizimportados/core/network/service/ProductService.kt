package com.refactoringlife.lizimportados.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await

class ProductService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance
) {
    suspend fun getMenProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Hombre")
                .whereEqualTo("is_available", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de hombre", e)
        }
    }

    suspend fun getChildrenProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("is_available", true)
                .whereIn("gender", listOf("Niño", "Bebe"))
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de niños", e)
        }
    }

    suspend fun getWomanProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Mujer")
                .whereEqualTo("is_available", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de mujer", e)
        }
    }
}

class ProductException(message: String, cause: Throwable? = null) : Exception(message, cause)