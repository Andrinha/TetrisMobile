package com.fit.tetris.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fit.tetris.ui.statistics.ScoreFragment
import com.fit.tetris.ui.statistics.TimeFragment


class ViewStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        // Hardcoded in this order, you'll want to use lists and make sure the titles match
        return if (position == 0) {
            ScoreFragment()
        } else TimeFragment()
    }

    override fun getItemCount(): Int {
        // Hardcoded, use lists
        return 2
    }
}