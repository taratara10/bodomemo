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
        @ColumnInfo(name = "todo_position") var todoPosition: Int = 0,
        @ColumnInfo(name = "play_time") var playTime: Int = 0,
        @ColumnInfo(name = "game_memo") var gameMemo:String = "",
)

@Entity(tableName = "playHistoryEntity")
data class PlayHistoryEntity(
        @PrimaryKey(autoGenerate = true) val playHistoryId: Int,
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "date") var date: Long,
        @ColumnInfo(name = "memo") var playMemo: String = "",
)



@Entity(tableName = "playAndGameCrossRef")
data class PlayAndGameCrossRef(
        @PrimaryKey(autoGenerate = true) val referenceId: Int,
        val playHistoryId: Int,
        val gameId: Int,
)

data class PlayHistoryWithGames(
        @Embedded val playHistoryEntity: PlayHistoryEntity,
        @Relation(
                parentColumn = "playHistoryId",
                entityColumn = "gameId",
                associateBy = Junction( PlayAndGameCrossRef::class)
        )
        val gameList: List<GameEntity>
)

data class GamesWithPlayHistory(
        @Embedded val gameEntity: GameEntity,
        @Relation(
                parentColumn = "gameId",
                entityColumn = "playHistoryId",
                associateBy = Junction( PlayAndGameCrossRef::class)
        )
        val playHistoryList: List<PlayHistoryEntity>
)