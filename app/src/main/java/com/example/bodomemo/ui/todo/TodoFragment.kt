package com.example.bodomemo.ui.todo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.MainActivity
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.fragment_create_todo.*
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.fragment_todo.view.*

class TodoFragment : Fragment(), TodoAdapter.TodoEvents {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        //Setting up RecyclerView
        val todo_list = root.rv_todo_list

        todo_list?.layoutManager = LinearLayoutManager(activity)
        todoAdapter = TodoAdapter(this)
        todo_list?.adapter = todoAdapter

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            todoAdapter.setAllGames(it)
        })
        return root

    }

    override fun onStart() {
        super.onStart()
        btn_create_todo.setOnClickListener {
           findNavController().navigate(R.id.action_navigation_todo_to_navigation_create_todo)
        }
    }

    override fun onCheckBoxClicked(gameEntity: GameEntity) {
        todoViewModel.updateGame(gameEntity)
    }

    override fun onViewClicked(gameEntity: GameEntity) {
        TODO("Not yet implemented")
    }
}
