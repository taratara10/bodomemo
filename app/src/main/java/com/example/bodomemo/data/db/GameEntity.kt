package com.example.bodomemo.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "gameEntity")
data class GameEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "rating") var rating: Int = 0,
        @ColumnInfo(name = "todo_check") var todoCheck: Boolean = false,
        @ColumnInfo(name = "owned_check") var ownedCheck: Boolean = false,
        @ColumnInfo(name = "favorite_check") var favoriteCheck: Boolean = false,
)