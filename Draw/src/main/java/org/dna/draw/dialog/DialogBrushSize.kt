package org.dna.draw.dialog

import android.app.Dialog
import android.content.Context
import android.widget.ImageButton
import org.dna.draw.R

class DialogBrushSize(foContext: Context, fnListener: OnSelectBrushSizeListener) {

    private val poContext: Context = foContext
    private val mListener: OnSelectBrushSizeListener = fnListener

    var poDialog: Dialog? = null

    init {
        poDialog = Dialog(poContext)
        poDialog?.setContentView(R.layout.dialog_brush_sizes)
        poDialog?.setTitle("Brush size: ")
        val btnSmall = poDialog?.findViewById<ImageButton>(R.id.btnSmallBrush)
        val btnMedium = poDialog?.findViewById<ImageButton>(R.id.btnMediumBrush)
        val btnLarge = poDialog?.findViewById<ImageButton>(R.id.btnLargeBrush)

        btnSmall!!.setOnClickListener {
            mListener.OnSelectBrushSize(10.toFloat())
            poDialog?.dismiss()
        }

        btnMedium!!.setOnClickListener {
            mListener.OnSelectBrushSize(20.toFloat())
            poDialog?.dismiss()
        }

        btnLarge!!.setOnClickListener {
            mListener.OnSelectBrushSize(30.toFloat())
            poDialog?.dismiss()
        }
    }

    fun showDialog(){
        poDialog?.show()
    }
}