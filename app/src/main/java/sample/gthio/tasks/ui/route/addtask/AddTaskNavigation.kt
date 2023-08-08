package sample.gthio.tasks.ui.route.addtask

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import sample.gthio.tasks.navigation.Screen

fun NavGraphBuilder.addTaskNavigation(
    onBack: () -> Unit
) {
    composable(Screen.AddTask.route) {
        AddTaskRoute(
            onBack = onBack
        )
    }
}

fun NavController.navigateToAddTask(
    navOptions: NavOptions? = null
) {
    this.navigate(route = Screen.AddTask.route)
}