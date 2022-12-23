package com.fit.tetris.ui.admin

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            buttonSave.setOnClickListener { view ->
                var success = true
                val name = binding.textName.text.toString()
                val width = binding.textWidth.text.toString().toInt()
                val height = binding.textHeight.text.toString().toInt()
                val speed = binding.textSpeed.text.toString().toInt()

                val find = viewModel.difficultyData.value!!.find { it.name == binding.textName.text.toString()}
                if (name.isBlank()) {
                    binding.textInputName.error = getString(R.string.enter_diff_name)
                    success = false
                } else if (find != null)  {
                    binding.textInputName.error = "Уровень с таким названием уже есть"
                    success = false
                } else binding.textInputName.error = null
                if (width !in 8..25) {
                    binding.textInputWidth.error = getString(R.string.width_range)
                    success = false
                } else binding.textInputWidth.error = null
                if (height !in 10..35) {
                    binding.textInputHeight.error = getString(R.string.height_range)
                    success = false
                } else binding.textInputHeight.error = null
                if (speed !in 1..60) {
                    binding.textInputSpeed.error = getString(R.string.speed_range)
                    success = false
                } else binding.textInputSpeed.error = null



                if (success) {
                    insertDataToDatabase()
                    this@AdminActivity.finish()
                }
            }
            buttonDelete.paintFlags = buttonSelectall.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            buttonDelete.setOnClickListener {
                showDeleteDialog(viewModel.selectedDifficulty.value!!)
            }
            buttonSelectall.paintFlags = buttonSelectall.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            buttonSelectall.setOnClickListener {
                val data = viewModel.selected.value!!.toMutableList()
                if (viewModel.selected.value!!.all { it })
                    viewModel.selected.value!!.forEachIndexed { i, _ ->
                        data[i] = false
                    }
                else
                    data.forEachIndexed { i, _ ->
                        data[i] = true
                    }
                viewModel.selected.value = data
            }

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
                        val bottomSheetFragment = BottomSheetPickPhotoFragment()
                        val bundle = Bundle()
                        bundle.putInt("position", it)
                        bottomSheetFragment.arguments = bundle
                        bottomSheetFragment.show(supportFragmentManager, "tag")
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
                        binding.buttonDelete.visibility = View.VISIBLE
                    }
                    else
                        binding.buttonDelete.visibility = View.GONE
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

        val dao = db.difficultyDao()
        dao.addRecord(difficulty)
    }

    private fun deleteDifficulty(name: String) {
        val db = Room.databaseBuilder(
            this,
            DifficultyDatabase::class.java, "difficulty_table"
        ).allowMainThreadQueries().build()

        val dao = db.difficultyDao()
        dao.deleteItem(name)
    }

    private fun showDeleteDialog(name: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Подтвердите удаление")
            .setMessage("Вы действительно хотите удалить уровень сложности \"$name\"?")
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, _ ->
                deleteDifficulty(name)
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}