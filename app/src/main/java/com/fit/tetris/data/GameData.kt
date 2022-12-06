package com.fit.tetris.data

import java.io.Serializable

data class GameData(val name: String, val width: Int, val height: Int, var speed: Int, val difficulty: String): Serializable
