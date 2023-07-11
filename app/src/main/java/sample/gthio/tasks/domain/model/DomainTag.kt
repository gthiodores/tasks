package sample.gthio.tasks.domain.model

import java.util.*

data class DomainTag(
    val id: UUID = UUID.randomUUID(),
    val title: String,
)