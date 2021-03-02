package com.example.bodomemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.PlayAndGameCrossRef

class PlayAndGameCrossRefViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : GameRepository = GameRepository(application)

    fun savePlayedGame(game: PlayAndGameCrossRef) {
        repository.savePlayedGame(game)
    }

    fun deletePlayedGame(game: PlayAndGameCrossRef){
        repository.deletePlayedGame(game)
    }

}