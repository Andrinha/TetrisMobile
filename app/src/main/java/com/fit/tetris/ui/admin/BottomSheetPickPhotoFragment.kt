package com.fit.tetris.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fit.tetris.R
import com.fit.tetris.databinding.FragmentBottomsheetActionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetPickPhotoFragment : BottomSheetDialogFragment() {

    // Можно обойтись без биндинга и использовать findViewById
    lateinit var binding: FragmentBottomsheetActionBinding

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
        binding.apply {
            tvBtnEdit.setOnClickListener {

            }
            tvBtnRemove.setOnClickListener {

            }
        }
        return binding.root
    }

    companion object {
        fun newInstance(): BottomSheetPickPhotoFragment {
            return BottomSheetPickPhotoFragment()
        }
    }
}