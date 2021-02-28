package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bodomemo.R
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import com.example.bodomemo.ui.search.CreateNewGameFragmentDirections
import kotlinx.android.synthetic.main.fragment_create_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_play_history.*
import kotlinx.android.synthetic.main.fragment_create_new_play_history.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class CreateNewPlayHistoryFragment: Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private var playHistoryDate = System.currentTimeMillis()
    private lateinit var editTextDate:EditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_new_play_history, container, false)

        editTextDate = root.et_create_play_date

        //calendarView setting
        val calendarView = root.cv_play_history_calendar
        calendarView.date = playHistoryDate
        calendarView.setOnDateChangeListener(calendarListener)
        root.et_create_play_date.setText(playHistoryViewModel.convertMilliSecToDate(playHistoryDate))

        root.btn_save_play_history.setOnClickListener {savePlayHistory()}

        return root
    }

    //
    private val calendarListener = CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
        val dateString = "${year}/${month+1}/${dayOfMonth}"
        playHistoryDate = playHistoryViewModel.convertDateToMilliSec(dateString)
        editTextDate.setText(dateString)
    }


    private fun savePlayHistory() {
        if (validateFields()) {
            //一旦id = 0 でautoIncrementで設定
            val newPlayHistory = PlayHistoryEntity(playHistoryId = 0, title = et_new_play_history_title.text.toString(),date = playHistoryDate )
            playHistoryViewModel.savePlayHistory(newPlayHistory)
            //@Insert したidの返り値を渡す
            val insertedPlayHistoryId = playHistoryViewModel.insertedPlayHistoryId.toString()
            val action = CreateNewPlayHistoryFragmentDirections.actionNavigationCreatePlayHistoryToNavigationPlayHistoryDetail(insertedPlayHistoryId)
            findNavController().navigate(action)

            //todo 追加したあと、popBackで戻りたくない

        }
    }

    /**
     * Validation of EditText
     * */
    private fun validateFields(): Boolean {
        if (et_new_play_history_title.text?.isEmpty() == true) {
            til_new_play_history_title.error = "pleaseEnterTitle"
            et_new_play_history_title.requestFocus()
            return false
        }
        return true
    }

}
