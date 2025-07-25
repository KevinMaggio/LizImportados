package com.refactoringlife.lizimportados.features.home.domain.usecase

import Either
import com.refactoringlife.lizimportados.features.home.data.model.HomeDataModel
import com.refactoringlife.lizimportados.features.home.data.repository.HomeRepository
import com.refactoringlife.lizimportados.features.home.domain.mapper.toProductModel

class GetHomeDataUseCase(
    private val repository: HomeRepository = HomeRepository()
) {
    suspend fun getHomeData(): Either<HomeDataModel, String> {
        // 1. Obtener configuración
        return when (val configResult = repository.getHomeConfig()) {
            is Either.Success -> {
                val config = configResult.value
                val homeDataModel = HomeDataModel(config = config)

                // 2. Si hay weekly_offers, obtener productos en oferta
                val offersProducts = if (config.isOffers) {
                    when (val offersResult = repository.getOffersProducts()) {
                        is Either.Success -> offersResult.value.map { it.toProductModel() }
                        else -> null
                    }
                } else null

                // 3. Procesar todos los combos activos
                val comboProducts = config.combos?.mapNotNull { combo ->
                    if (combo.showCombo && !combo.comboID.isNullOrEmpty()) {
                        when (val comboResult = repository.getProductById(combo.comboID)) {
                            is Either.Success -> comboResult.value.toProductModel()
                            else -> null
                        }
                    } else null
                }

                // 4. Retornar todo junto
                Either.Success(
                    homeDataModel.copy(
                        offersProducts = offersProducts,
                        comboProduct = comboProducts
                    )
                )
            }
            is Either.Error -> Either.Error(configResult.value)
            else -> Either.Error("Error desconocido")
        }
    }
} 