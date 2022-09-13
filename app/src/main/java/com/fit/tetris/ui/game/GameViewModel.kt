package com.fit.tetris.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fit.tetris.data.GameData

class GameViewModel: ViewModel() {
    var gameGrid: MutableLiveData<GameGrid> = MutableLiveData()
    var gameData: MutableLiveData<GameData> = MutableLiveData()

    fun createGameGrid() {
        if (gameData.value != null) {
            gameGrid.value = GameGrid(gameData.value!!.width, gameData.value!!.height)
        }
    }
}