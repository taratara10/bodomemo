package com.example.bodomemo.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.R.id.toolbar_action_delete
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_game_edit.view.*

class GameDetailFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedGame:GameEntity
    private lateinit var gameTitle: String
    private val searchFragmentArgs: SearchFragmentArgs by navArgs()
    private val createNewGameFragmentArgs: CreateNewGameFragmentArgs by navArgs()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_game_edit, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val gameTitleEditText = root.et_edit_game_title
        val todoCheckBox = root.cb_edit_todo_checked
        val favoriteCheckBox = root.cb_edit_favorite_checked
        val ownedCheckBox = root.cb_edit_owned_checked
        val ratingBar = root.rb_edit_rating

        //searchFragment、createFragmentのうちnullでないほうの値をセット
        //todo error処理
        val selectedGameId = searchFragmentArgs.gameId?.toInt() ?: createNewGameFragmentArgs.createdNewId?.toInt()

        //  set content
        selectedGame = gameViewModel.getGameById(selectedGameId)
        gameTitleEditText.setText(selectedGame.title)
        todoCheckBox.isChecked = selectedGame.todoCheck
        favoriteCheckBox.isChecked = selectedGame.favoriteCheck
        ownedCheckBox.isChecked = selectedGame.ownedCheck
        ratingBar.rating = selectedGame.rating.toFloat()

        gameTitle = gameTitleEditText.text.toString()

        ratingBar.setOnRatingBarChangeListener { r, rating, fromUser ->
            selectedGame.rating = rating.toInt()
            updateGame(selectedGame)            
        }

        //editText
        gameTitleEditText.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectedGame.title = s.toString()
                updateGame(selectedGame)
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

//        //popBackした際にupdate これをしないと、SearchFragmentのrecycleViewが更新されない
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            gameViewModel.updateGame(selectedGame)
//            findNavController().popBackStack()
            parentFragmentManager.popBackStack()

        }


        return root
    }

    fun updateGame(gameEntity: GameEntity) {
        if (gameTitle.trim().isNotEmpty()) gameViewModel.updateGame(gameEntity)
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
}
