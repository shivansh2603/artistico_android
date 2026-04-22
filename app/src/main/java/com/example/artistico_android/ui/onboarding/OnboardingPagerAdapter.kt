package com.example.artistico_android.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.artistico_android.databinding.ItemOnboardingPageBinding

class OnboardingPagerAdapter(
    private val pages: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingPagerAdapter.PageVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageVH {
        val binding = ItemOnboardingPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PageVH(binding)
    }

    override fun onBindViewHolder(holder: PageVH, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    class PageVH(
        private val binding: ItemOnboardingPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(page: OnboardingPage) {
            binding.imgPage.setImageResource(page.imageRes)
            binding.txtTitle.setText(page.titleRes)
            binding.txtDescription.setText(page.descRes)
        }
    }
}
