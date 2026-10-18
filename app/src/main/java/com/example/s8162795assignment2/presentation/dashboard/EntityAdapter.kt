package com.example.s8162795assignment2.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s8162795assignment2.R
import com.example.s8162795assignment2.databinding.ItemEntityBinding
import com.example.s8162795assignment2.domain.model.Entity
import com.example.s8162795assignment2.presentation.common.fieldLabel

class EntityAdapter(private val onClick: (Entity) -> Unit) :
    ListAdapter<Entity, EntityAdapter.Holder>(Diff) {
    class Holder(val binding: ItemEntityBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val entity = getItem(position)
        val fields = entity.summaryFields
        with(holder.binding) {
            entityTitle.text = fields.firstOrNull()?.value?.takeIf { it.isNotBlank() }
                ?: root.context.getString(R.string.untitled_entity)
            entitySummary.text = fields.joinToString("\n") { "${it.name.fieldLabel()}: ${it.value}" }
                .ifBlank { root.context.getString(R.string.no_summary) }
            root.setOnClickListener { onClick(entity) }
        }
    }
    private object Diff : DiffUtil.ItemCallback<Entity>() {
        // No stable entity ID is documented by the API. Structural identity is
        // sufficient for this read-only list; never pretend positions are IDs.
        override fun areItemsTheSame(oldItem: Entity, newItem: Entity) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Entity, newItem: Entity) = oldItem == newItem
    }
}
