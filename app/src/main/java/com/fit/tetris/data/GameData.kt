package com.fit.tetris.data

import com.fit.tetris.data.difficulty.Difficulty
import java.io.Serializable

data class GameData(
    val name: String,
    val difficulty: Difficulty,
    val type: Int
) : Serializable
