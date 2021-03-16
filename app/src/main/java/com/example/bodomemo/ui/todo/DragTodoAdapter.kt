package com.example.bodomemo.ui.todo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

class DragTodoAdapter(todoEvents: TodoAdapter.TodoEvents): DragDropSwipeAdapter<String, DragTodoAdapter.TodoViewHolder>() {
    private var todoList: List<GameEntity> = arrayListOf()
    private val listener: TodoAdapter.TodoEvents = todoEvents


    class TodoViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: TodoAdapter.TodoEvents) {
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

    override fun getViewHolder(itemLayout: View) = TodoViewHolder(itemLayout)

    override fun onBindViewHolder(item: String, holder: TodoViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        holder.bind(todoList[position], listener)
    }

    override fun getViewToTouchToStartDraggingItem(item: String, holder: TodoViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return holder.itemView
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