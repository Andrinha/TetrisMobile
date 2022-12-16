package com.fit.tetris.ui.game

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fit.tetris.data.Block
import com.fit.tetris.data.GameData
import com.fit.tetris.data.Position
import com.fit.tetris.utils.BaseShapes
import kotlin.math.min

class GameViewModel: ViewModel() {
    var gameGrid: MutableLiveData<GameGrid> = MutableLiveData()
    var gameData: MutableLiveData<GameData> = MutableLiveData()
    var blockQueue: MutableLiveData<BlockQueue> = MutableLiveData()
    var currentBlock: MutableLiveData<Block> = MutableLiveData()
    var score: MutableLiveData<Int> = MutableLiveData(0)
    var linesCleared: MutableLiveData<Int> = MutableLiveData(0)
    var placedBlock: MutableLiveData<Block> = MutableLiveData()
    var isGameOver: MutableLiveData<Boolean> = MutableLiveData(false)

    var startTime = MutableLiveData<Long>()
    var isRunning = false

    fun createGameGrid() {
        if (gameData.value != null) {
            create()
        }
    }

    private fun blockFits(): Boolean {
        currentBlock.value!!.tilePositions().forEach {
            if (!gameGrid.value!!.isEmpty(it.x, it.y))
                return false
        }
        return true
    }

    fun rotateBlockCW() {
        currentBlock.value!!.rotateCW()

        if (!blockFits())
            currentBlock.value!!.rotateCCW()
    }

    fun rotateBlockCCW() {
        currentBlock.value!!.rotateCCW()

        if (!blockFits())
            currentBlock.value!!.rotateCW()
    }

    fun moveBlockLeft() {
        currentBlock.value!!.move(-1, 0)
        if (!blockFits())
            currentBlock.value!!.move(1, 0)
    }

    fun moveBlockRight() {
        currentBlock.value!!.move(1, 0)
        if (!blockFits())
            currentBlock.value!!.move(-1, 0)
    }

    private fun isGameOver(): Boolean {
        return !(gameGrid.value!!.isRowEmpty(0) && gameGrid.value!!.isRowEmpty(1))
    }

    private fun placeBlock() {
        placedBlock.value = currentBlock.value
        currentBlock.value!!.tilePositions().forEach {
            gameGrid.value!![it.x, it.y] =
                Color.rgb(currentBlock.value!!.r, currentBlock.value!!.g, currentBlock.value!!.b)
        }
        val cleared = gameGrid.value!!.clearFullRows()
        score.value = score.value!! + cleared * cleared * gameGrid.value!!.width *
                gameData.value!!.difficulty.speed * gameData.value!!.difficulty.speed * (linesCleared.value!! + 1)

        linesCleared.value = linesCleared.value!! + cleared

        if (isGameOver() && !isGameOver.value!!) {
            isGameOver.value = true
        } else {
            currentBlock.value = blockQueue.value!!.getNext(gameGrid.value!!.width)
        }
    }

    fun moveBlockDown() {
        currentBlock.value!!.move(0, 1)
        if (!blockFits()) {
            currentBlock.value!!.move(0, -1)
            placeBlock()
        }
    }

    private fun tileDropDistance(p: Position): Int {
        var drop = 0
        while (gameGrid.value!!.isEmpty(p.x, p.y + drop + 1)) {
            drop++
        }
        return drop
    }

    fun blockDropDistance(): Int {
        var drop = gameGrid.value!!.height
        currentBlock.value!!.tilePositions().forEach {
            drop = min(drop, tileDropDistance(it))
        }
        return drop
    }

    fun moveDropBlock() {
        currentBlock.value!!.move(0, blockDropDistance())
        placeBlock()
    }

    fun create() {
        gameGrid.value = GameGrid(gameData.value!!.difficulty.width, gameData.value!!.difficulty.height)
        blockQueue.value = BlockQueue(gameData.value!!.difficulty.shapes + BaseShapes().list)
        currentBlock.value = blockQueue.value!!.getNext(gameGrid.value!!.width)
        score.value = 0
        linesCleared.value = 0
        isGameOver.value = false
        isRunning = false
    }
}