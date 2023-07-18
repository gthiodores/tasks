package sample.gthio.tasks.ui.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.model.UiGroup

fun LazyListScope.homeGroups(
    uiState: HomeState
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "My list", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /*TODO*/ }) {
                Text(text = "Add", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    items(uiState.uiGroups) { group ->
        HomeGroupItem(group = group)
    }
}

@Composable
fun HomeGroupItem(
    modifier: Modifier = Modifier,
    group: UiGroup,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.Home, contentDescription = "group icon")
            Text(text = group.group.title)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = group.quantity.toString())
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "to group route icon")
        }
    }
}