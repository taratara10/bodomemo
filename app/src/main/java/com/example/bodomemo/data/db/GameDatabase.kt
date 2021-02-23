package com.example.bodomemo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GameEntity::class], version = 4)
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

//@Database(entities = [PlayHistoryEntity::class], version = 2)
//abstract class PlayHistoryDatabase: RoomDatabase() {
//
//    abstract fun gameDao(): GameDAO
//
//    companion object {
//        private var INSTANCE: PlayHistoryDatabase? = null
//
//        fun getInstance(context: Context): PlayHistoryDatabase? {
//            if (INSTANCE == null) {
//                synchronized(PlayHistoryDatabase::class) {
//                    INSTANCE = Room.databaseBuilder(context,
//                            PlayHistoryDatabase::class.java,
//                            "play_history_db")
//                            .build()
//                }
//            }
//            return INSTANCE
//        }
//    }
//
//}