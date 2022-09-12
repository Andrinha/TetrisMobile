package com.fit.tetris.ui.game

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.fit.tetris.R
import com.fit.tetris.data.GameData
import com.fit.tetris.databinding.ActivityGameBinding
import kotlin.math.ceil


class GameActivity : AppCompatActivity() {

    private var _binding: ActivityGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameData = intent.getSerializableExtra("data") as GameData
        createTable(gameData.width, gameData.height)


    }

    private fun createTable(width: Int, height: Int) {
        val tableLayout = TableLayout(this).apply {
            gravity = Gravity.CENTER
            layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT)
            setBackgroundResource(R.drawable.table_background)
        }

        repeat(height) {
            val tableRow = TableRow(this).apply {
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            }
            repeat(width) {
                val cell = View(this).apply {
                    setBackgroundResource(R.drawable.cell_background)
                    layoutParams = TableRow.LayoutParams(ceil(20.toPx).toInt(), ceil(20.toPx).toInt())
                }
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
        binding.frameWell.addView(tableLayout)
    }

    private val Number.toPx get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null


    }
}