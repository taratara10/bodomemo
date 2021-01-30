package com.example.bodomemo.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.bodomemo.data.db.GameDAO
import com.example.bodomemo.data.db.GameDatabase
import com.example.bodomemo.data.db.GameEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameRepository(application: Application) {

    private val gameDAO: GameDAO
    private val allGames:LiveData<List<GameEntity>>
    private val todoList:LiveData<List<GameEntity>>

    init {
    // Create DB Instance
        val database = GameDatabase.getInstance(application.applicationContext)
        gameDAO = database!!.gameDao()
        allGames = gameDAO.getAllGameList()
        todoList = gameDAO.getTodoList()
    }

    fun saveGame(game: GameEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            gameDAO.saveGame(game)
        }
    }

    fun deleteGame(game: GameEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            gameDAO.deleteGame(game)
        }
    }

    fun updateGame(game: GameEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            gameDAO.updateGame(game)
        }
    }

    fun getAllGamaList(): LiveData<List<GameEntity>> {
        return allGames
    }

    fun getTodoList():  LiveData<List<GameEntity>> {
        return todoList
    }
}