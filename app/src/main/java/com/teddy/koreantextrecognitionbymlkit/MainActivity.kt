package com.teddy.koreantextrecognitionbymlkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.teddy.koreantextrecognitionbymlkit.ui.theme.KoreanTextRecognitionByMLKitTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoreanTextRecognitionByMLKitTheme {
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
                        val cameraController = remember { LifecycleCameraController(baseContext) }
                        val imageAnalyzer = remember { YourImageAnalyzer() }

                        LaunchedEffect(Unit) {
                            cameraController.setImageAnalysisAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                imageAnalyzer
                            )

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
                                            ContextCompat.getMainExecutor(context),
                                            object : ImageCapture.OnImageCapturedCallback() {
                                                override fun onCaptureSuccess(image: ImageProxy) {
                                                    super.onCaptureSuccess(image)
                                                    imageAnalyzer.analyze(image)
                                                }

                                                override fun onError(exception: ImageCaptureException) {
                                                    super.onError(exception)
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
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private class YourImageAnalyzer : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val recognizer =
                TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    println(visionText.text)
                }
                .addOnFailureListener { e ->
                }
        }
    }
}