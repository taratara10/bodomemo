package com.example.bodomemo.ui.playHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bodomemo.R
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.ui.PlayHistoryViewModel
import kotlinx.android.synthetic.main.fragment_create_new_game.*
import kotlinx.android.synthetic.main.fragment_create_new_game.et_new_play_history_title
import kotlinx.android.synthetic.main.fragment_create_new_game.til_new_play_history_title
import kotlinx.android.synthetic.main.fragment_create_new_play_history.*
import kotlinx.android.synthetic.main.fragment_create_new_play_history.view.*
import java.util.*

class PlayHistoryAddGameFragment: Fragment() {

    private lateinit var playHistoryViewModel: PlayHistoryViewModel
    private var playHistoryDate: Long = System.currentTimeMillis()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playHistoryViewModel = ViewModelProvider(this).get(PlayHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_new_play_history, container, false)

        //calendarView setting
        val calendarView = root.cv_play_history_calendar
        calendarView.date = playHistoryDate
        calendarView.setOnDateChangeListener(calendarListener)



        return root
    }


    override fun onStart() {
        super.onStart()
        btn_save_play_history.setOnClickListener {
            savePlayHistory()
        }
    }


    private val calendarListener = object : CalendarView.OnDateChangeListener{
        override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
            playHistoryDate = view.date //long

        }

    }

    private fun savePlayHistory() {
        if (validateFields()) {
            //一旦id = 0 でautoIncrementで設定
                //todo add date
            val newPlayHistory = PlayHistoryEntity(playHistoryId = 0, title = et_new_play_history_title.text.toString(),date = playHistoryDate )
            playHistoryViewModel.savePlayHistory(newPlayHistory)

            //@Insert したidの返り値を渡す
            val insertedGameId = playHistoryViewModel.insertedPlayHistoryId.toString()

            //todo add Direction
           // val action =
            //findNavController().navigate(action)
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