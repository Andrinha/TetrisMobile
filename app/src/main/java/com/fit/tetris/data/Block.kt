package com.fit.tetris.data

import java.util.*

class Block(var tiles: Array<BooleanArray>) {
    var offset: Position = Position(0, 0)
    var r = 0
    var g = 0
    var b = 0

    init {
        while (r + g + b < 256) {
            r = kotlin.math.abs(Random().nextInt(256))
            g = kotlin.math.abs(Random().nextInt(256))
            b = kotlin.math.abs(Random().nextInt(256))
        }
    }

    fun rotateCW() {
        repeat(3) {
            rotateCCW()
        }
    }

    fun rotateCCW() {
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

    fun move(x: Int, y: Int) {
        this.offset.x += x
        this.offset.y += y
    }

    fun reset(width: Int) {
        offset = Position(width / 2 - tiles.size / 2, 0)
    }

    fun isClosed(): Boolean {
        var t = 0
        for(i in tiles.indices) {
            for(j in tiles.indices) {
                if (tiles[i][j]) {
                    var k = 0
                    if (i - 1 >= 0 && tiles[i-1][j]) k++
                    if (i + 1 < tiles.size && tiles[i+1][j]) k++
                    if (j - 1 >= 0 && tiles[i][j-1]) k++
                    if (j + 1 < tiles.size && tiles[i][j+1]) k++
                    if (k == 0) return false
                    if (k == 4) tiles[i][j] = true
                    t++
                }
            }
        }
        return t > 0
    }

    fun tilePositions(): MutableList<Position> {
        val list: MutableList<Position> = LinkedList()
        for(i in tiles.indices) {
            for (j in tiles.indices) {
                if (tiles[i][j]) {
                    list.add(Position(this.offset.x + i, this.offset.y + j))
                }
            }
        }
        return list
    }
}