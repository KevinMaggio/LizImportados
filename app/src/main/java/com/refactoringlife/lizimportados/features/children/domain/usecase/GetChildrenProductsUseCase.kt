package com.refactoringlife.lizimportados.features.children.domain.usecase

import Either
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.children.data.repository.ChildrenRepository
import com.refactoringlife.lizimportados.features.man.domain.mapper.toDomain

class GetChildrenProductsUseCase(
    private val repository: ChildrenRepository = ChildrenRepository()
) {
    suspend operator fun invoke(): Either<List<ProductModel>, String> {
        return when (val result = repository.getChildrenProducts()) {
            is Either.Success -> Either.Success(result.value.map { it.toDomain() })
            is Either.Error -> Either.Error(result.value)
            else -> Either.Error("Error")
        }
    }
} 