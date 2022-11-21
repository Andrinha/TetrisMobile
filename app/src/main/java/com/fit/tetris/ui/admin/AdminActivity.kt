package com.fit.tetris.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.R
import com.fit.tetris.adapters.ShapeAdapter
import com.fit.tetris.databinding.ActivityAdminBinding
import com.fit.tetris.utils.onItemClick
import com.fit.tetris.utils.setOnItemClickListener
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: AdminViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        _viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        setContentView(binding.root)

        binding.buttonBlockEditor.setOnClickListener {
            val intent = Intent(this, BlockEditorActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSave.setOnClickListener {
            this.finish()
        }

        binding.recyclerShapes.onItemClick {
            viewModel.selected.value!![it] = !viewModel.selected.value!![it]
        }

        val adapter = ShapeAdapter()
        binding.recyclerShapes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerShapes.adapter = adapter

        viewModel.readAllData.observe(this) { shape ->
            adapter.addShape(shape)
            //viewModel.selected.value!!.add(false)
        }

//        viewModel.selected.observe(this) {
//            it.forEachIndexed { i, b ->
//                if (b)
//                    binding.recyclerShapes[i].setBackgroundColor(getColor(R.color.color_15))
//                else
//                    binding.recyclerShapes[i].setBackgroundColor(getColor(R.color.color_12_light))
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}