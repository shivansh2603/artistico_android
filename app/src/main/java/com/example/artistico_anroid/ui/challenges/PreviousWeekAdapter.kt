package com.example.artistico_anroid.ui.challenges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_anroid.databinding.ItemPreviousWeekBinding
import com.example.artistico_anroid.domain.model.PreviousWeek

class PreviousWeekAdapter : ListAdapter<PreviousWeek, PreviousWeekAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPreviousWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(private val binding: ItemPreviousWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(week: PreviousWeek) {
            binding.imgWeekCover.setImageResource(week.coverRes)
            binding.txtWeekNumber.text = "Week ${week.weekNumber}"
            binding.txtWeekTheme.text = week.theme
            binding.txtWeekRange.text = week.dateRangeLabel
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PreviousWeek>() {
            override fun areItemsTheSame(oldItem: PreviousWeek, newItem: PreviousWeek) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PreviousWeek, newItem: PreviousWeek) = oldItem == newItem
        }
    }
}
