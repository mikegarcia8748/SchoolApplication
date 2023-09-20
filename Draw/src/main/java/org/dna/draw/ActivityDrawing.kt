package org.dna.draw

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import org.dna.draw.dialog.DialogBrushSize
import org.dna.draw.dialog.OnSelectBrushSizeListener
import org.dna.utils.dialog.DialogMessage
import org.dna.utils.dialog.OnDialogInterfaceClickListener

class ActivityDrawing : AppCompatActivity(), OnSelectBrushSizeListener, OnDialogInterfaceClickListener {

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

    fun paintClick(view: View){
        if(view !== poImageBtnCurrentPaint){
            val btnPaint = view as ImageButton
            val loColorTag = btnPaint.tag.toString()
            drawingView?.setColor(loColorTag)

            btnPaint.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallete_selected)
            )

            poImageBtnCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallete_normal)
            )

            poImageBtnCurrentPaint = view
        }
    }

    override fun onBackPressed() {
        val loMessage: DialogMessage = DialogMessage(this, "Sample Title", "Sample message...")
        loMessage.setPositiveButtonInterface("Okay", )
    }

    override fun onClickPositiveButton(dialog: Dialog): Boolean {
        TODO("Not yet implemented")
    }

    override fun onClickNegativeButton(dialog: Dialog): Boolean {
        TODO("Not yet implemented")
    }
}