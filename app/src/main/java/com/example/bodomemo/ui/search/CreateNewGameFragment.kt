package com.example.bodomemo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_create_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_game.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class CreateNewGameFragment : Fragment() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var checkGameAdapter: CheckGameAdapter
    var gameEntity: GameEntity? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_new_game, container, false)

        //Setting up RecyclerView
        val search_game_list = root.rv_new_game_search_game_list
        search_game_list?.layoutManager = LinearLayoutManager(activity)
        checkGameAdapter = CheckGameAdapter()
        search_game_list?.adapter = checkGameAdapter

        gameViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            checkGameAdapter.setAllGames(it)
        })

        return root
    }

    override fun onStart() {
        super.onStart()
        btn_save_game_title.setOnClickListener {
            saveTodo()
        }
    }

    /**
     * Sends the updated information back to calling Activity
     * */
    private fun saveTodo() {
        if (validateFields()) {
            //一旦id = 0 でautoIncrementで設定
            val todo =  GameEntity(id = 0, title = et_new_game_title.text.toString())
            gameViewModel.saveGame(todo)
            val insertedGameId = gameViewModel.insertedGameId.toString()

            val action = CreateNewGameFragmentDirections.actionNavigationCreateGameToNavigationGameDetail(insertedGameId)
            findNavController().navigate(action)
        }
    }

    /**
     * Validation of EditText 検証
     * */
    private fun validateFields(): Boolean {
        if (et_new_game_title.text?.isEmpty() == true) {
            til_new_game_title.error ="pleaseEnterTitle"
            et_new_game_title.requestFocus()
            return false
        }
        return true
    }
}