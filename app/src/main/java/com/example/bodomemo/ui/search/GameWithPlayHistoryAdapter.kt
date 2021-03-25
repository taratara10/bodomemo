package com.example.bodomemo.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.PlayHistoryEntity
import kotlinx.android.synthetic.main.game_with_play_history_item.view.*
import java.text.SimpleDateFormat

class GameWithPlayHistoryAdapter: RecyclerView.Adapter<GameWithPlayHistoryAdapter.GameWithPlayViewHolder>() {

    private var playList :List<PlayHistoryEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameWithPlayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_with_play_history_item, parent, false)
        return GameWithPlayViewHolder(view)
    }


    override fun onBindViewHolder(holder: GameWithPlayViewHolder, position: Int) {
        holder.bind(playList[position])
    }

    override fun getItemCount(): Int = playList.size

    class GameWithPlayViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(playHistory: PlayHistoryEntity) {
            itemView.tv_game_with_play_history_adapter_date.text = convertDate(playHistory.date)
            itemView.tv_game_with_play_history_adapter_title.text = playHistory.title
        }

        private fun convertDate(milliSeconds: Long):String {
            return SimpleDateFormat("yyyy/MM/dd").format(milliSeconds)
        }
    }

    fun setAllPlayList(playList: List<PlayHistoryEntity>) {
        this.playList = playList
        notifyDataSetChanged()
    }

}