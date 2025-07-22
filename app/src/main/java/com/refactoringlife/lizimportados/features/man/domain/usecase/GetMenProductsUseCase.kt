package com.refactoringlife.lizimportados.features.man.domain.usecase

import Either
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.man.data.repository.ManRepository
import com.refactoringlife.lizimportados.features.man.domain.mapper.toDomain

class GetMenProductsUseCase(
    private val repository: ManRepository = ManRepository()
) {
    suspend operator fun invoke(): Either<List<ProductModel>, String> {
        return when (val result = repository.getMenProducts()) {
            is Either.Success -> Either.Success(result.value.map { it.toDomain() })
            is Either.Error -> Either.Error(result.value)
            else -> {Either.Error("Error")}
        }
    }
} 