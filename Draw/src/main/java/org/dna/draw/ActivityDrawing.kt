package org.dna.draw

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import org.dna.draw.dialog.DialogBrushSize
import org.dna.draw.dialog.OnSelectBrushSizeListener

class ActivityDrawing : AppCompatActivity(), OnSelectBrushSizeListener {

    private var drawingView: DrawingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setBrushSize(10.toFloat())

        val btnSelectBrushSize: ImageButton = findViewById(R.id.btnSelectBrushSize)

        btnSelectBrushSize.setOnClickListener {
            showBrushSizeChooserDialog()
        }
    }

    private fun showBrushSizeChooserDialog(){
//        var loDialog = Dialog(this)
//        loDialog.setContentView(R.layout.dialog_brush_sizes)
//        loDialog.setTitle("Brush size: ")
//        val btnSmall = loDialog.findViewById<ImageButton>(R.id.btnSmallBrush)
//        val btnMedium = loDialog.findViewById<ImageButton>(R.id.btnMediumBrush)
//        val btnLarge = loDialog.findViewById<ImageButton>(R.id.btnLargeBrush)
//
//        btnSmall.setOnClickListener {
//            drawingView?.setBrushSize(10.toFloat())
//            loDialog.dismiss()
//        }
//
//        btnMedium.setOnClickListener {
//            drawingView?.setBrushSize(20.toFloat())
//            loDialog.dismiss()
//        }
//
//        btnLarge.setOnClickListener {
//            drawingView?.setBrushSize(30.toFloat())
//            loDialog.dismiss()
//        }
//        loDialog.show()
        var loDialog = DialogBrushSize(this, this)
        loDialog.showDialog()
    }

    override fun OnSelectBrushSize(nBrushSize: Float) {
        drawingView?.setBrushSize(nBrushSize)
    }
}