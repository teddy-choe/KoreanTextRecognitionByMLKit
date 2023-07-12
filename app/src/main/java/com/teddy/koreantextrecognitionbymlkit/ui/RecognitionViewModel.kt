package com.teddy.koreantextrecognitionbymlkit.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecognitionViewModel: ViewModel() {
    private val _captureImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val captureImage: StateFlow<Bitmap?> = _captureImage.asStateFlow()

    fun setImage(bitmap: Bitmap) {
        _captureImage.update { bitmap }
    }
}