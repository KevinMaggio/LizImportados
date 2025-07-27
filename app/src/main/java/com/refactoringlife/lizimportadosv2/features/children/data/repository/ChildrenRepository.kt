package com.refactoringlife.lizimportadosv2.features.children.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.service.ProductException
import com.refactoringlife.lizimportadosv2.core.network.service.ProductService

class ChildrenRepository(
    private val service: ProductService = ProductService()
) {
    suspend fun getChildrenProducts(): Either<List<ProductResponse>, String> {
        return try {
            val response = service.getChildrenProducts()
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("ChildrenRepository", "Error obteniendo productos de niños", e)
            Either.Error(e.message ?: "Error")
        } catch (e: Exception) {
            Log.e("ChildrenRepository", "Error inesperado obteniendo productos de niños", e)
            Either.Error("Error inesperado: ${e.message}")
        }
    }
} 