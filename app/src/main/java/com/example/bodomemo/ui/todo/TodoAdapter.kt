package com.example.bodomemo.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoAdapter (): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var gameList :List<GameEntity> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind(gameList[position])
    }

//    override fun getItemCount(): Int = gameList.size
    override fun getItemCount(): Int = gameList.size

    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity) {
            itemView.tv_todo_title.text = game.title
        }
    }

    fun setAllGames(gameItems: List<GameEntity>) {
        this.gameList = gameItems
        notifyDataSetChanged()
    }

}