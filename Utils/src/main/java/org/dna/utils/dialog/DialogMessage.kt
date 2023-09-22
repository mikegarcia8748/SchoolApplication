package org.dna.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.dna.utils.R

class DialogMessage (context: Context, title: String, message: String){

    private val poContext = context

    private var btnPositive: MaterialButton? = null
    private var btnNegative: MaterialButton? = null

    private var poDialog: Dialog? = null
    init {
        poDialog = Dialog(poContext)
        poDialog?.setContentView(R.layout.dialog_message)

        val lblTitle = poDialog?.findViewById<MaterialTextView>(R.id.lblDialogTitle)
        val lblMessage = poDialog?.findViewById<MaterialTextView>(R.id.lblDialogMessage)
        btnPositive = poDialog?.findViewById(R.id.btnPositive)
        btnNegative = poDialog?.findViewById(R.id.btnNegative)

        lblTitle?.text = title
        lblMessage?.text = message
    }

    fun setPositiveButtonInterface(text: String, listener: OnDialogInterfaceClickCallback){
        btnPositive?.visibility = View.VISIBLE
        btnPositive?.text = text
        btnPositive?.setOnClickListener {
            listener.onClick(poDialog!!)
        }
    }

    fun setNegativeButtonInterface(text: String, listener: OnDialogInterfaceClickCallback){
        btnNegative?.visibility = View.VISIBLE
        btnNegative?.text = text
        btnNegative?.setOnClickListener {
            listener.onClick(poDialog!!)
        }
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

    interface OnDialogInterfaceClickCallback {
        fun onClick(dialog: Dialog)
    }
}