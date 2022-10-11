package com.fit.tetris.ui.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.R
import com.fit.tetris.data.Shape

class ShapeAdapter: RecyclerView.Adapter<ShapeHolder>() {
    private var shapeList = emptyList<Shape>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShapeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shape_item, parent, false)
        return ShapeHolder(view)
    }

    override fun onBindViewHolder(holder: ShapeHolder, position: Int) {
        holder.bind(shapeList[position])
    }

    override fun getItemCount(): Int {
        return shapeList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addShape(shape: List<Shape>) {
        this.shapeList = shape
        notifyDataSetChanged()
    }
}