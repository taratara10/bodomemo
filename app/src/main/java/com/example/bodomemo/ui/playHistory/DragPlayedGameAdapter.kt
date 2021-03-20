package com.example.bodomemo.ui.playHistory

import android.view.View
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.simple_list_item.view.*

class DragPlayedGameAdapter (dataSet: List<GameEntity> = emptyList(), gameDetailEvents: GameDetailEvents)
        : DragDropSwipeAdapter<GameEntity, DragPlayedGameAdapter.PlayedGameViewHolder>(dataSet) {

    private val listener :GameDetailEvents = gameDetailEvents

    class PlayedGameViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: GameDetailEvents) {
            itemView.simple_list_game_title.text = game.title
            itemView.simple_list_adapter.setOnClickListener {
                listener.onViewClicked(game.gameId.toString())
            }
        }
    }

    override fun getViewHolder(itemLayout: View) = PlayedGameViewHolder(itemLayout)

    override fun onBindViewHolder(item: GameEntity, holder: PlayedGameViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        holder.bind(item, listener)
    }

    override fun getViewToTouchToStartDraggingItem(item: GameEntity,viewHolder: PlayedGameViewHolder,position: Int
    ): View? {
        TODO("Not yet implemented")
    }

    interface GameDetailEvents {
        fun onViewClicked(gameId: String?)
    }
}
