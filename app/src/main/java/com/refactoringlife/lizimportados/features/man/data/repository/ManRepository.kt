package com.refactoringlife.lizimportados.features.man.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.service.ProductException
import com.refactoringlife.lizimportados.core.network.service.ProductService

class ManRepository(
    private val service: ProductService = ProductService()
) {
    suspend fun getMenProducts(): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getMenProducts()
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("ManRepository", "Error obteniendo productos de hombre", e)
            Either.Error(e.message ?: "Error")
        }
    }
} 