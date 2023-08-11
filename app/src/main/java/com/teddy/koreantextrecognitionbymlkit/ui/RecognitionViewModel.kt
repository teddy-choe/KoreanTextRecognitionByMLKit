package com.teddy.koreantextrecognitionbymlkit.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.FileNotFoundException
import java.io.IOException

class RecognitionViewModel : ViewModel() {
    private val _captureImage: MutableStateFlow<ImageProxy?> = MutableStateFlow(null)
    val captureImage: StateFlow<ImageProxy?> = _captureImage.asStateFlow()

    fun getImage(context: Context, uri: String): Bitmap? {
        try {
            context.contentResolver.openInputStream(Uri.parse(uri)).use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}