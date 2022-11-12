package com.fit.tetris.ui.admin

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.fit.tetris.R
import com.fit.tetris.data.Action
import com.fit.tetris.data.Shape
import com.fit.tetris.data.ShapeDatabase
import com.fit.tetris.databinding.ActivityBlockEditorBinding
import kotlinx.android.synthetic.main.activity_edit_game.view.*
import java.util.Random

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
        while (a + b + c !in 255 .. 512) {
            a = Random().nextInt(256)
            b = Random().nextInt(256)
            c = Random().nextInt(256)
        }
        val color = Color.rgb(a, b, c)

        binding.buttonSave.setOnClickListener {
            insertDataToDatabase()
            this.finish()
        }
        setTableTouchListener(binding.table)
        viewModel.tiles.observe(this) { tiles ->
            repeat(4) { j ->
                repeat(4) { i ->
                    getView(i, j).setBackgroundColor(
                        if (tiles[i][j]) {
                            color
                        } else
                        {
                            getColor(R.color.color_12_light)
                        }
                    )
                }
            }
        }
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
        val name = binding.textName.text.toString()
        var tiles = ""
        repeat(4) { j ->
            repeat(4) { i ->
                tiles += (if (viewModel.tiles.value!![i][j]) "1" else "0")
            }
        }
        val record = Shape(
            0,
            name,
            tiles.toInt(2))
        addShape(record)
    }

    private fun addShape(shape: Shape){
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