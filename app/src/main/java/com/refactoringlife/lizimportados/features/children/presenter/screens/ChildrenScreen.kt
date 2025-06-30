package com.refactoringlife.lizimportados.features.children.presenter.screens

import androidx.compose.runtime.Composable
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.children.presenter.views.ChildrenDataView

@Composable
fun ChildrenScreen (){
    ChildrenDataView(getProductsMock())
}