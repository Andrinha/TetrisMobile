package com.fit.tetris.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.ShapeRepository

class BottomViewModel(application: Application) : AndroidViewModel(application) {

    private val shapeDao = ShapeDatabase.getDatabase(application).shapeDao()
    private val shapeRepository = ShapeRepository(shapeDao)
    val shapesData: LiveData<List<Shape>> = shapeRepository.readAllData
    var shapes: List<Shape> = mutableListOf()
}
