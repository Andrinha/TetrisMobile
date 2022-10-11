package com.fit.tetris.ui.admin

import android.graphics.Color
import android.view.View
import android.widget.TableRow
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.data.Shape
import com.fit.tetris.databinding.ShapeItemBinding

class ShapeHolder(item: View): RecyclerView.ViewHolder(item) {
    private val binding = ShapeItemBinding.bind(item)
    fun bind(shape: Shape) = with(binding){
        textShapeName.text = shape.name
        var str = shape.tiles.toString(2)

        repeat(16 - str.length) {
            str = "0$str"
        }

        repeat(4) { j ->
            repeat(4) { i ->
                getView(i, j).setBackgroundColor(
                    if (str[j * 4 + i] == '1') Color.WHITE else Color.rgb(12,12,12)
                )
            }
        }
    }

    private fun getView(i: Int, j: Int): View {
        return (binding.table[i] as TableRow)[j]
    }
}