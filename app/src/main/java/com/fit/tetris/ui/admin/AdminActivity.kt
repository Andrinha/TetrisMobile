package com.fit.tetris.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: AdminViewModel? = null
    private val viewModel get() = _viewModel!!

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

        val adapter = ShapeAdapter()
        binding.recyclerShapes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerShapes.adapter = adapter

        _viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        viewModel.readAllData.observe(this) { shape ->
            adapter.addShape(shape)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}