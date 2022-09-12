package com.fit.tetris.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fit.tetris.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private var _binding: ActivityGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}