package sample.gthio.tasks.ui.route.home.editgroup

import androidx.compose.ui.graphics.Color

sealed interface EditGroupEvent {
    data class TitleValueChange(val title: String): EditGroupEvent
    data class SelectColor(val color: Color): EditGroupEvent
    object SaveButtonClick: EditGroupEvent
    object CloseWithoutSaving: EditGroupEvent
}