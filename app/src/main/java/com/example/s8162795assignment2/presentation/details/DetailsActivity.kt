package com.example.s8162795assignment2.presentation.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.s8162795assignment2.R
import com.example.s8162795assignment2.databinding.ItemDetailFieldBinding
import com.example.s8162795assignment2.databinding.ActivityDetailsBinding
import com.example.s8162795assignment2.domain.model.Entity
import com.example.s8162795assignment2.presentation.common.applySystemInsets
import com.example.s8162795assignment2.presentation.common.fieldLabel

/** Details renders a selected snapshot; it does not make a second network call. */
class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemInsets()
        binding.backButton.setOnClickListener { finish() }
        val names = intent.getStringArrayListExtra(NAMES)
        val values = intent.getStringArrayListExtra(VALUES)
        if (names == null || values == null || names.size != values.size) {
            binding.detailTitle.setText(R.string.details_unavailable)
            return
        }
        binding.detailTitle.text = names.indices.firstOrNull { !names[it].equals("description", true) }
            ?.let { values[it] }?.takeIf { it.isNotBlank() } ?: getString(R.string.entity_details)
        // Every original field is rendered, including the full description.
        names.indices.forEach { index ->
            // Inflate a shared XML component so all fields share spacing and typography.
            // Values remain selectable and unrestricted in length, including description.
            val field = ItemDetailFieldBinding.inflate(layoutInflater, binding.fieldsContainer, false)
            field.fieldLabel.text = names[index].fieldLabel()
            field.fieldValue.text = values[index]
            binding.fieldsContainer.addView(field.root)
        }
    }
    companion object {
        private const val NAMES = "entity_names"
        private const val VALUES = "entity_values"
        fun open(context: Context, entity: Entity) {
            context.startActivity(Intent(context, DetailsActivity::class.java)
                .putStringArrayListExtra(NAMES, ArrayList(entity.fields.map { it.name }))
                .putStringArrayListExtra(VALUES, ArrayList(entity.fields.map { it.value })))
        }
    }
}
