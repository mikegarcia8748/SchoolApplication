package org.dna.utils.dialog

import android.app.Dialog

interface OnDialogInterfaceClickListener {

    fun onClickPositiveButton(dialog:Dialog) : Boolean
    fun onClickNegativeButton(dialog: Dialog) : Boolean
}