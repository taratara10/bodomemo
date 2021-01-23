package com.example.bodomemo.ui.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.MainActivity
import com.example.bodomemo.R
import kotlinx.android.synthetic.main.fragment_todo.*

class TodoFragment(context: Context) : Fragment() {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        //Setting up RecyclerView
        //contextは何を設定すればいい？
        rv_todo_list.layoutManager = LinearLayoutManager(context)
        todoAdapter = TodoAdapter()

        todoViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            todoAdapter.setAllGames(it)
        })
        return root



    }
}