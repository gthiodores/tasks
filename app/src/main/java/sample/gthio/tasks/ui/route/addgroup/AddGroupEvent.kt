package sample.gthio.tasks.ui.route.addgroup

import androidx.compose.ui.graphics.Color

sealed interface AddGroupEvent {
    data class SelectColor(val color: Color): AddGroupEvent
    data class TitleValueChange(val title: String): AddGroupEvent
}