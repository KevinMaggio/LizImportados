package com.refactoringlife.lizimportados.core.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.features.home.presenter.screens.HomeScreen
import com.refactoringlife.lizimportados.features.cart.presenter.screens.CartScreen
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.features.children.presenter.screens.ChildrenScreen
import com.refactoringlife.lizimportados.features.man.presenter.screens.ManScreen
import com.refactoringlife.lizimportados.features.woman.presenter.screens.WomanScreen
import com.refactoringlife.lizimportados.features.details.presenter.screens.DetailsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppRoutes.HOME,
        modifier = modifier
    ) {
        composable(
            AppRoutes.HOME,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HomeScreen(modifier = Modifier,
                goTo = { destination, filter ->
                    navController.navigate("$destination/$filter") {
                        launchSingleTop = true
                        popUpTo(AppRoutes.HOME) { inclusive = false }
                    }
                }
            )
        }
        composable(
            AppRoutes.CART,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            CartScreen()
        }
        composable(
            AppRoutes.MAN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ManScreen()
        }
        composable(
            route = AppRoutes.DETAILS,
            arguments = listOf(
                androidx.navigation.navArgument("filter") { type = androidx.navigation.NavType.StringType }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter")
            DetailsScreen(filter = filter)
        }
        composable(
            AppRoutes.CHILDREN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ChildrenScreen()
        }
        composable(
            AppRoutes.WOMAN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            WomanScreen()
        }
    }
} 