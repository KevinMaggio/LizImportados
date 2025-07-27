package com.refactoringlife.lizimportadosv2.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse
import com.refactoringlife.lizimportadosv2.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await
import android.util.Log

class ProductService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance
) {
    suspend fun getHomeConfig(): ConfigResponse {
        return try {
            // Obtenemos el documento principal de config/general
            val configDoc = firestore.collection("config")
                .document("general")
                .get()
                .await()

            // Obtenemos las options como array del documento
            val optionsArray = configDoc.get("options") as? List<Map<String, Any>> ?: emptyList()
            
            // Convertimos los elementos del array a nuestra clase Option
            val options = optionsArray.mapNotNull { optionMap ->
                ConfigResponse.Option(
                    name = optionMap["name"] as? String ?: "",
                    image = optionMap["image"] as? String ?: ""
                )
            }.sortedBy { it.name } // Ordenamos por nombre para mantener consistencia

            // Obtenemos los datos base del config
            val isOffers = configDoc.getBoolean("is_offers") ?: false
            val hasCombo = configDoc.getBoolean("has_combos") ?: false

            // Construimos el ConfigResponse final
            ConfigResponse(
                isOffers = isOffers,
                circleOptions = options,
                hasCombo = hasCombo
            )
        } catch (e: Exception) {
            throw ProductException("Error al obtener configuración de home", e)
        }
    }

    suspend fun getCombos(): List<ComboResponse> {
        return try {
            val combosSnapshot = firestore.collection("combos")
                .get()
                .await()

            combosSnapshot.documents.mapNotNull { doc ->
                doc.toObject(ComboResponse::class.java)
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener combos", e)
        }
    }

    suspend fun getProductById(id: String): ProductResponse {
        return try {
            firestore.collection("products")
                .document(id)
                .get()
                .await()
                .toObject(ProductResponse::class.java)
                ?: throw ProductException("Producto no encontrado")
        } catch (e: Exception) {
            throw ProductException("Error al obtener producto por ID", e)
        }
    }

    // Obtener productos relacionados (paginación inicial)
    suspend fun getRelatedProducts(limit: Int = 10, excludeProductId: String? = null): List<ProductResponse> {
        return try {
            val query = firestore.collection("products")
                .limit(limit.toLong())
            
            // Si tenemos un ID para excluir, lo filtramos en Firestore
            val finalQuery = if (excludeProductId != null) {
                query.whereNotEqualTo("id", excludeProductId)
            } else {
                query
            }
            
            val snapshot = finalQuery.get().await()
            val products = snapshot.toObjects(ProductResponse::class.java)
            Log.d("ProductService", "✅ Productos relacionados cargados: ${products.size}")
            products
        } catch (e: Exception) {
            Log.e("ProductService", "❌ Error obteniendo productos relacionados", e)
            emptyList()
        }
    }

    // Obtener más productos para scroll infinito
    suspend fun getMoreProducts(limit: Int = 10, lastDocument: DocumentSnapshot?, excludeProductId: String? = null): List<ProductResponse> {
        return try {
            val query = firestore.collection("products")
                .limit(limit.toLong())
            
            // Si tenemos un ID para excluir, lo filtramos en Firestore
            val filteredQuery = if (excludeProductId != null) {
                query.whereNotEqualTo("id", excludeProductId)
            } else {
                query
            }
            
            // Aplicamos paginación si tenemos documento anterior
            val finalQuery = if (lastDocument != null) {
                filteredQuery.startAfter(lastDocument)
            } else {
                filteredQuery
            }
            
            val snapshot = finalQuery.get().await()
            val products = snapshot.toObjects(ProductResponse::class.java)
            Log.d("ProductService", "✅ Más productos cargados: ${products.size}")
            products
        } catch (e: Exception) {
            Log.e("ProductService", "❌ Error obteniendo más productos", e)
            emptyList()
        }
    }

    suspend fun getOffersProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("is_offer", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos en oferta", e)
        }
    }

    suspend fun getMenProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Hombre")
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de hombre", e)
        }
    }

    suspend fun getWomanProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Mujer")
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de mujer", e)
        }
    }

    suspend fun getChildrenProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Niño")
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de niños", e)
        }
    }
}

class ProductException(message: String, cause: Throwable? = null) : Exception(message, cause)