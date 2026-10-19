package com.example.s8162795assignment2.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.s8162795assignment2.databinding.ActivityLoginBinding
import com.example.s8162795assignment2.presentation.common.UiState
import com.example.s8162795assignment2.presentation.common.applySystemInsets
import com.example.s8162795assignment2.presentation.common.errorMessage
import com.example.s8162795assignment2.presentation.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemInsets()
        binding.loginButton.setOnClickListener { submit() }
        binding.passwordInput.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) { submit(); true } else false
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val busy = state == UiState.Loading
                    binding.progressBar.isVisible = busy
                    binding.loginButton.isEnabled = !busy
                    binding.usernameInput.isEnabled = !busy
                    binding.passwordInput.isEnabled = !busy
                    binding.errorText.isVisible = state is UiState.Error
                    binding.errorText.text = (state as? UiState.Error)?.let { errorMessage(it.error) }.orEmpty()
                    if (state is UiState.Success) {
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java).putExtra("keypass", state.data))
                        viewModel.navigationHandled()
                        binding.passwordInput.text?.clear()
                        finish()
                    }
                }
            }
        }
    }
    private fun submit() = viewModel.login(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
}
