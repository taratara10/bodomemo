package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.bodomemo.ui.search.SearchFragment
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
        val root = inflater.inflate(R.layout.fragment_play_history, container, false)
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        playHistoryAdapter = PlayHistoryAdapter(this)

        //search play title
        root.et_play_history_search.addTextChangedListener(object: SearchFragment.CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playHistoryAdapter.filter.filter(s)
            }
        })

        //setting up recyclerView
        root.rv_play_history.apply {
            setEmptyView(root.play_history_empty_view)
            layoutManager = LinearLayoutManager(activity)
            adapter = playHistoryAdapter
        }

        playHistoryViewModel.getAllPlayHistory().observe(viewLifecycleOwner, {
            playHistoryAdapter.setAllPlayList(it)
            playHistoryAdapter.filter.filter("")
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

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }
}