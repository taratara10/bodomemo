package com.example.bodomemo.ui.playHistory

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import com.example.bodomemo.ui.search.SimpleListAdapter
import kotlinx.android.synthetic.main.fragment_play_history_add_game.view.*
import kotlinx.android.synthetic.main.fragment_play_history_detail.*
import kotlinx.android.synthetic.main.fragment_play_history_detail.view.*
import java.util.*
import kotlin.properties.Delegates

class PlayHistoryDetailFragment:Fragment(),DragPlayedGameAdapter.GameDetailEvents {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private lateinit var selectedPlayHistory: PlayHistoryEntity
    private lateinit var dragPlayedGameAdapter: DragPlayedGameAdapter
    private var selectedPlayHistoryId by Delegates.notNull<Int>()
    private val playHistoryFragmentArgs: PlayHistoryFragmentArgs by navArgs()
    private val createNewPlayHistoryFragmentArgs: CreateNewPlayHistoryFragmentArgs by navArgs()
    private val playHistoryAddGameFragmentArgs: PlayHistoryAddGameFragmentArgs by navArgs()

    private lateinit var allPlayedGameList: List<GameEntity>
    private var playHistoryDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_play_history_detail, container, false)
        val rv_played_game = root.rv_played_game_list

        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        //navArgsのうちnullでないほうの値をセット
        selectedPlayHistoryId = playHistoryFragmentArgs.playHistoryId?.toInt()
            ?: createNewPlayHistoryFragmentArgs.createdPlayHistoryId?.toInt()
                    ?: playHistoryAddGameFragmentArgs.playHistoryId?.toInt()
                    ?: throw Exception("cannot get playHistoryId")
        selectedPlayHistory = playHistoryViewModel.getPlayHistoryById(selectedPlayHistoryId)


        //recyclerView

        playHistoryViewModel.getPlayedGameById(selectedPlayHistoryId).observe(viewLifecycleOwner,{ playWithGames ->
            //set dataSet
            allPlayedGameList = playWithGames.gameList
            rv_played_game.adapter = DragPlayedGameAdapter(allPlayedGameList,this)

            //Switch EmptyView
            if (playWithGames.gameList.isNotEmpty()){
                root.play_history_detail_empty_view.visibility = View.GONE
            }else{
                root.play_history_detail_empty_view.visibility = View.VISIBLE
            }
        })


        dragPlayedGameAdapter = DragPlayedGameAdapter(emptyList(),this)
        rv_played_game.layoutManager = LinearLayoutManager(activity)
        rv_played_game.adapter = dragPlayedGameAdapter
//        rv_played_game.dragListener = dragPlayedGameAdapter




        //title editText
        val titleEditText = root.et_play_title_detail
        titleEditText.setText(selectedPlayHistory.title)
        titleEditText.addTextChangedListener(object :CustomTextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (validateFields()){
                    selectedPlayHistory.title = s.toString()
                    updatePlayHistory(selectedPlayHistory)
                }
            }
        })

        //Date EditText
        playHistoryDate = selectedPlayHistory.date
        val dateEditText = root.et_play_date_select_detail
        dateEditText.setText(playHistoryViewModel.convertMilliSecToDate(playHistoryDate))
        dateEditText.setOnClickListener{ showDatePicker() }

        //add game
        root.play_history_detail_add_game.setOnClickListener {
            val action = PlayHistoryDetailFragmentDirections
                .actionNavigationPlayHistoryDetailToNavigationPlayHistoryAddGame(selectedPlayHistoryId.toString())
            findNavController().navigate(action)

        }

        //memo
        val memoEditText = root.et_play_history_detail_memo
        memoEditText.setText(selectedPlayHistory.memo)
        memoEditText.addTextChangedListener(object :CustomTextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (validateFields()){
                    selectedPlayHistory.memo = s.toString()
                    updatePlayHistory(selectedPlayHistory)
                }
            }
        })


        return root
    }

    override fun onResume() {
        super.onResume()
        selectedPlayHistoryId = playHistoryFragmentArgs.playHistoryId?.toInt()
            ?: createNewPlayHistoryFragmentArgs.createdPlayHistoryId?.toInt()
                    ?: playHistoryAddGameFragmentArgs.playHistoryId?.toInt()
                    ?: throw Exception("cannot get playHistoryId")
        Log.d("resume","${selectedPlayHistory}")
    }



    private fun showDatePicker() {
        val calender = Calendar.getInstance()
        calender.timeInMillis = selectedPlayHistory.date
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view, y, m, d ->
                //playHistoryDate:Long を更新して、EditTextの表示も更新
                calender.set(y,m,d)
                playHistoryDate = calender.timeInMillis
                selectedPlayHistory.date = playHistoryDate
                et_play_date_select_detail.setText(playHistoryViewModel.convertMilliSecToDate(playHistoryDate))
                updatePlayHistory(selectedPlayHistory)
            },
            year,month,day
        )
        datePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_action_delete -> {
                playHistoryViewModel.deletePlayHistory(selectedPlayHistory)
                findNavController().popBackStack()
                Toast.makeText(activity,"削除しました", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun updatePlayHistory(playHistoryEntity: PlayHistoryEntity) {
        if (validateFields()) playHistoryViewModel.updatePlayHistory(playHistoryEntity)
    }

    private fun validateFields(): Boolean {
        if (et_play_title_detail.text?.isEmpty() == true) {
            til_play_title_detail.error = "pleaseEnterTitle"
            et_play_title_detail.requestFocus()
            return false
        }
        return true
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onViewClicked(gameId: String?) {
        TODO("Not yet implemented")
    }

}