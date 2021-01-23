package com.example.bodomemo.ui.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.MainActivity
import com.example.bodomemo.R
import kotlinx.android.synthetic.main.fragment_create_todo.*
import kotlinx.android.synthetic.main.fragment_todo.*

class TodoFragment : Fragment() {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        //Setting up RecyclerView
        rv_todo_list?.layoutManager = LinearLayoutManager(activity)
        todoAdapter = TodoAdapter()
        rv_todo_list?.adapter = todoAdapter

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
}