package com.fit.tetris.repository

import com.fit.tetris.data.difficulty.Difficulty
import com.fit.tetris.data.difficulty.DifficultyDao

class DifficultyRepository(private val difficultyDao: DifficultyDao) {
    val readAllData = difficultyDao.readAllData()

    fun addRecord(difficulty: Difficulty) {
        difficultyDao.addRecord(difficulty)
    }

    fun deleteDifficulty(name: String) {
        difficultyDao.deleteItem(name)
    }
}