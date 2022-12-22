package com.example.minerals.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.DEFAULT
import java.io.ByteArrayOutputStream
import java.util.*


object ImageCoder {
    fun encodeBitmap(bitmap: Bitmap): String {
        // initialize byte stream
        // initialize byte stream
        val stream = ByteArrayOutputStream()
        // compress Bitmap
        // compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        // Initialize byte array
        // Initialize byte array
        val bytes: ByteArray = stream.toByteArray()
        // get base64 encoded string
        // get base64 encoded string
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun decodeBitmap(string: String): Bitmap {
        val bytes: ByteArray = Base64.getDecoder().decode(string)
        // Initialize bitmap
        // Initialize bitmap
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        // set bitmap on imageView
        // set bitmap on imageView
       return bitmap
    }
}