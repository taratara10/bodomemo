package com.example.bodomemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.GamesWithPlayHistory
import com.example.bodomemo.data.db.PlayHistoryEntity
import com.example.bodomemo.data.db.PlayHistoryWithGames
import java.text.SimpleDateFormat
import java.util.*

class PlayHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : GameRepository = GameRepository(application)
    private val allPlayHistory : LiveData<List<PlayHistoryEntity>> = repository.getAllPlayHistory()
    var insertedPlayHistoryId: Long = 0

    fun savePlayHistory(playHistory: PlayHistoryEntity){
        insertedPlayHistoryId = repository.savePlayHistory(playHistory)
    }

    fun updatePlayHistory(playHistory: PlayHistoryEntity){
        repository.updatePlayHistory(playHistory)
    }

    fun deletePlayHistory(playHistory: PlayHistoryEntity){
        repository.deletePlayHistory(playHistory)
    }

    fun getAllPlayHistory() : LiveData<List<PlayHistoryEntity>> {
        return allPlayHistory
    }

    fun getPlayHistoryById(playHistoryId: Int?): PlayHistoryEntity{
        return repository.getPlayHistoryById(playHistoryId)
    }


    fun getPlayedGameById(playHistoryId: Int?): LiveData<PlayHistoryWithGames>{
        return repository.getPlayedGameById(playHistoryId)
    }

    fun getGameWithPlayById(gameId:Int?) : LiveData<GamesWithPlayHistory>{
        return repository.getGameWithPlayById(gameId)
    }


    //yyyy/MM/dd -> MilliSec
    fun convertDateToMilliSec(date: String): Long {
        val dataFormat = SimpleDateFormat("yyyy/MM/dd")
        val parsedDate = dataFormat.parse(date)
        return parsedDate.time
    }

    //MilliSec -> yyyy/MM/dd
    fun convertMilliSecToDate(milliSec: Long): String{
        val calender = Calendar.getInstance()
        calender.timeInMillis = milliSec
        val dataFormat = SimpleDateFormat("yyyy/MM/dd")
        return dataFormat.format(calender.time)
    }
}