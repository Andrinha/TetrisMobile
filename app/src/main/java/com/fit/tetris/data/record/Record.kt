package com.fit.tetris.data.record

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_table")
data class Record (
    @PrimaryKey(autoGenerate = true)
    val recordId: Int,
    val name: String,
    val score: Int,
    val time: Long,
    val type: Int
)