package com.example.artistico_android.ui.auth

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.artistico_android.R
import com.example.artistico_android.core.ui.collectWhenStarted
import com.example.artistico_android.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTermsCheckbox()
        bindSignUpPrompt()

        binding.inputEmail.doAfterTextChanged { viewModel.onEmailChanged() }
        binding.inputPassword.doAfterTextChanged { viewModel.onPasswordChanged() }

        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClicked(
                email = binding.inputEmail.text?.toString().orEmpty(),
                password = binding.inputPassword.text?.toString().orEmpty(),
                termsAgreed = binding.checkTerms.isChecked
            )
        }

        // Placeholder taps
        binding.txtNeedHelp.setOnClickListener { toast(R.string.help_toast) }
        binding.txtForgot.setOnClickListener { toast(R.string.forgot_password_toast) }
        binding.btnGoogle.setOnClickListener { toast(R.string.google_signin_toast) }

        collectWhenStarted(viewModel.uiState) { state ->
            binding.tilEmail.error = state.emailError?.let { getString(it) }
            binding.tilPassword.error = state.passwordError?.let { getString(it) }

            binding.progressLogin.isVisible = state.isSubmitting
            binding.btnLogin.isEnabled = !state.isSubmitting
            binding.btnLogin.text = if (state.isSubmitting) "" else getString(R.string.action_login)

            state.formError?.let { showSnackbar(getString(it)) }

            if (state.loginSucceeded) {
                // Consume the flag immediately so a config change doesn't re-trigger this block.
                viewModel.onNavigationConsumed()
                saveCredentialAndNavigate(
                    email = binding.inputEmail.text?.toString()?.trim().orEmpty(),
                    password = binding.inputPassword.text?.toString().orEmpty()
                )
            }
        }
    }

    /** Builds the "I agree to all the [Terms & Conditions].*" label with a coloured+bold span. */
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

    /** Builds "New to Artistico? [Sign Up]" with a clickable maroon "Sign Up". */
    private fun bindSignUpPrompt() {
        val signUp = getString(R.string.action_sign_up)
        val full = getString(R.string.new_to_artistico, signUp)
        val start = full.indexOf(signUp)
        val spannable = SpannableStringBuilder(full)
        if (start >= 0) {
            val end = start + signUp.length
            val color = ContextCompat.getColor(requireContext(), R.color.brand_maroon)
            spannable.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.txtSignupPrompt.text = spannable
        binding.txtSignupPrompt.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_sign_up)
        }
    }

    /**
     * Offers the credential to Google Password Manager (or any other Credential Manager
     * provider). The save dialog is modal over this Fragment — we await the result,
     * then navigate regardless of whether the user saved, dismissed, or errored.
     */
    private fun saveCredentialAndNavigate(email: String, password: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val request = CreatePasswordRequest(id = email, password = password)
                CredentialManager.create(requireContext())
                    .createCredential(requireActivity(), request)
            } catch (e: CreateCredentialException) {
                // User dismissed, no provider available, etc. — don't block login.
                Timber.w(e, "Credential save skipped")
            } catch (e: Exception) {
                Timber.w(e, "Credential save failed")
            }
            if (!isAdded) return@launch
            navigateToExplore()
        }
    }

    private fun navigateToExplore() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.nav_login, inclusive = true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.nav_explore, null, options)
    }

    private fun toast(resId: Int) {
        Snackbar.make(binding.root, getString(resId), Snackbar.LENGTH_SHORT).show()
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
