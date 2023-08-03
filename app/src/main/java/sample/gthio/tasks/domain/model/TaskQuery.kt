package sample.gthio.tasks.domain.model

import java.util.UUID

sealed interface TaskQuery {
    object isToday: TaskQuery
    object IsImportant: TaskQuery
    data class HasTagWithId(val id: UUID): TaskQuery
    data class HasGroupWithId(val id: UUID): TaskQuery
}