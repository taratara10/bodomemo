package com.example.bodomemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.PlayHistoryEntity

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

}