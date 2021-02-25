package com.example.bodomemo.ui.playHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayHistoryEntity
import kotlinx.android.synthetic.main.play_history_list_item.view.*
import java.text.SimpleDateFormat

class PlayHistoryAdapter (detailsEvents: DetailsEvents): RecyclerView.Adapter<PlayHistoryAdapter.PlayHistoryViewHolder>() {

    private var playList :List<PlayHistoryEntity> = arrayListOf()
    private val listener: PlayHistoryAdapter.DetailsEvents = detailsEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_history_list_item, parent, false)
        return PlayHistoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: PlayHistoryViewHolder, position: Int) {
        holder.bind(playList[position], listener)
    }

    override fun getItemCount(): Int = playList.size

    class PlayHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(playHistory: PlayHistoryEntity, listener: DetailsEvents) {
            itemView.tv_play_date.text = convertDate(playHistory.date)
            itemView.tv_play_title.text = playHistory.title

            itemView.play_history_adapter.setOnClickListener {
                //safeArgs Intがnullサポートしてないので、stringで渡す
                listener.onViewClicked(playHistory.playHistoryId.toString())
            }
        }

        private fun convertDate(milliSeconds: Long):String {
            return SimpleDateFormat("yyyy/MM/dd").format(milliSeconds)
        }
    }

    fun setAllPlayList(playList: List<PlayHistoryEntity>) {
        this.playList
        notifyDataSetChanged()
    }

    interface DetailsEvents {
        fun onViewClicked(playHistoryId: String?)
    }
}