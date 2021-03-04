package com.example.bodomemo.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.ui.GameViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment(), SearchAdapter.DetailsEvents{

    private lateinit var gameViewModel: GameViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        //Setting up RecyclerView
        val search_game_list = root.rv_search_game_list
        search_game_list.setEmptyView(root.play_history_detail_empty_view)
        search_game_list?.layoutManager = LinearLayoutManager(activity)
        searchAdapter =  SearchAdapter(this)
        search_game_list?.adapter = searchAdapter

        gameViewModel.getAllGameList().observe(viewLifecycleOwner, Observer {
            searchAdapter.setAllGames(it)
        })

        // Set spinner layout
        val spinner = root.sp_filter_select
        val spinnerItem = resources.getStringArray(R.array.sp_game_filter)
        activity?.let { setSpinnerItem(it, spinner, spinnerItem) }
        //setOnClickListener
        spinner.onItemSelectedListener =spinnerListener


        //検索した際にfilter
        val et_search_game = root.et_game_detail_title
        et_search_game.addTextChangedListener(object: CustomTextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }
        })

        //fab_Btn -> createNewGameFragment
        root.btn_create_new_game.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_search_to_navigation_create_todo)
        }

        return root
    }
    //ここまでonCreate


    //画面遷移時にrecyclerViewを更新する　1回filter通さないと表示してくれない
    override fun onResume() {
        super.onResume()
        searchAdapter.filter.filter("")
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


    //Spinner.OnItemSelectedListener
    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                //すべて選択
                0 -> { searchAdapter.filterAllGame() }
                //favorite
                1 -> { searchAdapter.filterFavorite() }
                //to do
                2 -> { searchAdapter.filterTodo()}
                //owned
                3 -> { searchAdapter.filterOwned() }
            }

        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}
