package com.refactoringlife.lizimportadosv2.features.details.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.service.ProductException
import com.refactoringlife.lizimportadosv2.core.network.service.ProductService

class DetailsRepository(
    private val service: ProductService = ProductService()
) {
    suspend fun getProductById(id: String): Either<ProductResponse, String> {
        return try {
            val response = service.getProductById(id)
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("DetailsRepository", "Error obteniendo producto por ID", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun getRelatedProducts(): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getRelatedProducts(limit = 10)
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("DetailsRepository", "Error obteniendo productos relacionados", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun getMoreProducts(lastDocumentId: String?): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getMoreProducts(limit = 10, lastDocument = null) // TODO: Implementar cursor
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("DetailsRepository", "Error obteniendo más productos", e)
            Either.Error(e.message ?: "Error")
        }
    }
} 