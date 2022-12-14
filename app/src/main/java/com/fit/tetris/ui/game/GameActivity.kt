package com.fit.tetris.ui.game

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.fit.tetris.R
import com.fit.tetris.data.Action
import com.fit.tetris.data.Block
import com.fit.tetris.data.GameData
import com.fit.tetris.data.record.Record
import com.fit.tetris.data.record.RecordDatabase
import com.fit.tetris.databinding.ActivityGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.IOException
import java.lang.Integer.min
import kotlin.math.ln
import kotlin.math.sqrt


class GameActivity : AppCompatActivity() {

    private var _binding: ActivityGameBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: GameViewModel? = null
    private val viewModel get() = _viewModel!!

    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer.create(this, R.raw.anime).apply { isLooping = true }
    }
    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
    private lateinit var soundPool: SoundPool
    private lateinit var assetManager: AssetManager
    private var streamID = 0
    private var soundNormal = 0
    private var soundFinish = 0
    private var soundWhistle = 0
    private var soundVolume = 0f
    private var musicVolume = 0f
    private var stroke = false

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var delay = 100L
        set(value) {
            field = if (value < 17) 17 else value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        _binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.gameData.value = intent.getSerializableExtra("data") as GameData
        viewModel.createGameGrid()
        delay = 1000L / viewModel.gameData.value!!.difficulty.speed
        var oldLines = 0
        musicVolume = preferences.getInt("music", 0) / 100f
        soundVolume = preferences.getInt("sounds", 0) / 100f
        stroke = preferences.getBoolean("stroke", false)
        mediaPlayer.setVolume(musicVolume, musicVolume)

        binding.frameWell.post {
            val height = binding.frameWell.height
            val width = binding.frameWell.width
            val size = min(
                height / viewModel.gameData.value!!.difficulty.height,
                width / viewModel.gameData.value!!.difficulty.width
            )
            createTable(
                viewModel.gameData.value!!.difficulty.width,
                viewModel.gameData.value!!.difficulty.height,
                size
            )
        }

        binding.frameNext.post {
            val width = binding.frameNext.width
            val block = viewModel.blockQueue.value!!.nextBlock
            createTable(width, block)
            drawPreview(block)
        }

        if (viewModel.startTime.value == null) {
            viewModel.startTime.value = System.currentTimeMillis()
        }

        setButtonTouchListener(binding.buttonDown, Action.DOWN)
        setButtonTouchListener(binding.buttonBottom, Action.BOTTOM)
        setButtonTouchListener(binding.buttonLeft, Action.LEFT)
        setButtonTouchListener(binding.buttonRight, Action.RIGHT)
        setButtonTouchListener(binding.buttonCW, Action.CW)
        setButtonTouchListener(binding.buttonCWW, Action.CCW)

        viewModel.score.observe(this) {
            binding.textScoreValue.text = it.toString()
        }
        viewModel.linesCleared.observe(this) {
            delay =
                1000L / (viewModel.gameData.value!!.difficulty.speed + sqrt(it.toFloat()).toLong())
            binding.textSpeedValue.text = (1000 / delay).toString()
            binding.textClearedValue.text = it.toString()
            when {
                //it - oldLines > 3 -> playSound(soundWhistle)
                it - oldLines > 0 -> playSound(soundWhistle)
                else -> playSound(soundFinish)
            }
            oldLines = it
        }
        viewModel.currentBlock.observe(this) {
            val width = binding.frameNext.width
            val block = viewModel.blockQueue.value!!.nextBlock
            createTable(width, block)
            drawPreview(block)
        }
        viewModel.placedBlock.observe(this) {
            drawBlock(it)
        }
        viewModel.isGameOver.observe(this) {
            if (it) {
                showRetryAlert(getString(R.string.defeat), viewModel.score.value!!)
            }
        }
    }

    private fun createTable(width: Int, height: Int, size: Int) {
        val tableLayout = TableLayout(this).apply {
            gravity = Gravity.CENTER
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
            )
            //setBackgroundResource(R.drawable.table_background)
        }

        repeat(height) {
            val tableRow = TableRow(this).apply {
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            }
            repeat(width) {
                val cell = View(this).apply {
                    if (stroke)
                        background = shape(Color.BLACK, Color.GRAY)
                    else
                        setBackgroundColor(Color.BLACK)
                    layoutParams = TableRow.LayoutParams(size, size)
                }
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
        binding.frameWell.addView(tableLayout)
    }

    fun shape(background: Int, stroke: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(background)
        setStroke(1, stroke)
    }

    private fun createTable(width: Int, block: Block) {
        val size = block.tiles.size
        val background = getColor(R.color.color_12_light)

        binding.frameNext.removeAllViews()
        val tableLayout = TableLayout(this).apply {
            gravity = Gravity.CENTER
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
            )
            //setBackgroundResource(R.drawable.table_background)
        }

        repeat(size) {
            val tableRow = TableRow(this).apply {
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            }
            repeat(size) {
                val cell = View(this).apply {
                    //setBackgroundResource(R.drawable.cell_background)
                    setBackgroundColor(background)
                    //layoutParams = TableRow.LayoutParams(ceil(20.toPx).toInt(), ceil(20.toPx).toInt())
                    layoutParams = TableRow.LayoutParams(width / size, width / size)
                }
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
        binding.frameNext.addView(tableLayout)
    }

    private fun getView(x: Int, y: Int): View {
        return ((binding.frameWell[0] as TableLayout)[y] as TableRow)[x]
    }

    private fun drawGrid(grid: GameGrid) {
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (grid[x, y] != 0) {
                    getView(x, y).setBackgroundColor(grid[x, y])
                    //getView(x, y).background = shape(grid[x, y], Color.GRAY)
                }
            }
        }
    }

    private fun drawBlock(block: Block) {
        block.tilePositions().forEach {
            //getView(it.x, it.y).setBackgroundResource(R.drawable.cell_active_background)
            getView(it.x, it.y).setBackgroundColor(Color.rgb(block.r, block.g, block.b))
        }
    }

    private fun drawGhostBlock(block: Block) {
        val distance = viewModel.blockDropDistance()
        block.tilePositions().forEach {
            getView(it.x, it.y + distance).apply {
                setBackgroundColor(Color.rgb(block.r / 3, block.g / 3, block.b / 3))
            }
        }
    }

    private fun clearScreen(grid: GameGrid) {
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
//                if (grid[x, y] == 0)
//                    getView(x, y).apply {
//                        //setBackgroundResource(R.drawable.cell_background)
//                        setBackgroundColor(Color.BLACK)
//                    }
                if (stroke)
                    getView(x, y).background = shape(Color.BLACK, Color.GRAY)
                else
                    getView(x, y).setBackgroundColor(Color.BLACK)
            }
        }
    }

    private fun draw(grid: GameGrid, block: Block) {
        if (binding.frameWell.childCount != 0) {
            clearScreen(grid)
            drawGrid(grid)
            drawGhostBlock(block)
            drawBlock(block)
            playSound(soundNormal)
        }
    }

    private fun drawPreview(block: Block) {
        block.tiles.forEachIndexed { y, j ->
            j.forEachIndexed { x, i ->
                if (i && binding.frameNext.childCount != 0) {
                    ((binding.frameNext[0] as TableLayout)[x] as TableRow)[y]
                        .setBackgroundColor(Color.rgb(block.r, block.g, block.b))
                    //.setBackgroundColor(getColor(R.color.color_10))
                }
            }
        }
    }

    private fun playSound(sound: Int): Int {
        if (sound > 0) {
            streamID = soundPool.play(sound, soundVolume, soundVolume, 1, 0, 1F)
        }
        return streamID
    }

    private fun loadSound(fileName: String): Int {
        val afd: AssetFileDescriptor = try {
            application?.assets?.openFd(fileName)!!
        } catch (e: IOException) {
            return -1
        }
        return soundPool.load(afd, 1)
    }

    private fun setButtonTouchListener(view: View, action: Action) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (action) {
                        Action.DOWN -> viewModel.moveBlockDown()
                        Action.BOTTOM -> viewModel.moveDropBlock()
                        Action.LEFT -> viewModel.moveBlockLeft()
                        Action.RIGHT -> viewModel.moveBlockRight()
                        Action.CW -> viewModel.rotateBlockCW()
                        Action.CCW -> viewModel.rotateBlockCCW()
                    }
                    draw(viewModel.gameGrid.value!!, viewModel.currentBlock.value!!)
                }
            }
            v.performClick()
            false
        }
    }

    private fun showRetryAlert(title: String, score: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(resources.getString(R.string.score) + ": $score\n" + resources.getString(R.string.start_again_message))
            .setCancelable(false)
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                insertDataToDatabase()
                this.finish()
            }
            .setPositiveButton(resources.getString(R.string.start)) { _, _ ->
                insertDataToDatabase()
                viewModel.create()
            }
            .show()
    }

    override fun onResume() {

        handler.postDelayed(Runnable {
            if (!viewModel.isGameOver.value!!) {
                viewModel.moveBlockDown()
                draw(viewModel.gameGrid.value!!, viewModel.currentBlock.value!!)
            }
            handler.postDelayed(runnable, delay)
        }.also { runnable = it }, delay)

        super.onResume()

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(10)
            .build()
        assetManager = assets!!

        soundNormal = loadSound("normal.wav")
        soundFinish = loadSound("finish.wav")
        soundWhistle = loadSound("whistle.wav")
        mediaPlayer.start()
    }

    private fun insertDataToDatabase() {
        val name = viewModel.gameData.value!!.name
        val currentTimeMillis = System.currentTimeMillis()
        val gameTime = currentTimeMillis - viewModel.startTime.value!!
        val score = viewModel.score.value!!
        val difficulty = viewModel.gameData.value!!.difficulty
        val type = viewModel.gameData.value!!.type

        // Create Record Object
        val record = Record(
            0,
            name,
            difficulty.name,
            score,
            gameTime,
            currentTimeMillis,
            type
        )
        // Add Data to Database
        addRecord(record)
    }

    private fun addRecord(record: Record) {
        val db = Room.databaseBuilder(
            applicationContext,
            RecordDatabase::class.java, "record_database"
        ).allowMainThreadQueries().build()

        val recordDao = db.recordDao()

        recordDao.addRecord(record)
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
        soundPool.release()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}