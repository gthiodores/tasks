package sample.gthio.tasks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sample.gthio.tasks.ui.route.addgroup.AddGroupRoute
import sample.gthio.tasks.ui.route.addtask.AddTaskRoute
import sample.gthio.tasks.ui.route.home.HomeRoute
import sample.gthio.tasks.ui.route.taskList.navigateToTaskList
import sample.gthio.tasks.ui.route.taskList.taskListNavigation
import java.util.*

@Composable
fun AppNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Home.route) {
            HomeRoute(
                toAddTask = { navController.navigate(route = Screen.AddTask.route) },
                toAddGroup = { navController.navigate(route = Screen.AddGroup.route) },
                toTaskList = { query, groupId ->
                    navController.navigateToTaskList(
                        groupId = groupId?.let { UUID.fromString(groupId) },
                        tagId = null,
                    )
                }
            )
        }
        composable(Screen.AddTask.route) {
            AddTaskRoute(
                onBack = { navController.navigateUp() }
            )
        }
        composable(Screen.AddGroup.route) {
            AddGroupRoute(
                onBack = { navController.navigateUp() }
            )
        }

        taskListNavigation(onBack = navController::navigateUp)
    }
}