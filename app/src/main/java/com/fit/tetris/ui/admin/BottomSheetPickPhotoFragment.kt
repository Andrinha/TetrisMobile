package com.fit.tetris.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.fit.tetris.R
import com.fit.tetris.data.shape.ShapeDatabase
import com.fit.tetris.databinding.FragmentBottomsheetActionBinding
import com.fit.tetris.ui.blockeditor.BlockEditorActivity
import com.fit.tetris.utils.BaseShapes
import com.fit.tetris.utils.toBooleanArray
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class BottomSheetPickPhotoFragment : BottomSheetDialogFragment() {

    // Можно обойтись без биндинга и использовать findViewById
    lateinit var binding: FragmentBottomsheetActionBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[BottomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetActionBinding.bind(
            inflater.inflate(
                R.layout.fragment_bottomsheet_action,
                container
            )
        )
        val position =  arguments?.getInt("position")
        binding.apply {
            tvBtnEdit.setOnClickListener {
                val tiles = viewModel.shapes[position!!].tiles
                val id = viewModel.shapes[position].shapeId
                val intent = Intent(requireContext(), BlockEditorActivity::class.java)
                intent.putExtra("tiles", tiles)
                intent.putExtra("id", id)
                startActivity(intent)
            }
            tvBtnRemove.setOnClickListener {
                showDeleteDialog(it.context, viewModel.shapes[position!!].shapeId)
                this@BottomSheetPickPhotoFragment.dismiss()
            }
        }
        viewModel.shapesData.observe(this) {
            viewModel.shapes = it
        }
        return binding.root
    }

    private fun deleteItem(context: Context, id: Int) {
        val db = Room.databaseBuilder(
            context,
            ShapeDatabase::class.java, "shape_database"
        ).allowMainThreadQueries().build()

        val groupDao = db.shapeDao()
        groupDao.deleteItem(id)
    }

    private fun showDeleteDialog(context: Context, id: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Вы действительно хотите удалить фигуру?")
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                deleteItem(context, id)
                dismiss()
            }
            .show()
    }
}