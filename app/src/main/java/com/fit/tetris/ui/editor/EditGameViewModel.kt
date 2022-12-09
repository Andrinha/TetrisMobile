package com.fit.tetris.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fit.tetris.data.difficulty.Difficulty
import com.fit.tetris.data.difficulty.DifficultyDatabase
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.repository.DifficultyRepository
import com.fit.tetris.repository.ShapeRepository

class EditGameViewModel(application: Application) : AndroidViewModel(application) {
    val password = "1111"

    private val difficultyDao = DifficultyDatabase.getDatabase(application).difficultyDao()
    private val difficultyRepository = DifficultyRepository(difficultyDao)
    val difficultyData: LiveData<List<Difficulty>> = difficultyRepository.readAllData

    private val shapeDao = ShapeDatabase.getDatabase(application).shapeDao()
    private val shapeRepository = ShapeRepository(shapeDao)
    val shapesData: LiveData<List<Shape>> = shapeRepository.readAllData

    val selectedDifficulty = MutableLiveData("")
    val selected: MutableLiveData<List<Boolean>> = MutableLiveData(mutableListOf())
    val selectedDifficultyItem: MutableLiveData<Difficulty> = MutableLiveData()
}