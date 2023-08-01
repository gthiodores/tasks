package sample.gthio.tasks.ui.route.taskList

import TaskCenterAppBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sample.gthio.tasks.R
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.ui.component.TaskTagChip
import sample.gthio.tasks.ui.extension.toDateString
import sample.gthio.tasks.ui.extension.toTimeString
import sample.gthio.tasks.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskListRoute(
    viewModel: TaskListViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) onBack()
    }

    Scaffold(
        topBar = {
            TaskCenterAppBar(
                title = { Text(text = "All tasks") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(TaskListEvent.BackPressed) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "nav back stack")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_filter_list_24),
                            contentDescription = "filter list"
                        )
                    }
                }
            )
        },
        containerColor = surfaceGray
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.padding(0.dp)) }
            uiState
                .tasks
                .groupBy { task -> task.group.title }
                .forEach { (key, entry) ->
                    stickyHeader {
                        TaskListItemHeader(title = key)
                    }
                    items(items = entry, key = { task -> task.id }) { task ->
                        TaskListItem(
                            modifier = Modifier.animateItemPlacement(),
                            task = task,
                            onTaskFinishClick = { _task -> viewModel.onEvent(TaskListEvent.TaskFinishClick(_task)) }
                        )
                    }
                }

            if (uiState.completedTasks.isNotEmpty()) {
                item { Text("Completed tasks", style = MaterialTheme.typography.titleLarge) }

                uiState
                    .completedTasks
                    .groupBy { task -> task.group.title }
                    .forEach { (key, entry) ->
                        stickyHeader {
                            TaskListItemHeader(title = key)
                        }
                        items(items = entry, key = { task -> task.id }) { task ->
                            TaskListItem(
                                modifier = Modifier.animateItemPlacement(),
                                task = task,
                                onTaskFinishClick = { _task -> viewModel.onEvent(TaskListEvent.TaskFinishClick(_task)) }
                            )
                        }
                    }
            }
        }
    }
}

@Composable
fun TaskListItemHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(modifier = modifier, text = title, style = MaterialTheme.typography.titleLarge)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskListItem(
    modifier: Modifier = Modifier,
    task: DomainTask,
    onTaskFinishClick: (DomainTask) -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color = if (task.isFinished) textGray else Color.Transparent)
                    .border(width = 2.dp, color = textGray, shape = CircleShape)
                    .clickable { onTaskFinishClick(task) }
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(textGray)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(containerWhite)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(task.group.groupColor.toColor())
            )
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                    if (task.isImportant) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = importantContainer, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.baseline_star_24),
                                tint = importantIcon,
                                contentDescription = "important icon"
                            )
                        }
                    }
                }
                Text(text = "${task.date.toDateString()}, ${task.time.toTimeString()}")
                Text(text = task.description ?: "")
                if (task.tags.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        task.tags.forEach { tag ->
                            TaskTagChip(name = tag.title, color = task.group.groupColor.toColor())
                        }
                    }
                }
            }
        }
    }
}