package com.google.facedetectionapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var mFaceOverlayView: FaceOverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFaceOverlayView = findViewById(R.id.face_view)

        val stream : InputStream = resources.openRawResource(R.raw.face)
        val bitmap : Bitmap = BitmapFactory.decodeStream(stream)

        mFaceOverlayView.setBitmap(bitmap)
    }
}