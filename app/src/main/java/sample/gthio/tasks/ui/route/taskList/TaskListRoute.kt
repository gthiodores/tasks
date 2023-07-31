package sample.gthio.tasks.ui.route.taskList

import TaskCenterAppBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sample.gthio.tasks.R
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.ui.component.TaskTagChip
import sample.gthio.tasks.ui.extension.toDateString
import sample.gthio.tasks.ui.extension.toTimeString
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.importantContainer
import sample.gthio.tasks.ui.theme.importantIcon
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textGray

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
            uiState.tasks.forEach { (date, tasks) ->
                stickyHeader {
                    TaskListItemHeader(title = date)
                }
                items(items = tasks) { task ->
                    TaskListItem(task = task)
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
    Text(modifier = modifier, text = title, style = MaterialTheme.typography.headlineSmall)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskListItem(
    modifier: Modifier = Modifier,
    task: DomainTask,
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
                    .border(width = 2.dp, color = textGray, shape = CircleShape)
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