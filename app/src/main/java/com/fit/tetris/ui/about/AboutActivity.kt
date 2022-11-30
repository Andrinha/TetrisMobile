package com.fit.tetris.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fit.tetris.R
import com.fit.tetris.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.materialToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}