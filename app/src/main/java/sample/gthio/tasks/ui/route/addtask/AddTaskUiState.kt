package sample.gthio.tasks.ui.route.addtask

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag

data class AddTaskUiState(
    val title: String = "",
    val description: String = "",
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
    val isImportant: Boolean = false,
    val selectedTags: List<DomainTag> = emptyList(),
    val selectedGroup: DomainGroup? = null,
    val shouldNavigateBack: Boolean = false,
)