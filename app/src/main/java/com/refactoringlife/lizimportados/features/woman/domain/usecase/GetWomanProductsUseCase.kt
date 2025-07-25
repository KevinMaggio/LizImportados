package com.refactoringlife.lizimportados.features.woman.domain.usecase

import Either
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.woman.data.repository.WomanRepository
import com.refactoringlife.lizimportados.features.home.domain.mapper.toProductModel

class GetWomanProductsUseCase(
    private val repository: WomanRepository = WomanRepository()
) {
    suspend operator fun invoke(): Either<List<ProductModel>, String> {
        return when (val result = repository.getWomanProducts()) {
            is Either.Success -> Either.Success(result.value.map { it.toProductModel() })
            is Either.Error -> Either.Error(result.value)
            else -> Either.Error("Error")
        }
    }
} 