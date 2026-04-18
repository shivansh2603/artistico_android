package com.example.artistico_anroid.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artistico_anroid.R
import com.example.artistico_anroid.core.common.TimeFormat
import com.example.artistico_anroid.core.ui.collectWhenStarted
import com.example.artistico_anroid.databinding.FragmentPostDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostDetailsViewModel by viewModels()
    private val commentAdapter = CommentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnOverflow.setOnClickListener { anchor ->
            PopupMenu(requireContext(), anchor).apply {
                menuInflater.inflate(R.menu.menu_post_overflow, menu)
                setOnMenuItemClickListener { item ->
                    Snackbar.make(binding.root, item.title ?: "", Snackbar.LENGTH_SHORT).show()
                    true
                }
            }.show()
        }

        binding.btnLike.setOnClickListener { viewModel.onLikeClicked() }

        binding.btnSend.setOnClickListener {
            val text = binding.inputComment.text?.toString().orEmpty()
            if (text.isNotBlank()) {
                viewModel.onSendComment(text)
                binding.inputComment.setText("")
            }
        }
        binding.inputComment.doAfterTextChanged { editable ->
            binding.btnSend.alpha = if (editable.isNullOrBlank()) 0.4f else 1f
        }
        binding.btnSend.alpha = 0.4f

        collectWhenStarted(viewModel.uiState) { state ->
            val post = state.post ?: return@collectWhenStarted

            binding.imgAuthor.setImageResource(post.author.avatarRes ?: R.drawable.placeholder_avatar)
            binding.txtAuthorName.text = post.author.displayName
            binding.txtAuthorTimestamp.text =
                TimeFormat.relativeFromNow(post.createdAt).asString(requireContext())

            binding.imgPost.setImageResource(post.imageRes)

            binding.btnLike.setImageResource(
                if (post.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            )
            binding.btnLike.contentDescription = getString(
                if (post.isLiked) R.string.cd_unlike else R.string.cd_like
            )

            val hashtagText = post.hashtags.joinToString(" ") { "#$it" }
            val desc = listOfNotNull(post.description?.takeIf { it.isNotBlank() }, hashtagText.takeIf { it.isNotBlank() })
                .joinToString("\n\n")
            binding.txtDescription.text = desc.ifBlank { hashtagText }

            binding.txtCommentsEmpty.isVisible = state.showEmptyComments
            if (state.showEmptyComments) {
                binding.txtCommentsEmpty.text =
                    getString(R.string.comments_empty, post.author.displayName)
            }

            binding.recyclerComments.isVisible = state.comments.isNotEmpty()
            commentAdapter.submitList(state.comments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerComments.adapter = null
        _binding = null
    }
}
