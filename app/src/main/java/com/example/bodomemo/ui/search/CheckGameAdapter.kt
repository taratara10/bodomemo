package com.example.bodomemo.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.todo.AddGameAdapter
import kotlinx.android.synthetic.main.search_game_item.view.*

class CheckGameAdapter(): RecyclerView.Adapter<CheckGameAdapter.SearchViewHolder>(), Filterable {

    private var gameList: List<GameEntity> = arrayListOf()
    private var filteredGameList: List<GameEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_game_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(filteredGameList[position])
    }

    override fun getItemCount(): Int = filteredGameList.size

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity) {
            itemView.search_game_title.text = game.title
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
    
}