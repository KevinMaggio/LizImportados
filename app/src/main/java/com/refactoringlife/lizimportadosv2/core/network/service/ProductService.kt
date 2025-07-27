package com.refactoringlife.lizimportadosv2.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse
import com.refactoringlife.lizimportadosv2.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await

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
    suspend fun getRelatedProducts(limit: Int = 10): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos relacionados", e)
        }
    }

    // Obtener más productos para scroll infinito
    suspend fun getMoreProducts(limit: Int = 10, lastDocument: DocumentSnapshot?): List<ProductResponse> {
        return try {
            val query = firestore.collection("products")
            
            val snapshot = if (lastDocument != null) {
                query.startAfter(lastDocument).limit(limit.toLong()).get().await()
            } else {
                query.limit(limit.toLong()).get().await()
            }
            
            snapshot.toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener más productos", e)
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