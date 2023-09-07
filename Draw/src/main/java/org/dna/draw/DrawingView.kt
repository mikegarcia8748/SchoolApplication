package org.dna.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.BitmapCompat

class DrawingView(mContext: Context, mAttr: AttributeSet): View(mContext, mAttr) {

    private var poDrawPath: CustomPath? = null
    private var poCanvasBmp: Bitmap? = null
    private var poPaint: Paint? = null
    private var poCanvasPnt: Paint? = null
    private var pnBrushSize: Float = 0.toFloat()
    private var poColor = Color.BLACK
    private var poCanvas: Canvas? = null
    private var poPaths = ArrayList<CustomPath>()

    init {
        setupDrawing()
    }

    private fun setupDrawing(){
        poPaint = Paint()
        poDrawPath = CustomPath(poColor, pnBrushSize)
        poPaint!!.color = poColor
        poPaint!!.style = Paint.Style.STROKE
        poPaint!!.strokeJoin = Paint.Join.ROUND
        poPaint!!.strokeCap = Paint.Cap.ROUND
        poCanvasPnt = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        poCanvasBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        poCanvas = Canvas(poCanvasBmp!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for(path in poPaths){
            poPaint!!.strokeWidth = path!!.brushThickness
            poPaint!!.color = path!!.color
            canvas.drawPath(path!!, poPaint!!)
        }

        canvas.drawBitmap(poCanvasBmp!!, 0f, 0f, poCanvasPnt)

        if(!poDrawPath!!.isEmpty) {
            poPaint!!.strokeWidth = poDrawPath!!.brushThickness
            poPaint!!.color = poPaint!!.color
            canvas.drawPath(poDrawPath!!, poPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val lnTouchx = event?.x
        val lnTouchy = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                poDrawPath!!.color = poColor

                poDrawPath!!.reset()
                if(lnTouchx != null
                    && lnTouchy != null){
                    poDrawPath!!.moveTo(lnTouchx, lnTouchy)
                }
            }

            MotionEvent.ACTION_MOVE ->{
                if (lnTouchx != null
                    && lnTouchy != null) {
                        poDrawPath!!.lineTo(lnTouchx, lnTouchy)
                }
            }

            MotionEvent.ACTION_UP ->{
                poPaths.add(poDrawPath!!)
                poDrawPath = CustomPath(poColor, pnBrushSize)
            }
            else -> return false
        }

        invalidate()

        return true
    }

    fun setBrushSize(size: Float){
        pnBrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            size,
            resources.displayMetrics)

        poPaint!!.strokeWidth = pnBrushSize
    }

    internal inner class CustomPath(
        var color: Int,
        var brushThickness: Float): Path(){
    }
}