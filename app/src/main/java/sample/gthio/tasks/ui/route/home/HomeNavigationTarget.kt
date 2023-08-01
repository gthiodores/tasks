package sample.gthio.tasks.ui.route.home

sealed interface HomeNavigationTarget {
    object AddTask: HomeNavigationTarget
    object AddGroup: HomeNavigationTarget
    data class TaskList(val query: String?, val groupId: String?): HomeNavigationTarget
}