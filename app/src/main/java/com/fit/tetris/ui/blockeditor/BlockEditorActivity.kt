package com.fit.tetris.ui.blockeditor

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.fit.tetris.R
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.databinding.ActivityBlockEditorBinding
import com.fit.tetris.utils.toBooleanArray
import java.util.*

class BlockEditorActivity : AppCompatActivity() {

    private var _binding: ActivityBlockEditorBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: BlockEditorViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[BlockEditorViewModel::class.java]
        _binding = ActivityBlockEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var a = 0
        var b = 0
        var c = 0
        while (a + b + c !in 255..512) {
            a = Random().nextInt(256)
            b = Random().nextInt(256)
            c = Random().nextInt(256)
        }
        val color = Color.rgb(a, b, c)

        binding.buttonSave.setOnClickListener {
            insertDataToDatabase()
            this.finish()
        }
        binding.materialToolbar.setOnClickListener {
            this.finish()
        }
        setTableTouchListener(binding.table)

        viewModel.tiles.observe(this) { tiles ->
            repeat(4) { j ->
                repeat(4) { i ->
                    getView(i, j).setBackgroundColor(
                        if (tiles[i][j]) {
                            color
                        } else {
                            getColor(R.color.color_12_light)
                        }
                    )
                }
            }
            var intTiles = ""
            repeat(4) { j ->
                repeat(4) { i ->
                    intTiles += (if (tiles[i][j]) "1" else "0")
                }
            }
            binding.textValue.text = "Значение: ${intTiles.toInt(2)}"

            var isShapeGood = 0

            if (checkShapeForConnection(tiles)) {
                binding.textIsConnected.text = "Фигура связная"
                binding.textIsConnected.setTextColor(ContextCompat.getColor(this, R.color.color_15))
                isShapeGood++
            } else {
                binding.textIsConnected.text = "Фигура должна быть связной"
                binding.textIsConnected.setTextColor(ContextCompat.getColor(this, R.color.color_10))
            }

            if (checkForDuplicate(
                    tiles,
                    viewModel.shapesData.value?.map { it.tiles.toBooleanArray() })
            ) {
                binding.textIsStored.text = "Фигура новая"
                binding.textIsStored.setTextColor(ContextCompat.getColor(this, R.color.color_15))
                isShapeGood++
            } else {
                binding.textIsStored.text = "Фигура уже существует"
                binding.textIsStored.setTextColor(ContextCompat.getColor(this, R.color.color_10))
            }

            binding.buttonSave.isEnabled = isShapeGood == 2
        }

    }

    private fun checkShapeForConnection(tiles: Array<BooleanArray>): Boolean {
        val island = Islands()
        var result = true
        repeat(4) { j ->
            repeat(4) { i ->
                if (tiles[i][j] && countNeighbors(tiles, i, j) == 0)
                    result = false
            }
        }
        return (result && island.countIslands(tiles) == 1)
    }

    private fun checkForDuplicate(
        tiles: Array<BooleanArray>,
        list: List<Array<BooleanArray>>?
    ): Boolean {
        if (list.isNullOrEmpty())
            return true
        if (list.contains(tiles))
            return false

        return true
    }

    private fun countNeighbors(tiles: Array<BooleanArray>, i: Int, j: Int): Int {
        var result = 0
        if (i > 0 && tiles[i - 1][j])
            result++
        if (i < 3 && tiles[i + 1][j])
            result++
        if (j < 3 && tiles[i][j + 1])
            result++
        if (j > 0 && tiles[i][j - 1])
            result++


        return result
    }

    private fun getView(i: Int, j: Int): View {
        return (binding.table[i] as TableRow)[j]
    }

    private fun setTableTouchListener(table: TableLayout) {
        var i = 0
        table.forEach { row ->
            (row as TableRow).forEach { view ->
                setViewTouchListener(view, i)
                i++
            }
        }
    }

    private fun setViewTouchListener(view: View, id: Int) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.invertTile(id)
                }
            }
            v.performClick()
            false
        }
    }

    private fun insertDataToDatabase() {
        var tiles = ""
        repeat(4) { j ->
            repeat(4) { i ->
                tiles += (if (viewModel.tiles.value!![i][j]) "1" else "0")
            }
        }
        val record = Shape(
            0,
            tiles.toInt(2)
        )
        addShape(record)
    }

    private fun addShape(shape: Shape) {
        val db = Room.databaseBuilder(
            applicationContext,
            ShapeDatabase::class.java, "shape_database"
        ).allowMainThreadQueries().build()

        val shapeDao = db.shapeDao()
        shapeDao.addShape(shape)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}