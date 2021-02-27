package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.ui.PlayHistoryViewModel
import com.example.bodomemo.ui.search.SearchFragmentDirections
import kotlinx.android.synthetic.main.fragment_play_history.view.*

class PlayHistoryFragment : Fragment(), PlayHistoryAdapter.DetailsEvents {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private lateinit var playHistoryAdapter: PlayHistoryAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_play_history, container, false)

        //setting up recyclerView
        val play_list = root.rv_play_history
        play_list.setEmptyView(root.play_history_empty_view)
        play_list?.layoutManager = LinearLayoutManager(activity)
        playHistoryAdapter = PlayHistoryAdapter(this)
        play_list.adapter = playHistoryAdapter

        playHistoryViewModel.getAllPlayHistory().observe(viewLifecycleOwner, {
            playHistoryAdapter.setAllPlayList(it)
        })

        root.btn_create_play_history.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_play_history_to_navigation_create_play_history)
        }


        return root
    }

    override fun onViewClicked(playHistoryId: String?) {
        if (playHistoryId != null){
            val action = PlayHistoryFragmentDirections.actionNavigationPlayHistoryToNavigationPlayHistoryDetail(playHistoryId)
            findNavController().navigate(action)
        }
    }
}