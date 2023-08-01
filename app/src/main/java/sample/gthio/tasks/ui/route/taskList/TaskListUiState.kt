package sample.gthio.tasks.ui.route.taskList

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTask
import java.util.*

data class TaskListUiState(
    val tasks: List<DomainTask> = emptyList(),
    val completedTasks: List<DomainTask> = emptyList(),
    val groups: List<DomainGroup> = emptyList(),
    val selectedGroupId: UUID? = null,
    val selectedTagId: UUID? = null,
    val shouldNavigateBack: Boolean = false
)

data class TaskListInputState(
    val shouldNavigateBack: Boolean = false
)