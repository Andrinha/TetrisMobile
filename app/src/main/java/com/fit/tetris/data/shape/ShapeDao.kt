package com.fit.tetris.data.shape

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShapeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShape(shape: Shape)

    @Query("SELECT * FROM shape_table")
    fun readAllData(): LiveData<List<Shape>>

    @Query("DELETE FROM shape_table WHERE shapeId = :id")
    fun deleteItem(id: Int)
}