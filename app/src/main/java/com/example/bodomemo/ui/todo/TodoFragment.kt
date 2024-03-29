package com.example.bodomemo.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.fragment_todo.view.*
import java.util.*
import java.util.Collections.swap

class  TodoFragment : Fragment(), DragTodoAdapter.TodoEvents {

    private lateinit var gameViewModel: GameViewModel
    private var dragTodoAdapter: DragTodoAdapter = DragTodoAdapter(emptyList(), this)
    private lateinit var allTodoList: MutableList<GameEntity>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_todo, container, false)
        val rv_todo = root.rv_drag_todo_list
        val todo_empty_view = root.todo_empty_view

        //Setting up RecyclerView
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.getTodoList().observe(viewLifecycleOwner, Observer { todoList ->
            //set dataSet
            allTodoList = todoList
            dragTodoAdapter.setTodoList(allTodoList)

            //Switch EmptyView
            if (todoList.isNotEmpty()){
                todo_empty_view.visibility = View.GONE
                tv_todo_swipe_explain.visibility = View.VISIBLE
            }else{
                todo_empty_view.visibility = View.VISIBLE
                tv_todo_swipe_explain.visibility = View.GONE
            }
        })

        //set up recyclerView
        rv_todo.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = dragTodoAdapter
            dragListener = dragTodoAdapter.onItemDragListener
            swipeListener = dragTodoAdapter.onItemSwipeListener
            disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
        }

        //add to do btn
        root.btn_create_todo.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_todo_to_navigation_add_todo)
        }

        runLayoutAnimation(rv_todo)
        return root
    }

    override fun onViewSwiped(position: Int) {
        val selectedGame = allTodoList[position]
        selectedGame.todoCheck = false
        gameViewModel.updateGame(selectedGame)
        Toast.makeText(activity,"完了しました！",Toast.LENGTH_SHORT).show()
    }

    override fun onViewClicked(gameId: String?) {
        if (gameId != null){
            val action = TodoFragmentDirections.actionNavigationTodoToNavigationGameDetail(gameId)
            findNavController().navigate(action)
        }
    }

    override fun onViewDropped(initialPosition: Int, finalPosition: Int, item: GameEntity) {
        //移動したときにallTodoListのpositionをすべてupdate
        if (initialPosition != finalPosition){
            //todoPosition default value is 0.
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
    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}
