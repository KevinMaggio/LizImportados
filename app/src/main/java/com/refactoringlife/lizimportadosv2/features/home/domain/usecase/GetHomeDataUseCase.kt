package com.refactoringlife.lizimportadosv2.features.home.domain.usecase

import Either
import com.refactoringlife.lizimportadosv2.features.home.data.model.HomeDataModel
import com.refactoringlife.lizimportadosv2.features.home.data.repository.HomeRepository
import com.refactoringlife.lizimportadosv2.features.home.domain.mapper.toProductModel

class GetHomeDataUseCase(
    private val repository: HomeRepository = HomeRepository()
) {
    suspend fun getHomeData(): Either<HomeDataModel, String> {
        return when (val configResult = repository.getHomeConfig()) {
            is Either.Success -> {
                val config = configResult.value
                val homeData = HomeDataModel(config = config)

                // Si hay ofertas activas, obtener productos en oferta
                val offersProducts = if (config.isOffers) {
                    when (val offersResult = repository.getOffersProducts()) {
                        is Either.Success -> offersResult.value.map { it.toProductModel() }
                        else -> null
                    }
                } else null

                // Los combos ya vienen en el config, no necesitamos hacer otra llamada
                Either.Success(
                    homeData.copy(
                        offersProducts = offersProducts,
                        comboProduct = config.combos
                    )
                )
            }
            is Either.Error -> Either.Error(configResult.value)
            else -> Either.Error("Error desconocido")
        }
    }
} 