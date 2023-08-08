package sample.gthio.tasks.ui.route.taskList

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQueryModel
import sample.gthio.tasks.ui.extension.addOrRemoveDuplicate
import java.util.UUID

data class TaskListUiState(
    val tasks: List<DomainTask> = emptyList(),
    val completedTasks: List<DomainTask> = emptyList(),
    val groups: List<DomainGroup> = emptyList(),
    val tags: List<DomainTag> = emptyList(),
    val selectedGroupId: List<UUID> = emptyList(),
    val selectedTagId: List<UUID> = emptyList(),
    val isFilterOpen: Boolean = false,
    val shouldNavigateBack: Boolean = false,
)

data class TaskListInputState(
    val filterQuery: TaskFilterQuery = TaskFilterQuery.ALL,
    val selectedGroupId: List<UUID> = emptyList(),
    val selectedTagId: List<UUID> = emptyList(),
    val isFilterOpen: Boolean = false,
    val shouldNavigateBack: Boolean = false
) {
    fun selectGroup(groupId: UUID): TaskListInputState = copy(
        selectedGroupId = selectedGroupId
            .addOrRemoveDuplicate(groupId) { a, b -> a == b }
    )

    fun selectTag(tagId: UUID): TaskListInputState = copy(
        selectedTagId = selectedTagId
            .addOrRemoveDuplicate(tagId) { a, b -> a == b }
    )

    fun toQuery(): TaskQueryModel = TaskQueryModel(
        isToday = filterQuery == TaskFilterQuery.TODAY,
        isImportant = filterQuery == TaskFilterQuery.IMPORTANT,
        hasGroupWithId = selectedGroupId,
        hasTagWithId = selectedTagId
    )
}