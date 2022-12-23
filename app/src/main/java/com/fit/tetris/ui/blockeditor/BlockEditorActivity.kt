package com.fit.tetris.ui.blockeditor

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
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
import com.fit.tetris.utils.BaseShapes
import com.fit.tetris.utils.toBooleanArray
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

class BlockEditorActivity : AppCompatActivity() {

    private var _binding: ActivityBlockEditorBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: BlockEditorViewModel? = null
    private val viewModel get() = _viewModel!!
    val bundleTiles = intent.extras?.getInt("tiles")
    val bundleId = intent.extras?.getInt("id")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[BlockEditorViewModel::class.java]
        _binding = ActivityBlockEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bundleTiles != null) {
            viewModel.tiles.value = bundleTiles.toBooleanArray()
        }

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

            var isShapeGood = true

            if (checkShapeForConnection(tiles)) {
                binding.textIsConnected.text = "Фигура связная"
                binding.textIsConnected.setTextColor(ContextCompat.getColor(this, R.color.color_15))
            } else {
                binding.textIsConnected.text = "Фигура должна быть связной"
                binding.textIsConnected.setTextColor(ContextCompat.getColor(this, R.color.color_10))
                isShapeGood = false
            }
            if (checkForDuplicate(truncate(tiles), viewModel.shapes.map { truncate(it) })) {
                binding.textIsStored.text = "Фигура новая"
                binding.textIsStored.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_15
                    )
                )
            } else {
                binding.textIsStored.text = "Фигура уже существует"
                binding.textIsStored.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_10
                    )
                )
                isShapeGood = false
            }
            binding.textValue.text = count(tiles).toString()
            binding.buttonSave.isEnabled = isShapeGood

        }

        viewModel.shapesData.observe(this) { shapes ->
            val based = BaseShapes().list.map { it.toBooleanArray() }
            val saved = shapes.map { it.tiles.toBooleanArray() }
            viewModel.shapes = (based + saved).toMutableList()
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
        shapeList: List<Array<BooleanArray>>?
    ): Boolean {
        if (shapeList.isNullOrEmpty())
            return true

        shapeList.forEach { shape ->
            if (shape.size == tiles.size) {
                var rotated = shape
                repeat(4) {
                    rotated = rotateCCW(rotated)
                    var contains = true
                    repeat(tiles.size) { j ->
                        repeat(tiles.size) { i ->
                            if (rotated[i][j] != tiles[i][j]) {
                                contains = false
                            }
                        }
                    }
                    if (contains) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun getSize(tiles: Array<BooleanArray>): Int {
        if (count(tiles) == 0)
            return 0

        var genMinHeight = Int.MAX_VALUE
        var genMaxHeight = Int.MIN_VALUE

        var genMinWidth = Int.MAX_VALUE
        var genMaxWidth = Int.MIN_VALUE
        repeat(tiles.size) { j ->
            var minHeight = Int.MAX_VALUE
            var maxHeight = Int.MIN_VALUE
            var isHeightFirst = true

            var minWidth = Int.MAX_VALUE
            var maxWidth = Int.MIN_VALUE
            var isWidthFirst = true
            repeat(tiles.size) { i ->
                if (tiles[i][j]) {
                    if (isHeightFirst) {
                        minHeight = i
                        isHeightFirst = false
                    }
                    maxHeight = i
                }

                if (tiles[i][j]) {
                    if (isWidthFirst) {
                        minWidth = j
                        isWidthFirst = false
                    }
                    maxWidth = j
                }
            }
            genMinHeight = min(genMinHeight, minHeight)
            genMaxHeight = max(genMaxHeight, maxHeight)

            genMinWidth = min(genMinWidth, minWidth)
            genMaxWidth = max(genMaxWidth, maxWidth)
        }
        return max(genMaxHeight - genMinHeight, genMaxWidth - genMinWidth) + 1
    }

    private fun truncate(_tiles: Array<BooleanArray>): Array<BooleanArray> {
        when (val size = getSize(_tiles)) {
            4 -> return _tiles
            else -> {
                val tiles = Array(size) { BooleanArray(size) }
                var maxSize = Int.MIN_VALUE
                var offsetI = 0
                var offsetJ = 0
                repeat(4 - size + 1) { j ->
                    repeat(4 - size + 1) { i ->

                        repeat(size) { y ->
                            repeat( size) { x ->
                                tiles[x][y] = _tiles[x + i][y + j]
                            }
                        }
                        val locSize = count(tiles)
                        if (locSize > maxSize) {
                            maxSize = locSize
                            offsetI = i
                            offsetJ = j
                        }
                    }
                }
                repeat(size) { y ->
                    repeat( size) { x ->
                        tiles[x][y] = _tiles[x + offsetI][y + offsetJ]
                    }
                }

                return tiles
            }
        }
    }

    private fun count(tiles: Array<BooleanArray>): Int {
        val size = tiles.size
        var sum = 0
        repeat(size) { j ->
            repeat(size) { i ->
                if (tiles[i][j]) {
                    sum++
                }
            }
        }
        return sum
    }

    private fun rotateCCW(tiles: Array<BooleanArray>): Array<BooleanArray> {
        for (i in 0 until tiles.size / 2) {
            for (j in i until tiles.size - i - 1) {
                val temp: Boolean = tiles[i][j]
                tiles[i][j] = tiles[tiles.size - 1 - j][i]
                tiles[tiles.size - 1 - j][i] = tiles[tiles.size - 1 - i][tiles.size - 1 - j]
                tiles[tiles.size - 1 - i][tiles.size - 1 - j] = tiles[j][tiles.size - 1 - i]
                tiles[j][tiles.size - 1 - i] = temp
            }
        }
        return tiles
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
        var id = 0
        if (bundleId != null) {
            id = bundleId
        }
        val record = Shape(
            id,
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