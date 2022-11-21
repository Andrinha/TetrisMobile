package com.fit.tetris.repository

import com.fit.tetris.data.record.Record
import com.fit.tetris.data.record.RecordDao

class RecordRepository(private val recordDao: RecordDao) {
    val readScoreRecords = recordDao.readScoreRecords()
    val readTimeRecords = recordDao.readTimeRecords()

    suspend fun addRecord(record: Record) {
        recordDao.addRecord(record)
    }
}