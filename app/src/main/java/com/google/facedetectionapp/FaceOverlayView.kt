package com.google.facedetectionapp

import android.content.Context
import android.graphics.*
import android.graphics.Color.BLUE
import android.icu.number.Scale
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import androidx.core.util.valueIterator
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector

public class FaceOverlayView (context: Context , attrs: AttributeSet? = null , defStyleAttr:Int) : View(context , attrs , defStyleAttr){
    constructor(context: Context , attrs: AttributeSet):this(context , attrs ,  0 )

    private lateinit var mBitmap: Bitmap
    private var mFaces: SparseArray<Face> = SparseArray<Face>()

    fun setBitmap(bitmap:Bitmap){
        mBitmap = bitmap

        val faceDetector : FaceDetector = FaceDetector.Builder(context)
            .setTrackingEnabled(true)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .setMode(FaceDetector.ACCURATE_MODE)
            .build()
        if(!faceDetector.isOperational()){
            //TODO: Colocar verificação , por causa da playstore
        }
        else{
            val frame : Frame = Frame.Builder()
                .setBitmap(mBitmap)
                .build()

            mFaces = faceDetector.detect(frame)
            faceDetector.release()
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(mBitmap != null && mFaces != null){
            var scale : Int? = canvas?.let{ drawBitmap(it) }
            if (canvas != null && scale != null){
                drawFaceBox(canvas ,scale)
            }
        }
    }

    fun drawBitmap(canvas: Canvas): Int {
        val viewWidth: Int = canvas?.width ?: 0
        val viewHeigth: Int = canvas?.height ?: 0
        val imageWidth : Int = mBitmap.width
        val imageHeigth : Int = mBitmap.height
        val scale : Int = Math.min(viewWidth/imageWidth , viewHeigth/imageHeigth)
        val bounds : Rect = Rect( 0, 0 , imageWidth * scale , imageHeigth*scale)
        canvas.drawBitmap(mBitmap,null,bounds,null)
        return scale
    }

    fun drawFaceBox(canvas: Canvas , scale: Int){
        var paint : Paint = Paint()
        paint.setColor(Color.BLUE)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5F

        var left : Float= 0F
        var top : Float= 0F
        var rigth : Float= 0F
        var bottom : Float= 0F

        for (face in mFaces.valueIterator()){
            left = face.position.x * scale
            top = face.position.y * scale
            rigth = scale * (face.position.x * face.width)
            bottom = scale * (face.position.y * face.height)

            canvas.drawRect(left , top , rigth , bottom , paint)
        }
    }
}

