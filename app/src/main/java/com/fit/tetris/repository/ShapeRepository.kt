package com.fit.tetris.repository

import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDao

class ShapeRepository(private val shapeDao: ShapeDao) {
    val readAllData = shapeDao.readAllData()

    suspend fun addShape(shape: Shape) {
        shapeDao.addShape(shape)
    }
}