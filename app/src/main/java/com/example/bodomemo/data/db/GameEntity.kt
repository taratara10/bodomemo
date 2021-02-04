package com.example.bodomemo.data.db

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "gameEntity")
data class GameEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "title") val title: String = "",
        @ColumnInfo(name = "rating") val rating: Int = 0,
        @ColumnInfo(name = "todo_check") var todoCheck: Boolean = false,
        @ColumnInfo(name = "owned_check") val ownedCheck : Boolean = false,
        @ColumnInfo(name = "favorite_check") val favoriteCheck: Boolean = false,
)