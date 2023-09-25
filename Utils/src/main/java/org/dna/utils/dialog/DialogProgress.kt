package org.dna.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.android.material.textview.MaterialTextView
import org.dna.utils.R

class DialogProgress(context: Context, message: String) {

    private var poDialog: Dialog? = null
    init {
        poDialog = Dialog(context)
        poDialog?.setContentView(R.layout.dialog_circular_progress)

        val lblMessage = poDialog?.findViewById<MaterialTextView>(R.id.lblDialogMessage)

        lblMessage?.text = message
    }

    fun showDialog(){
        val isShowing: Boolean = poDialog?.isShowing!!
        if(!isShowing){
            val loColor = ColorDrawable(Color.TRANSPARENT)
            poDialog?.window?.setBackgroundDrawable(loColor)
            poDialog?.show()
            return
        }
    }

    fun dismiss(){
        poDialog?.dismiss()
    }
}