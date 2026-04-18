package com.example.artistico_anroid.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_anroid.R
import com.example.artistico_anroid.databinding.ItemExplorePostBinding
import com.example.artistico_anroid.domain.model.Post

class ExplorePostAdapter(
    private val onPostClick: (Post) -> Unit,
    private val onLikeClick: (Post) -> Unit
) : ListAdapter<Post, ExplorePostAdapter.PostViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemExplorePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onPostClick, onLikeClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(
        private val binding: ItemExplorePostBinding,
        private val onPostClick: (Post) -> Unit,
        private val onLikeClick: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.imgPost.setImageResource(post.imageRes)
            binding.imgAvatar.setImageResource(post.author.avatarRes ?: R.drawable.placeholder_avatar)
            binding.txtAuthor.text = post.author.displayName
            binding.imgCrown.isVisible = post.author.isPremium
            binding.txtTimestamp.text = RelativeTimeFormatter.format(binding.root.context, post.createdAt)

            binding.btnLike.setImageResource(
                if (post.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            )
            binding.btnLike.contentDescription = binding.root.context.getString(
                if (post.isLiked) R.string.cd_unlike else R.string.cd_like
            )
            binding.btnLike.setOnClickListener { onLikeClick(post) }
            binding.cardPost.setOnClickListener { onPostClick(post) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
        }
    }
}
