package sample.gthio.tasks.ui.route.taskList

sealed interface TaskListEvent {
    object BackPressed: TaskListEvent
}