package com.fit.tetris.data.shape

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "shape_table")
data class Shape (
    @PrimaryKey(autoGenerate = true)
    val shapeId: Int,
    val name: String,
    val tiles: Int
)