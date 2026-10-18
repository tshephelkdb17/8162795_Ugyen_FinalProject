package com.example.s8162795assignment2.presentation.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.s8162795assignment2.R
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
            val label = TextView(this).apply {
                text = names[index].fieldLabel()
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge)
                setTextColor(ContextCompat.getColor(context, R.color.accent))
                setPadding(0, dp(24), 0, dp(8))
            }
            val value = TextView(this).apply {
                text = values[index]
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
                setTextColor(ContextCompat.getColor(context, R.color.ink))
                setTextIsSelectable(true)
                setLineSpacing(dp(4).toFloat(), 1f)
            }
            binding.fieldsContainer.addView(label)
            binding.fieldsContainer.addView(value, LinearLayout.LayoutParams(-1, -2))
        }
    }
    private fun dp(value: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics).toInt()
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
