package com.example.bodomemo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameDao {

    /**
     * GameEntity
     **/
    @Insert
    suspend fun saveGame(gameEntity: GameEntity): Long

    @Delete
    suspend fun deleteGame(gameEntity: GameEntity)

    @Update
    suspend fun updateGame(gameEntity: GameEntity)

    @Query("SELECT * FROM gameEntity ORDER BY title DESC")
    abstract fun getAllGameList(): LiveData<MutableList<GameEntity>>

    @Query("SELECT * FROM gameEntity WHERE todo_check = 1 ORDER BY todo_position ASC")
    abstract fun getTodoList(): LiveData<MutableList<GameEntity>>

    @Query("SELECT * FROM gameEntity WHERE gameId =:gameId")
    suspend fun getGameById(gameId:Int?): GameEntity

    /**
     * PlayHistory
     **/
    @Insert
    suspend fun savePlayHistory(playHistoryEntity: PlayHistoryEntity): Long

    @Delete
    suspend fun deletePlayHistory(playHistoryEntity: PlayHistoryEntity)

    @Update
    suspend fun updatePlayHistory(playHistoryEntity: PlayHistoryEntity)

    @Query("SELECT * FROM playHistoryEntity ORDER BY date DESC")
    abstract fun getAllPlayHistory(): LiveData<MutableList<PlayHistoryEntity>>

    @Query("SELECT * FROM playHistoryEntity WHERE playHistoryId = :playHistoryId")
    suspend fun getPlayHistoryById(playHistoryId:Int?): PlayHistoryEntity

    /**
     * playAndGameCrossRef
     **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePlayedGame(playAndGameCrossRef:  PlayAndGameCrossRef)

    @Query("DELETE FROM playAndGameCrossRef WHERE playHistoryId = :playHistoryId")
    suspend fun deleteAllPlayedGameById(playHistoryId: Int?)



    /**
     * PlayWithGame
     **/
    @Transaction
    @Query("SELECT * FROM playHistoryEntity WHERE playHistoryId = :playHistoryId")
    fun getPlayedGameById(playHistoryId:Int?): LiveData<PlayHistoryWithGames>


    /**
     * GameWithPlay
     **/
    @Transaction
    @Query("SELECT * FROM gameEntity WHERE gameId = :gameId")
    fun getGameWithPlayById(gameId: Int?): LiveData<GamesWithPlayHistory>

    @Transaction
    @Query("SELECT * FROM gameEntity ")
    fun getAllGameWithPlayList():LiveData<MutableList<GamesWithPlayHistory>>





}