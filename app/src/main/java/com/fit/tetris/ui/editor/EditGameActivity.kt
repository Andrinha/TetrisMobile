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

        val easy = getString(R.string.easy)
        val normal = getString(R.string.normal)
        val hard = getString(R.string.hard)
        val custom = getString(R.string.custon)

        val items = listOf(easy, normal, hard, custom)
        val adapter = ArrayAdapter(this, R.layout.item_list, items)
        (binding.textInputDifficulty.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.buttonStartGame.setOnClickListener {
            val name = binding.textName.text.toString()
            val width = binding.textWidth.text.toString()
            val height = binding.textHeight.text.toString()
            val speed = binding.textSpeed.text.toString()
            var success = true
            val difficulty = binding.textDifficulty.text.toString()
            val type = if (binding.radioScore.isChecked) 0 else 1

            if (name.isBlank()) {
                binding.textInputName.error = getString(R.string.enter_player_name)
                success = false
            } else binding.textInputName.error = null
            if (width.isBlank() || width.toInt() !in 8..25) {
                binding.textInputWidth.error = getString(R.string.width_range)
                success = false
            } else binding.textInputWidth.error = null
            if (height.isBlank() || height.toInt() !in 10..35) {
                binding.textInputHeight.error = getString(R.string.height_range)
                success = false
            } else binding.textInputHeight.error = null
            if (speed.isBlank() || speed.toInt() !in 1..60) {
                binding.textInputSpeed.error = getString(R.string.speed_range)
                success = false
            } else binding.textInputSpeed.error = null
            if (success) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(
                    "data",
                    GameData(name, width.toInt(), height.toInt(), speed.toInt(), difficulty, type)
                )
                startActivity(intent)
            }
        }

        binding.buttonLogin.setOnClickListener {
            //showAlertWithTextInput()
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
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
            .setTitle(resources.getString(R.string.enter_password))
            .setView(input)
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