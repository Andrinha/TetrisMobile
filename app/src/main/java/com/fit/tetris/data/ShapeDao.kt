package com.fit.tetris.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShapeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addShape(shape: Shape)

    @Query("SELECT * FROM shape_table")
    fun readAllData(): LiveData<List<Shape>>
}