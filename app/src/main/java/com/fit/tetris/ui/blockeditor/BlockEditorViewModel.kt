package com.fit.tetris.ui.blockeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlockEditorViewModel: ViewModel() {
    fun invertTile(i: Int) {
        val t = tiles.value!!
        t[i / 4][i % 4] = t[i / 4][i % 4].not()
        tiles.value = t
    }

    var tiles: MutableLiveData<Array<BooleanArray>> = MutableLiveData(Array(4) { BooleanArray(4) })
}