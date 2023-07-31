package com.teddy.koreantextrecognitionbymlkit.ui.result

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.teddy.koreantextrecognitionbymlkit.ui.RecognitionViewModel

@Composable
fun ResultRoute(viewModel: RecognitionViewModel) {
    ResultScreen(viewModel::getImage)
}

@Composable
fun ResultScreen(
    getBitmap: (context: Context) -> Bitmap?
) {
    val recognizer = remember {
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }

    val context = LocalContext.current
    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        bitmap = getBitmap(context)
    }

    Column {
        if (bitmap == null) {

        } else {
            var text by remember {
                mutableStateOf("")
            }
            LaunchedEffect(Unit) {
                recognizer.process(
                    InputImage.fromBitmap(bitmap!!, 0)
                )
                    .addOnSuccessListener { text = it.text }
                    .addOnFailureListener { }
            }

            AsyncImage(model = bitmap, contentDescription = "")
            Text(text = text)
        }
    }
}