package com.example.bodomemo.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import com.example.bodomemo.ui.todo.TodoAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_todo.view.*
import kotlinx.android.synthetic.main.search_game_item.view.*

class SearchFragment : Fragment(), SearchAdapter.DetailsEvents{

    private lateinit var gameViewModel: GameViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gameViewModel =
                ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        //Setting up RecyclerView
        val search_game_list = root.rv_search_game_list

        search_game_list?.layoutManager = LinearLayoutManager(activity)
        searchAdapter = SearchAdapter(this)
        search_game_list?.adapter = searchAdapter

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            searchAdapter.setAllGames(it)
        })

    //* Set spinner layout
        val spinner = root.sp_filter_select
        val spinnerItem = resources.getStringArray(R.array.sp_game_filter)
        activity?.let { setSpinnerItem(it, spinner, spinnerItem) }

        val et_search_game = root.et_search_game
        et_search_game.addTextChangedListener(object: CustomTextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }
        })


        return root
    }


    private fun setSpinnerItem(activity: Activity, spinner: Spinner, item: Array<String>): Spinner {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return spinner
    }

    override fun onViewClicked(gameId: Int) {
        findNavController().navigate(R.id.action_navigation_search_to_navigation_game_detail)
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }
}