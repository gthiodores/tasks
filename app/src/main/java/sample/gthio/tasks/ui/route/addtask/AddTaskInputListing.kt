package sample.gthio.tasks.ui.route.addtask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import sample.gthio.tasks.R
import sample.gthio.tasks.domain.model.DomainTag
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
            onDateChange = {},
            isExpanded = true,
            onClick = {}
        )
        AddTaskInputTime(
            modifier = Modifier,
            time = uiState.time,
            result = "",
            onTimeChange = {},
            onClick = {},
            isExpanded = false
        )
        AddTaskInputTags(
            modifier = Modifier,
            tags = uiState.selectedTags,
            result = "",
            selectedTags = uiState.selectedTags,
            onTagsSelected = {},
            isExpanded = true,
            onClick = {}
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
    onClick: () -> Unit,
    isExpanded: Boolean,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerWhite)
    ) {
        AddTaskInputContainer(
            iconId = iconId,
            title = title,
            result = result,
            onClick = onClick,
            isExpanded = isExpanded,
        )
        if (isExpanded) {
            content()
        }
    }
}

@Composable
fun AddTaskInputContainer(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    result: String,
    onClick: () -> Unit,
    isExpanded: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerWhite)
            .padding(16.dp)
            .clickable { onClick() },
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
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "expand container icon")
            } else {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "close container icon")
            }
        }
    }
}

@Composable
fun AddTaskInputDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    result: String,
    onDateChange: (LocalDate) -> Unit,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    AddTaskInputContainerExpanded(
        modifier = modifier,
        iconId = R.drawable.baseline_calendar_today_24,
        title = "Date",
        result = result,
        isExpanded = isExpanded,
        onClick = onClick
    ) {

    }
}

@Composable
fun AddTaskInputTime(
    modifier: Modifier = Modifier,
    time: LocalTime,
    result: String,
    onTimeChange: (LocalTime) -> Unit,
    onClick: () -> Unit,
    isExpanded: Boolean
) {
    AddTaskInputContainer(
        modifier = modifier,
        iconId = R.drawable.baseline_access_time_filled_24,
        title = "Time",
        result = result,
        onClick = onClick,
        isExpanded = isExpanded
    )
}

@Composable
fun AddTaskInputTags(
    modifier: Modifier = Modifier,
    tags: List<DomainTag>,
    result: String,
    selectedTags: List<DomainTag>,
    onTagsSelected: (List<DomainTag>) -> Unit,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    AddTaskInputContainerExpanded(
        modifier = modifier,
        iconId = R.drawable.tag_48px,
        title = "Tags",
        result = result,
        isExpanded = isExpanded,
        onClick = onClick
    ) {

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
            .padding(16.dp)
            .clickable { onClick() },
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
        RadioButton(selected = isImportant, onClick = onClick)
    }
}