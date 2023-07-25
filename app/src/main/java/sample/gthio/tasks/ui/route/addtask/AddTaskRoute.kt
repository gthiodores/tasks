package sample.gthio.tasks.ui.route.addtask

import TaskCenterAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskRoute(
    onBack: () -> Unit,
    viewModel: AddTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val groups by viewModel.groups.collectAsState(initial = emptyList())
    val tags by viewModel.tags.collectAsState(initial = emptyList())

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val timePickerState = rememberTimePickerState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(key1 = uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack)  {
            onBack()
            viewModel.addTaskNavigationDone()
        }
    }

    LaunchedEffect(key1 = uiState.isTimeOpen) {
        scope.launch {
            if (uiState.isTimeOpen) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    BottomSheetScaffold(
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
        },
        containerColor = surfaceGray,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            AddTaskBottomSheet(
                timePickerState = timePickerState,
                scope = scope,
                scaffoldState = scaffoldState,
                onClick = { viewModel.onEvent(AddTaskEvent.SaveTime) }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
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
            addTaskInputListing(
                tags = tags,
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            addTaskGroupListing(
                groups = groups,
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AddTaskSaveButton(onClick = { viewModel.onEvent(AddTaskEvent.SaveButtonClick) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    timePickerState: TimePickerState,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker(state = timePickerState)
        Spacer(Modifier.height(20.dp))
        AddTaskSaveButton(onClick = onClick)
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

@Composable
fun AddTaskSaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(width = 120.dp, height = 50.dp)
            .background(color = textBlack, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Save", color = containerWhite, style = MaterialTheme.typography.titleMedium)
    }
}