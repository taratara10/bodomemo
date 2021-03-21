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
    private val todoList:LiveData<MutableList<GameEntity>>
    private val allPlayHistory:LiveData<List<PlayHistoryEntity>>


    init {
    // Create DB Instance
        val database = GameDatabase.getInstance(application.applicationContext)
        gameDao = database!!.gameDao()
        allGames = gameDao.getAllGameList()
        todoList = gameDao.getTodoList()
        allPlayHistory = gameDao.getAllPlayHistory()

    }

    /**
     * GameEntity
     * */

    fun saveGame(game: GameEntity): Long = runBlocking {
         gameDao.saveGame(game)
    }

    fun deleteGame(game: GameEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            gameDao.deleteGame(game)
        }
    }

    fun updateGame(game: GameEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            gameDao.updateGame(game)
        }
    }

    fun getAllGamaList(): LiveData<List<GameEntity>> {
        return allGames
    }

    fun getTodoList():  LiveData<MutableList<GameEntity>> {
        return todoList
    }

    fun getGameById(gameId:Int?): GameEntity  = runBlocking{
         gameDao.getGameById(gameId)
    }

    /**
     * PlayHistory
     * */

    fun savePlayHistory(record: PlayHistoryEntity): Long = runBlocking {
        gameDao.savePlayHistory(record)
    }

    fun deletePlayHistory(record: PlayHistoryEntity) = runBlocking {
        gameDao.deletePlayHistory(record)
    }

    fun updatePlayHistory(record: PlayHistoryEntity) = runBlocking {
        gameDao.updatePlayHistory(record)
    }

    fun getAllPlayHistory(): LiveData<List<PlayHistoryEntity>>{
        return allPlayHistory
    }
    fun getPlayHistoryById(playHistoryId: Int?): PlayHistoryEntity = runBlocking {
        gameDao.getPlayHistoryById(playHistoryId)
    }

    /**
     * playAndGameCrossRef
     * */

    fun savePlayedGame(ref: PlayAndGameCrossRef) = runBlocking{
        gameDao.savePlayedGame(ref)
    }

    fun deletePlayedGame(playHistoryId: Int?) = runBlocking{
        gameDao.deletePlayedGame(playHistoryId)
    }

    /**
     * playWithGames
     * */

    fun getPlayedGameById(playHistoryId: Int?): LiveData<PlayHistoryWithGames> = runBlocking {
        gameDao.getPlayedGameById(playHistoryId)
    }
}