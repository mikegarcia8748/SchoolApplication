package org.dna.draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import org.dna.draw.dialog.DialogBrushSize
import org.dna.draw.dialog.OnSelectBrushSizeListener

class ActivityDrawing : AppCompatActivity(), OnSelectBrushSizeListener {

    private var drawingView: DrawingView? = null

    private var poImageBtnCurrentPaint : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setBrushSize(10.toFloat())

        val loBrushColors = findViewById<LinearLayout>(R.id.linearBrushColor)

        poImageBtnCurrentPaint = loBrushColors[0] as ImageButton
        poImageBtnCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallete_selected)
        )

        val btnSelectBrushSize: ImageButton = findViewById(R.id.btnSelectBrushSize)

        btnSelectBrushSize.setOnClickListener {
            showBrushSizeChooserDialog()
        }
    }

    private fun showBrushSizeChooserDialog(){
        var loDialog = DialogBrushSize(this, this)
        loDialog.showDialog()
    }

    override fun OnSelectBrushSize(nBrushSize: Float) {
        drawingView?.setBrushSize(nBrushSize)
    }
}