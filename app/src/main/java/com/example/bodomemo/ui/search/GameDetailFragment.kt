package com.example.bodomemo.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.R.id.toolbar_action_delete
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import com.example.bodomemo.ui.playHistory.PlayHistoryAdapter
import com.example.bodomemo.ui.playHistory.PlayHistoryDetailFragmentArgs
import com.example.bodomemo.ui.todo.TodoFragmentArgs
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.android.synthetic.main.fragment_game_detail.view.*

class GameDetailFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedGame:GameEntity
    private lateinit var gameWithPlayHistoryAdapter: GameWithPlayHistoryAdapter
    private val todoFragmentArgs:TodoFragmentArgs by navArgs()
    private val searchFragmentArgs: SearchFragmentArgs by navArgs()
    private val createNewGameFragmentArgs: CreateNewGameFragmentArgs by navArgs()
    private val playHistoryDetailFragmentArgs: PlayHistoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game_detail, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameWithPlayHistoryAdapter = GameWithPlayHistoryAdapter()
        //searchFragment、createFragmentのうちnullでないほうの値をセット
        val selectedGameId = searchFragmentArgs.gameId?.toInt()
                ?: createNewGameFragmentArgs.createdNewId?.toInt()
                ?: createNewGameFragmentArgs.gameId?.toInt()
                ?: playHistoryDetailFragmentArgs.gameId?.toInt()
                ?: todoFragmentArgs.gameId?.toInt()
                ?: throw Exception("cannot get gameId")

        val gameTitleEditText = root.et_game_title_detail
        val todoCheckBox = root.cb_edit_todo_checked
        val favoriteCheckBox = root.cb_edit_favorite_checked
        val ownedCheckBox = root.cb_edit_owned_checked
        val ratingBar = root.rb_edit_rating
        val gameMemo = root.et_game_detail_memo

        val animation = AnimationUtils.loadAnimation(activity,R.anim.icon_animation)

        //  set content
        selectedGame = gameViewModel.getGameById(selectedGameId)
        gameTitleEditText.setText(selectedGame.title)
        todoCheckBox.isChecked = selectedGame.todoCheck
        favoriteCheckBox.isChecked = selectedGame.favoriteCheck
        ownedCheckBox.isChecked = selectedGame.ownedCheck
        gameMemo.setText(selectedGame.gameMemo)
        ratingBar.rating = selectedGame.rating.toFloat()
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
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

        //gameMemo
        gameMemo.addTextChangedListener(object: CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (validateFields()) {
                    selectedGame.gameMemo = s.toString()
                    updateGame(selectedGame)
                }
            }
        })

        //favorite
        favoriteCheckBox.setOnClickListener {
            it.startAnimation(animation)
            selectedGame.favoriteCheck = favoriteCheckBox.isChecked
            updateGame(selectedGame)
        }

        //owned
        ownedCheckBox.setOnClickListener {
            it.startAnimation(animation)
            selectedGame.ownedCheck = ownedCheckBox.isChecked
            updateGame(selectedGame)
        }

        //to do
        todoCheckBox.setOnClickListener {
            it.startAnimation(animation)
            selectedGame.todoCheck = todoCheckBox.isChecked
            updateGame(selectedGame)
        }


        gameViewModel.getGameWithPlayById(selectedGame.gameId).observe(viewLifecycleOwner,{
            val playList = it.playHistoryList
            gameWithPlayHistoryAdapter.setAllPlayList(playList)
            root.tv_game_with_play_history_time.text = playList.size.toString()
            it.gameEntity.rating
        })

        //playGame recyclerView
        root.rv_game_detail_game_played.apply {
            setEmptyView(root.game_detail_empty_view)
            layoutManager = LinearLayoutManager(activity)
            adapter = gameWithPlayHistoryAdapter
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val playHistoryId = playHistoryDetailFragmentArgs.playHistoryId ?: null
            if (playHistoryId != null){
                val action = GameDetailFragmentDirections
                        .actionNavigationGameDetailToNavigationPlayHistoryDetail(playHistoryId)
                findNavController().navigate(action)
            }
            findNavController().popBackStack()
        }


        setHasOptionsMenu(true)

        return root
    }


    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_toolbar, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            toolbar_action_delete -> {
                gameViewModel.deleteGame(selectedGame)
                findNavController().popBackStack()
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
