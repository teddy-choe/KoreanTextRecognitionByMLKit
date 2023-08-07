package com.teddy.koreantextrecognitionbymlkit.ui

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.Calendar

@Composable
fun rememberCameraState(
    context: Context,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
): CameraState {
    return remember(context, onSuccess, onFailure) {
        CameraState(context, onSuccess, onFailure)
    }
}

class CameraState(
    val context: Context,
    val onSuccess: () -> Unit,
    val onFailure: () -> Unit
) {
    val previewView: PreviewView = PreviewView(context)
    val cameraController = LifecycleCameraController(context)

    init {
        cameraController.imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        cameraController.bindToLifecycle(context as ComponentActivity)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
    }

    fun captureImage(isSave: Boolean) {
        if (isSave) {
            cameraController.takePicture(
                ImageCapture.OutputFileOptions.Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues().apply {
                        this.put(
                            MediaStore.Images.Media.DISPLAY_NAME,
                            Calendar.getInstance().time.toString()
                        )
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
                        onSuccess()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Timber.e("error: $exception")
                        onFailure()
                    }

                }
            )

        }
    }
}