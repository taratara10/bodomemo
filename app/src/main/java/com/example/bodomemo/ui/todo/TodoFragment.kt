package com.example.bodomemo.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_todo.view.*
import java.util.*
import java.util.Collections.swap

class  TodoFragment : Fragment(), DragTodoAdapter.TodoEvents {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var dragTodoAdapter: DragTodoAdapter
    private lateinit var allTodoList: MutableList<GameEntity>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        //Setting up RecyclerView
        //lateinit出来ないので、一旦emptyListでadapter初期化する

        val todo_list_view = root.rv_drag_todo_list
        val todo_empty_view = root.todo_empty_view
        todo_list_view.layoutManager = LinearLayoutManager(activity)
        dragTodoAdapter = DragTodoAdapter(emptyList(), this)
        todo_list_view?.adapter = dragTodoAdapter
        todo_list_view.dragListener = dragTodoAdapter.onItemDragListener

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getTodoList().observe(viewLifecycleOwner, Observer { todoList ->
            //set dataSet
            allTodoList = todoList
            todo_list_view.adapter = DragTodoAdapter(todoList, this)

            //Switch EmptyView
            if (todoList.isNotEmpty()){
                todo_empty_view.visibility = View.GONE
            }else{
                todo_empty_view.visibility = View.VISIBLE
            }

        })

        root.btn_create_todo.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_todo_to_navigation_add_todo)
        }

        return root
    }



    override fun onCheckBoxClicked(gameEntity: GameEntity) {
        gameViewModel.updateGame(gameEntity)
    }

    override fun updatePosition(initialPosition: Int, finalPosition: Int, item: GameEntity) {

        var position = 1
        //change entity position
        allTodoList.removeAt(initialPosition)
        allTodoList.add(finalPosition, item)

        allTodoList.forEach{ gameEntity ->
            gameEntity.todoPosition = position
            gameViewModel.updateGame(gameEntity)
            position ++
        }
    }


}
