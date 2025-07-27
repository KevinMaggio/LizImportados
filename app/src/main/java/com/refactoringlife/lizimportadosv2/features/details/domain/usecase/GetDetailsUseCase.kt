package com.refactoringlife.lizimportadosv2.features.details.domain.usecase

import Either
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.features.details.data.repository.DetailsRepository
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.features.home.domain.mapper.toProductModel

class GetDetailsUseCase(
    private val repository: DetailsRepository = DetailsRepository()
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

    suspend fun getInitialRelatedProducts(): Either<List<ProductModel>, String> {
        return try {
            val result = repository.getRelatedProducts()
            when (result) {
                is Either.Success -> Either.Success(result.value.map { it.toProductModel() })
                is Either.Error -> Either.Error(result.value)
                else -> Either.Error("Error desconocido")
            }
        } catch (e: Exception) {
            Either.Error("Error al obtener productos relacionados: ${e.message}")
        }
    }

    suspend fun getMoreProducts(lastDocumentId: String?): Either<List<ProductModel>, String> {
        return try {
            val result = repository.getMoreProducts(lastDocumentId)
            when (result) {
                is Either.Success -> Either.Success(result.value.map { it.toProductModel() })
                is Either.Error -> Either.Error(result.value)
                else -> Either.Error("Error desconocido")
            }
        } catch (e: Exception) {
            Either.Error("Error al obtener más productos: ${e.message}")
        }
    }
} 