package sample.gthio.tasks.domain.model

import java.util.UUID

data class DomainGroup(
    val id: UUID = UUID.randomUUID(),
    val title: String,
)