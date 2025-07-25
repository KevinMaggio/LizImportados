package com.refactoringlife.lizimportados.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await

class ProductService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance
) {
    suspend fun getHomeConfig(): ConfigResponse {
        return try {
            firestore.collection("config")
                .document("home")
                .get()
                .await()
                .toObject(ConfigResponse::class.java) 
                ?: throw ProductException("Error: Configuración no encontrada")
        } catch (e: Exception) {
            throw ProductException("Error al obtener configuración de home", e)
        }
    }

    suspend fun getProductById(id: String): ProductResponse {
        return try {
            firestore.collection("products")
                .document(id)
                .get()
                .await()
                .toObject(ProductResponse::class.java)
                ?: throw ProductException("Producto no encontrado")
        } catch (e: Exception) {
            throw ProductException("Error al obtener producto por ID", e)
        }
    }

    suspend fun getOffersProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("is_offer", true)
                .whereEqualTo("is_available", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos en oferta", e)
        }
    }

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