package sample.gthio.tasks.ui.route.taskList

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTask
import java.util.UUID

data class TaskListUiState(
    val tasks: Map<String, List<DomainTask>> = mapOf(),
    val groups: List<DomainGroup> = emptyList(),
    val query: String? = null,
    val selectedGroupId: UUID? = null,
    val shouldNavigateBack: Boolean = false
)

data class TaskListInputState(
    val query: String? = null,
    val selectedGroupId: UUID? = null,
    val shouldNavigateBack: Boolean = false
)