package com.example.bodomemo.ui.playHistory

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.dialog_add_new_game.*
import kotlinx.android.synthetic.main.dialog_add_new_game.view.*
import kotlinx.android.synthetic.main.fragment_create_new_game.*

class DialogCreateGameFragment:DialogFragment() {
    private lateinit var gameViewModel: GameViewModel
    private val addGameFragmentArgs:PlayHistoryAddGameFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.dialog_add_new_game, null)
            val gameTitle = root.et_dialog_game_title.text



            builder.apply {
                setView(root)
                setTitle("ゲームを追加する")
                root.et_dialog_game_title.setText(addGameFragmentArgs.gameTitle)
                //add btn
                setPositiveButton("追加", DialogInterface.OnClickListener { _, _ ->
                    if (gameTitle?.isEmpty() == false) {
                        val newGame = GameEntity(gameId = 0, title = gameTitle.toString())
                        gameViewModel.saveGame(newGame)
                    }
                })

                setNegativeButton("キャンセル", DialogInterface.OnClickListener { _, _ ->
                    dialog?.cancel()
                })
            }

            return builder.create()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
    }

}