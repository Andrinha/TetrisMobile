package com.fit.tetris.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fit.tetris.data.Shape
import com.fit.tetris.data.ShapeDatabase
import com.fit.tetris.repository.ShapeRepository

class AdminViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Shape>>
    private val repository: ShapeRepository

    init {
        val userDao = ShapeDatabase.getDatabase(application).shapeDao()
        repository = ShapeRepository(userDao)
        readAllData = repository.readAllData
    }
}