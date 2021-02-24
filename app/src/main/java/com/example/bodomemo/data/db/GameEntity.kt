package com.example.bodomemo.data.db

import androidx.room.*


@Entity(tableName = "gameEntity")
data class GameEntity(
        @PrimaryKey(autoGenerate = true) val gameId: Int,
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "rating") var rating: Int = 0,
        @ColumnInfo(name = "todo_check") var todoCheck: Boolean = false,
        @ColumnInfo(name = "owned_check") var ownedCheck: Boolean = false,
        @ColumnInfo(name = "favorite_check") var favoriteCheck: Boolean = false,
)

@Entity(tableName = "playHistoryEntity")
data class PlayHistoryEntity(
        @PrimaryKey(autoGenerate = true) val playHistoryId: Int,
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "date") var date: Long,
)



@Entity(tableName = "playHistoryAndGameCrossReference",
        primaryKeys = ["playHistoryId","gameId"])
data class PlayHistoryAndGameCrossReference(
        val playHistoryId: Int,
        val gameId: Int
)

data class PlayHistoryWithGames(
        @Embedded val playHistoryEntity: PlayHistoryEntity,
        @Relation(
                parentColumn = "playHistoryId",
                entityColumn = "gameId",
                associateBy = Junction(PlayHistoryAndGameCrossReference::class)
        )
        val gameList: List<GameEntity>
)

data class GamesWithPlayHistory(
        @Embedded val gameEntity: GameEntity,
        @Relation(
                parentColumn = "gameId",
                entityColumn = "playHistoryId",
                associateBy = Junction(PlayHistoryAndGameCrossReference::class)
        )
        val playHistoryList: List<PlayHistoryEntity>
)