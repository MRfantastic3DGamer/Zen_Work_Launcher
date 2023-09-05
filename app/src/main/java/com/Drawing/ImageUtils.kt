package com.example.launcher.Drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class ImageUtils {
    companion object{
        fun generateBitmap(view: View): Bitmap {
            val bitmap = Bitmap.createBitmap(
                300,
                300,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.layout(
                -150,
                -150,
                150,
                150
            )
            view.draw(canvas)
            return bitmap
        }
    }
}