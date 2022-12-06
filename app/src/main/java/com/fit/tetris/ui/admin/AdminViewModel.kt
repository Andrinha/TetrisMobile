package com.fit.tetris.ui.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fit.tetris.data.difficulty.Difficulty
import com.fit.tetris.data.difficulty.DifficultyDatabase
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.DifficultyRepository
import com.fit.tetris.repository.ShapeRepository

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    val selected: MutableLiveData<List<Boolean>> = MutableLiveData(mutableListOf())
    val isDataReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    val selectedDifficulty = MutableLiveData("")

    private val shapeDao = ShapeDatabase.getDatabase(application).shapeDao()
    private val shapeRepository = ShapeRepository(shapeDao)
    val shapesData: LiveData<List<Shape>> = shapeRepository.readAllData

    private val difficultyDao = DifficultyDatabase.getDatabase(application).difficultyDao()
    private val difficultyRepository = DifficultyRepository(difficultyDao)
    val difficultyData: LiveData<List<Difficulty>> = difficultyRepository.readAllData
}