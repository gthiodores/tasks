package sample.gthio.tasks.ui.route.taskList

import androidx.navigation.*
import androidx.navigation.compose.composable
import sample.gthio.tasks.navigation.Screen
import java.util.*

fun NavGraphBuilder.taskListNavigation(
    onBack: () -> Unit,
) {
    composable(
        route = Screen.TaskList.route.plus("?groupId={groupId}&tagId={tagId}"),
        arguments = listOf(
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
    groupId: UUID? = null,
    tagId: UUID? = null,
    filterQuery: TaskFilterQuery = TaskFilterQuery.ALL,
    navOptions: NavOptions? = null,
) {
    val routeBuilder = StringBuilder(Screen.TaskList.route)
        .letAppend(groupId) { value -> append("?groupId=$value") }
        .letAppend(tagId) { value -> append("&tagId=$value") }

    navigate(
        route = Screen.TaskList.route.plus(routeBuilder.toString()),
        navOptions = navOptions
    )
}

fun <T> StringBuilder.letAppend(
    value: T?,
    f: StringBuilder.(T) -> StringBuilder,
): StringBuilder {
    return when (value) {
        null -> this
        else -> f(value)
    }
}