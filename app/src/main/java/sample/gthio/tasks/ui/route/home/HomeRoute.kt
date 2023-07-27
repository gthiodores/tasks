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
import androidx.compose.runtime.LaunchedEffect
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
    val navigation by viewModel.navigationTarget.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    if (navigation != null) {
        LaunchedEffect(key1 = navigation) {
            navigation?.let { target ->
                when (target) {
                    is HomeNavigationTarget.AddTask -> toAddTask()
                }
                viewModel.homeNavigationDone()
            }
        }
    }

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
            homeTagsChipGroup(
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