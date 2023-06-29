package com.teddy.koreantextrecognitionbymlkit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "home screen")
        Button(onClick = { navigatePreview() }) {
            Text(text = "navigate to preview screen")
        }
    }
}