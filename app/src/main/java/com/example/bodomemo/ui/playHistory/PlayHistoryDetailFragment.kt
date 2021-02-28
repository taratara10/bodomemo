package com.example.bodomemo.ui.playHistory

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import kotlinx.android.synthetic.main.fragment_play_history_detail.*
import kotlinx.android.synthetic.main.fragment_play_history_detail.view.*
import java.util.*

class PlayHistoryDetailFragment:Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private lateinit var selectedPlayHistory: PlayHistoryEntity

    private val playHistoryFragmentArgs: PlayHistoryFragmentArgs by navArgs()
    private val createNewPlayHistoryFragmentArgs: CreateNewPlayHistoryFragmentArgs by navArgs()

    private lateinit var dateEditText: EditText
    private lateinit var playHistoryTitle: String
    private var playHistoryDate: Long = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_play_history_detail, container, false)
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)

        //navArgsのうちnullでないほうの値をセット
        val selectedPlayHistoryId = playHistoryFragmentArgs.playHistoryId?.toInt()
                ?: createNewPlayHistoryFragmentArgs.createdPlayHistoryId?.toInt()
                ?: throw Exception("cannot get playHistoryId")
        selectedPlayHistory = playHistoryViewModel.getPlayHistoryById(selectedPlayHistoryId)


        //title editText
        playHistoryTitle = selectedPlayHistory.title
        val titleEditText = root.et_play_title_detail
        titleEditText.setText(playHistoryTitle)
        titleEditText.addTextChangedListener(object :CustomTextWatcher{
            override fun afterTextChanged(s: Editable?) {
                selectedPlayHistory.title = s.toString()
                updatePlayHistory(selectedPlayHistory)
            }
        })

        //Date EditText
        playHistoryDate = selectedPlayHistory.date
        dateEditText = root.et_play_date_select_detail
        dateEditText.setText(playHistoryViewModel.convertMilliSecToDate(playHistoryDate))

        //DatePick
        dateEditText.setOnClickListener{
            showDatePicker()
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
                    dateEditText.setText(playHistoryViewModel.convertMilliSecToDate(playHistoryDate))
                    updatePlayHistory(selectedPlayHistory)
                },
                year,month,day
                )
        datePickerDialog.show()
    }

    fun updatePlayHistory(playHistoryEntity: PlayHistoryEntity) {
        if (playHistoryTitle.trim().isNotEmpty()) playHistoryViewModel.updatePlayHistory(playHistoryEntity)
    }

    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}