package com.example.artistico_android.ui.auth

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.artistico_android.R
import com.example.artistico_android.core.ui.collectWhenStarted
import com.example.artistico_android.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTermsCheckbox()
        bindLoginPrompt()

        binding.inputName.doAfterTextChanged { viewModel.onNameChanged() }
        binding.inputEmail.doAfterTextChanged { viewModel.onEmailChanged() }
        binding.inputPassword.doAfterTextChanged { viewModel.onPasswordChanged() }
        binding.inputConfirm.doAfterTextChanged { viewModel.onConfirmPasswordChanged() }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnHelp.setOnClickListener { toast(R.string.help_toast) }

        binding.btnSignUp.setOnClickListener {
            viewModel.onSignUpClicked(
                name = binding.inputName.text?.toString().orEmpty(),
                email = binding.inputEmail.text?.toString().orEmpty(),
                password = binding.inputPassword.text?.toString().orEmpty(),
                confirmPassword = binding.inputConfirm.text?.toString().orEmpty(),
                termsAgreed = binding.checkTerms.isChecked
            )
        }

        collectWhenStarted(viewModel.uiState) { state ->
            binding.tilName.error = state.nameError?.let { getString(it) }
            binding.tilEmail.error = state.emailError?.let { getString(it) }
            binding.tilPassword.error = state.passwordError?.let { getString(it) }
            binding.tilConfirm.error = state.confirmPasswordError?.let { getString(it) }

            binding.progressSignUp.isVisible = state.isSubmitting
            binding.btnSignUp.isEnabled = !state.isSubmitting
            binding.btnSignUp.text = if (state.isSubmitting) "" else getString(R.string.action_sign_up)

            state.formError?.let { toast(it) }

            if (state.signUpPreviewShown) {
                toast(R.string.signup_preview_toast)
                viewModel.onPreviewConsumed()
            }
        }
    }

    private fun bindTermsCheckbox() {
        val terms = getString(R.string.terms_and_conditions)
        val full = getString(R.string.terms_agreement, terms)
        val start = full.indexOf(terms)
        val spannable = SpannableStringBuilder(full)
        if (start >= 0) {
            val end = start + terms.length
            val color = ContextCompat.getColor(requireContext(), R.color.brand_maroon)
            spannable.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.checkTerms.text = spannable
    }

    private fun bindLoginPrompt() {
        val login = getString(R.string.action_login)
        val full = getString(R.string.already_have_account, login)
        val start = full.indexOf(login)
        val spannable = SpannableStringBuilder(full)
        if (start >= 0) {
            val end = start + login.length
            val color = ContextCompat.getColor(requireContext(), R.color.brand_maroon)
            spannable.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.txtLoginPrompt.text = spannable
        binding.txtLoginPrompt.setOnClickListener { findNavController().navigateUp() }
    }

    private fun toast(resId: Int) {
        Snackbar.make(binding.root, getString(resId), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
