package com.fit.tetris

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fit.tetris.databinding.ActivityMainBinding
import com.fit.tetris.ui.about.AboutActivity
import com.fit.tetris.ui.editor.EditGameActivity
import com.fit.tetris.ui.settings.SettingsActivity
import com.fit.tetris.ui.statistics.StatisticsActivity
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            buttonStart.setOnClickListener {
                val intent = Intent(this@MainActivity, EditGameActivity::class.java)
                startActivity(intent)
            }
            buttonStats.setOnClickListener {
                val intent = Intent(this@MainActivity, StatisticsActivity::class.java)
                startActivity(intent)
            }
            buttonSettings.button_settings.setOnClickListener {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            textAbout.paintFlags = textAbout.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            textAbout.setOnClickListener {
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
            }
        }

        Glide.with(this)
            .load(R.drawable.pochita)
            .into(binding.imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}