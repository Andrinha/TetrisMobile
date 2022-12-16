package com.fit.tetris.ui.blockeditor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.ShapeRepository

class BlockEditorViewModel(application: Application) : AndroidViewModel(application) {
    fun invertTile(i: Int) {
        val t = tiles.value!!
        t[i / 4][i % 4] = t[i / 4][i % 4].not()
        tiles.value = t
    }

    var tiles: MutableLiveData<Array<BooleanArray>> = MutableLiveData(Array(4) { BooleanArray(4) })

    private val shapeDao = ShapeDatabase.getDatabase(application).shapeDao()
    private val shapeRepository = ShapeRepository(shapeDao)
    val shapesData: LiveData<List<Shape>> = shapeRepository.readAllData
}