package sample.gthio.tasks.domain.model

import java.util.UUID

sealed interface TaskQuery {
    object isToday : TaskQuery
    object IsImportant : TaskQuery
    data class HasTagWithId(val id: List<UUID>) : TaskQuery
    data class HasGroupWithId(val ids: List<UUID>) : TaskQuery
}

data class TaskQueryModel(
    val isToday: Boolean = false,
    val isImportant: Boolean = false,
    val hasTagWithId: List<UUID> = emptyList(),
    val hasGroupWithId: List<UUID> = emptyList(),
) {
    fun isEmpty(): Boolean {
        return !isToday && !isImportant && hasTagWithId.isEmpty() && hasGroupWithId.isEmpty()
    }
}