package com.fit.tetris.data.shape

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shape::class], version = 1, exportSchema = false)
abstract class ShapeDatabase: RoomDatabase() {

    abstract fun shapeDao(): ShapeDao

    companion object {
        @Volatile
        private var INSTANCE: ShapeDatabase? = null

        fun getDatabase(context: Context): ShapeDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShapeDatabase::class.java,
                    "shape_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}