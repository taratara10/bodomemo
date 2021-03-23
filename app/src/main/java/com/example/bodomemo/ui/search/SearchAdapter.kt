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
import kotlinx.android.synthetic.main.search_game_item.view.*

class SearchAdapter (detailsEvents: DetailsEvents): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {

    private var originalGameList: MutableList<GameEntity> = arrayListOf()
    private var gameList :MutableList<GameEntity> = arrayListOf()
    private var filteredGameList: MutableList<GameEntity> = arrayListOf()
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


    fun setAllGames(gameItems: MutableList<GameEntity>) {
        this.originalGameList = gameItems
        this.gameList = originalGameList
        this.filteredGameList = gameItems
        notifyDataSetChanged()
    }

    //すべてのゲーム
    fun filterAllGame(){
        gameList = originalGameList
        filteredGameList = originalGameList
        notifyDataSetChanged()
    }

    //お気に入り
    fun filterFavorite(){
        gameList.sortedWith( compareBy { it.favoriteCheck })
        filteredGameList = gameList
        Log.d("a","${gameList} ")
        notifyDataSetChanged()
    }

    //持ってる
    fun filterOwned(){
        gameList.sortedBy { it.ownedCheck }
        filteredGameList = gameList
        notifyDataSetChanged()
    }

    //評価準
    fun filterRating(){
        gameList = originalGameList.sortedByDescending { it.rating } as MutableList<GameEntity>
        filteredGameList = gameList
        notifyDataSetChanged()
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface DetailsEvents {
        fun onViewClicked(gameId: String?)
    }

}