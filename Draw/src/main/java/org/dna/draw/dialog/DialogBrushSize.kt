package org.dna.draw.dialog

import android.app.Dialog
import android.content.Context
import android.widget.ImageButton
import org.dna.draw.R

class DialogBrushSize(foContext: Context, fnListener: OnSelectBrushSizeListener) {

    private val poContext: Context = foContext
    private val mListener: OnSelectBrushSizeListener = fnListener

    var loDialog: Dialog? = null

    init {
        loDialog = Dialog(poContext)
        loDialog?.setContentView(R.layout.dialog_brush_sizes)
        loDialog?.setTitle("Brush size: ")
        val btnSmall = loDialog?.findViewById<ImageButton>(R.id.btnSmallBrush)
        val btnMedium = loDialog?.findViewById<ImageButton>(R.id.btnMediumBrush)
        val btnLarge = loDialog?.findViewById<ImageButton>(R.id.btnLargeBrush)

        btnSmall!!.setOnClickListener {
            mListener.OnSelectBrushSize(10.toFloat())
            loDialog?.dismiss()
        }

        btnMedium!!.setOnClickListener {
            mListener.OnSelectBrushSize(20.toFloat())
            loDialog?.dismiss()
        }

        btnLarge!!.setOnClickListener {
            mListener.OnSelectBrushSize(30.toFloat())
            loDialog?.dismiss()
        }
    }

    fun showDialog(){
        loDialog?.show()
    }
}