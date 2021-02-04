package com.example.bodomemo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_game_edit.view.*
import kotlinx.android.synthetic.main.todo_list_item.view.*

class GameEditFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedGame:GameEntity
    private lateinit var gameTitle: String
    private val args: SearchFragmentArgs by navArgs()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_game_edit, container, false)
        gameTitle = root.et_edit_game_title.text.toString()

//       * set content
        selectedGame = gameViewModel.getGameById(args.gameId)
        root.et_edit_game_title.setText(selectedGame.title)
        root.cb_edit_todo_checked.isChecked = selectedGame.todoCheck
        root.cb_edit_favorite_checked.isChecked = selectedGame.favoriteCheck
        root.cb_edit_owned_checked.isChecked = selectedGame.ownedCheck


        root.rb_edit_rating.setOnRatingBarChangeListener{ ratingBar: RatingBar, fl: Float, b: Boolean ->
        }



        return root
    }

    fun onCheckBoxClicked(gameEntity: GameEntity) {
        if (gameTitle.trim().isNotEmpty())
        gameViewModel.updateGame(gameEntity)
    }

}

//gameTitle onCreateのスコープ外で参照するにはどうする？