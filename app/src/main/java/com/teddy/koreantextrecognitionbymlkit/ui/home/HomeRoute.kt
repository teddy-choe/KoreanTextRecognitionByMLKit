package com.teddy.koreantextrecognitionbymlkit.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    navigatePreview: () -> Unit
) {
    HomeScreen(navigatePreview = navigatePreview)
}

@Composable
fun HomeScreen(
    navigatePreview: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "home screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Button(
            onClick = { navigatePreview() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "navigate to preview screen")
        }
    }
}