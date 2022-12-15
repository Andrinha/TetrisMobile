package com.fit.tetris.data.shape

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shape_table")
data class Shape(
    @PrimaryKey(autoGenerate = true)
    val shapeId: Int,
    val tiles: Int,
    var selected: Boolean = false,
    val isBase: Boolean = false,
    var r: Int = 0,
    var g: Int = 0,
    var b: Int = 0
)