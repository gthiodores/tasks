package sample.gthio.tasks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sample.gthio.tasks.ui.route.addtask.AddTaskRoute
import sample.gthio.tasks.ui.route.home.HomeRoute

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
                toAddTask = { navController.navigate(route = Screen.AddTask.route) }
            )
        }
        composable(Screen.AddTask.route) {
            AddTaskRoute(
                onBack = { navController.navigateUp() }
            )
        }
    }
}