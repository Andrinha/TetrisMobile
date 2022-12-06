package com.fit.tetris.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fit.tetris.R
import com.fit.tetris.data.record.Record
import com.fit.tetris.databinding.ItemRecordBinding
import com.fit.tetris.utils.toDateTime
import java.util.*

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordHolder>() {
    private var recordList = emptyList<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return RecordHolder(view)
    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        holder.bind(recordList[position])
        holder.binding.textId.text = (position + 1).toString()
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(record: List<Record>) {
        this.recordList = record
        notifyDataSetChanged()
    }

    class RecordHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemRecordBinding.bind(item)
        fun bind(record: Record) = with(binding) {
            textName.text = record.name
            textDifficulty.text = record.difficulty
            textScore.text = if (record.type == 0) "Score: ${record.score}" else "Time: ${record.time}"
            textDate.text = record.date.toDateTime()
        }
    }
}