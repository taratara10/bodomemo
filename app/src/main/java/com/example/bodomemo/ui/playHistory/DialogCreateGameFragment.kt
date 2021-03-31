package com.example.bodomemo.ui.playHistory

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.bodomemo.R
import kotlinx.android.synthetic.main.dialog_add_new_game.*

class DialogCreateGameFragment:DialogFragment() {
    internal lateinit var listener: DialogListener
    var gameTitle:String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

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
        //set EditText
        et_dialog_game_title.setText(gameTitle)
    }

    interface DialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }
}