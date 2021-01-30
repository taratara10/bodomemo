package com.example.bodomemo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GameEntity::class], version = 2)
abstract class GameDatabase: RoomDatabase() {

    abstract fun gameDao(): GameDAO

    companion object {
        private var INSTANCE: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase? {
            if (INSTANCE == null) {
                synchronized(GameDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                            GameDatabase::class.java,
                            "game_db")
                            .build()
                }
            }
            return INSTANCE
        }
    }

}