package com.example.artistico_android.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_android.R
import com.example.artistico_android.core.common.TimeFormat
import com.example.artistico_android.databinding.ItemCommentBinding
import com.example.artistico_android.domain.model.Comment

class CommentAdapter : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            val ctx = binding.root.context
            binding.imgCommentAvatar.setImageResource(
                comment.author.avatarRes ?: R.drawable.placeholder_avatar
            )
            binding.txtCommentAuthor.text = comment.author.displayName
            binding.txtCommentText.text = comment.text
            binding.txtCommentTime.text = TimeFormat.relativeFromNow(comment.createdAt).asString(ctx)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = oldItem == newItem
        }
    }
}
