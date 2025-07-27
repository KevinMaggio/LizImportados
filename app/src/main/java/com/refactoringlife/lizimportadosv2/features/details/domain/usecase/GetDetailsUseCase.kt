package com.refactoringlife.lizimportadosv2.features.details.domain.usecase

import Either
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.features.details.data.repository.DetailsRepository

class GetDetailsUseCase(
    private val repository: DetailsRepository = DetailsRepository.getInstance()
) {
    suspend fun getProductDetails(productId: String): Either<ProductResponse, String> {
        return try {
            val result = repository.getProductById(productId)
            when (result) {
                is Either.Success -> Either.Success(result.value)
                is Either.Error -> Either.Error(result.value)
                else -> Either.Error("Error desconocido")
            }
        } catch (e: Exception) {
            Either.Error("Error al obtener detalles del producto: ${e.message}")
        }
    }
} 