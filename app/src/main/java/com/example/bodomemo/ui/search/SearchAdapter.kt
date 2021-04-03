package com.example.bodomemo.ui.search


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.GamesWithPlayHistory
import kotlinx.android.synthetic.main.search_game_item.view.*

class SearchAdapter (detailsEvents: DetailsEvents): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {

    private var gameList :MutableList<GameEntity> = arrayListOf()
    private var filteredGameList: MutableList<GameEntity> = arrayListOf()
    private var gamesWithPlayHistory: MutableList<GamesWithPlayHistory> = arrayListOf()
    private val listener: DetailsEvents = detailsEvents


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_game_item, parent, false)

        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(filteredGameList[position], listener)
    }

    override fun getItemCount(): Int = filteredGameList.size

    class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: DetailsEvents) {
            itemView.search_game_title.text = game.title
            itemView.search_adapter_plat_time.text = game.playTime.toString()
            if (game.ownedCheck) itemView.image_search_bag.visibility = View.VISIBLE
                    else itemView.image_search_bag.visibility = View.INVISIBLE
            if (game.favoriteCheck) itemView.image_search_fav.visibility = View.VISIBLE
                    else itemView.image_search_fav.visibility = View.INVISIBLE

            itemView.search_adapter.setOnClickListener {
                //safeArgs Intがnullサポートしてないので、stringで渡す
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
                filteredGameList = p1?.values as MutableList<GameEntity>
                notifyDataSetChanged()
            }
        }
    }


    fun setAllGames(list: MutableList<GamesWithPlayHistory>) {
        gamesWithPlayHistory = list
        this.gameList = list.map { it.gameEntity } as MutableList<GameEntity>
        this.filteredGameList = gameList
        notifyDataSetChanged()
    }

    //すべてのゲーム
    fun filterAllGame(){
        gameList.sortByDescending { it.title }
        filteredGameList = gameList
        gamesWithPlayHistory.forEach { listener.updatePlayTime(it) }
        notifyDataSetChanged()
    }

    //お気に入り
    fun filterFavorite(){
        gameList.sortByDescending { it.favoriteCheck }
        notifyDataSetChanged()
    }

    //持ってる
    fun filterOwned(){
        gameList.sortByDescending { it.ownedCheck }
        notifyDataSetChanged()
    }

    //評価準
    fun filterRating(){
        gameList.sortByDescending { it.rating }
        notifyDataSetChanged()
    }

    //プレイ回数
    fun filterPlayNumber(){
        gamesWithPlayHistory.forEach { listener.updatePlayTime(it) }
        gameList.sortedByDescending { it.playTime }
        notifyDataSetChanged()
    }

    //未プレイ
    fun filterUnPlayed(){
        gameList.sortedBy { it.playTime }
        notifyDataSetChanged()
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface DetailsEvents {
        fun onViewClicked(gameId: String?)
        fun updatePlayTime(gamesWithPlayHistory: GamesWithPlayHistory)
    }

}