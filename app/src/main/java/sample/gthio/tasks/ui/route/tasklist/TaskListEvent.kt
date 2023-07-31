package sample.gthio.tasks.ui.route.tasklist

sealed interface TaskListEvent {
    object BackPressed: TaskListEvent
}