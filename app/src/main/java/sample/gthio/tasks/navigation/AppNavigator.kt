package sample.gthio.tasks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import sample.gthio.tasks.ui.route.addgroup.addGroupNavigation
import sample.gthio.tasks.ui.route.addgroup.navigateToAddGroup
import sample.gthio.tasks.ui.route.addtask.addTaskNavigation
import sample.gthio.tasks.ui.route.addtask.navigateToAddTask
import sample.gthio.tasks.ui.route.home.homeNavigation
import sample.gthio.tasks.ui.route.taskList.navigateToTaskList
import sample.gthio.tasks.ui.route.taskList.taskListNavigation

@Composable
fun AppNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        homeNavigation(
            toAddTask = navController::navigateToAddTask,
            toAddGroup = navController::navigateToAddGroup,
            toTaskList = { filterQuery, groupId, tagId ->
                navController.navigateToTaskList(
                    filterQuery = filterQuery,
                    groupId = groupId,
                    tagId = tagId,
                )
            }
        )

        addTaskNavigation(onBack = navController::navigateUp)

        addGroupNavigation(onBack = navController::navigateUp)

        taskListNavigation(onBack = navController::navigateUp)
    }
}