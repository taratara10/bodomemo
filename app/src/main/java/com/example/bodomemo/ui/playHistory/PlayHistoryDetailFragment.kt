package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bodomemo.R
import com.example.bodomemo.ui.PlayHistoryViewModel

class PlayHistoryDetailFragment:Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_play_history, container, false)

        return root
    }

}