package com.example.bodomemo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_game_detail.view.*

class GameDetailFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel
<<<<<<< HEAD
    val args: SearchFragmentArgs by navArgs()
=======
    private var gameList: List<GameEntity> = arrayListOf()
    private val args: SearchFragmentArgs by navArgs()

>>>>>>> refs/remotes/origin/master

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_game_detail, container, false)

<<<<<<< HEAD
        root.et_search_game.setText(args.toString())
=======
        val gameId:Int = args.gameId
        val gameList = gameViewModel.getAllGameList(1)
        val gameRecord =
>>>>>>> refs/remotes/origin/master
        return root
    }

}
