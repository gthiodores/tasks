package sample.gthio.tasks.domain.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class DomainGroup(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val groupColor: GroupColor
)

enum class GroupColor(val id: Int) {
    PURPLE(1),
    RED(2),
    BLUE(3),
    GREEN(4),
    CYAN(5),
    YELLOW(6);

    companion object {
        infix fun from(id: Int): GroupColor
            = GroupColor.values().first { it.id == id }
    }
}

fun GroupColor.toColor(): Color {
    return when (this) {
        GroupColor.PURPLE -> Color.Magenta
        GroupColor.RED -> Color.Red
        GroupColor.BLUE -> Color.Blue
        GroupColor.GREEN -> Color.Green
        GroupColor.CYAN -> Color.Cyan
        GroupColor.YELLOW -> Color.Yellow
    }
}