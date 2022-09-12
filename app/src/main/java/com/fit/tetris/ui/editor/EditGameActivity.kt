package com.fit.tetris.ui.editor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fit.tetris.R
import com.fit.tetris.data.GameData
import com.fit.tetris.databinding.ActivityEditGameBinding
import com.fit.tetris.ui.game.GameActivity
import com.fit.tetris.ui.game.GameViewModel

class EditGameActivity : AppCompatActivity() {

    private var _binding: ActivityEditGameBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: GameViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        _binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val items = listOf("Easy", "Normal", "Hard", "Custom")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.textfieldDifficulty.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.buttonStartGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            val name = binding.textPlayerName.text.toString()
            val width = binding.textWidth.text.toString().toInt()
            val height = binding.textHeight.text.toString().toInt()
            val speed = binding.textSpeed.text.toString().toInt()
            intent.putExtra("data", GameData(name, width, height, speed))
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}