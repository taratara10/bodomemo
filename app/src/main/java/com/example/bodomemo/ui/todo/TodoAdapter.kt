package com.example.bodomemo.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoAdapter (todoEvents: TodoEvents): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var gameList :List<GameEntity> = arrayListOf()
    private var todoGameList: List<GameEntity> = arrayListOf()
    private val listener: TodoEvents = todoEvents


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind(todoGameList[position], listener)
    }

    override fun getItemCount(): Int = todoGameList.size

    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: TodoEvents) {
            itemView.tv_todo_title.text = game.title
            itemView.cb_todo_checked.isChecked = game.todoCheck


            itemView.cb_todo_checked.setOnClickListener {
                val checked = itemView.cb_todo_checked.isChecked
                game.todoCheck = checked
                listener.onCheckBoxClicked(game)
            }
        }
    }

    fun setAllGames(gameItems: List<GameEntity>) {
        this.gameList = gameItems
        this.todoGameList = gameItems
        notifyDataSetChanged()
    }

    fun filterTodoGames() {
        val todoGameList = arrayListOf<GameEntity>()
        for (row in gameList) {
            if (row.todoCheck) {
                todoGameList.add(row)
            }
        }
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