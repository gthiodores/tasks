package sample.gthio.tasks.ui.route.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.theme.chipBlue
import sample.gthio.tasks.ui.theme.chipGray
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.textGray

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.homeTags(
    uiState: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tags", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "tags expand hide button")
            }
        }
    }
    item {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HomeTagChip(
                name = "All Tags",
                isSelected = uiState.selectedTag == null,
                onClick = { onEvent(HomeEvent.SelectAllTags) },
            )
            uiState.tags.forEach { tag ->
                HomeTagChip(
                    name = "#${tag.title}",
                    isSelected = uiState.selectedTag?.id == tag.id,
                    onClick = { onEvent(HomeEvent.SelectTag(tag)) }
                )
            }
        }
    }
}

@Composable
fun HomeTagChip(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor: Color by animateColorAsState(
        targetValue = if (isSelected) chipBlue else chipGray,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            text = name,
            color = if (isSelected) containerWhite else textGray
        )
    }
}