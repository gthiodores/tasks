package sample.gthio.tasks.ui.route.home

import sample.gthio.tasks.ui.route.taskList.TaskFilterQuery
import java.util.UUID

sealed interface HomeNavigationTarget {
    object AddTask: HomeNavigationTarget
    object AddGroup: HomeNavigationTarget
    data class TaskList(val query: TaskFilterQuery?, val groupId: UUID?, val tagId: UUID?): HomeNavigationTarget
}