package sample.gthio.tasks.ui.route.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.component.TagChip

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.homeTagsChipGroup(
    uiState: HomeUiState,
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
            TagChip(
                name = "All Tags",
                isSelected = uiState.selectedTag == null,
                onClick = { onEvent(HomeEvent.SelectAllTags) },
            )
            uiState.tags.forEach { tag ->
                TagChip(
                    name = "#${tag.title}",
                    isSelected = uiState.selectedTag?.id == tag.id,
                    onClick = { onEvent(HomeEvent.SelectTag(tag)) }
                )
            }
        }
    }
}