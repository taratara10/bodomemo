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

    private var isFavorite = false
    private var isOwned = false
    private var isRating = false
    private var isPlayed = false



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_game_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(filteredGameList[position], listener)
    }

    override fun getItemCount(): Int = filteredGameList.size

    inner class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: DetailsEvents) {
            val bg = R.drawable.shape_rounded_corner
            val clearBg = R.drawable.shape_rounded_corner_opacity

            itemView.search_game_title.text = game.title
            itemView.search_adapter_plat_time.text = game.playTime.toString()
            itemView.search_adapter.setOnClickListener {
                listener.onViewClicked(game.gameId.toString())
            }

            //set icon visibility
            if (game.ownedCheck) itemView.image_search_bag.visibility = View.VISIBLE
                else itemView.image_search_bag.visibility = View.INVISIBLE
            if (game.favoriteCheck) itemView.image_search_fav.visibility = View.VISIBLE
                else itemView.image_search_fav.visibility = View.INVISIBLE

            //change adapter background
            itemView.setBackgroundResource(bg)
            if (this@SearchAdapter.isFavorite) if (!game.favoriteCheck) itemView.setBackgroundResource(clearBg)
            if (this@SearchAdapter.isOwned) if (!game.ownedCheck) itemView.setBackgroundResource(clearBg)
            if (this@SearchAdapter.isPlayed) if (game.playTime != 0) itemView.setBackgroundResource(clearBg)

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
    }

    //すべてのゲーム
    fun filterAllGame(){
        gameList.sortByDescending { it.title }
        filteredGameList = gameList
        gamesWithPlayHistory.forEach { listener.updatePlayTime(it) }
        resetFilterStatus()
    }

    //お気に入り
    fun filterFavorite(){
        gameList.sortByDescending { it.favoriteCheck }
        resetFilterStatus()
        isFavorite = true
    }

    //持ってる
    fun filterOwned(){
        gameList.sortByDescending { it.ownedCheck }
        resetFilterStatus()
        isOwned = true
    }

    //評価準
    fun filterRating(){
        gameList.sortByDescending { it.rating }
        resetFilterStatus()
        isRating = true
    }

    //プレイ回数
    fun filterPlayNumber(){
        gameList.sortByDescending { it.playTime }
    }

    //未プレイ
    fun filterUnPlayed(){
        gameList.sortBy { it.playTime }
        resetFilterStatus()
        isRating = true
    }

    private fun resetFilterStatus(){
        isFavorite =false
        isOwned = false
        isPlayed = false
        isRating = false
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface DetailsEvents {
        fun onViewClicked(gameId: String?)
        fun updatePlayTime(gamesWithPlayHistory: GamesWithPlayHistory)
    }

}