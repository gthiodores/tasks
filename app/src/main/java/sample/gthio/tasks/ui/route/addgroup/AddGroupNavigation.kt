package sample.gthio.tasks.ui.route.addgroup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import sample.gthio.tasks.navigation.Screen

fun NavGraphBuilder.addGroupNavigation(
    onBack: () -> Unit
) {
    composable(Screen.AddGroup.route) {
        AddGroupRoute(
            onBack = onBack
        )
    }
}

fun NavController.navigateToAddGroup(
    navOptions: NavOptions? = null
) {
    this.navigate(
        Screen.AddGroup.route,
        navOptions = navOptions
    )
}