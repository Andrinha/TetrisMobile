package com.fit.tetris.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        Glide.with(this)
            .load(R.drawable.makima_power)
            .into(binding.imagePochita)
    }
}