package com.example.bodomemo.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.bodomemo.data.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameRepository(application: Application) {

    private val gameDao: GameDao
    private val allGames:LiveData<List<GameEntity>>
    private val todoList:LiveData<List<GameEntity>>
    private val allPlayHistory:LiveData<List<PlayHistoryEntity>>


    init {
    // Create DB Instance
        val database = GameDatabase.getInstance(application.applicationContext)
        gameDao = database.gameDao()
        allGames = gameDao.getAllGameList()
        todoList = gameDao.getTodoList()

        allPlayHistory = gameDao.getAllPlayHistory()

    }

    /**
     * Game List
     * */

    fun saveGame(game: GameEntity): Long = runBlocking {
         gameDAO.saveGame(game)
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

    fun getGameById(gameId:Int?): GameEntity  = runBlocking{
         gameDAO.getGameById(gameId)
    }

    /**
     * PlayHistory
     * */

    fun savePlayHistory(record: PlayHistoryEntity): Long = runBlocking {
        gameDAO.savePlayHistory(record)
    }

    fun deletePlayHistory(record: PlayHistoryEntity) = runBlocking {
        gameDAO.deletePlayHistory(record)
    }

    fun updatePlayHistory(record: PlayHistoryEntity) = runBlocking {
        gameDAO.updatePlayHistory(record)
    }

    fun getAllPlayHistory(): LiveData<List<PlayHistoryEntity>>{
        return allPlayHistory
    }
}