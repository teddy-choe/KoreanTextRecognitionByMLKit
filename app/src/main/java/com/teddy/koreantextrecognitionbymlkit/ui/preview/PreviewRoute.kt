package com.teddy.koreantextrecognitionbymlkit.ui.preview

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.teddy.koreantextrecognitionbymlkit.ui.RecognitionViewModel
import java.io.File

@Composable
fun PreviewRoute(
    viewModel: RecognitionViewModel,
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
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )

        val previewView: PreviewView = remember { PreviewView(context) }
        val cameraController = remember { LifecycleCameraController(context) }

        LaunchedEffect(Unit) {
            cameraController.imageAnalysisBackpressureStrategy =
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            cameraController.bindToLifecycle(context as ComponentActivity)
            cameraController.cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA
            previewView.controller = cameraController
        }

        if (cameraPermissionState.status.isGranted) {
            Box {
                AndroidView(factory = { ctx ->
                    previewView
                }, modifier = Modifier.fillMaxSize())

                Button(
                    onClick = {
                        cameraController.takePicture(
                            ImageCapture.OutputFileOptions.Builder(
                                context.contentResolver,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                ContentValues().apply {
                                    this.put(MediaStore.Images.Media.DISPLAY_NAME, "123")
                                    this.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                    this.put(
                                        MediaStore.Images.Media.DATE_TAKEN,
                                        ""
                                    )
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        this.put(MediaStore.Images.Media.IS_PENDING, 1)
                                    }
                                }).build(),
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    navigateToResult()
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    TODO("Not yet implemented")
                                }

                            }
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(text = "Click!")
                }
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