package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayAndGameCrossRef
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.GameViewModel
import com.example.bodomemo.ui.PlayAndGameCrossRefViewModel
import com.example.bodomemo.ui.search.SimpleListAdapter
import kotlinx.android.synthetic.main.dialog_add_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_game.*
import kotlinx.android.synthetic.main.fragment_play_history_add_game.view.*

class PlayHistoryAddGameFragment: Fragment(), SimpleListAdapter.GameAddEvents{
    private lateinit var playAndGameCrossRefViewModel: PlayAndGameCrossRefViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedPlayHistory: PlayHistoryEntity
    private lateinit var simpleListAdapter: SimpleListAdapter
    private val playHistoryDetailFragmentArgs: PlayHistoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_play_history_add_game, container, false)
        playAndGameCrossRefViewModel = ViewModelProvider(this).get(PlayAndGameCrossRefViewModel::class.java)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //setting up recyclerView
        simpleListAdapter = SimpleListAdapter(this)
        root.rv_play_history_add_game_list.apply {
            setEmptyView(root.play_history_add_game_empty_view)
            layoutManager = LinearLayoutManager(activity)
            adapter = simpleListAdapter
        }

        gameViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            simpleListAdapter.setAllGames(it)
            //dialogから戻ってきたときに、検索する
            val gameTitle = root.et_play_history_add_game_title.text.toString()
            simpleListAdapter.filter.filter(gameTitle)
        })

        //Search filter
        root.et_play_history_add_game_title.addTextChangedListener(object: CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                simpleListAdapter.filter.filter(s)
            }
        })

        //add game btn
        root.btn_play_history_add_game.setOnClickListener {
            val gameTitle = root.et_play_history_add_game_title.text?.toString() ?: ""
            val action = PlayHistoryAddGameFragmentDirections.actionNavigationPlayHistoryAddGameToNavigationCreateGameDialog(gameTitle)
            findNavController().navigate(action)
        }



        return root
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    //recyclerView item click events
    override fun onViewClicked(gameId: String?) {
        if(gameId != null) {
            val newReference = PlayAndGameCrossRef(
                    referenceId = 0,
                    playHistoryId = playHistoryDetailFragmentArgs.playHistoryId!!.toInt(),
                    gameId = gameId.toInt()
                    )
            playAndGameCrossRefViewModel.savePlayedGame(newReference)
        }
        val action = PlayHistoryAddGameFragmentDirections
                .actionNavigationPlayHistoryAddGameToNavigationPlayHistoryDetail(playHistoryDetailFragmentArgs.playHistoryId.toString())
        findNavController().navigate(action)
    }



    private fun navigateCreateGameDialog() {
        val newFragment = DialogCreateGameFragment()
        newFragment.show(childFragmentManager, "newGame")
    }
}