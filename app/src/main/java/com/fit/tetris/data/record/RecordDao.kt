package com.fit.tetris.data.record

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecord(record: Record)

    @Query("SELECT * FROM record_table WHERE type = 1")
    fun readScoreRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM record_table WHERE type = 0")
    fun readTimeRecords(): LiveData<List<Record>>
}