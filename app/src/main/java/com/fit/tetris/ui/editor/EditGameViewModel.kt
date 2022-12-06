package com.fit.tetris.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fit.tetris.data.difficulty.Difficulty
import com.fit.tetris.data.difficulty.DifficultyDatabase
import com.fit.tetris.repository.DifficultyRepository

class EditGameViewModel(application: Application) : AndroidViewModel(application) {
//    var name: MutableLiveData<String> = MutableLiveData()
//    var width: MutableLiveData<Int> = MutableLiveData()
//    var height: MutableLiveData<Int> = MutableLiveData()
//    var speed: MutableLiveData<Int> = MutableLiveData()

    val password = "1111"

    private val difficultyDao = DifficultyDatabase.getDatabase(application).difficultyDao()
    private val difficultyRepository = DifficultyRepository(difficultyDao)
    val difficultyData: LiveData<List<Difficulty>> = difficultyRepository.readAllData

}