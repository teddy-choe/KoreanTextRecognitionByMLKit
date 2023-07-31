package com.teddy.koreantextrecognitionbymlkit.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class RecognitionViewModel : ViewModel() {
    private val _captureImage: MutableStateFlow<ImageProxy?> = MutableStateFlow(null)
    val captureImage: StateFlow<ImageProxy?> = _captureImage.asStateFlow()

    fun setImage(imageProxy: ImageProxy) {
        _captureImage.update { imageProxy }
    }

    fun saveImage(context: Context, fileName: String, bitmap: Bitmap) {
        val values = ContentValues().apply {
            this.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            this.put(MediaStore.Images.Media.MIME_TYPE, "image/png") //TYPE
            this.put(MediaStore.Images.Media.DATE_TAKEN, "") //DATA_TAKEN -> 날짜 정보 어떻게 설정할지
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this.put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?.let { uri ->
                println("hi: ${(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/" + fileName).toUri()}")
                context.contentResolver.openFileDescriptor(
                    (MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/" + fileName).toUri(),
                    "w",
                    null
                ).use { parcelFileDescriptor ->
                    parcelFileDescriptor?.run {
                        // write something to OutputStream
                        FileOutputStream(this.fileDescriptor).use { outputStream ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream)
                            outputStream.flush()
                            outputStream.close()
                        }
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                }
            }
    }

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

            while (moveToNext()) {
                val str = getString(index)
                val contentUrl = externalUri.toString() + "/" + str
                try {
                    val `is`: InputStream? =
                        context.contentResolver.openInputStream(Uri.parse(contentUrl))
                    if (`is` != null) {
                        val bitmap = BitmapFactory.decodeStream(`is`)
                        `is`.close()
                        return bitmap
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return null
    }
}