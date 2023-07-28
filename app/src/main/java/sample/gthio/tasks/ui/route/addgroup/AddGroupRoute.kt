package sample.gthio.tasks.ui.route.addgroup

import TaskCenterAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupRoute(
    viewModel: AddGroupViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TaskCenterAppBar(
                title = { Text(text = "Add Group") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AddGroupEvent.BackPressed) }) {
                        Icon(Icons.Default.Close, contentDescription = "nav back stack")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(AddGroupEvent.CheckPressed) }) {
                        Icon(Icons.Default.Check, contentDescription = "save group")
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AddGroupTitleEditText(
                    modifier = Modifier.fillMaxWidth(),
                    color = uiState.selectedGroupColor,
                    title = uiState.title,
                    onValueChange = { value -> viewModel.onEvent(AddGroupEvent.TitleValueChange(value)) }
                )
            }
            item {
                AddGroupColorInputList(
                    availableColors = uiState.availableColors,
                    onColorSelect = { color -> viewModel.onEvent(AddGroupEvent.SelectColor(color)) }
                )
            }
        }
    }
}

@Composable
fun AddGroupTitleEditText(
    modifier: Modifier = Modifier,
    color: Color?,
    title: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = title,
        onValueChange = onValueChange,
        label = { Text(text = "Group title") },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = color ?: Color.White,
                        shape = CircleShape
                    )
            )
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddGroupColorInputList(
    availableColors: List<Color>,
    onColorSelect: (Color) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        availableColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
                    .clickable { onColorSelect(color) }
            )
        }
    }
}

