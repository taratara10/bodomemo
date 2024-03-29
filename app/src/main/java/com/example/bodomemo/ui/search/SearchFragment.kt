package com.example.bodomemo.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.GamesWithPlayHistory
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment(), SearchAdapter.DetailsEvents{

    private lateinit var gameViewModel: GameViewModel
    private lateinit var searchAdapter: SearchAdapter
    private var displayRating = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //Setting up RecyclerView
        searchAdapter =  SearchAdapter(this)
        root.rv_search_game_list.apply {
            setEmptyView(root.search_empty_view)
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }

        //set LiveData
        gameViewModel.getAllGameWithPlayList().observe(viewLifecycleOwner, Observer {
            searchAdapter.setAllGames(it)
            //画面遷移時にrecyclerViewを更新する　1回filter通さないと表示してくれない
            searchAdapter.filter.filter("")
            toggleExplainTextVisibility()
        })

        // Set spinner layout
        val spinner = root.sp_filter_select
        val spinnerItem = resources.getStringArray(R.array.sp_game_filter)
        activity?.let { setSpinnerItem(it, spinner, spinnerItem) }
        //setOnClickListener
        spinner.onItemSelectedListener =spinnerListener


        //search editText
        root.et_game_detail_title.addTextChangedListener(object: CustomTextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }
        })

        //fab_Btn -> createNewGameFragment
        root.btn_create_new_game.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_search_to_navigation_create_game)
        }

        return root
    }

    override fun onViewClicked(gameId: String?) {
        if (gameId != null){
            val action = SearchFragmentDirections.actionNavigationSearchToNavigationGameDetail(gameId)
            findNavController().navigate(action)
        }
    }

    private fun setSpinnerItem(activity: Activity, spinner: Spinner, item: Array<String>): Spinner {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return spinner
    }

    override fun updatePlayTime(gamesWithPlayHistory: GamesWithPlayHistory){
        val playTime = gamesWithPlayHistory.playHistoryList.size
        val gameEntity = gamesWithPlayHistory.gameEntity
        if (playTime != gameEntity.playTime) {
            gameEntity.playTime = playTime
            gameViewModel.updateGame(gameEntity)
            Log.d("if","実行された${playTime}/${gameEntity.playTime}")
        }
    }

    fun toggleExplainTextVisibility(){
        if (search_empty_view.visibility == View.GONE){
            tv_search_play_time_title.visibility = View.GONE
        } else {
            tv_search_play_time_title.visibility = View.VISIBLE
        }
    }

    fun toggleExplainText(){
        if (displayRating){
            tv_search_play_time_title.text = "評価点"
        }else{
            tv_search_play_time_title.text = "プレイ回数"
        }

    }

    //Spinner.OnItemSelectedListener
    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){

                //すべて選択
                0 -> {
                    searchAdapter.filterAllGame()
                    displayRating = false
                }
                //favorite
                1 -> {
                    searchAdapter.filterFavorite()
                    displayRating =false
                }
                //owned
                2 -> {
                    searchAdapter.filterOwned()
                    displayRating =false
                 }
                //rating
                3 -> {
                    searchAdapter.filterRating()
                    displayRating =true
                }
                //play number
                4 -> {
                    searchAdapter.filterPlayNumber()
                    displayRating =false
                }
                //unPlayed
                5 -> {
                    searchAdapter.filterUnPlayed()
                    displayRating =false
                }
            }
            toggleExplainText()
            //検索フィールドを空にして、animation
            et_game_detail_title.text?.clear()
            runLayoutAnimation(rv_search_game_list)
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}
