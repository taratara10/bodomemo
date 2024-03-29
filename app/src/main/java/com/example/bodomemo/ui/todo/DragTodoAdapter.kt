package com.example.bodomemo.ui.todo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.ernestoyaquello.dragdropswiperecyclerview.util.DragDropSwipeDiffCallback
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.playHistory.DragPlayedGameAdapter
import kotlinx.android.synthetic.main.drag_game_item.view.*
import kotlinx.android.synthetic.main.todo_list_item.view.*

class DragTodoAdapter(dataSet: List<GameEntity> = emptyList(),todoEvents: TodoEvents)
        : DragDropSwipeAdapter<GameEntity, DragTodoAdapter.DragTodoViewHolder>(dataSet) {

    private val listener: TodoEvents = todoEvents

    class DragTodoViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: TodoEvents) {
            itemView.tv_drag_game_title.text = game.title
            itemView.drag_game_adapter.setOnClickListener {
                listener.onViewClicked(game.gameId.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DragTodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.drag_game_item, parent, false)
        return DragTodoViewHolder(view)
    }

    override fun getViewHolder(itemLayout: View) = DragTodoViewHolder(itemLayout)

    override fun onBindViewHolder(item: GameEntity, holder: DragTodoViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        holder.bind(item, listener)
    }

    override fun getViewToTouchToStartDraggingItem(item: GameEntity, holder: DragTodoViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return holder.itemView.imgae_drag_game_handle
    }

     val onItemDragListener = object : OnItemDragListener<GameEntity> {
        override fun onItemDragged(previousPosition: Int, newPosition: Int, item: GameEntity) {  }

        override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: GameEntity) {
           listener.onViewDropped(initialPosition,finalPosition,item)

        }
    }

    val onItemSwipeListener = object : OnItemSwipeListener<GameEntity> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: GameEntity): Boolean {
            // Handle action of item swiped
            // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
            // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
            listener.onViewSwiped(position)
            return false
        }
    }

    fun setTodoList(todoList: List<GameEntity>) {
        this.dataSet = todoList
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface TodoEvents {
        fun onViewDropped(initialPosition: Int, finalPosition: Int,item:GameEntity)
        fun onViewSwiped(position: Int)
        fun onViewClicked(gameId:String?)
    }
}