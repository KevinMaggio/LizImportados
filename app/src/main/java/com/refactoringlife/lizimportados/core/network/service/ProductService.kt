package com.refactoringlife.lizimportados.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await

class ProductService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance
) {
    suspend fun getHomeConfig(): ConfigResponse {
        return try {
            // Obtenemos el documento principal de config/home
            val configDoc = firestore.collection("config")
                .document("home")
                .get()
                .await()

            // Obtenemos la colección de options
            val optionsSnapshot = firestore.collection("config")
                .document("home")
                .collection("options")
                .get()
                .await()

            // Convertimos los documentos de options a nuestra clase Option
            val options = optionsSnapshot.documents.mapNotNull { doc ->
                doc.data?.let {
                    ConfigResponse.Option(
                        name = it["name"] as? String ?: "",
                        image = it["image"] as? String ?: ""
                    )
                }
            }.sortedBy { it.name } // Ordenamos por nombre para mantener consistencia

            // Obtenemos los datos base del config
            val isOffers = configDoc.getBoolean("is_offers") ?: false
            val combos = configDoc.get("combo") as? List<Map<String, Any>>

            // Mapeamos los combos si existen
            val mappedCombos = combos?.mapNotNull { comboMap ->
                try {
                    ConfigResponse.ComboModel(
                        oldPrice = (comboMap["old_price"] as? Number)?.toInt() ?: 0,
                        price = (comboMap["price"] as? Number)?.toInt() ?: 0,
                        firstProduct = mapComboProduct(comboMap["first_product"] as? Map<String, Any>),
                        secondProduct = mapComboProduct(comboMap["second_product"] as? Map<String, Any>)
                    )
                } catch (e: Exception) {
                    null
                }
            }

            // Construimos el ConfigResponse final
            ConfigResponse(
                isOffers = isOffers,
                circleOptions = options,
                combos = mappedCombos
            )
        } catch (e: Exception) {
            throw ProductException("Error al obtener configuración de home", e)
        }
    }

    private fun mapComboProduct(data: Map<String, Any>?): ConfigResponse.ComboProductModel {
        return ConfigResponse.ComboProductModel(
            id = data?.get("id") as? String,
            brand = data?.get("brand") as? String,
            description = (data?.get("description") as? String) ?: "",
            image = (data?.get("image") as? String) ?: ""
        )
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

    suspend fun getOffersProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("is_offer", true)
                .whereEqualTo("is_available", true)
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
                .whereEqualTo("is_available", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de hombre", e)
        }
    }

    suspend fun getChildrenProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("is_available", true)
                .whereIn("gender", listOf("Niño", "Bebe"))
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de niños", e)
        }
    }

    suspend fun getWomanProducts(): List<ProductResponse> {
        return try {
            firestore.collection("products")
                .whereEqualTo("gender", "Mujer")
                .whereEqualTo("is_available", true)
                .get()
                .await()
                .toObjects(ProductResponse::class.java)
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de mujer", e)
        }
    }
}

class ProductException(message: String, cause: Throwable? = null) : Exception(message, cause)