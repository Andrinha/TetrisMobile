package com.fit.tetris.ui.game

import com.fit.tetris.data.Block
import kotlin.random.Random

class BlockQueue {

    var nextBlock: Block = tetrisBlock()
    private var block: Block = nextBlock
    private var prevId = 0

    private fun randomBlock(size: Int): Block {
        val tiles: Array<BooleanArray> = Array(size) {BooleanArray(size)}
        val block = Block(tiles)
        while (!block.isClosed()) {
            for(i in 0 until size) {
                for(j in 0 until size) {
                    tiles[i][j] = Random.nextBoolean()
                }
            }
            block.tiles = tiles
        }
        return block
    }

    private fun tetrisBlock(): Block {
        val tiles: Array<Array<BooleanArray>> =
            arrayOf(
                arrayOf(
                    booleanArrayOf(false, false, true, false),
                    booleanArrayOf(false, false, true, false),
                    booleanArrayOf(false, false, true, false),
                    booleanArrayOf(false, false, true, false)
                ),
                arrayOf(
                    booleanArrayOf(false, true, true),
                    booleanArrayOf(false, true, false),
                    booleanArrayOf(false, true, false)
                ),
                arrayOf(
                    booleanArrayOf(false, true, false),
                    booleanArrayOf(false, true, false),
                    booleanArrayOf(false, true, true)
                ),
                arrayOf(
                    booleanArrayOf(true, true),
                    booleanArrayOf(true, true),
                ),
                arrayOf(
                    booleanArrayOf(false, true, false),
                    booleanArrayOf(false, true, true),
                    booleanArrayOf(false, false, true)
                ),
                arrayOf(
                    booleanArrayOf(false, false, true),
                    booleanArrayOf(false, true, true),
                    booleanArrayOf(false, true, false)
                ),
                arrayOf(
                    booleanArrayOf(false, true, false),
                    booleanArrayOf(false, true, true),
                    booleanArrayOf(false, true, false)
                )
            )
        var id: Int
        do {
            id = Random.nextInt(0, tiles.size)
        } while (id == prevId)
        prevId = id

        return Block(tiles[id])
    }

    fun getNext(width: Int): Block {
        block = nextBlock
        //nextBlock = randomBlock(size)
        nextBlock = tetrisBlock()
        block.reset(width)
        return block
    }
}