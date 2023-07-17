package sample.gthio.tasks.ui.route

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.ui.theme.containerWhite
import sample.gthio.tasks.ui.theme.surfaceGray

@Composable
fun HomeMenu(
    modifier: Modifier = Modifier
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
                icon = 0,
                color = containerWhite,
                isLarge = true,
            )
            HomeMenuGrid(
                name = "Important",
                quantity = 0,
                icon = 0,
                color = containerWhite,
                isLarge = false,
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
                icon = 0,
                color = containerWhite,
                isLarge = false,
            )
            HomeMenuGrid(
                name = "All Tasks",
                quantity = 0,
                icon = 0,
                color = containerWhite,
                isLarge = true,
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
    color: Color,
    isLarge: Boolean,
) {
    Box(
        modifier = modifier
            .height(if (isLarge) 150.dp else 100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
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
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "grid menu icon"
                )
            }
            Text(modifier = Modifier.align(Alignment.TopEnd), text = quantity.toString())
            Text(modifier = Modifier.align(Alignment.BottomEnd), text = name)
        }
    }
}