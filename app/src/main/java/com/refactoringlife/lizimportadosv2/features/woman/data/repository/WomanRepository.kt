package com.refactoringlife.lizimportadosv2.features.woman.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.service.ProductException
import com.refactoringlife.lizimportadosv2.core.network.service.ProductService

class WomanRepository(
    private val service: ProductService = ProductService()
) {
    suspend fun getWomanProducts(): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getWomanProducts()
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("WomanRepository", "Error obteniendo productos de mujer", e)
            Either.Error(e.message ?: "Error")
        }
    }
} 