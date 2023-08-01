package sample.gthio.tasks.ui.route.taskList

import sample.gthio.tasks.domain.model.DomainTask

sealed interface TaskListEvent {
    object BackPressed: TaskListEvent
    data class TaskFinishClick(val task: DomainTask): TaskListEvent
}