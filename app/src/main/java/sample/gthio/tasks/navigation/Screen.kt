package sample.gthio.tasks.navigation

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object AddTask: Screen(route = "add_task_screen")
}