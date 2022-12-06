package com.fit.tetris.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.ShapeRepository

class AdminViewModel(application: Application): AndroidViewModel(application) {
    val selected: MutableLiveData<List<Boolean>> = MutableLiveData(mutableListOf())
    val isDataReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    private val userDao = ShapeDatabase.getDatabase(application).shapeDao()
    private val repository = ShapeRepository(userDao)
    val shapesData: LiveData<List<Shape>> = repository.readAllData
}