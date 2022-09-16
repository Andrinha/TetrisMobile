package com.fit.tetris.ui.game

class GameGrid(val width: Int, val height: Int) {
    private val grid: Array<IntArray> = Array(height) { IntArray(width) }

    operator fun get(x: Int, y: Int) = grid[y][x]
    operator fun set(x: Int, y: Int, value: Int) {
        grid[y][x] = value
    }

    private fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun isEmpty(x: Int, y: Int): Boolean {
        return isInside(x, y) && grid[y][x] == 0
    }

    private fun isRowFull(y: Int): Boolean {
        for (x in 0 until width) {
            if (grid[y][x] == 0) {
                return false
            }
        }
        return true
    }

    fun isRowEmpty(y: Int): Boolean {
        for (x in 0 until width) {
            if (grid[y][x] != 0) {
                return false
            }
        }
        return true
    }

    fun clearRow(y: Int) {
        for (x in 0 until width) {
            grid[y][x] = 0
        }
    }

    private fun moveRowDown(y: Int, numRows: Int) {
        for (x in 0 until width) {
            grid[y + numRows][x] = grid[y][x]
            grid[y][x] = 0
        }
    }

    fun clearFullRows(): Int {
        var cleared = 0

        for (y in height - 1 downTo 0) {
            if (isRowFull(y)) {
                clearRow(y)
                cleared++
            } else if (cleared > 0) {
                moveRowDown(y, cleared)
            }
        }

        return cleared
    }
}