package com.fit.tetris.ui.editor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.fit.tetris.R
import com.fit.tetris.databinding.ActivityEditGameBinding
import com.fit.tetris.databinding.ActivityMainBinding
import com.fit.tetris.ui.game.GameActivity

class EditGameActivity : AppCompatActivity() {

    private var _binding: ActivityEditGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val items = listOf("Easy", "Normal", "Hard", "Custom")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.textfieldDifficulty.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.buttonStartGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}