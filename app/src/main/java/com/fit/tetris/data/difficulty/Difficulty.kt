package com.fit.tetris.data.difficulty

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fit.tetris.utils.IntTypeConverter

@Entity(tableName = "difficulty_table")
data class Difficulty (
    @PrimaryKey(autoGenerate = true)
    val difficultyId: Int,
    val name: String,
    val width: Int,
    val height: Int,
    @field:TypeConverters(IntTypeConverter::class)
    val shapes : List<Int> = listOf()
)