package com.fit.tetris.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.ShapeRepository

class AdminViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Shape>>
    val selected = MutableLiveData<ArrayList<Boolean>>(arrayListOf())

    private val repository: ShapeRepository

    init {
        val userDao = ShapeDatabase.getDatabase(application).shapeDao()
        repository = ShapeRepository(userDao)
        readAllData = repository.readAllData
//        readAllData.value!!.forEach { _ ->
//            selected.value!!.add(false)
//        }
    }
}