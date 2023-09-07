package org.dna.draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityDrawing : AppCompatActivity() {

    private var drawingView: DrawingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setBrushSize(10.toFloat())
    }
}