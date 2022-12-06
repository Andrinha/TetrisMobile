package com.fit.tetris.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fit.tetris.adapters.RecordAdapter
import com.fit.tetris.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScoreViewModel by lazy {
        ViewModelProvider(this)[ScoreViewModel::class.java]
    }
    private val adapter = RecordAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)

        viewModel.readAllData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        binding.apply {
            recyclerRecords.layoutManager = LinearLayoutManager(requireContext())
            recyclerRecords.adapter = adapter
        }

        return binding.root
    }
}
