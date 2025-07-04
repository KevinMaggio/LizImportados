package com.refactoringlife.lizimportados.core.dto.response

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductResponseTest(){

    lateinit var productResponse: ProductResponse

    @Before
    fun setUp(){
        productResponse = ProductResponse(
            brand = ""
        )
    }


    @Test
    fun `test product response model complete`(){
        Assert.assertTrue(productResponse.title is String)
    }

}