package com.fit.tetris.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.fit.tetris.R
import com.fit.tetris.adapters.ShapeAdapter
import com.fit.tetris.data.difficulty.Difficulty
import com.fit.tetris.data.difficulty.DifficultyDatabase
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.databinding.ActivityAdminBinding
import com.fit.tetris.ui.blockeditor.BlockEditorActivity
import com.fit.tetris.utils.BaseShapes
import com.fit.tetris.utils.setOnItemClickListener
import com.google.android.material.textview.MaterialTextView
import java.util.*

class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[AdminViewModel::class.java]
    }
    private val adapter = ShapeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonBlockEditor.setOnClickListener {
                val intent = Intent(this@AdminActivity, BlockEditorActivity::class.java)
                startActivity(intent)
            }
            buttonSave.setOnClickListener {
                insertDataToDatabase()
                this@AdminActivity.finish()
            }
//            recyclerShapes.onItemClick {
//                val data = viewModel.selected.value!!.toMutableList()
//                data[it] = !data[it]
//                viewModel.selected.value = data
//            }
//            recyclerShapes.onLongItemClick {
//                BottomSheetPickPhotoFragment().show(supportFragmentManager, "tag")
//            }
            recyclerShapes.setOnItemClickListener(
                {
                    if (it < viewModel.shapesData.value!!.size) {
                        val data = viewModel.selected.value!!.toMutableList()
                        data[it] = !data[it]
                        viewModel.selected.value = data
                    }
                },
                {
                    if (it < viewModel.shapesData.value!!.size) {
                        BottomSheetPickPhotoFragment().show(supportFragmentManager, "tag")
                    }
                }
            )

            toolbar.setNavigationOnClickListener {
                this@AdminActivity.finish()
            }
            recyclerShapes.layoutManager = GridLayoutManager(
                this@AdminActivity,
                3
            )//LinearLayoutManager(this@AdminActivity, RecyclerView.HORIZONTAL, false)
            recyclerShapes.adapter = adapter
            textDifficulty.setOnItemClickListener { _, view, _, _ ->
                viewModel.selectedDifficulty.value = (view as MaterialTextView).text.toString()
            }
        }

        viewModel.apply {
            shapesData.observe(this@AdminActivity) { shape ->
                if (isDataReceived.value != true) {
                    val data = viewModel.selected.value!!.toMutableList()
                    shape.forEach {
                        data.add(false)
                        while (it.r + it.g + it.b !in 255..512) {
                            it.r = Random().nextInt(256)
                            it.g = Random().nextInt(256)
                            it.b = Random().nextInt(256)
                        }
                    }
                    viewModel.selected.value = data
                    isDataReceived.value = true

                }
                adapter.setData(shape + BaseShapes().list.map { Shape(0, it, isBase = true) })
            }
            difficultyData.observe(this@AdminActivity) { difficulties ->
                val data: MutableList<String> = mutableListOf()
                difficulties.forEach {
                    data.add(it.name)
                }
                val adapter = ArrayAdapter(this@AdminActivity, R.layout.item_list, data)
                (binding.textInputDifficulty.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            }
            selected.observe(this@AdminActivity) { selected ->
                if (!shapesData.value.isNullOrEmpty()) {
                    val data = viewModel.shapesData.value!!.toMutableList()
                    selected.forEachIndexed { i, isSelected ->
                        data[i].selected = isSelected
                    }
                    adapter.setData(data + BaseShapes().list.map { Shape(0, it, isBase = true) })
                }
            }
            selectedDifficulty.observe(this@AdminActivity) { difficulty ->
                if (!difficultyData.value.isNullOrEmpty()) {
                    val find = difficultyData.value!!.find { it.name == difficulty }
                    if (find != null) {
                        binding.textWidth.setText(find.width.toString())
                        binding.textHeight.setText(find.height.toString())
                        binding.textName.setText(find.name)

                        val newSelected: MutableList<Boolean> = selected.value!!.toMutableList()
                        viewModel.shapesData.value!!.forEachIndexed { i, shape ->
                            newSelected[i] = find.shapes.contains(shape.tiles)
                        }
                        viewModel.selected.value = newSelected
                    }
                }
            }
        }
    }

    private fun insertDataToDatabase() {
        val name = binding.textName.text.toString()
        val width = binding.textWidth.text.toString()
        val height = binding.textHeight.text.toString()
        val speed = binding.textSpeed.text.toString()
        val shapes: MutableList<Int> = mutableListOf()
        viewModel.shapesData.value!!.forEachIndexed { i, shape ->
            if (viewModel.selected.value!![i]) {
                shapes.add(shape.tiles)
            }
        }

        val difficulty = Difficulty(
            0,
            name,
            width.toInt(),
            height.toInt(),
            speed.toInt(),
            shapes
        )
        addRecord(difficulty)
    }

    private fun addRecord(difficulty: Difficulty) {
        val db = Room.databaseBuilder(
            this,
            DifficultyDatabase::class.java, "difficulty_table"
        ).allowMainThreadQueries().build()

        val recordDao = db.difficultyDao()
        recordDao.addRecord(difficulty)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}