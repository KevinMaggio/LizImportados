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
            // Primero traer productos sin filtro de disponibilidad
            val allProducts = service.getProductsByCategoryForScroll(category, limit = 3)
            Log.d("DetailsRepository", "📊 Total de productos encontrados en categoría '$category': ${allProducts.size}")
            
            // Filtrar por disponibilidad
            val availableProducts = allProducts.filter { it.isAvailable == true }
            Log.d("DetailsRepository", "✅ Productos disponibles en categoría '$category': ${availableProducts.size}")
            
            val filteredProducts = availableProducts.filter { !loadedProductIds.contains(it.id) }.take(3)
            
            // Agregar los productos cargados al set
            filteredProducts.forEach { loadedProductIds.add(it.id) }
            
            // Si encontramos productos disponibles, asumimos que puede haber más
            hasMoreProducts = availableProducts.isNotEmpty()
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
            // Primero traer productos sin filtro de disponibilidad
            val allProducts = service.getRelatedProductsForScroll(limit = 3, excludeProductId = excludeProductId)
            Log.d("DetailsRepository", "📊 Total de productos encontrados: ${allProducts.size}")
            
            // Filtrar por disponibilidad
            val availableProducts = allProducts.filter { it.isAvailable == true }
            Log.d("DetailsRepository", "✅ Productos disponibles: ${availableProducts.size}")
            
            val filteredProducts = availableProducts.filter { !loadedProductIds.contains(it.id) }.take(3)
            Log.d("DetailsRepository", "📦 Productos filtrados para mostrar: ${filteredProducts.size}")
            
            // Agregar los productos cargados al set
            filteredProducts.forEach { loadedProductIds.add(it.id) }
            
            // Si encontramos productos disponibles, asumimos que puede haber más
            hasMoreProducts = availableProducts.isNotEmpty()
            Log.d("DetailsRepository", "✅ Cargados ${filteredProducts.size} productos aleatorios")
            filteredProducts
        } catch (e: Exception) {
            Log.e("DetailsRepository", "❌ Error cargando productos aleatorios", e)
            emptyList()
        }
    }

    // Cargar más productos cuando el usuario hace scroll
    suspend fun loadMoreProducts(currentIndex: Int): List<ProductResponse> {
        Log.d("DetailsRepository", "📱 Cargando más productos. Índice actual: $currentIndex")
        
        return try {
            val newProducts = if (currentCategory != null) {
                // Si estamos en una categoría específica, cargar más de esa categoría
                val allProducts = service.getProductsByCategoryForScroll(currentCategory!!, limit = 3)
                val availableProducts = allProducts.filter { it.isAvailable == true }
                availableProducts.filter { !loadedProductIds.contains(it.id) }.take(3)
            } else {
                // Si no hay categoría, cargar productos aleatorios
                val allProducts = service.getRelatedProductsForScroll(limit = 3, excludeProductId = currentProductId)
                val availableProducts = allProducts.filter { it.isAvailable == true }
                availableProducts.filter { !loadedProductIds.contains(it.id) }.take(3)
            }
            
            // Agregar los nuevos productos al set
            newProducts.forEach { loadedProductIds.add(it.id) }
            
            Log.d("DetailsRepository", "✅ Cargados ${newProducts.size} productos adicionales")
            newProducts
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

    // Obtener el estado de si hay más productos disponibles
    fun hasMoreProducts(): Boolean {
        return hasMoreProducts
    }
} 