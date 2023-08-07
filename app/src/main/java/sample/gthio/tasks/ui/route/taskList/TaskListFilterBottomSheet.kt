package sample.gthio.tasks.ui.route.taskList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.ui.component.TagChip
import sample.gthio.tasks.ui.route.addtask.AddTaskSaveButton
import sample.gthio.tasks.ui.theme.containerWhite
import java.util.UUID

@Composable
fun TaskListFilterBottomSheet(
    uiState: TaskListUiState,
    onEvent: (TaskListEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TaskListResetFilterButton(
                    onClick = { onEvent(TaskListEvent.ResetFilter) }
                )
            }
        }
        taskListTagChipGroup(uiState = uiState, onEvent = onEvent)
        taskListGroupList(uiState = uiState, onEvent = onEvent)
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AddTaskSaveButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { onEvent(TaskListEvent.SaveFilter) }
                )
            }
        }
    }
}

@Composable
fun TaskListResetFilterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .clickable { onClick() },
        text = "Reset Filter",
        color = Color.Red
    )
}

fun LazyListScope.taskListGroupList(
    uiState: TaskListUiState,
    onEvent: (TaskListEvent) -> Unit
) {
    item { TaskListGroupTitle(Modifier.padding(vertical = 16.dp)) }
    itemsIndexed(items = uiState.groups) { index, group ->
        TaskListGroupItem(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (index == 0) 20.dp else 0.dp,
                        topEnd = if (index == 0) 20.dp else 0.dp,
                        bottomStart = if (index == uiState.groups.lastIndex) 20.dp else 0.dp,
                        bottomEnd = if (index == uiState.groups.lastIndex) 20.dp else 0.dp,
                    )
                )
                .background(containerWhite),
            group = group,
            isSelected = uiState.selectedGroupId.any { groupId -> groupId == group.id },
            onClick = { onEvent(TaskListEvent.FilterByGroup(group.id)) }
        )
    }
}

@Composable
fun TaskListGroupTitle(
    modifier: Modifier
) {
    Text(
        modifier = modifier,
        text = "List",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun TaskListGroupItem(
    modifier: Modifier = Modifier,
    group: DomainGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(group.groupColor.toColor())
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.title,
                style = MaterialTheme.typography.bodyLarge
            )
            RadioButton(
                modifier = Modifier.size(24.dp),
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.taskListTagChipGroup(
    uiState: TaskListUiState,
    onEvent: (TaskListEvent) -> Unit,
) {
    item {
        Text(text = "Tags", style = MaterialTheme.typography.titleLarge)
    }
    item {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagChip(
                name = "All Tags",
                isSelected = uiState.selectedTagId == emptyList<UUID>(),
                onClick = { onEvent(TaskListEvent.FilterAllTag) },
            )
            uiState.tags.forEach { tag ->
                TagChip(
                    name = "#${tag.title}",
                    isSelected = uiState.selectedTagId.any { tagId -> tagId == tag.id },
                    onClick = { onEvent(TaskListEvent.FilterByTag(tag.id)) }
                )
            }
        }
    }
}