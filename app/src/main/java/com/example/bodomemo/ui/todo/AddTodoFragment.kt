package com.example.bodomemo.ui.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_add_todo.view.*
import java.util.*

class AddTodoFragment:Fragment() ,AddTodoAdapter.CheckEvents{

    private lateinit var gameViewModel: GameViewModel
    private lateinit var addTodoAdapter: AddTodoAdapter
    private lateinit var et_search_game:EditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add_todo, container, false)

        //Setting up RecyclerView
        val todo_list = root.rv_add_todo_list
        todo_list.setEmptyView(root.add_todo_empty_view)
        todo_list?.layoutManager = LinearLayoutManager(activity)
        addTodoAdapter = AddTodoAdapter(this)
        todo_list?.adapter = addTodoAdapter

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            addTodoAdapter.setAllGames(it)
            //checkEventでFilterListがリセットされてしまうので、titleで再度Filterする
            addTodoAdapter.filter.filter(et_search_game.text)
        })

        //検索した際にfilter
        et_search_game = root.et_search_game_title_todo
        et_search_game.addTextChangedListener(object: AddTodoFragment.CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addTodoAdapter.filter.filter(s)
            }
        })
        return root

    }


    override fun onCheckBoxClicked(gameEntity: GameEntity) {
        gameViewModel.updateGame(gameEntity)
        addTodoAdapter.filter.filter(et_search_game.text)
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}