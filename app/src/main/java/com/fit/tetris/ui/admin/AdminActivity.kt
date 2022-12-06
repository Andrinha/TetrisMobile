package com.fit.tetris.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.adapters.ShapeAdapter
import com.fit.tetris.databinding.ActivityAdminBinding
import com.fit.tetris.utils.onItemClick
import java.util.*

class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: AdminViewModel? = null
    private val viewModel get() = _viewModel!!
    private val adapter = ShapeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        _viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        setContentView(binding.root)

        binding.apply {
            buttonBlockEditor.setOnClickListener {
                val intent = Intent(this@AdminActivity, BlockEditorActivity::class.java)
                startActivity(intent)
            }
            buttonSave.setOnClickListener {
                this@AdminActivity.finish()
            }
            recyclerShapes.onItemClick {
                val data = viewModel.selected.value!!.toMutableList()
                data[it] = !data[it]
                viewModel.selected.value = data
            }
            toolbar.setNavigationOnClickListener {
                this@AdminActivity.finish()
            }
            recyclerShapes.layoutManager = LinearLayoutManager(this@AdminActivity, RecyclerView.HORIZONTAL, false)
            recyclerShapes.adapter = adapter
        }

        viewModel.apply {
            shapesData.observe(this@AdminActivity) { shape ->
                if (isDataReceived.value != true) {
                    val data = viewModel.selected.value!!.toMutableList()
                    shape.forEach {
                        data.add(false)
                        while (it.r + it.g + it.b !in 255 .. 512) {
                            it.r = Random().nextInt(256)
                            it.g = Random().nextInt(256)
                            it.b = Random().nextInt(256)
                        }
                    }
                    viewModel.selected.value = data
                    isDataReceived.value = true
                }
                adapter.setData(shape)
            }
            selected.observe(this@AdminActivity) { selected ->
                if (!shapesData.value.isNullOrEmpty()) {
                    val data = viewModel.shapesData.value!!.toMutableList()
                    selected.forEachIndexed { i, isSelected ->
                        data[i].selected = isSelected
                    }
                    adapter.setData(data)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}