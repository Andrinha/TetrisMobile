package com.fit.tetris.data.difficulty

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DifficultyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecord(record: Difficulty)

    @Query("SELECT * FROM difficulty_table")
    fun readAllData(): LiveData<List<Difficulty>>
}