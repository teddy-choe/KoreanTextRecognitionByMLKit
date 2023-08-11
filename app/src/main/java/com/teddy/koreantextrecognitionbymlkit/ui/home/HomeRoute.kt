package com.teddy.koreantextrecognitionbymlkit.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeRoute(
    navigateToPreview: () -> Unit,
    navigateToResult: (uri: String) -> Unit
) {
    HomeScreen(
        navigateToPreview = navigateToPreview,
        navigateToResult = navigateToResult,
    )
}

@Composable
fun HomeScreen(
    navigateToPreview: () -> Unit,
    navigateToResult: (uri: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                val encodedUrl = URLEncoder.encode(it.toString(), StandardCharsets.UTF_8.toString())
                navigateToResult(encodedUrl)
            }

        Text(
            text = "Select Feature",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = { navigateToPreview() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Live Recognition")
        }

        Button(
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Recognition From Saved Image")
        }
    }
}