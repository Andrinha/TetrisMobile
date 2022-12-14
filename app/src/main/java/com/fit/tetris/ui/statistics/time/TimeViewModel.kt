package com.fit.tetris.ui.statistics.time

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fit.tetris.data.record.Record
import com.fit.tetris.data.record.RecordDatabase
import com.fit.tetris.repository.RecordRepository

class TimeViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = RecordDatabase.getDatabase(application).recordDao()
    private val repository: RecordRepository = RecordRepository(userDao)
    val readAllData: LiveData<List<Record>> = repository.readTimeRecords

}