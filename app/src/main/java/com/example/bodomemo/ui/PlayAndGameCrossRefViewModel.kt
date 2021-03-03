package com.example.bodomemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.PlayAndGameCrossRef

class PlayAndGameCrossRefViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : GameRepository = GameRepository(application)

    fun savePlayedGame(crossRef: PlayAndGameCrossRef) {
        repository.savePlayedGame(crossRef)
    }

    fun deletePlayedGame(crossRef: PlayAndGameCrossRef){
        repository.deletePlayedGame(crossRef)
    }

}