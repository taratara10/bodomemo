package com.example.bodomemo.ui.playHistory

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import kotlinx.android.synthetic.main.fragment_play_history_add_game.view.*
import kotlinx.android.synthetic.main.fragment_play_history_detail.*
import kotlinx.android.synthetic.main.fragment_play_history_detail.view.*
import java.util.*

class PlayHistoryDetailFragment:Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private lateinit var selectedPlayHistory: PlayHistoryEntity
    private val playHistoryFragmentArgs: PlayHistoryFragmentArgs by navArgs()
    private val createNewPlayHistoryFragmentArgs: CreateNewPlayHistoryFragmentArgs by navArgs()

    private var playHistoryDate: Long = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_play_history_detail, container, false)
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)

        //navArgsのうちnullでないほうの値をセット
        val selectedPlayHistoryId = playHistoryFragmentArgs.playHistoryId?.toInt()
                ?: createNewPlayHistoryFragmentArgs.createdPlayHistoryId?.toInt()
                ?: throw Exception("cannot get playHistoryId")
        selectedPlayHistory = playHistoryViewModel.getPlayHistoryById(selectedPlayHistoryId)


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
        root.btn_play_history_add_game_tentative.setOnClickListener {
            val action = PlayHistoryDetailFragmentDirections
                    .actionNavigationPlayHistoryDetailToNavigationPlayHistoryAddGame(selectedPlayHistoryId.toString())
            findNavController().navigate(action)

        }

        return root
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
                parentFragmentManager.popBackStack()
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

}