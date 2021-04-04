package com.example.bodomemo.ui.playHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayHistoryEntity
import kotlinx.android.synthetic.main.play_history_list_item.view.*
import java.text.SimpleDateFormat

class PlayHistoryAdapter (detailsEvents: DetailsEvents): RecyclerView.Adapter<PlayHistoryAdapter.PlayHistoryViewHolder>() ,Filterable{

    private var playList :MutableList<PlayHistoryEntity> = arrayListOf()
    private var filteredPlayList :MutableList<PlayHistoryEntity> = arrayListOf()
    private val listener: DetailsEvents = detailsEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_history_list_item, parent, false)
        return PlayHistoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: PlayHistoryViewHolder, position: Int) {
        holder.bind(filteredPlayList[position], listener)
    }

    override fun getItemCount(): Int = filteredPlayList.size

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


    /**
     * Search Filter implementation
     * */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredPlayList = if (charString.isEmpty()) {
                    playList
                } else {
                    val filteredList = arrayListOf<PlayHistoryEntity>()
                    for (row in playList) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredPlayList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredPlayList = p1?.values as MutableList<PlayHistoryEntity>
                notifyDataSetChanged()
            }
        }
    }

    fun setAllPlayList(list: MutableList<PlayHistoryEntity>) {
        this.playList = list
        this.filteredPlayList = list
        notifyDataSetChanged()
    }

    interface DetailsEvents {
        fun onViewClicked(playHistoryId: String?)
    }


}