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
    val newTag: String = "",
    val selectedGroup: DomainGroup? = null,
    val isDateOpen: Boolean = false,
    val isTimeOpen: Boolean = false,
    val isTagOpen: Boolean = false,
    val tags: List<DomainTag> = emptyList(),
    val groups: List<DomainGroup> = emptyList(),
    val shouldNavigateBack: Boolean = false,
) {
    fun fromInputState(input: AddTaskInputState): AddTaskUiState =
        AddTaskUiState(
            title = input.title,
            description = input.description,
            date = input.date,
            time = input.time,
            isImportant = input.isImportant,
            selectedTags = input.selectedTags,
            newTag = input.newTag,
            selectedGroup = input.selectedGroup,
            isDateOpen = input.isDateOpen,
            isTimeOpen = input.isTimeOpen,
            isTagOpen = input.isTagOpen,
            shouldNavigateBack = input.shouldNavigateBack
        )
}

data class AddTaskInputState(
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
    val newTag: String = "",
    val selectedGroup: DomainGroup? = null,
    val isDateOpen: Boolean = false,
    val isTimeOpen: Boolean = false,
    val isTagOpen: Boolean = false,
    val shouldNavigateBack: Boolean = false,
)