package sample.gthio.tasks.ui.route.taskList

import sample.gthio.tasks.domain.model.DomainTask
import java.util.*

sealed interface TaskListEvent {
    object BackPressed : TaskListEvent
    data class TaskFinishClick(val task: DomainTask) : TaskListEvent
    object FilterAllTag : TaskListEvent
    data class FilterByTag(val tagId: UUID) : TaskListEvent
    data class FilterByGroup(val groupId: UUID) : TaskListEvent
    object FilterButtonClick : TaskListEvent
    object DismissFilter : TaskListEvent
    object SaveFilter : TaskListEvent
    object ResetFilter : TaskListEvent
}