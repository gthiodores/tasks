package sample.gthio.tasks.ui.route.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import sample.gthio.tasks.navigation.Screen
import sample.gthio.tasks.ui.route.taskList.TaskFilterQuery
import java.util.UUID

fun NavGraphBuilder.homeNavigation(
    toAddTask: () -> Unit,
    toAddGroup: () -> Unit,
    toTaskList: (TaskFilterQuery?, UUID?, UUID?) -> Unit,
) {
    composable(
        route = Screen.Home.route
    ) {
        HomeRoute(
            toAddTask = toAddTask,
            toAddGroup = toAddGroup,
            toTaskList = toTaskList
        )
    }
}