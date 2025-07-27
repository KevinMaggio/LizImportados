package com.refactoringlife.lizimportadosv2.features.details.data.repository

import Either
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.service.ProductException
import com.refactoringlife.lizimportadosv2.core.network.service.ProductService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsRepository private constructor(
    private val service: ProductService = ProductService()
) {
    companion object {
        @Volatile
        private var INSTANCE: DetailsRepository? = null
        
        fun getInstance(): DetailsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DetailsRepository().also { INSTANCE = it }
            }
        }
    }

    // Flow local para almacenar productos en memoria
    private val _productsFlow = MutableStateFlow<List<ProductResponse>>(emptyList())
    val productsFlow: StateFlow<List<ProductResponse>> = _productsFlow.asStateFlow()

    // Control de paginación
    private var lastDocument: DocumentSnapshot? = null
    private var hasMoreProducts = true
    private var isLoading = false
    private var currentProductId: String? = null

    suspend fun getProductById(id: String): Either<ProductResponse, String> {
        return try {
            val response = service.getProductById(id)
            currentProductId = id // Guardamos el ID del producto principal
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("DetailsRepository", "Error obteniendo producto por ID", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun loadInitialProducts() {
        if (isLoading) return
        
        isLoading = true
        try {
            // Filtramos directamente en Firestore para evitar traer el producto principal
            val products = service.getRelatedProducts(limit = 10, excludeProductId = currentProductId)
            
            _productsFlow.value = products
            hasMoreProducts = products.isNotEmpty()
            Log.d("DetailsRepository", "✅ Cargados ${products.size} productos (excluyendo producto principal)")
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando productos iniciales", e)
        } finally {
            isLoading = false
        }
    }

    suspend fun loadMoreProductsIfNeeded(currentIndex: Int) {
        // Cargar más productos cuando el usuario llegue al 8vo elemento
        if (currentIndex >= 7 && hasMoreProducts && !isLoading) {
            Log.d("DetailsRepository", "🔄 Cargando más productos desde índice $currentIndex")
            loadMoreProducts()
        }
    }

    private suspend fun loadMoreProducts() {
        if (isLoading || !hasMoreProducts) return
        
        isLoading = true
        try {
            // Filtramos directamente en Firestore para evitar traer el producto principal
            val newProducts = service.getMoreProducts(limit = 10, lastDocument, excludeProductId = currentProductId)
            
            if (newProducts.isNotEmpty()) {
                val currentProducts = _productsFlow.value
                _productsFlow.value = currentProducts + newProducts
                Log.d("DetailsRepository", "✅ Agregados ${newProducts.size} productos más. Total: ${_productsFlow.value.size}")
            } else {
                hasMoreProducts = false
                Log.d("DetailsRepository", "📭 No hay más productos disponibles")
            }
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando más productos", e)
        } finally {
            isLoading = false
        }
    }

    fun clearProducts() {
        _productsFlow.value = emptyList()
        lastDocument = null
        hasMoreProducts = true
        isLoading = false
        currentProductId = null
    }

    fun getCurrentProducts(): List<ProductResponse> = _productsFlow.value

    fun hasMoreProducts(): Boolean = hasMoreProducts

    fun isLoading(): Boolean = isLoading
} 