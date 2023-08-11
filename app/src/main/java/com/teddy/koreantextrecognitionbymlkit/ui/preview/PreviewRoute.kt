package com.teddy.koreantextrecognitionbymlkit.ui.preview

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.teddy.koreantextrecognitionbymlkit.ui.rememberCameraState

@Composable
fun PreviewRoute(
    navigateToResult: () -> Unit
) {
    PreviewScreen(
        navigateToResult = navigateToResult,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PreviewScreen(
    navigateToResult: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )

        if (cameraPermissionState.status.isGranted) {
            val cameraState = rememberCameraState(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onSuccess = { navigateToResult() },
                onFailure = {
                    Toast.makeText(
                        context,
                        "an error happens while saving the image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            
            Box {
                AndroidView(factory = { ctx ->
                    cameraState.previewView.also {
                        cameraState.initializeCameraController()
                    }
                }, modifier = Modifier.fillMaxSize())
                
            }
            
        } else {
            Column {
                val textToShow =
                    if (cameraPermissionState.status.shouldShowRationale) {
                        "The camera is important for this app. Please grant the permission."
                    } else {
                        "Camera permission required for this feature to be available. " +
                                "Please grant the permission"
                    }
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}