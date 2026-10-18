package com.example.s8162795assignment2.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s8162795assignment2.R
import com.example.s8162795assignment2.databinding.ActivityDashboardBinding
import com.example.s8162795assignment2.presentation.common.UiState
import com.example.s8162795assignment2.presentation.common.applySystemInsets
import com.example.s8162795assignment2.presentation.common.errorMessage
import com.example.s8162795assignment2.presentation.common.fieldLabel
import com.example.s8162795assignment2.presentation.details.DetailsActivity
import com.example.s8162795assignment2.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemInsets()
        binding.topicText.text = viewModel.topic.fieldLabel()
        val adapter = EntityAdapter { entity -> DetailsActivity.open(this, entity) }
        binding.entityList.layoutManager = LinearLayoutManager(this)
        binding.entityList.adapter = adapter
        binding.retryButton.setOnClickListener { viewModel.load() }
        binding.refreshButton.setOnClickListener { viewModel.load() }
        binding.logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.isVisible = state == UiState.Loading
                    binding.refreshButton.isEnabled = state != UiState.Loading
                    binding.errorGroup.isVisible = state is UiState.Error
                    binding.entityList.isVisible = state is UiState.Success
                    binding.emptyText.isVisible = state is UiState.Success && state.data.entities.isEmpty()
                    binding.countText.isVisible = state is UiState.Success
                    when (state) {
                        is UiState.Success -> {
                            adapter.submitList(state.data.entities)
                            binding.countText.text = getString(R.string.entity_count, state.data.entities.size, state.data.entityTotal)
                        }
                        is UiState.Error -> binding.errorText.text = errorMessage(state.error)
                        else -> Unit
                    }
                }
            }
        }
    }
}
