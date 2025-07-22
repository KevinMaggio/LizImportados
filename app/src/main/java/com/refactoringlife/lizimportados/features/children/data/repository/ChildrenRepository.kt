package com.refactoringlife.lizimportados.features.children.data.repository

import Either
import android.util.Log
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.service.ProductException
import com.refactoringlife.lizimportados.core.network.service.ProductService

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
        }
    }
} 