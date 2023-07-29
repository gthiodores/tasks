package sample.gthio.tasks.ui.route.addtask

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import sample.gthio.tasks.R
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.ui.component.TagChip
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textGray

fun LazyListScope.addTaskInputListing(
    tags: List<DomainTag>,
    uiState: AddTaskUiState,
    onEvent: (AddTaskEvent) -> Unit
) {
    item {
        AddTaskInputDate(
            modifier = Modifier
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            date = uiState.date,
            result = "",
            onDateChange = { dateInMillis -> onEvent(AddTaskEvent.SaveDate(dateInMillis))},
            isExpanded = uiState.isDateOpen,
            onClick = { onEvent(AddTaskEvent.OpenDate) },
        )
        AddTaskInputTime(
            modifier = Modifier,
            result = "",
            isExpanded = uiState.isTimeOpen,
            onClick = { onEvent(AddTaskEvent.OpenTime) }
        )
        AddTaskInputTags(
            modifier = Modifier,
            tags = tags,
            result = "",
            selectedTags = uiState.selectedTags,
            newTag = uiState.newTag,
            onTagSelected = { tag -> onEvent(AddTaskEvent.TagSelect(tag)) },
            onNewTagValueChange = { newTag -> onEvent(AddTaskEvent.NewTagValueChange(newTag)) },
            onNewTagAddButtonClick = { newTag -> onEvent(AddTaskEvent.NewTagAddButtonClick(newTag)) },
            isExpanded = uiState.isTagOpen,
            onClick = { onEvent(AddTaskEvent.OpenTag) },
        )
        AddTaskMarkAsImportantToggle(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
            isImportant = uiState.isImportant,
            onClick = { onEvent(AddTaskEvent.ImportantSelect) }
        )
    }
}

@Composable
fun AddTaskInputContainerExpanded(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    result: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .background(containerWhite)
    ) {
        AddTaskInputContainer(
            iconId = iconId,
            title = title,
            result = result,
            isExpanded = isExpanded,
            onClick = onClick
        )
        Crossfade(
            targetState = isExpanded,
            animationSpec = tween(durationMillis = 200)
        ) { isExpanded ->
            if (isExpanded) {
                Column {
                    content()
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(surfaceGray)
                    )
                }
            }
        }
    }
}

@Composable
fun AddTaskInputContainer(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    result: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerWhite)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = surfaceGray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = iconId),
                    tint = textGray,
                    contentDescription = "input icon"
                )
            }
            Text(text = title)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = result)
            if (isExpanded) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "expand container icon"
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "close container icon"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskInputDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    result: String,
    onDateChange: (Long) -> Unit,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    AddTaskInputContainerExpanded(
        modifier = modifier,
        iconId = R.drawable.baseline_calendar_today_24,
        title = "Date",
        result = result,
        isExpanded = isExpanded,
        onClick = onClick,
    ) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date
                .atStartOfDayIn(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        )
        LaunchedEffect(key1 = datePickerState.selectedDateMillis) {
            if (datePickerState.selectedDateMillis != null) {
                onDateChange(datePickerState.selectedDateMillis!!)
            }
        }
        DatePicker(
            state = datePickerState,
        )
    }
}

@Composable
fun AddTaskInputTime(
    modifier: Modifier = Modifier,
    result: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    AddTaskInputContainer(
        modifier = modifier,
        iconId = R.drawable.baseline_access_time_filled_24,
        title = "Time",
        result = result,
        isExpanded = isExpanded,
        onClick = onClick
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTaskInputTags(
    modifier: Modifier = Modifier,
    tags: List<DomainTag>,
    result: String,
    selectedTags: List<DomainTag>,
    newTag: String,
    onTagSelected: (DomainTag) -> Unit,
    onNewTagValueChange: (String) -> Unit,
    onNewTagAddButtonClick: (String) -> Unit,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    AddTaskInputContainerExpanded(
        modifier = modifier,
        iconId = R.drawable.tag_48px,
        title = "Tags",
        result = result,
        isExpanded = isExpanded,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (tags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    tags.forEach { tag ->
                        TagChip(
                            name = "#${tag.title}",
                            isSelected = selectedTags.any { selectedTag -> selectedTag.id == tag.id },
                            onClick = { onTagSelected(tag) }
                        )
                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = newTag,
                onValueChange = { newTag -> onNewTagValueChange(newTag) },
                label = { Text("Add new tag...") },
                trailingIcon = {
                    IconButton(onClick = { onNewTagAddButtonClick(newTag) }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "add new tag")
                    }
                }
            )
        }
    }
}

@Composable
fun AddTaskMarkAsImportantToggle(
    modifier: Modifier = Modifier,
    isImportant: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerWhite)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = surfaceGray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    tint = textGray,
                    contentDescription = "input icon"
                )
            }
            Text(text = "Mark as important")
        }
        RadioButton(
            modifier = Modifier.size(24.dp),
            selected = isImportant,
            onClick = onClick
        )
    }
}