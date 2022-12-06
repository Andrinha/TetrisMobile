package com.fit.tetris.data.difficulty

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Difficulty::class], version = 1, exportSchema = false)
abstract class DifficultyDatabase: RoomDatabase() {

    abstract fun difficultyDao(): DifficultyDao

    companion object {
        @Volatile
        private var INSTANCE: DifficultyDatabase? = null

        fun getDatabase(context: Context): DifficultyDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DifficultyDatabase::class.java,
                    "difficulty_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}