package sample.gthio.tasks.ui.route

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray
import sample.gthio.tasks.ui.theme.textBlack

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.homeTags(
    tags: List<DomainTag>
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tags")
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "tags expand hide button")
            }
        }
    }
    item {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HomeTagChip(name = "All Tags", isSelected = true)
            tags.forEach { tag ->
                HomeTagChip(name = "#${tag.title}", isSelected = true)
            }
        }
    }
}

@Composable
fun HomeTagChip(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (isSelected) textBlack else surfaceGray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            text = name,
            color = if (isSelected) containerWhite else textBlack
        )
    }
}