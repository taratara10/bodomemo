package com.example.bodomemo.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoAdapter (todoEvents: TodoEvents): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var todoList: List<GameEntity> = arrayListOf()
    private val listener: TodoEvents = todoEvents


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind(todoList[position], listener)
    }

    override fun getItemCount(): Int = todoList.size

    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: TodoEvents) {
            itemView.tv_todo_item_title.text = game.title
            //to_do_check = true のものを空のcheckBox(false)で表したい
            itemView.cb_todo_checked.isChecked = !game.todoCheck


            itemView.cb_todo_checked.setOnClickListener {
                val checked:Boolean = !itemView.cb_todo_checked.isChecked
                game.todoCheck = checked
                listener.onCheckBoxClicked(game)
            }
        }
    }


    fun setAllGames(gameItems: List<GameEntity>) {
        this.todoList = gameItems
        notifyDataSetChanged()
    }


    /**
     * RecycleView touch event callbacks
     * */
    interface TodoEvents {
        fun onCheckBoxClicked(gameEntity: GameEntity)
        fun onViewClicked(gameEntity: GameEntity)
    }

}