package com.fit.tetris.ui.game

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.fit.tetris.R
import com.fit.tetris.data.GameData
import com.fit.tetris.data.RandomBlock
import com.fit.tetris.databinding.ActivityGameBinding
import java.lang.Integer.min


class GameActivity : AppCompatActivity() {

    private var _binding: ActivityGameBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: GameViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        _binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.gameData.value = intent.getSerializableExtra("data") as GameData
        viewModel.createGameGrid()

        binding.frameWell.post {
            val height = binding.frameWell.height - 3
            val width = binding.frameWell.width - 3
            val size = min(height / viewModel.gameData.value!!.height, width / viewModel.gameData.value!!.width)
            createTable(viewModel.gameData.value!!.width, viewModel.gameData.value!!.height, size)
        }

        var xx = 0
        var yy = 0

        binding.buttonDown.setOnClickListener {
            yy++
            viewModel.gameGrid.value!![xx, yy] = 1
            drawGrid(viewModel.gameGrid.value!!)
        }
        binding.buttonLeft.setOnClickListener {
            xx--
            viewModel.gameGrid.value!![xx, yy] = 1
            drawGrid(viewModel.gameGrid.value!!)
        }
        binding.buttonRight.setOnClickListener {
            xx++
            viewModel.gameGrid.value!![xx, yy] = 1
            drawGrid(viewModel.gameGrid.value!!)
        }
        val array = Array(4) { BooleanArray(4)}
        var t = 0
        for (y in 0..3) {
            for (x in 0..3) {
                array[y][x] = (x + y) % 2 == 0
                t++
            }
        }
        val block = RandomBlock(array)
        block.rotateCCW()
        block.rotateCW()
    }

    private fun createTable(width: Int, height: Int, size: Int) {
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
                    //layoutParams = TableRow.LayoutParams(ceil(20.toPx).toInt(), ceil(20.toPx).toInt())
                    layoutParams = TableRow.LayoutParams(size, size)
                }
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
        binding.frameWell.addView(tableLayout)
    }

    private fun getView(x: Int, y: Int): View {
        return ((binding.frameWell[0] as TableLayout)[y] as TableRow)[x]
    }

    private fun drawGrid(grid: GameGrid) {
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (grid[x, y] != 0)
                    getView(x, y).setBackgroundResource(R.drawable.cell_active_background)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}