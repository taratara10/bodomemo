package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import kotlinx.android.synthetic.main.fragment_play_history_detail.view.*

class PlayHistoryDetailFragment:Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private lateinit var selectedPlayHistory: PlayHistoryEntity
    private lateinit var playHistoryTitle: String
    private val playHistoryFragmentArgs: PlayHistoryFragmentArgs by navArgs()
    private val createNewPlayHistoryFragmentArgs: CreateNewPlayHistoryFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_play_history, container, false)

        val titleEditText = root.et_play_title_detail

        //navArgsのうちnullでないほうの値をセット
        val selectedPlayHistoryId = playHistoryFragmentArgs.playHistoryId?.toInt()
                ?: createNewPlayHistoryFragmentArgs.createdPlayHistoryId?.toInt()
                ?: throw Exception("cannot get gameId")

        selectedPlayHistory = playHistoryViewModel.getPlayHistoryById(selectedPlayHistoryId)
        return root
    }

}