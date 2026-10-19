package com.example.s8162795assignment2.presentation.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.example.s8162795assignment2.R
import com.example.s8162795assignment2.domain.model.EntityField
import java.util.Locale

/** Presentation-only illustrations. API fields remain the source of entity data. */
object FoodImages {
    private fun normalise(value: String): String = value.lowercase(Locale.ROOT)
        .filter { it.isLetterOrDigit() }

    @DrawableRes
    fun resourceFor(fields: List<EntityField>): Int? {
        // Match the semantic field, never a RecyclerView position or description.
        val dish = fields.firstOrNull { normalise(it.name) == "dishname" }
            ?: return null
        return when (normalise(dish.value)) {
            "sushi" -> R.drawable.food_sushi
            "pizza" -> R.drawable.food_pizza
            "tacos", "taco" -> R.drawable.food_tacos
            "croissant" -> R.drawable.food_croissant
            "padthai" -> R.drawable.food_pad_thai
            "hamburger", "burger" -> R.drawable.food_hamburger
            "curry" -> R.drawable.food_curry
            else -> null
        }
    }

    fun bind(view: ImageView, fields: List<EntityField>) {
        val resource = resourceFor(fields)
        // Clear recycled images even when the next record has no matching picture.
        if (resource == null) view.setImageDrawable(null)
        else view.setImageResource(resource)
        view.isVisible = resource != null
    }
}
