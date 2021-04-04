package com.example.bodomemo.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

class AddTodoAdapter(checkEvents: CheckEvents): RecyclerView.Adapter<AddTodoAdapter.AddTodoViewHolder>(), Filterable {

    private var gameList: List<GameEntity> = arrayListOf()
    private var filteredGameList: List<GameEntity> = arrayListOf()
    private val listener: CheckEvents = checkEvents


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return AddTodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddTodoViewHolder, position: Int) {
        holder.bind(filteredGameList[position], listener)
    }


    override fun getItemCount(): Int = filteredGameList.size

    class AddTodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: GameEntity, listener: CheckEvents) {
            itemView.tv_todo_item_title.text = game.title
            itemView.cb_todo_checked.isChecked = game.todoCheck
            itemView.cb_todo_checked.setOnClickListener {
                game.todoCheck = itemView.cb_todo_checked.isChecked
                listener.onCheckBoxClicked(game)
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
    }


    /**
     * RecycleView touch event callbacks
     * */
    interface CheckEvents {
        fun onCheckBoxClicked(gameEntity: GameEntity)
    }



}