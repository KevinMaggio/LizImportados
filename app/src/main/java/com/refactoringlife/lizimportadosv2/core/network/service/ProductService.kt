package com.refactoringlife.lizimportadosv2.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse
import com.refactoringlife.lizimportadosv2.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboProductResponse

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
                .whereEqualTo("available", true) // Solo combos disponibles
                .get()
                .await()

            combosSnapshot.documents.mapNotNull { doc ->
                // Mapear manualmente desde Firestore
                val available = doc.getBoolean("available") ?: false
                val oldPrice = doc.getLong("oldPrice")?.toInt() ?: 0
                val newPrice = doc.getLong("newPrice")?.toInt() ?: 0
                val product1Id = doc.getString("product1Id") ?: ""
                val product2Id = doc.getString("product2Id") ?: ""
                
                // Obtener productos completos
                val product1 = try {
                    getProductById(product1Id)
                } catch (e: Exception) {
                    null
                }
                
                val product2 = try {
                    getProductById(product2Id)
                } catch (e: Exception) {
                    null
                }
                
                // Crear ComboProductResponse para cada producto
                val firstProduct = ComboProductResponse(
                    id = product1?.id ?: product1Id,
                    brand = product1?.brand ?: "",
                    description = product1?.name ?: "",
                    image = product1?.images?.firstOrNull() ?: ""
                )
                
                val secondProduct = ComboProductResponse(
                    id = product2?.id ?: product2Id,
                    brand = product2?.brand ?: "",
                    description = product2?.name ?: "",
                    image = product2?.images?.firstOrNull() ?: ""
                )
                
                ComboResponse(
                    available = available,
                    oldPrice = oldPrice,
                    price = newPrice,
                    firstProduct = firstProduct,
                    secondProduct = secondProduct
                )
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener combos", e)
        }
    }

    suspend fun getProductById(id: String): ProductResponse {
        return try {
            val doc = firestore.collection("products")
                .document(id)
                .get()
                .await()

            if (!doc.exists()) {
                throw ProductException("Producto no encontrado")
            }

            ProductResponse(
                id = doc.getString("id") ?: "",
                name = doc.getString("name"),
                description = doc.getString("description"),
                size = doc.getString("size"),
                brand = doc.getString("brand"),
                categories = doc.get("categories") as? List<String>,
                comboId = doc.get("combo_id") as? List<String>,
                comboPrice = doc.getLong("combo_price")?.toInt(),
                gender = doc.getString("gender"),
                images = doc.get("images") as? List<String>,
                isAvailable = doc.getBoolean("is_available"),
                isOffer = doc.getBoolean("is_offer"),
                offerPrice = doc.getLong("offer_price")?.toInt(),
                price = doc.getLong("price")?.toInt(),
                season = doc.getString("season"),
                circleOptionFilter = doc.getString("circle_option_filter")
            )
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
            snapshot.documents.mapNotNull { doc ->
                try {
                    ProductResponse(
                        id = doc.getString("id") ?: "",
                        name = doc.getString("name"),
                        description = doc.getString("description"),
                        size = doc.getString("size"),
                        brand = doc.getString("brand"),
                        categories = doc.get("categories") as? List<String>,
                        comboId = doc.get("combo_id") as? List<String>,
                        comboPrice = doc.getLong("combo_price")?.toInt(),
                        gender = doc.getString("gender"),
                        images = doc.get("images") as? List<String>,
                        isAvailable = doc.getBoolean("is_available"),
                        isOffer = doc.getBoolean("is_offer"),
                        offerPrice = doc.getLong("offer_price")?.toInt(),
                        price = doc.getLong("price")?.toInt(),
                        season = doc.getString("season"),
                        circleOptionFilter = doc.getString("circle_option_filter")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
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
            snapshot.documents.mapNotNull { doc ->
                try {
                    ProductResponse(
                        id = doc.getString("id") ?: "",
                        name = doc.getString("name"),
                        description = doc.getString("description"),
                        size = doc.getString("size"),
                        brand = doc.getString("brand"),
                        categories = doc.get("categories") as? List<String>,
                        comboId = doc.get("combo_id") as? List<String>,
                        comboPrice = doc.getLong("combo_price")?.toInt(),
                        gender = doc.getString("gender"),
                        images = doc.get("images") as? List<String>,
                        isAvailable = doc.getBoolean("is_available"),
                        isOffer = doc.getBoolean("is_offer"),
                        offerPrice = doc.getLong("offer_price")?.toInt(),
                        price = doc.getLong("price")?.toInt(),
                        season = doc.getString("season"),
                        circleOptionFilter = doc.getString("circle_option_filter")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getOffersProducts(): List<ProductResponse> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("is_offer", true)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                ProductResponse(
                    id = doc.getString("id") ?: "",
                    name = doc.getString("name"),
                    description = doc.getString("description"),
                    size = doc.getString("size"),
                    brand = doc.getString("brand"),
                    categories = doc.get("categories") as? List<String>,
                    comboId = doc.get("combo_id") as? List<String>,
                    comboPrice = doc.getLong("combo_price")?.toInt(),
                    gender = doc.getString("gender"),
                    images = doc.get("images") as? List<String>,
                    isAvailable = doc.getBoolean("is_available"),
                    isOffer = doc.getBoolean("is_offer"),
                    offerPrice = doc.getLong("offer_price")?.toInt(),
                    price = doc.getLong("price")?.toInt(),
                    season = doc.getString("season"),
                    circleOptionFilter = doc.getString("circle_option_filter")
                )
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos en oferta", e)
        }
    }

    suspend fun getMenProducts(): List<ProductResponse> {
        return try {
            val snapshot = firestore.collection("products")
                .whereArrayContains("gender", "Hombre")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    ProductResponse(
                        id = doc.getString("id") ?: "",
                        name = doc.getString("name"),
                        description = doc.getString("description"),
                        size = doc.getString("size"),
                        brand = doc.getString("brand"),
                        categories = doc.get("categories") as? List<String>,
                        comboId = doc.get("combo_id") as? List<String>,
                        comboPrice = doc.getLong("combo_price")?.toInt(),
                        gender = doc.getString("gender"),
                        images = doc.get("images") as? List<String>,
                        isAvailable = doc.getBoolean("is_available"),
                        isOffer = doc.getBoolean("is_offer"),
                        offerPrice = doc.getLong("offer_price")?.toInt(),
                        price = doc.getLong("price")?.toInt(),
                        season = doc.getString("season"),
                        circleOptionFilter = doc.getString("circle_option_filter")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de hombre", e)
        }
    }

    suspend fun getWomanProducts(): List<ProductResponse> {
        return try {
            val snapshot = firestore.collection("products")
                .whereArrayContains("gender", "Mujer")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    ProductResponse(
                        id = doc.getString("id") ?: "",
                        name = doc.getString("name"),
                        description = doc.getString("description"),
                        size = doc.getString("size"),
                        brand = doc.getString("brand"),
                        categories = doc.get("categories") as? List<String>,
                        comboId = doc.get("combo_id") as? List<String>,
                        comboPrice = doc.getLong("combo_price")?.toInt(),
                        gender = doc.getString("gender"),
                        images = doc.get("images") as? List<String>,
                        isAvailable = doc.getBoolean("is_available"),
                        isOffer = doc.getBoolean("is_offer"),
                        offerPrice = doc.getLong("offer_price")?.toInt(),
                        price = doc.getLong("price")?.toInt(),
                        season = doc.getString("season"),
                        circleOptionFilter = doc.getString("circle_option_filter")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de mujer", e)
        }
    }

    suspend fun getChildrenProducts(): List<ProductResponse> {
        return try {
            val snapshot = firestore.collection("products")
                .whereArrayContains("gender", "Niño")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    ProductResponse(
                        id = doc.getString("id") ?: "",
                        name = doc.getString("name"),
                        description = doc.getString("description"),
                        size = doc.getString("size"),
                        brand = doc.getString("brand"),
                        categories = doc.get("categories") as? List<String>,
                        comboId = doc.get("combo_id") as? List<String>,
                        comboPrice = doc.getLong("combo_price")?.toInt(),
                        gender = doc.getString("gender"),
                        images = doc.get("images") as? List<String>,
                        isAvailable = doc.getBoolean("is_available"),
                        isOffer = doc.getBoolean("is_offer"),
                        offerPrice = doc.getLong("offer_price")?.toInt(),
                        price = doc.getLong("price")?.toInt(),
                        season = doc.getString("season"),
                        circleOptionFilter = doc.getString("circle_option_filter")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener productos de niños", e)
        }
    }

    suspend fun getProductsByCategory(category: String, limit: Int = 10): List<ProductResponse> {
        val snapshot = firestore.collection("products")
            .whereArrayContains("categories", category)
            .limit(limit.toLong())
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            try {
                ProductResponse(
                    id = doc.getString("id") ?: "",
                    name = doc.getString("name"),
                    description = doc.getString("description"),
                    size = doc.getString("size"),
                    brand = doc.getString("brand"),
                    categories = doc.get("categories") as? List<String>,
                    comboId = doc.get("combo_id") as? List<String>,
                    comboPrice = doc.getLong("combo_price")?.toInt(),
                    gender = doc.getString("gender"),
                    images = doc.get("images") as? List<String>,
                    isAvailable = doc.getBoolean("is_available"),
                    isOffer = doc.getBoolean("is_offer"),
                    offerPrice = doc.getLong("offer_price")?.toInt(),
                    price = doc.getLong("price")?.toInt(),
                    season = doc.getString("season"),
                    circleOptionFilter = doc.getString("circle_option_filter")
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getMoreProductsByCategory(
        category: String, 
        limit: Int = 10, 
        lastDocument: DocumentSnapshot?
    ): List<ProductResponse> {
        return try {
            val query = firestore.collection("products")
                .whereArrayContains("categories", category)
                .limit(limit.toLong())
            
            val finalQuery = if (lastDocument != null) {
                query.startAfter(lastDocument)
            } else {
                query
            }
            
            val snapshot = finalQuery.get().await()
            snapshot.documents.mapNotNull { doc ->
                ProductResponse(
                    id = doc.getString("id") ?: "",
                    name = doc.getString("name"),
                    description = doc.getString("description"),
                    size = doc.getString("size"),
                    brand = doc.getString("brand"),
                    categories = doc.get("categories") as? List<String>,
                    comboId = doc.get("combo_id") as? List<String>,
                    comboPrice = doc.getLong("combo_price")?.toInt(),
                    gender = doc.getString("gender"),
                    images = doc.get("images") as? List<String>,
                    isAvailable = doc.getBoolean("is_available"),
                    isOffer = doc.getBoolean("is_offer"),
                    offerPrice = doc.getLong("offer_price")?.toInt(),
                    price = doc.getLong("price")?.toInt(),
                    season = doc.getString("season"),
                    circleOptionFilter = doc.getString("circle_option_filter")
                )
            }
        } catch (e: Exception) {
            throw ProductException("Error al obtener más productos por categoría: $category", e)
        }
    }
}

class ProductException(message: String, cause: Throwable? = null) : Exception(message, cause)