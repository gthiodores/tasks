package sample.gthio.tasks.ui.route.home

sealed interface HomeNavigationTarget {
    object AddTask: HomeNavigationTarget
    object AddGroup: HomeNavigationTarget
    object TaskList: HomeNavigationTarget
}