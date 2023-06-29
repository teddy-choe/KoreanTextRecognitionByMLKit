package com.teddy.koreantextrecognitionbymlkit.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RecognitionNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(navigatePreview = { navController.navigate("preview") })
        }

        composable("preview") {
            PreviewRoute()
        }

    }
}