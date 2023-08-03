package com.teddy.koreantextrecognitionbymlkit.ui

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
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

    fun getImage(context: Context): Bitmap? {
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE
        )

        val cursor: Cursor? =
            context.contentResolver.query(externalUri, projection, null, null, null)

        cursor?.apply {
            val index = getColumnIndex(MediaStore.Images.Media._ID)
            cursor.moveToLast()
            val str = getString(index)
            val contentUrl = externalUri.toString() + "/" + str
            try {
                context.contentResolver.openInputStream(Uri.parse(contentUrl)).use { inputStream ->
                    return BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return null
    }
}