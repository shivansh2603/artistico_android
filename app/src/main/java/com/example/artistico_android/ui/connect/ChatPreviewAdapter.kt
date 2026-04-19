package com.example.artistico_android.ui.connect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_android.R
import com.example.artistico_android.core.common.TimeFormat
import com.example.artistico_android.databinding.ItemChatPreviewBinding
import com.example.artistico_android.domain.model.ChatPreview

class ChatPreviewAdapter : ListAdapter<ChatPreview, ChatPreviewAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemChatPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(private val binding: ItemChatPreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatPreview) {
            val ctx = binding.root.context
            binding.imgChatAvatar.setImageResource(chat.avatarRes ?: R.drawable.placeholder_avatar)
            binding.txtChatTitle.text = chat.title
            binding.txtChatLast.text = chat.lastMessage.orEmpty()
            binding.txtChatTime.text = chat.lastMessageAt
                ?.let { TimeFormat.relativeFromNow(it).asString(ctx) }
                .orEmpty()
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ChatPreview>() {
            override fun areItemsTheSame(oldItem: ChatPreview, newItem: ChatPreview) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ChatPreview, newItem: ChatPreview) = oldItem == newItem
        }
    }
}
