package com.teddy.koreantextrecognitionbymlkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.teddy.koreantextrecognitionbymlkit.ui.theme.KoreanTextRecognitionByMLKitTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoreanTextRecognitionByMLKitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val context = LocalContext.current

                        val cameraPermissionState = rememberPermissionState(
                            android.Manifest.permission.CAMERA
                        )

                        val previewView: PreviewView = remember { PreviewView(context) }
                        var cameraController = remember { LifecycleCameraController(baseContext) }

                        if (cameraPermissionState.status.isGranted) {
                            Text("Camera permission Granted")

                            AndroidView(factory = { ctx ->
                                cameraController.setImageAnalysisAnalyzer(
                                    ContextCompat.getMainExecutor(context),
                                    YourImageAnalyzer()
                                )

                                cameraController.bindToLifecycle(context as ComponentActivity)
                                cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                previewView.controller = cameraController

                                previewView
                            }, modifier = Modifier.fillMaxSize())
                        } else {
                            Column {
                                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                                    // If the user has denied the permission but the rationale can be shown,
                                    // then gently explain why the app requires this permission
                                    "The camera is important for this app. Please grant the permission."
                                } else {
                                    // If it's the first time the user lands on this feature, or the user
                                    // doesn't want to be asked again for this permission, explain that the
                                    // permission is required
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
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private class YourImageAnalyzer : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            println("123123")
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    // ...
                    println("hihihi ${visionText.text}")
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        }
    }
}