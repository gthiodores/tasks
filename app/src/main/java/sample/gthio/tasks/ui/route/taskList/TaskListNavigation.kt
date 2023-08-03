package sample.gthio.tasks.ui.route.taskList

import androidx.navigation.*
import androidx.navigation.compose.composable
import sample.gthio.tasks.navigation.Screen
import sample.gthio.tasks.ui.extension.letAppend
import java.util.*

fun NavGraphBuilder.taskListNavigation(
    onBack: () -> Unit,
) {
    composable(
        route = Screen.TaskList.route.plus("?filterQuery={filterQuery}&groupId={groupId}&tagId={tagId}"),
        arguments = listOf(
            navArgument("filterQuery") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument("groupId") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument("tagId") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    ) {
        TaskListRoute(
            onBack = onBack
        )
    }
}

fun NavController.navigateToTaskList(
    filterQuery: TaskFilterQuery? = TaskFilterQuery.ALL,
    groupId: UUID? = null,
    tagId: UUID? = null,
    navOptions: NavOptions? = null,
) {
    val route = StringBuilder(Screen.TaskList.route)
        .letAppend(filterQuery) { value -> append("?filterQuery=${value.arg}") }
        .letAppend(groupId) { value -> append("&groupId=$value") }
        .letAppend(tagId) { value -> append("&tagId=$value") }
        .toString()

    navigate(
        route = route,
        navOptions = navOptions
    )
}