package com.refactoringlife.lizimportadosv2.features.details.data.repository

import Either
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.service.ProductException
import com.refactoringlife.lizimportadosv2.core.network.service.ProductService

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

    // Control de paginación
    private var lastDocument: DocumentSnapshot? = null
    private var hasMoreProducts = true
    private var currentProductId: String? = null
    private var currentCategory: String? = null // Agregar control de categoría actual
    
    // Control de productos ya cargados para evitar duplicados
    private val loadedProductIds = mutableSetOf<String>()

    suspend fun getProductById(id: String): Either<ProductResponse, String> {
        return try {
            val response = service.getProductById(id)
            currentProductId = id
            currentCategory = null // Resetear categoría
            loadedProductIds.add(id) // Agregar el producto principal
            Either.Success(response)
        } catch (e: ProductException) {
            Log.e("DetailsRepository", "Error obteniendo producto por ID", e)
            Either.Error(e.message ?: "Error")
        }
    }

    suspend fun loadProductsByCategory(category: String): List<ProductResponse> {
        lastDocument = null
        hasMoreProducts = true
        currentCategory = category // Guardar categoría actual
        currentProductId = null // Resetear producto principal
        loadedProductIds.clear() // Limpiar productos cargados
        
        Log.d("DetailsRepository", "🔍 Buscando productos por categoría: '$category'")
        
        return try {
            val products = service.getProductsByCategory(category, limit = 10) // Traer más para filtrar
            Log.d("DetailsRepository", "📦 Encontrados ${products.size} productos de categoría '$category'")
            
            val filteredProducts = products.filter { !loadedProductIds.contains(it.id) }.take(2)
            
            // Agregar los productos cargados al set
            filteredProducts.forEach { loadedProductIds.add(it.id) }
            
            hasMoreProducts = products.size >= 2
            Log.d("DetailsRepository", "✅ Cargados ${filteredProducts.size} productos de categoría: '$category'")
            filteredProducts
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando productos por categoría: '$category'", e)
            emptyList()
        }
    }

    suspend fun loadRandomProducts(excludeProductId: String): List<ProductResponse> {
        currentProductId = excludeProductId
        currentCategory = null // Resetear categoría
        lastDocument = null
        hasMoreProducts = true
        loadedProductIds.clear() // Limpiar productos cargados
        loadedProductIds.add(excludeProductId) // Agregar el producto principal
        
        return try {
            val products = service.getRelatedProducts(limit = 10, excludeProductId = excludeProductId) // Traer más para filtrar
            val filteredProducts = products.filter { !loadedProductIds.contains(it.id) }.take(2)
            
            // Agregar los productos cargados al set
            filteredProducts.forEach { loadedProductIds.add(it.id) }
            
            hasMoreProducts = products.size >= 2
            Log.d("DetailsRepository", "✅ Cargados ${filteredProducts.size} productos aleatorios")
            filteredProducts
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando productos aleatorios", e)
            emptyList()
        }
    }

    suspend fun loadMoreProducts(currentIndex: Int): List<ProductResponse> {
        if (!hasMoreProducts) return emptyList()
        
        return try {
            val newProducts = if (currentCategory != null) {
                // Si hay categoría actual, cargar más productos de esa categoría
                Log.d("DetailsRepository", "🔄 Cargando más productos de categoría: '$currentCategory'")
                service.getMoreProductsByCategory(currentCategory!!, limit = 10, lastDocument)
            } else {
                // Si no hay categoría, cargar productos aleatorios
                Log.d("DetailsRepository", "🔄 Cargando productos aleatorios")
                service.getMoreProducts(limit = 10, lastDocument, currentProductId)
            }
            
            val filteredProducts = newProducts.filter { !loadedProductIds.contains(it.id) }.take(2)
            
            // Agregar los productos cargados al set
            filteredProducts.forEach { loadedProductIds.add(it.id) }
            
            if (filteredProducts.isNotEmpty()) {
                Log.d("DetailsRepository", "✅ Agregados ${filteredProducts.size} productos más (sin duplicados)")
            } else {
                hasMoreProducts = false
                Log.d("DetailsRepository", "📭 No hay más productos disponibles")
            }
            
            filteredProducts
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando más productos", e)
            emptyList()
        }
    }

    fun clearProducts() {
        lastDocument = null
        hasMoreProducts = true
        currentProductId = null
        currentCategory = null // Limpiar categoría actual
        loadedProductIds.clear() // Limpiar productos cargados
    }
} 