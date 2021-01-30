package com.example.bodomemo.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.bodomemo.data.GameRepository
import com.example.bodomemo.data.db.GameEntity

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : GameRepository = GameRepository(application)
    private val allGameList : LiveData<List<GameEntity>> = repository.getAllGamaList()
    private val todoList : LiveData<List<GameEntity>> = repository.getTodoList()

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

    fun getTodoList() : LiveData<List<GameEntity>> {
        return todoList
    }

}