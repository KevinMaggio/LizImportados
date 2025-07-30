package com.refactoringlife.lizimportadosv2.core.navigator

import androidx.navigation.NavController

/**
 * Función utilitaria para navegar desde el BottomBar
 * Mantiene HOME como base y reemplaza pantallas en lugar de agregarlas al back stack
 */
fun NavController.navigateFromBottomBar(destination: String) {
    navigate(destination) {
        // Pop up to HOME (inclusive = false) para mantener HOME como base
        popUpTo(AppRoutes.HOME) { inclusive = false }
        // Evita múltiples instancias de la misma pantalla
        launchSingleTop = true
        // Restaura el estado si la pantalla ya existe
        restoreState = true
    }
}

/**
 * Función utilitaria para navegar a detalles desde cualquier pantalla
 * Mantiene la pantalla anterior en el back stack para poder volver
 */
fun NavController.navigateToDetails(category: String, id: String) {
    navigate("details/$category/$id") {
        launchSingleTop = true
        // NO hacemos popUpTo para mantener la pantalla anterior en el back stack
        // Esto permite que el onBack regrese a la pantalla anterior
    }
}

/**
 * Función utilitaria para navegar desde LOGIN a HOME
 * Elimina LOGIN del back stack
 */
fun NavController.navigateFromLoginToHome() {
    navigate(AppRoutes.HOME) {
        popUpTo(AppRoutes.LOGIN) { inclusive = true } // elimina LOGIN del backstack
        launchSingleTop = true
    }
} 