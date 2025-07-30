package com.refactoringlife.lizimportadosv2.core.navigator

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.refactoringlife.lizimportadosv2.features.cart.presenter.screens.CartScreen
import com.refactoringlife.lizimportadosv2.features.children.presenter.screens.ChildrenScreen
import com.refactoringlife.lizimportadosv2.features.details.presenter.screens.DetailsScreen
import com.refactoringlife.lizimportadosv2.features.home.presenter.screens.HomeScreen
import com.refactoringlife.lizimportadosv2.features.login.presenter.screens.LoginScreen
import com.refactoringlife.lizimportadosv2.features.login.presenter.viewmodel.LoginViewModel
import com.refactoringlife.lizimportadosv2.features.man.presenter.screens.ManScreen
import com.refactoringlife.lizimportadosv2.features.woman.presenter.screens.WomanScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onGoogleSignInClick: (Intent) -> Unit = {},
    loginViewModel: LoginViewModel
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(
            AppRoutes.LOGIN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoginScreen(
                onGoogleClick = {
                    navController.navigateFromLoginToHome()
                },
                onGoogleSignInClick = onGoogleSignInClick,
                viewModel = loginViewModel
            )
        }

        composable(
            AppRoutes.HOME,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HomeScreen(
                modifier = Modifier,
                navController
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
            ManScreen(navController)
        }

        composable(
            route = AppRoutes.DETAILS,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val id = backStackEntry.arguments?.getString("id")
            DetailsScreen(category = category, id = id)
        }

        composable(
            AppRoutes.CHILDREN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ChildrenScreen(navController)
        }

        composable(
            AppRoutes.WOMAN,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            WomanScreen(navController)
        }
    }
}
