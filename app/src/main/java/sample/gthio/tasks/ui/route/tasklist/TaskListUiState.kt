package sample.gthio.tasks.ui.route.tasklist

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTask

data class TaskListUiState(
    val tasks: List<DomainTask> = emptyList(),
    val selectedGroup: DomainGroup? = null,
    val shouldNavigateBack: Boolean = false
)

data class TaskListInputState(
    val selectedGroup: DomainGroup? = null,
    val shouldNavigateBack: Boolean = false
)