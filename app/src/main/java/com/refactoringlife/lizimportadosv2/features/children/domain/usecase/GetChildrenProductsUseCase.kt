package com.refactoringlife.lizimportadosv2.features.children.domain.usecase

import Either
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.features.children.data.repository.ChildrenRepository
import com.refactoringlife.lizimportadosv2.features.home.domain.mapper.toProductModel

class GetChildrenProductsUseCase(
    private val repository: ChildrenRepository = ChildrenRepository()
) {
    suspend operator fun invoke(): Either<List<ProductModel>, String> {
        return when (val result = repository.getChildrenProducts()) {
            is Either.Success -> Either.Success(result.value.map { it.toProductModel() })
            is Either.Error -> Either.Error(result.value)
            else -> Either.Error("Error")
        }
    }
} 