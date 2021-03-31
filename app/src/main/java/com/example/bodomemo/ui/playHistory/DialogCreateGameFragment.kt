package com.example.bodomemo.ui.playHistory

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.bodomemo.R

class DialogCreateGameFragment:DialogFragment() {
    internal lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_add_new_game, null))
                    // Add action buttons
                    .setPositiveButton("追加",DialogInterface.OnClickListener { dialog, id ->
                                listener.onDialogPositiveClick(this)
                            })
                    .setNegativeButton("キャンセル",DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DialogListener
    }

    interface DialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }
}