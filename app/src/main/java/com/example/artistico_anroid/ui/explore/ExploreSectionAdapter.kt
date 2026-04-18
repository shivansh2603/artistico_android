package com.example.artistico_anroid.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_anroid.databinding.ItemExploreSectionBinding
import com.example.artistico_anroid.domain.model.ExploreSection
import com.example.artistico_anroid.domain.model.Post

class ExploreSectionAdapter(
    private val onPostClick: (Post) -> Unit,
    private val onLikeClick: (Post) -> Unit
) : ListAdapter<ExploreSection, ExploreSectionAdapter.SectionViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemExploreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding, onPostClick, onLikeClick)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(getItem(position), isFirst = position == 0)
    }

    class SectionViewHolder(
        private val binding: ItemExploreSectionBinding,
        private val onPostClick: (Post) -> Unit,
        private val onLikeClick: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val postAdapter = ExplorePostAdapter(onPostClick, onLikeClick)

        init {
            binding.recyclerPosts.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = postAdapter
                setHasFixedSize(true)
            }
        }

        fun bind(section: ExploreSection, isFirst: Boolean) {
            binding.txtScreenTitle.isVisible = isFirst
            binding.txtSectionTitle.text = section.title
            postAdapter.submitList(section.posts)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ExploreSection>() {
            override fun areItemsTheSame(oldItem: ExploreSection, newItem: ExploreSection) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ExploreSection, newItem: ExploreSection) =
                oldItem == newItem
        }
    }
}
