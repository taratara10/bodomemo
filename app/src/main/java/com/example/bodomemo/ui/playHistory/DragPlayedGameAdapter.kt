package com.example.bodomemo.ui.playHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.playHistory.DragPlayedGameAdapter.PlayedGameViewHolder
import kotlinx.android.synthetic.main.simple_list_item.view.*

class DragPlayedGameAdapter (dataSet: List<GameEntity> = emptyList(), gameDetailEvents: GameDetailEvents)
        : DragDropSwipeAdapter<GameEntity, PlayedGameViewHolder>(dataSet) {

    private val listener :GameDetailEvents = gameDetailEvents

    class PlayedGameViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: GameDetailEvents) {
            itemView.simple_list_game_title.text = game.title
            itemView.simple_list_adapter.setOnClickListener {
                listener.onViewClicked(game.gameId.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item, parent, false)
        return PlayedGameViewHolder(view)
    }

    override fun getViewHolder(itemLayout: View) = PlayedGameViewHolder(itemLayout)

    override fun onBindViewHolder(item: GameEntity, holder: PlayedGameViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        holder.bind(item, listener)
    }

    override fun getViewToTouchToStartDraggingItem(item: GameEntity,viewHolder: PlayedGameViewHolder,position: Int
    ): View? {
        return viewHolder.itemView
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<GameEntity> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: GameEntity): Boolean {
            // Handle action of item swiped
            // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
            // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
            listener.onViewSwiped(position)
            return false
        }
    }

    interface GameDetailEvents {
        fun onViewClicked(gameId: String?)
        fun onViewSwiped(position: Int)
    }
}
