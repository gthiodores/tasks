package sample.gthio.tasks.domain.model

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

data class DomainTask(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val date: LocalDate = Clock
        .System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date,
    val time: LocalTime = Clock
        .System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .time,
    val tags: List<DomainTag> = emptyList(),
    val isImportant: Boolean = false,
    val group: DomainGroup
)