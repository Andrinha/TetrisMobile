package com.fit.tetris.ui.editor

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fit.tetris.R
import com.fit.tetris.adapters.ShapeAdapter
import com.fit.tetris.data.GameData
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.databinding.ActivityEditGameBinding
import com.fit.tetris.ui.admin.AdminActivity
import com.fit.tetris.ui.game.GameActivity
import com.fit.tetris.utils.BaseShapes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.google.android.material.textview.MaterialTextView
import java.util.*

class EditGameActivity : AppCompatActivity() {

    private var _binding: ActivityEditGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[EditGameViewModel::class.java]
    }
    private val adapter = ShapeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            buttonStartGame.setOnClickListener {
                val name = binding.textName.text.toString()
                val difficulty = binding.textDifficulty.text.toString()
                var success = true
                val type = if (binding.radioScore.isChecked) 0 else 1

                if (name.isBlank()) {
                    binding.textInputName.error = getString(R.string.enter_player_name)
                    success = false
                } else binding.textInputName.error = null

                if (difficulty.isBlank()) {
                    binding.textInputDifficulty.error = "?????????????? ?????? ???????????????? ??????????????????"
                    success = false
                } else binding.textInputDifficulty.error = null

                if (success) {
                    val intent = Intent(this@EditGameActivity, GameActivity::class.java)
                    intent.putExtra(
                        "data",
                        GameData(
                            name,
                            viewModel.selectedDifficultyItem.value!!,
                            type
                        )
                    )
                    startActivity(intent)
                }
            }
            buttonLogin.setOnClickListener {
                showAlertWithTextInput()
//                val intent = Intent(this@EditGameActivity, AdminActivity::class.java)
//                startActivity(intent)
            }

            toolbar.setNavigationOnClickListener {
                this@EditGameActivity.finish()
            }

            recyclerShapes.layoutManager = GridLayoutManager(this@EditGameActivity, 3)
            recyclerShapes.adapter = adapter

            textDifficulty.setOnItemClickListener { _, view, _, _ ->
                viewModel.selectedDifficulty.value = (view as MaterialTextView).text.toString()
            }
        }

        viewModel.apply {
            shapesData.observe(this@EditGameActivity) { shape ->
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
                adapter.setData(shape + BaseShapes().list.map { Shape(0, it, isBase = true) })
            }
            selectedDifficulty.observe(this@EditGameActivity) { difficulty ->
                if (!difficultyData.value.isNullOrEmpty()) {
                    val find = difficultyData.value!!.find { it.name == difficulty }
                    if (find != null) {
//                        binding.textWidth.setText(find.width.toString())
//                        binding.textHeight.setText(find.height.toString())
                        //binding.textName.setText(find.name)

                        val newSelected: MutableList<Boolean> = selected.value!!.toMutableList()
                        viewModel.shapesData.value!!.forEachIndexed { i, shape ->
                            newSelected[i] = find.shapes.contains(shape.tiles)
                        }
                        viewModel.selected.value = newSelected
                        viewModel.selectedDifficultyItem.value = find
                    }
                }
            }
            selected.observe(this@EditGameActivity) { selected ->
                if (!shapesData.value.isNullOrEmpty()) {
                    val data = viewModel.shapesData.value!!.toMutableList()
                    selected.forEachIndexed { i, isSelected ->
                        data[i].selected = isSelected
                    }
                    adapter.setData(data.filter { it.selected } + BaseShapes().list.map {
                        Shape(
                            0,
                            it,
                            isBase = true
                        )
                    })
                }
            }
            difficultyData.observe(this@EditGameActivity) { difficulties ->
                if (difficulties.isNotEmpty()) {
                    val data: MutableList<String> = mutableListOf()
                    difficulties.forEach {
                        data.add(it.name)
                    }
                    val adapter = ArrayAdapter(this@EditGameActivity, R.layout.item_list, data)
                    (binding.textInputDifficulty.editText as? AutoCompleteTextView)?.setAdapter(
                        adapter
                    )

                    viewModel.selectedDifficulty.value = difficulties.last().name
                    binding.textDifficulty.setText(difficulties.last().name, false)
                }
            }
        }
    }

    private fun showMessageAlert(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)) { it, _ ->
                it.dismiss()
            }
            .show()
    }

    private fun showAlertWithTextInput() {

        val input = TextInputEditText(this).apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        val layout = TextInputLayout(this).apply {
            addView(input)
            endIconMode = END_ICON_PASSWORD_TOGGLE
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.enter_password))
            .setView(layout)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                if (viewModel.password == input.text.toString()) {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                } else {
                    showMessageAlert(
                        resources.getString(R.string.login),
                        resources.getString(R.string.login_fail)
                    )
                }
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}