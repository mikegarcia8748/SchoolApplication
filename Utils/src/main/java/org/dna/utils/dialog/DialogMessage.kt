package org.dna.utils.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.dna.utils.R

class DialogMessage (context: Context, title: String, message: String, listener: OnDialogInterfaceClickListener){

    private val poContext = context
    private var mListener: OnDialogInterfaceClickListener? = null

    private var poDialog: Dialog? = null
    init {
        poDialog = Dialog(poContext)
        poDialog?.setContentView(R.layout.dialog_message)

        val lblTitle = poDialog?.findViewById<MaterialTextView>(R.id.lblDialogTitle)
        val lblMessage = poDialog?.findViewById<MaterialTextView>(R.id.lblDialogMessage)
        val btnPositive = poDialog?.findViewById<MaterialButton>(R.id.btnPositive)
        val btnNegative = poDialog?.findViewById<MaterialButton>(R.id.btnNegative)

    }

    fun setPositiveButtonInterface(text: String, listener: ){


    }

    fun setNegativeButtonInterface(text: String, listener: OnDialogInterfaceClickListener){
        btnNegative!!.visibility = View.VISIBLE
        btnNegative!!.text = text
        btnNegative.setOnClickListener {
            listener.onClickNegativeButton(poDialog!!)
        }
    }

    fun showDialog(){
        val isShowing: Boolean = poDialog?.isShowing!!
        if(!isShowing){
            poDialog?.show()
            return
        }
    }
}