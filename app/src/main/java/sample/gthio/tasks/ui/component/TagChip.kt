package sample.gthio.tasks.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun TagChip(
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

@Composable
fun TaskTagChip(
    modifier: Modifier = Modifier,
    name: String,
    color: Color,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            text = "#$name",
            color = color
        )
    }
}