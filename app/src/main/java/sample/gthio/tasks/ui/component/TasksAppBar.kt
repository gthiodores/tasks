import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textBlack

@OptIn(ExperimentalMaterial3Api::class)
object TasksAppBarDefaults {
    @Composable
    fun centerAppBarColors(): TopAppBarColors {
        return TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = surfaceGray,
            titleContentColor = textBlack
        )
    }
    
    @Composable
    fun appBarColors() : TopAppBarColors {
        return TopAppBarDefaults.topAppBarColors(
            containerColor = surfaceGray,
            titleContentColor = textBlack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    colors: TopAppBarColors = TasksAppBarDefaults.appBarColors(),
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        actions = actions,
        navigationIcon = navigationIcon,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCenterAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    colors: TopAppBarColors = TasksAppBarDefaults.centerAppBarColors(),
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = colors,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}