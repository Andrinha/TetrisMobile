package com.fit.tetris.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.R
import com.fit.tetris.data.shape.Shape
import com.fit.tetris.databinding.ShapeItemBinding
import java.util.*

class ShapeAdapter: RecyclerView.Adapter<ShapeAdapter.ShapeHolder>() {
    private var shapeList = emptyList<Shape>()
    private lateinit var listener: (Shape) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShapeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shape_item, parent, false)
        return ShapeHolder(view)
    }

    override fun onBindViewHolder(holder: ShapeHolder, position: Int) {
        holder.bind(shapeList[position])
        //holder.itemView.setOnClickListener { listener(shapeList[position]) }
    }

    override fun getItemCount(): Int {
        return shapeList.size
    }

    fun setOnClickListener(listener: (Shape) -> Unit) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(shape: List<Shape>) {
        this.shapeList = shape
        notifyDataSetChanged()
    }

    class ShapeHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ShapeItemBinding.bind(item)
        fun bind(shape: Shape) = with(binding){
//            var r = 0
//            var g = 0
//            var b = 0
//            while (r + g + b !in 255 .. 512) {
//                r = Random().nextInt(256)
//                g = Random().nextInt(256)
//                b = Random().nextInt(256)
//            }
            val bg = itemView.context.getColor(R.color.color_12_light) //if (shape.selected) itemView.context.getColor(R.color.color_15) else itemView.context.getColor(R.color.color_12_light)
            val fg = Color.rgb(shape.r, shape.g, shape.b)
            var str = shape.tiles.toString(2)
            textShapeName.text = shape.name
            repeat(16 - str.length) {
                str = "0$str"
            }

            repeat(4) { j ->
                repeat(4) { i ->
                    getView(i, j).setBackgroundColor(
                        if (str[j * 4 + i] == '1') fg else bg
                    )
                }
            }
            binding.cardMain.setCardBackgroundColor(if (shape.selected) itemView.context.getColor(R.color.color_15) else itemView.context.getColor(R.color.color_12_light))
        }

        private fun getView(i: Int, j: Int): View {
            return (binding.table[i] as TableRow)[j]
        }
    }
}