package sample.gthio.tasks.ui.route.addtask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.ui.theme.containerWhite

fun LazyListScope.addTaskGroupListing(
    groups: List<DomainGroup>,
    uiState: AddTaskUiState,
    onEvent: (AddTaskEvent) -> Unit
) {
    item { AddTaskGroupTitle(Modifier.padding(vertical = 16.dp)) }
    itemsIndexed(items = groups) { index, group ->
        AddTaskGroupItem(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (index == 0) 20.dp else 0.dp,
                        topEnd = if (index == 0) 20.dp else 0.dp,
                        bottomStart = if (index == groups.lastIndex) 20.dp else 0.dp,
                        bottomEnd = if (index == groups.lastIndex) 20.dp else 0.dp,
                    )
                )
                .background(containerWhite),
            group = group,
            isSelected = uiState.selectedGroup == group,
            onGroupSelected = { selectedGroup -> onEvent(AddTaskEvent.GroupSelect(selectedGroup)) }
        )
    }
}

@Composable
fun AddTaskGroupTitle(
    modifier: Modifier
) {
    Text(
        modifier = modifier,
        text = "List",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun AddTaskGroupItem(
    modifier: Modifier = Modifier,
    group: DomainGroup,
    isSelected: Boolean,
    onGroupSelected: (DomainGroup) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
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
            RadioButton(selected = isSelected, onClick = { onGroupSelected(group) })
        }
    }
}