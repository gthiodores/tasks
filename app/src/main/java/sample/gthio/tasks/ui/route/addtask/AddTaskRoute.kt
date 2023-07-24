package sample.gthio.tasks.ui.route.addtask

import TaskCenterAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskRoute(
    onBack: () -> Unit,
    viewModel: AddTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val groups by viewModel.groups.collectAsState(initial = emptyList())
    val tags by viewModel.tags.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) onBack()
    }

    Scaffold(
        topBar = {
            TaskCenterAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AddTaskEvent.BackPressed) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "nav back stack")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Delete, contentDescription = "dismiss add task")
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .background(surfaceGray)
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                AddTaskTitleEditText(
                    title = uiState.title,
                    onValueChange = { value -> viewModel.onEvent(AddTaskEvent.TitleValueChange(value)) }
                )
            }
            item {
                AddTaskDescriptionEditText(
                    description = uiState.description,
                    onValueChange = { value ->
                        viewModel.onEvent(
                            AddTaskEvent.DescriptionValueChange(value)
                        )
                    }
                )
            }
            addTaskInputListing(tags = tags, uiState = uiState, onEvent = viewModel::onEvent)
            addTaskGroupListing(groups = groups, uiState = uiState, onEvent = viewModel::onEvent)
        }
    }
}

@Composable
fun AddTaskTitleEditText(
    modifier: Modifier = Modifier,
    title: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .padding(top = 16.dp),
        value = title,
        onValueChange = { value -> onValueChange(value) },
        textStyle = MaterialTheme.typography.bodyMedium,
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = containerWhite, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (title.isEmpty()) {
                    Text(text = "Title", style = MaterialTheme.typography.bodyMedium)
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun AddTaskDescriptionEditText(
    modifier: Modifier = Modifier,
    description: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .padding(top = 16.dp),
        value = description,
        onValueChange = { value -> onValueChange(value) },
        textStyle = MaterialTheme.typography.bodyMedium,
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = containerWhite, shape = RoundedCornerShape(16.dp))
                    .heightIn(100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (description.isEmpty()) {
                    Text(text = "Description", style = MaterialTheme.typography.bodyMedium)
                }
                innerTextField()
            }
        }
    )
}