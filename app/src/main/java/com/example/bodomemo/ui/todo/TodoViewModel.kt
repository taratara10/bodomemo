package com.example.bodomemo.ui.todo

import android.app.Application
import androidx.lifecycle.*
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.GameEntity

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : GameRepository = GameRepository(application)
    private val allGameList : LiveData<List<GameEntity>> = repository.getAllGamaList()

    fun saveGame(game : GameEntity){
        repository.saveGame(game)
    }

    fun updateGame(game : GameEntity){
        repository.updateGame(game)
    }

    fun deleteGame(game : GameEntity){
        repository.deleteGame(game)
    }

    fun getAllGameList() : LiveData<List<GameEntity>> {
        return allGameList
    }


//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
}