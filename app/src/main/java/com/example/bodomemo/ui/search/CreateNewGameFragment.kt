package com.example.bodomemo.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class CreateNewGameFragment : Fragment(), SearchAdapter.DetailsEvents {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_new_game, container, false)

        //Setting up RecyclerView
        searchAdapter =  SearchAdapter(this)

        root.rv_new_game_search_game_list.apply {
            setEmptyView(root.create_empty_view)
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }


        gameViewModel.getAllGameWithPlayList().observe(viewLifecycleOwner, Observer {
            searchAdapter.setAllGames(it)
        })

        //EditText filter recycleView item
        root.et_new_game_title.addTextChangedListener(object : CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }
        })

        root.btn_save_game_title.setOnClickListener {
            saveGame()
        }

        return root
    }


    private fun saveGame() {
        if (validateFields()) {
            //一旦id = 0 でautoIncrementで設定
            val newGame = GameEntity(gameId = 0, title = et_new_game_title.text.toString())
            gameViewModel.saveGame(newGame)

            //@Insert したidの返り値を渡す
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
            til_new_game_title.error = "pleaseEnterTitle"
            et_new_game_title.requestFocus()
            return false
        }
        return true
    }

    interface CustomTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onViewClicked(gameId: String?) {
        if (gameId != null){
            val action = CreateNewGameFragmentDirections.actionNavigationCreateGameToNavigationGameDetail(gameId)
            findNavController().navigate(action)
        }
    }


}