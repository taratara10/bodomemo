package com.example.bodomemo.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.search_game_item.view.*
import kotlinx.android.synthetic.main.simple_list_item.view.*

class SimpleListAdapter(gameAddEvents: GameAddEvents): RecyclerView.Adapter<SimpleListAdapter.SearchViewHolder>(), Filterable {

    private var gameList: List<GameEntity> = arrayListOf()
    private var filteredGameList: List<GameEntity> = arrayListOf()
    private val listener: GameAddEvents = gameAddEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(filteredGameList[position], listener)
    }

    override fun getItemCount(): Int = filteredGameList.size

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: GameAddEvents) {
            itemView.simple_list_game_title.text = game.title
            itemView.simple_list_adapter.setOnClickListener {
                listener.onViewClicked(game.gameId.toString())
            }
        }
    }

    /**
     * Search Filter implementation
     * */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredGameList = if (charString.isEmpty()) {
                    gameList
                } else {
                    val filteredList = arrayListOf<GameEntity>()
                    for (row in gameList) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredGameList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredGameList = p1?.values as List<GameEntity>
                notifyDataSetChanged()
            }
        }
    }


    fun setAllGames(gameItems: List<GameEntity>) {
        this.gameList = gameItems
        this.filteredGameList = gameItems
        notifyDataSetChanged()
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface GameAddEvents {
        fun onViewClicked(gameId: String?)
    }
}