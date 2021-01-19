package com.example.bodomemo.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "gameEntity")
data class GameEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "boardGame") val boardGame: String?,
        @ColumnInfo(name = "todo_check") val todoCheck: Int?,
)