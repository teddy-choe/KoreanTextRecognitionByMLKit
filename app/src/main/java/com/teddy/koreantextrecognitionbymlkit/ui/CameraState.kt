package com.teddy.koreantextrecognitionbymlkit.ui

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

@Composable
fun rememberCameraState(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
): CameraState {
    return remember(context, onSuccess, onFailure) {
        CameraState(context, lifecycleOwner, onSuccess, onFailure)
    }
}

@Stable
class CameraState(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val onSuccess: () -> Unit,
    val onFailure: () -> Unit
) {
    val previewView: PreviewView = PreviewView(context)
    val cameraController = LifecycleCameraController(context)

    fun initializeCameraController() {
        cameraController.imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        previewView.controller = cameraController
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val recognizer =
            TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(recognizer),
                CameraController.IMAGE_CAPTURE,
                ContextCompat.getMainExecutor(context)
            ) { result ->
                println("111: ${result.getValue(recognizer)?.text}")
            }
        )
    }
}