package sample.gthio.tasks.ui.route.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.R
import sample.gthio.tasks.ui.theme.allTaskContainer
import sample.gthio.tasks.ui.theme.allTaskIcon
import sample.gthio.tasks.ui.theme.importantContainer
import sample.gthio.tasks.ui.theme.importantIcon
import sample.gthio.tasks.ui.theme.scheduledContainer
import sample.gthio.tasks.ui.theme.scheduledIcon
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textGray
import sample.gthio.tasks.ui.theme.todayContainer
import sample.gthio.tasks.ui.theme.todayIcon

@Composable
fun HomeMenu(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeMenuGrid(
                name = "Schedules",
                quantity = 0,
                icon = R.drawable.baseline_description_24,
                iconColor = scheduledIcon,
                containerColor = scheduledContainer,
                isLarge = true,
                onClick = {}
            )
            HomeMenuGrid(
                name = "Important",
                quantity = 0,
                icon = R.drawable.baseline_star_24,
                iconColor = importantIcon,
                containerColor = importantContainer,
                isLarge = false,
                onClick = {}
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeMenuGrid(
                name = "Today",
                quantity = 0,
                icon = R.drawable.baseline_calendar_today_24,
                iconColor = todayIcon,
                containerColor = todayContainer,
                isLarge = false,
                onClick = {}
            )
            HomeMenuGrid(
                name = "All Tasks",
                quantity = uiState.tasks.size,
                icon = R.drawable.baseline_folder_open_24,
                iconColor = allTaskIcon,
                containerColor = allTaskContainer,
                isLarge = true,
                onClick = { onEvent(HomeEvent.AllTasksClick) }
            )
        }
    }
}

@Composable
fun HomeMenuGrid(
    modifier: Modifier = Modifier,
    name: String,
    quantity: Int,
    icon: Int,
    iconColor: Color,
    containerColor: Color,
    isLarge: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(if (isLarge) 150.dp else 100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(surfaceGray)
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = iconColor,
                    contentDescription = "grid menu icon",
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                text = quantity.toString(),
                style = MaterialTheme.typography.displaySmall,
            )
            Text(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = name,
                color = textGray,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}