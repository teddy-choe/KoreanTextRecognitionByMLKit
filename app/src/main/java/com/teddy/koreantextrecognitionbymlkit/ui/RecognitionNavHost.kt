package com.teddy.koreantextrecognitionbymlkit.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teddy.koreantextrecognitionbymlkit.ui.home.HomeRoute
import com.teddy.koreantextrecognitionbymlkit.ui.preview.PreviewRoute
import com.teddy.koreantextrecognitionbymlkit.ui.result.ResultRoute

@Composable
fun RecognitionNavHost(
    viewModel: RecognitionViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(
                navigateToPreview = {
                    navController.navigate("preview")
                },
                navigateToResult = { uri ->
                    navController.navigate("result/$uri")
                }
            )
        }

        composable("preview") {
            PreviewRoute(
                navigateToResult = { navController.navigate("result") }
            )
        }

        composable("result/{uri}") { backStackEntry ->
            ResultRoute(
                viewModel = viewModel,
                uri = backStackEntry.arguments?.getString("uri") ?: ""
            )
        }
    }
}