package com.example.bodomemo.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.R.id.toolbar_action_delete
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.android.synthetic.main.fragment_game_detail.view.*

class GameDetailFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedGame:GameEntity
    private val searchFragmentArgs: SearchFragmentArgs by navArgs()
    private val createNewGameFragmentArgs: CreateNewGameFragmentArgs by navArgs()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_game_detail, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val gameTitleEditText = root.et_game_title_detail
        val todoCheckBox = root.cb_edit_todo_checked
        val favoriteCheckBox = root.cb_edit_favorite_checked
        val ownedCheckBox = root.cb_edit_owned_checked
        val ratingBar = root.rb_edit_rating

        //searchFragment、createFragmentのうちnullでないほうの値をセット
        val selectedGameId = searchFragmentArgs.gameId?.toInt()
                ?: createNewGameFragmentArgs.createdNewId?.toInt()
                ?: throw Exception("cannot get gameId")

        //  set content
        selectedGame = gameViewModel.getGameById(selectedGameId)
        gameTitleEditText.setText(selectedGame.title)
        todoCheckBox.isChecked = selectedGame.todoCheck
        favoriteCheckBox.isChecked = selectedGame.favoriteCheck
        ownedCheckBox.isChecked = selectedGame.ownedCheck
        ratingBar.rating = selectedGame.rating.toFloat()

        ratingBar.setOnRatingBarChangeListener { r, rating, fromUser ->
            selectedGame.rating = rating.toInt()
            updateGame(selectedGame)            
        }

        //editText
        gameTitleEditText.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (validateFields()){
                selectedGame.title = s.toString()
                updateGame(selectedGame)
                }
            }
        })

        //favorite
        favoriteCheckBox.setOnClickListener {
            selectedGame.favoriteCheck = favoriteCheckBox.isChecked
            updateGame(selectedGame)
        }

        //to do
        todoCheckBox.setOnClickListener {
            selectedGame.todoCheck = todoCheckBox.isChecked
            updateGame(selectedGame)
        }

        //owned
        ownedCheckBox.setOnClickListener {
            selectedGame.ownedCheck = ownedCheckBox.isChecked
            updateGame(selectedGame)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        updateGame(selectedGame)
    }


    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.game_detail_toolbar, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            toolbar_action_delete -> {
                gameViewModel.deleteGame(selectedGame)
                parentFragmentManager.popBackStack()
                Toast.makeText(activity,"削除しました", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun updateGame(gameEntity: GameEntity) {
        if (validateFields()) gameViewModel.updateGame(gameEntity)
    }

    private fun validateFields(): Boolean {
        if (et_game_title_detail.text?.isEmpty() == true) {
            til_game_title_detail.error = "pleaseEnterTitle"
            et_game_title_detail.requestFocus()
            return false
        }
        return true
    }
}
