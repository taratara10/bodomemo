package com.example.bodomemo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameDao {

    //GameList
    @Insert
    suspend fun saveGame(gameEntity: GameEntity): Long

    @Delete
    suspend fun deleteGame(gameEntity: GameEntity)

    @Update
    suspend fun updateGame(gameEntity: GameEntity)

    @Query("SELECT * FROM gameEntity ORDER BY gameId DESC")
    abstract fun getAllGameList(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM gameEntity WHERE todo_check = 1")
    abstract fun getTodoList(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM gameEntity WHERE gameId =:gameId")
    suspend fun getGameById(gameId:Int?): GameEntity

    @Insert
    suspend fun savePlayHistory(playHistoryEntity: PlayHistoryEntity): Long

    @Delete
    suspend fun deletePlayHistory(playHistoryEntity: PlayHistoryEntity)

    @Update
    suspend fun updatePlayHistory(playHistoryEntity: PlayHistoryEntity)

    @Query("SELECT * FROM playHistoryEntity ORDER BY playHistoryId DESC")
    abstract fun getAllPlayHistory(): LiveData<List<PlayHistoryEntity>>



}