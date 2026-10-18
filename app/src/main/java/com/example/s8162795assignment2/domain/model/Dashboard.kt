package com.example.s8162795assignment2.domain.model

/** Pure Kotlin models: no Android, Gson or Retrofit types cross this boundary. */
data class EntityField(val name: String, val value: String)
data class Entity(val fields: List<EntityField>) {
    val summaryFields: List<EntityField>
        get() = fields.filterNot { it.name.equals("description", ignoreCase = true) }
}
data class Dashboard(val entities: List<Entity>, val entityTotal: Int)
