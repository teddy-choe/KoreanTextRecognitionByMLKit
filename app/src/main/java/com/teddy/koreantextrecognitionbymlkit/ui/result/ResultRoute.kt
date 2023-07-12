package com.teddy.koreantextrecognitionbymlkit.ui.result

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.teddy.koreantextrecognitionbymlkit.ui.RecognitionViewModel

@Composable
fun ResultRoute(viewModel: RecognitionViewModel) {
    val bitmap = viewModel.captureImage.collectAsState()

    ResultScreen(bitmap.value)
}

@Composable
fun ResultScreen(bitmap: Bitmap?) {
    val recognizer = remember {
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }

    //                                        recognizer.process(image)
//                                            .addOnSuccessListener {  }
//                                            .addOnFailureListener {  }
//                                    }

    Column {
        if (bitmap != null) {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "")
        }
    }
}