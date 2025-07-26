package com.refactoringlife.lizimportadosv2.features.man.domain.usecase

import Either
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.features.man.data.repository.ManRepository
import com.refactoringlife.lizimportadosv2.features.home.domain.mapper.toProductModel

class GetMenProductsUseCase(
    private val repository: ManRepository = ManRepository()
) {
    suspend operator fun invoke(): Either<List<ProductModel>, String> {
        return when (val result = repository.getMenProducts()) {
            is Either.Success -> Either.Success(result.value.map { it.toProductModel() })
            is Either.Error -> Either.Error(result.value)
            else -> {Either.Error("Error")}
        }
    }
} 