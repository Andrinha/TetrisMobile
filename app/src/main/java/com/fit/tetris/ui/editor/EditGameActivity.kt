package com.fit.tetris.ui.editor

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fit.tetris.R
import com.fit.tetris.data.GameData
import com.fit.tetris.databinding.ActivityEditGameBinding
import com.fit.tetris.ui.admin.AdminActivity
import com.fit.tetris.ui.admin.BlockEditorActivity
import com.fit.tetris.ui.game.GameActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class EditGameActivity : AppCompatActivity() {

    private var _binding: ActivityEditGameBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: EditGameViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProvider(this)[EditGameViewModel::class.java]
        _binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val items = listOf("Easy", "Normal", "Hard", "Custom")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.textInputDifficulty.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.buttonStartGame.setOnClickListener {
            val name = binding.textName.text.toString()
            val width = binding.textWidth.text.toString()
            val height = binding.textHeight.text.toString()
            val speed = binding.textSpeed.text.toString()
            var success = true

            if (name == "") {
                binding.textInputName.error = "Enter player name"
                success = false
            } else binding.textInputName.error = null
            if (width == "" || width.toInt() !in 4..25) {
                binding.textInputWidth.error = "Width must be between 4 and 25"
                success = false
            } else binding.textInputWidth.error = null
            if (height == "" || height.toInt() !in 6..35) {
                binding.textInputHeight.error = "Height must be between 6 and 35"
                success = false
            } else binding.textInputHeight.error = null
            if (speed == "" || speed.toInt() !in 1..60) {
                binding.textInputSpeed.error = "Speed must be between 1 and 60"
                success = false
            } else binding.textInputSpeed.error = null
            if (success) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("data", GameData(name, width.toInt(), height.toInt(), speed.toInt()))
                startActivity(intent)
            }
        }

        binding.buttonLogin.setOnClickListener {
            showAlertWithTextInput()
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
        val input = TextInputEditText(this)
        input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        MaterialAlertDialogBuilder(this)
            .setTitle("Enter password")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                if (viewModel.password == input.text.toString()) {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }
                else {
                    showMessageAlert(resources.getString(R.string.login), resources.getString(R.string.login_fail))
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                this.finish()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}