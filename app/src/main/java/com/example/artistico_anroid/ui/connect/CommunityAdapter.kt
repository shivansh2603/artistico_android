package com.example.artistico_anroid.ui.connect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_anroid.R
import com.example.artistico_anroid.databinding.ItemCommunityBinding
import com.example.artistico_anroid.domain.model.Community

class CommunityAdapter : ListAdapter<Community, CommunityAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(private val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(community: Community) {
            binding.imgCommunity.setImageResource(community.coverRes ?: R.drawable.placeholder_avatar_artistica)
            binding.txtCommunityName.text = community.name
            binding.txtCommunityTag.text = "#${community.tag} · ${community.memberCount} members"
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Community>() {
            override fun areItemsTheSame(oldItem: Community, newItem: Community) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Community, newItem: Community) = oldItem == newItem
        }
    }
}
