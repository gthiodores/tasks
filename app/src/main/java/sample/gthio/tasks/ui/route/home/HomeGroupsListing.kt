package sample.gthio.tasks.ui.route.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.model.UiGroup
import sample.gthio.tasks.ui.theme.containerWhite

fun LazyListScope.homeGroups(
    uiState: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "My list", style = MaterialTheme.typography.titleLarge)

            IconButton(onClick = { onEvent(HomeEvent.AddClick) }) {
                Text(text = "Add", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    itemsIndexed(uiState.uiGroups) { index, group ->
        HomeGroupItem(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (index == 0) 20.dp else 0.dp,
                        topEnd = if (index == 0) 20.dp else 0.dp,
                        bottomStart = if (index == uiState.uiGroups.lastIndex) 20.dp else 0.dp,
                        bottomEnd = if (index == uiState.uiGroups.lastIndex) 20.dp else 0.dp,
                    )
                )
                .background(containerWhite),
            group = group
        )
    }
}

@Composable
fun HomeGroupItem(
    modifier: Modifier = Modifier,
    group: UiGroup,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = {  },
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.group.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "to group route icon")
            }
        }
    }
}