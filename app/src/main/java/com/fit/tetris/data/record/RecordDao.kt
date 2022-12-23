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

    @Query("SELECT * FROM record_table WHERE type = 0 ORDER BY score DESC LIMIT 10")
    fun readScoreRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM record_table WHERE type = 1 ORDER BY time DESC LIMIT 10")
    fun readTimeRecords(): LiveData<List<Record>>
}