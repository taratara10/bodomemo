package com.example.bodomemo.ui.playHistory

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.bodomemo.R
import kotlinx.android.synthetic.main.dialog_add_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_game.*

class DialogCreateGameFragment:DialogFragment() {
    internal lateinit var listener: DialogListener
    var gameTitle:String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater


            builder.apply {
                setView(inflater.inflate(R.layout.dialog_add_new_game, null))
                        // Add action buttons
                setTitle("ゲームを追加する")
                setPositiveButton("追加", DialogInterface.OnClickListener { dialog, id ->
                    if (validateFields()) {
                        listener.onDialogPositiveClick(DialogFragment())
                    }
                })

                setNegativeButton("キャンセル", DialogInterface.OnClickListener { dialog, id ->
                    getDialog()?.cancel()
                })
            }

            return builder.create()
    }



    companion object {
        fun newInstance(fragment: Fragment): DialogCreateGameFragment {
            val instance = DialogCreateGameFragment()
            instance.setTargetFragment(fragment,0)
            return instance
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        newInstance(this)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = this.targetFragment as DialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
//        listener = context as DialogListener
//        //set EditText
//        et_dialog_game_title.setText(gameTitle)
    }

    private fun validateFields(): Boolean {
        if (et_dialog_game_title.text?.isEmpty() == true) {
            til_dialog_title.error = "pleaseEnterTitle"
            et_dialog_game_title.requestFocus()
            return false
        }
        return true
    }

    interface DialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }
}