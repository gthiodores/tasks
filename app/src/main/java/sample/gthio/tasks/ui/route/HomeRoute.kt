import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.R
import sample.gthio.tasks.ui.route.HomeMenu
import sample.gthio.tasks.ui.route.HomeViewModel
import sample.gthio.tasks.ui.route.homeGroups
import sample.gthio.tasks.ui.route.homeTags
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleRoute(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskAppBar(
                title = { Text(text = "Hi, Username", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "user photo",
                    )
                },
                actions = {
                    IconButton(onClick = {  TODO("Implement Home Menu Button") }) {
                        Icon(Icons.Default.Menu, contentDescription = "home menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = textBlack,
                contentColor = containerWhite,
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "add task")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = surfaceGray,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(surfaceGray),
        ) {
            item { HomeMenu() }
            homeTags(
                tags = viewModel.tags,
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            homeGroups(groups = viewModel.groups)
        }
    }
}