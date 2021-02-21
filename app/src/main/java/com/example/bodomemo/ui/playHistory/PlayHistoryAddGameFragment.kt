package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import com.example.bodomemo.ui.search.CreateNewGameFragmentDirections
import kotlinx.android.synthetic.main.fragment_create_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_game.et_new_play_history_title
import kotlinx.android.synthetic.main.fragment_create_new_game.til_new_play_history_title
import kotlinx.android.synthetic.main.fragment_create_new_play_history.*

class PlayHistoryAddGameFragment: Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_new_play_history, container, false)



        return root
    }


    override fun onStart() {
        super.onStart()
        btn_save_play_history.setOnClickListener {
            savePlayHistory()
        }
    }

    private fun savePlayHistory() {
        if (validateFields()) {
            //一旦id = 0 でautoIncrementで設定
                //todo add date
            val newGame = PlayHistoryEntity(playHistoryId = 0, title = et_new_play_history_title.text.toString(),date = )
            playHistoryViewModel.savePlayHistory(newGame)

            //@Insert したidの返り値を渡す
            val insertedGameId = playHistoryViewModel.insertedPlayHistoryId.toString()

            //todo add Direction
           // val action =
            //findNavController().navigate(action)
        }
    }

    /**
     * Validation of EditText 検証
     * */
    private fun validateFields(): Boolean {
        if (et_new_play_history_title.text?.isEmpty() == true) {
            til_new_play_history_title.error = "pleaseEnterTitle"
            et_new_play_history_title.requestFocus()
            return false
        }
        return true
    }
}