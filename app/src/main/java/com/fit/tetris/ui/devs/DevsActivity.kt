package com.fit.tetris.ui.devs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fit.tetris.R
import com.fit.tetris.databinding.ActivityAboutBinding
import com.fit.tetris.databinding.ActivityDevsBinding

class DevsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDevsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.materialToolbar.setNavigationOnClickListener {
            this.finish()
        }

        Glide.with(this)
            .load(R.drawable.pochita_dev)
            .into(binding.imagePochita)
    }
}