package sample.gthio.tasks.ui.route.home

import TaskAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sample.gthio.tasks.R
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    toAddTask: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val tags by viewModel.tags.collectAsState(initial = emptyList())
    val groups by viewModel.groups.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskAppBar(
                title = {
                    Text(text = "Hi, Username", style = MaterialTheme.typography.headlineSmall)
                },
                actions = {
                    IconButton(onClick = { TODO("Implement Home Menu Button") }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = "home menu"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = textBlack,
                contentColor = containerWhite,
                onClick = {
                    viewModel.onEvent(HomeEvent.FabClick)
//                    toAddTask()
                }
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
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            homeGroups(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }
    }
}