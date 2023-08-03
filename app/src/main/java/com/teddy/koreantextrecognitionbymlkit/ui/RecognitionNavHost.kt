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
            HomeRoute(navigatePreview = {
                navController.navigate("preview")
            })
        }

        composable("preview") {
            PreviewRoute(
                navigateToResult = { navController.navigate("result") }
            )
        }

        composable("result") {
            ResultRoute(viewModel = viewModel)
        }

    }
}