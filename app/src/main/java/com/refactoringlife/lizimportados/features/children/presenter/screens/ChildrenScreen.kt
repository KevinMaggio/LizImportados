package com.refactoringlife.lizimportados.features.children.presenter.screens

import androidx.compose.runtime.Composable
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.children.presenter.views.ChildrenDataView
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel

@Composable
fun ChildrenScreen (){
    ChildrenDataView(getProductsMock())
}