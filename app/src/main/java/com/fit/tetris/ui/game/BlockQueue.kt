package com.fit.tetris.ui.game

import com.fit.tetris.data.Block
import com.fit.tetris.utils.toBooleanArray
import kotlin.random.Random

class BlockQueue(private val shapes: List<Int>) {

    var nextBlock: Block = tetrisBlock()
    private var block: Block = nextBlock
    private var prevId = 0

    private fun randomBlock(size: Int): Block {
        val tiles: Array<BooleanArray> = Array(size) { BooleanArray(size) }
        val block = Block(tiles)
        while (!block.isClosed()) {
            for (i in 0 until size) {
                for (j in 0 until size) {
                    tiles[i][j] = Random.nextBoolean()
                }
            }
            block.tiles = tiles
        }
        return block
    }

    private fun tetrisBlock(): Block {
        var id: Int
        do {
            id = Random.nextInt(0, shapes.size)
        } while (id == prevId)
        prevId = id

        val tiles = shapes[id]

        return Block(tiles.toBooleanArray())
    }

    fun getNext(width: Int): Block {
        block = nextBlock
        //nextBlock = randomBlock(size)
        nextBlock = tetrisBlock()
        block.reset(width)
        return block
    }
}
