package com.refactoringlife.lizimportados.core.utils

import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel

fun getProductsMock() = listOf(
    ProductModel(
        url = "https://i.postimg.cc/6q7WXnwC/pngwing-com.png",
        title = "campera jean",
        subtitle = "hermosa campera",
        oldPrice = "$55000",
        price = "$34000"
    ),
    ProductModel(
        url = "https://i.postimg.cc/6q7WXnwC/pngwing-com.png",
        title = "campera jean",
        subtitle = "hermosa campera",
        oldPrice = "$55000",
        price = "$34000"
    ),
    ProductModel(
        url = "https://i.postimg.cc/6q7WXnwC/pngwing-com.png",
        title = "campera jean",
        subtitle = "hermosa campera",
        oldPrice = "$55000",
        price = "$34000"
    ),
    ProductModel(
        url = "https://i.postimg.cc/6q7WXnwC/pngwing-com.png",
        title = "campera jean",
        subtitle = "hermosa campera",
        oldPrice = "$55000",
        price = "$34000"
    ),
    ProductModel(
        url = "https://i.postimg.cc/6q7WXnwC/pngwing-com.png",
        title = "campera jean",
        subtitle = "hermosa campera",
        oldPrice = "$55000",
        price = "$34000"
    )
)

fun getConfigMock() = ConfigResponse(
    ConfigResponse.Combo(
        showCombo = true,
        comboID = listOf("1", "2")
    ),
    circleOptions = listOf("invierno", "verano", "ositos", "primavera"),
    weeklyOffers = true
)
