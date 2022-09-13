package com.fit.tetris.data

abstract class Block {
    abstract val tiles: Array<BooleanArray>

    fun rotateCW() {
        for (i in 0 until tiles.size / 2) {
            for (j in i until tiles.size - i - 1) {
                val temp: Boolean = tiles[i][j]
                tiles[i][j] = tiles[tiles.size - 1 - j][i]
                tiles[tiles.size - 1 - j][i] = tiles[tiles.size - 1 - i][tiles.size - 1 - j]
                tiles[tiles.size - 1 - i][tiles.size - 1 - j] = tiles[j][tiles.size - 1 - i]
                tiles[j][tiles.size - 1 - i] = temp
            }
        }
    }
    fun rotateCCW() {
        repeat(3) {
            rotateCW()
        }
    }
}