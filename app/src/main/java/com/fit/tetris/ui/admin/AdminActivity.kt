package com.fit.tetris.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fit.tetris.R
import com.fit.tetris.databinding.ActivityAdminBinding
import com.fit.tetris.databinding.ActivityEditGameBinding

class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBlockEditor.setOnClickListener {
            val intent = Intent(this, BlockEditorActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSave.setOnClickListener {
            this.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}