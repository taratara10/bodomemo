package com.example.bodomemo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bodomemo.R
import com.example.bodomemo.ui.GameViewModel

class GameDetailFragment: Fragment() {
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }

}