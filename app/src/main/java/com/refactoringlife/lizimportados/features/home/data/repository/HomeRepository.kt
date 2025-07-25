package com.refactoringlife.lizimportados.features.home.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.service.ProductException
import com.refactoringlife.lizimportados.core.network.service.ProductService

class HomeRepository(
    private val service: ProductService = ProductService()
) {
    suspend fun getHomeConfig(): Either<ConfigResponse, String> {
        return try {
            val response = service.getHomeConfig()
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("HomeRepository", "Error obteniendo configuración de home", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun getOffersProducts(): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getOffersProducts()
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("HomeRepository", "Error obteniendo productos en oferta", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun getProductById(id: String): Either<ProductResponse, String> {
        return try {
            val response = service.getProductById(id)
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("HomeRepository", "Error obteniendo producto por ID", e)
            Either.Error(e.message ?: "Error")
        }
    }
} 